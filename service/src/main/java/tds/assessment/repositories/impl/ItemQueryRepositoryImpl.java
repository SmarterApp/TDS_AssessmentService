package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import tds.assessment.Item;
import tds.assessment.ItemFileMetadata;
import tds.assessment.ItemFileType;
import tds.assessment.ItemProperty;
import tds.assessment.repositories.ItemQueryRepository;

@Repository
public class ItemQueryRepositoryImpl implements ItemQueryRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Item> findItemsForAssessment(final String assessmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey);
        final String itemsSQL =
            "SELECT \n" +
                "   item._key AS id,\n" +
                "   item.itemtype,\n" +
                "   adminItems._fk_adminsubject AS segmentKey,\n" +
                "   formItem._fk_testform AS formKey,\n" +
                "   adminItems.groupid,\n" +
                "   adminItems.groupkey,\n" +
                "   adminItems.blockid, \n" +
                "   adminItems.itemposition AS position,\n" +
                "   adminItems.isfieldtest,\n" +
                "   adminItems.isrequired, \n" +
                "   adminItems.strandname, \n" +
                "   CONCAT(client.homepath, bank.homepath, bank.itempath, item.filepath, item.filename) AS itemFilePath, \n" +
                "   CONCAT(client.homepath, bank.homepath, bank.stimulipath, stimulus.filepath, stimulus.filename) AS stimulusFilePath, \n" +
                "   adminItems.isprintable \n" +
                "FROM \n" +
                "   itembank.tblsetofadminitems AS adminItems \n" +
                "JOIN \n" +
                "   itembank.tblitem item \n" +
                "   ON item._key = adminItems._fk_item \n" +
                "JOIN \n" +
                "   itembank.testformitem formItem \n" +
                "   ON formItem._fk_item = item._key \n" +
                "JOIN \n" +
                "   itembank.tblsetofadminsubjects segments \n" +
                "   ON segments._key = adminItems._fk_adminsubject \n" +
                "JOIN \n" +
                "   itembank.tblclient client \n" +
                "   ON client.name = segments._fk_testadmin \n" +
                "JOIN \n" +
                "   itembank.tblitembank bank \n" +
                "   ON bank._fk_client = client._key \n" +
                "LEFT JOIN \n" +
                "   tblsetofitemstimuli itemStimuli \n" +
                "   ON itemStimuli._fk_adminsubject = segments._key \n" +
                "   AND itemStimuli._fk_item = item._key \n" +
                "LEFT JOIN \n" +
                "   itembank.tblstimulus stimulus \n" +
                "   ON stimulus._key = itemStimuli._fk_stimulus \n" +
                "   AND stimulus._efk_itembank = bank._efk_itembank \n" +
                "WHERE \n" +
                "   (segments.virtualtest = :key \n" +
                "       OR segments._key = :key) \n" +
                "AND \n" +
                "   adminItems.isactive = 1 \n" +
                "AND \n" +
                "   formItem.isactive = 1";

        return jdbcTemplate.query(itemsSQL, parameters, resultExtractor -> {
            Map<String, Item> itemsMap = new HashMap<>();

            while (resultExtractor.next()) {
                Item item = itemsMap.get(resultExtractor.getString("id"));
                if (item == null) {
                    item = new Item(resultExtractor.getString("id"));
                    itemsMap.put(item.getId(), item);
                }

                item.setSegmentKey(resultExtractor.getString("segmentKey"));
                item.setItemType(resultExtractor.getString("itemtype"));
                item.setGroupId(resultExtractor.getString("groupid"));
                item.setGroupKey(resultExtractor.getString("groupkey"));
                item.setBlockId((resultExtractor.getString("blockid")));
                item.setPosition(resultExtractor.getInt("position"));
                item.setFieldTest(resultExtractor.getBoolean("isfieldtest"));
                item.setRequired(resultExtractor.getBoolean("isrequired"));
                item.setStrand(resultExtractor.getString("strandname"));
                item.getFormKeys().add(resultExtractor.getString("formKey"));
                item.setItemFilePath(resultExtractor.getString("itemFilePath"));
                item.setStimulusFilePath(resultExtractor.getString("stimulusFilePath"));
                item.setPrintable(resultExtractor.getBoolean("isprintable"));
            }

            return itemsMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        });
    }

    @Override
    public List<ItemProperty> findActiveItemsProperties(final String assessmentKey) {
        final SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey);

        final String SQL =
            "SELECT \n" +
                "   _fk_item AS itemId, \n" +
                "   propname AS name, \n" +
                "   propvalue AS value, \n" +
                "   propdescription AS description \n" +
                "FROM \n" +
                "   itembank.tblitemprops P \n" +
                "JOIN " +
                "   itembank.tblsetofadminsubjects segments \n" +
                "   ON segments._key = P._fk_adminsubject \n" +
                "WHERE \n" +
                "   segments.virtualtest = :key \n" +
                "   OR segments._key = :key \n" +
                "AND\n" +
                "   isActive = 1";

        return jdbcTemplate.query(SQL, parameters, (rs, row) ->
            new ItemProperty(
                rs.getString("name"),
                rs.getString("value"),
                rs.getString("description"),
                rs.getString("itemId")
            )
        );
    }

    @Override
    public Optional<ItemFileMetadata> findItemFileMetadataByStimulusKey(final String clientName, final long bankKey, final long stimulusKey) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("stimulusKey", createItemKey(bankKey, stimulusKey));

        final String SQL = "SELECT \n" +
            "  S.FilePath, \n" +
            "  S.FileName, \n" +
            "  S._key AS id \n" +
            "FROM\n" +
            "  tblstimulus S \n" +
            "JOIN \n" +
            "  tblitembank B ON B._efk_Itembank = S._efk_Itembank \n" +
            "JOIN \n" +
            "  tblclient C ON B._fk_Client = C._Key \n" +
            "WHERE\n" +
            "  C.name = :clientName AND S._key = :stimulusKey";

        Optional<ItemFileMetadata> maybeItemFile;
        try {
            maybeItemFile = Optional.of(jdbcTemplate.queryForObject(SQL, parameters, (rs, rowNum) ->
                ItemFileMetadata.create(ItemFileType.STIMULUS,
                    rs.getString("id"),
                    rs.getString("filename"),
                    rs.getString("filepath"))));
        } catch (EmptyResultDataAccessException e) {
            maybeItemFile = Optional.empty();
        }

        return maybeItemFile;
    }

    @Override
    public Optional<ItemFileMetadata> findItemFileMetadataByItemKey(final String clientName, final long bankKey, final long itemKey) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("itemKey", createItemKey(bankKey, itemKey));

        final String SQL = "SELECT\n" +
            "  I.Filepath,\n" +
            "  I.filename,\n" +
            "  I._key AS id\n" +
            "FROM\n" +
            "  tblitem I\n" +
            "JOIN \n" +
            "  tblitembank B ON B._efk_Itembank = I._efk_Itembank\n" +
            "JOIN \n" +
            "  tblclient C ON B._fk_Client = C._Key\n" +
            "WHERE\n" +
            "  C.name = :clientName AND I._key = :itemKey  ";

        Optional<ItemFileMetadata> maybeItemFile;
        try {
            maybeItemFile = Optional.of(jdbcTemplate.queryForObject(SQL, parameters, (rs, rowNum) ->
                ItemFileMetadata.create(ItemFileType.ITEM,
                    rs.getString("id"),
                    rs.getString("filename"),
                    rs.getString("filepath"))));
        } catch (EmptyResultDataAccessException e) {
            maybeItemFile = Optional.empty();
        }

        return maybeItemFile;
    }

    private static String createItemKey(final long bankKey, final long key) {
        return bankKey + "-" + key;
    }
}
