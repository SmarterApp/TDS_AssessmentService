package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tds.assessment.Item;
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
                "   I._key AS id,\n" +
                "   I.itemtype,\n" +
                "   A._fk_adminsubject AS segmentKey,\n" +
                "   FI._fk_testform AS formKey,\n" +
                "   A.groupid,\n" +
                "   A.groupkey,\n" +
                "   A.itemposition AS position,\n" +
                "   A.isfieldtest,\n" +
                "   A.isrequired, \n" +
                "   A.strandname \n" +
                "FROM \n" +
                "   itembank.tblsetofadminitems AS A \n" +
                "JOIN \n" +
                "   itembank.tblitem I \n" +
                "   ON I._key = A._fk_item \n" +
                "JOIN \n" +
                "   itembank.testformitem FI \n" +
                "   ON FI._fk_item = I._key \n" +
                "JOIN " +
                "   itembank.tblsetofadminsubjects segments \n" +
                "   ON segments._key = A._fk_adminsubject \n" +
                "WHERE \n" +
                "   (segments.virtualtest = :key \n" +
                "       OR segments._key = :key) \n" +
                "AND \n" +
                "   A.isactive = 1 \n" +
                "AND \n" +
                "   FI.isactive = 1";

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
                item.setPosition(resultExtractor.getInt("position"));
                item.setFieldTest(resultExtractor.getBoolean("isfieldtest"));
                item.setRequired(resultExtractor.getBoolean("isrequired"));
                item.setStrand(resultExtractor.getString("strandname"));
                item.getFormKeys().add(resultExtractor.getString("formKey"));
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

}
