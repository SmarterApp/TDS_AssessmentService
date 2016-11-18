package tds.assessment.repositories.impl;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.repositories.ItemQueryRepository;

@Repository
public class ItemQueryRepositoryImpl implements ItemQueryRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Item> findItemsForAssessment(final String assessmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey);
        final String itemsSQL =
                "SELECT \n" +
                        "    I._key AS id,\n" +
                        "    I.itemtype,\n" +
                        "    A._fk_adminsubject AS segmentKey,\n" +
                        "    A.groupid,\n" +
                        "    A.groupkey,\n" +
                        "    A.itemposition AS position,\n" +
                        "    A.isactive,\n" +
                        "    A.isfieldtest,\n" +
                        "    A.isrequired, \n" +
                        "    A.strandname \n" +
                        "FROM \n" +
                        "    itembank.tblsetofadminitems as A \n" +
                        "JOIN \n" +
                        "    itembank.tblitem I \n" +
                        "    ON I._key = A._fk_item \n" +
                        "JOIN itembank.tblsetofadminsubjects segments \n" +
                        "    ON segments._key = A._fk_adminsubject \n" +
                        "WHERE \n" +
                        "   segments.virtualtest = :key OR segments._key = :key";

        List<Item> items = jdbcTemplate.query(itemsSQL, parameters, (RowMapper<Item>) (rs, row) -> {
                    Item item = new Item(rs.getString("id"));
                    item.setSegmentKey(rs.getString("segmentKey"));
                    item.setItemType(rs.getString("itemtype"));
                    item.setGroupId(rs.getString("groupid"));
                    item.setGroupKey(rs.getString("groupkey"));
                    item.setPosition(rs.getInt("position"));
                    item.setActive(rs.getBoolean("isactive"));
                    item.setFieldTest(rs.getBoolean("isfieldtest"));
                    item.setRequired(rs.getBoolean("isrequired"));
                    item.setStrand(rs.getString("strandname"));
                    return item;
                }
        );

        return items;
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
            "JOIN itembank.tblsetofadminsubjects segments \n" +
            "    ON segments._key = P._fk_adminsubject \n" +
            "WHERE \n" +
            "   segments.virtualtest = :key OR segments._key = :key AND" +
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
