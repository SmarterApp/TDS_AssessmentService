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
                "   item.scorepoint AS maxScore, \n" +
                "   item.itemid AS clientId, \n" +
                "   item._efk_item as itemKey, \n" +
                "   item._efk_itembank as bankKey, \n" +
                "   adminItems._fk_adminsubject AS segmentKey,\n" +
                "   formItem._fk_testform AS formKey,\n" +
                "   formItem.formposition AS formPosition, \n" +
                "   strands.name AS contentLevel, \n" +
                "   adminItems.groupid,\n" +
                "   adminItems.groupkey,\n" +
                "   adminItems.blockid, \n" +
                "   adminItems.itemposition AS position,\n" +
                "   adminItems.isfieldtest,\n" +
                "   adminItems.isrequired, \n" +
                "   adminItems.strandname, \n" +
                "   adminItems.responseMimeType, \n" +
                "   adminItems.notForScoring, \n" +
                "   CONCAT(client.homepath, bank.homepath, bank.itempath, item.filepath, item.filename) AS itemFilePath, \n" +
                "   CONCAT(client.homepath, bank.homepath, bank.stimulipath, stimulus.filepath, stimulus.filename) AS stimulusFilePath, \n" +
                "   upper(adminItems.IRT_Model) AS irtModel, \n" +
                "   adminItems.IRT_b AS irtB, \n" +
                "   adminItems.IRT_a AS irtA, \n" +
                "   adminItems.IRT_c AS irtC, \n" +
                "   adminItems.bVector, \n" +
                "   adminItems.clString, \n" +
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
                "LEFT JOIN \n" +
                "   itembank.tblstrand strands \n" +
                "   ON strands._key = adminItems.strandname \n" +
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
                item.setFormPosition(resultExtractor.getInt("formPosition"));
                item.setFieldTest(resultExtractor.getBoolean("isfieldtest"));
                item.setRequired(resultExtractor.getBoolean("isrequired"));
                item.setStrand(resultExtractor.getString("strandname"));
                item.getFormKeys().add(resultExtractor.getString("formKey"));
                item.setItemFilePath(resultExtractor.getString("itemFilePath"));
                item.setStimulusFilePath(resultExtractor.getString("stimulusFilePath"));
                item.setPrintable(resultExtractor.getBoolean("isprintable"));
                item.setMimeType(resultExtractor.getString("responseMimeType"));
                item.setMaxScore(resultExtractor.getInt("maxScore"));
                item.setContentLevel(resultExtractor.getString("contentLevel"));
                item.setClientId(resultExtractor.getString("clientId"));
                item.setNotForScoring(resultExtractor.getBoolean("notForScoring"));
                item.setbVector(resultExtractor.getString("bVector"));
                item.setItemResponseTheoryModel(resultExtractor.getString("irtModel"));
                item.setItemResponseTheoryAParameter((Float) resultExtractor.getObject("irtA"));
                item.setItemResponseTheoryBParameter(resultExtractor.getString("irtB"));
                item.setItemResponseTheoryCParameter((Float) resultExtractor.getObject("irtC"));
                item.setClaims(resultExtractor.getString("clString"));
                item.setItemKey(resultExtractor.getLong("itemKey"));
                item.setBankKey(resultExtractor.getLong("bankKey"));
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

    @Override
    public List<Item> findItemsForSegment(final String segmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey);

        String SQL = "SELECT" +
            "   item._key AS itemId,\n" +
            "   item.itemtype,\n" +
            "   item.scorepoint AS maxScore, \n" +
            "   item.itemid AS clientId, \n" +
            "   item._efk_item as itemKey, \n" +
            "   item._efk_itembank as bankKey, \n" +
            "   formitem.formposition AS formPosition, \n" +
            "   adminItems.groupid,\n" +
            "   adminItems.groupkey,\n" +
            "   adminItems.blockid, \n" +
            "   adminItems.itemposition AS position,\n" +
            "   adminItems.isfieldtest,\n" +
            "   adminItems.isrequired, \n" +
            "   adminItems.strandname, \n" +
            "   adminItems.responseMimeType, \n" +
            "   adminItems.notForScoring, \n" +
            "   adminItems._fk_Item AS itemkey, \n" +
            "   adminItems.isActive, " +
            "   upper(adminItems.IRT_Model) AS irtModel, \n" +
            "   adminItems.IRT_b AS irtB, \n" +
            "   adminItems.IRT_a AS irtA, \n" +
            "   adminItems.IRT_c AS irtC, \n" +
            "   adminItems.bVector, \n" +
            "   adminItems.clString, \n" +
            "   adminItems.isPrintable \n" +
            "FROM \n" +
            "   itembank.tblsetofadminitems AS adminItems \n" +
            "JOIN \n" +
            "   itembank.tblitem item \n" +
            "   ON item._key = adminItems._fk_item \n" +
            "LEFT JOIN \n" +
            "   itembank.testformitem formitem \n" +
            "   ON formitem._fk_item = item._key \n" +
            "WHERE \n" +
            "   adminItems._fk_AdminSubject = :segmentKey";

        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) -> {
            Item item = new Item(rs.getString("itemId"));
            item.setItemType(rs.getString("itemtype"));
            item.setGroupId(rs.getString("groupid"));
            item.setGroupKey(rs.getString("groupkey"));
            item.setBlockId((rs.getString("blockid")));
            item.setPosition(rs.getInt("position"));
            item.setFormPosition(rs.getInt("formPosition"));
            item.setFieldTest(rs.getBoolean("isfieldtest"));
            item.setRequired(rs.getBoolean("isrequired"));
            item.setStrand(rs.getString("strandname"));
            item.setPrintable(rs.getBoolean("isprintable"));
            item.setMimeType(rs.getString("responseMimeType"));
            item.setMaxScore(rs.getInt("maxScore"));
            item.setClientId(rs.getString("clientId"));
            item.setNotForScoring(rs.getBoolean("notForScoring"));
            item.setbVector(rs.getString("bVector"));
            item.setItemResponseTheoryModel(rs.getString("irtModel"));
            item.setItemResponseTheoryAParameter((Float) rs.getObject("irtA"));
            item.setItemResponseTheoryBParameter(rs.getString("irtB"));
            item.setItemResponseTheoryCParameter((Float) rs.getObject("irtC"));
            item.setClaims(rs.getString("clString"));
            item.setActive(rs.getBoolean("isActive"));
            item.setItemKey(rs.getLong("itemKey"));
            item.setBankKey(rs.getLong("bankKey"));

            return item;
        });
    }

    private static String createItemKey(final long bankKey, final long key) {
        return bankKey + "-" + key;
    }
}
