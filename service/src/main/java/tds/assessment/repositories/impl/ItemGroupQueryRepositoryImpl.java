package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import tds.assessment.ItemGroup;
import tds.assessment.repositories.ItemGroupQueryRepository;

@Repository
public class ItemGroupQueryRepositoryImpl implements ItemGroupQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemGroupQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ItemGroup> findItemGroupsBySegment(final String segmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey);

        String SQL =
            "SELECT \n" +
            "   groupID as itemGroup, \n" +
            "   numItemsRequired as itemsRequired, \n" +
            "   maxItems, \n" +
            "   bpweight \n" +
            "FROM tbladminstimulus \n" +
            "WHERE _fk_AdminSubject = :segmentKey ";

        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) -> new ItemGroup(
            rs.getString("itemGroup"),
            rs.getInt("itemsRequired"),
            rs.getInt("maxItems"),
            rs.getFloat("bpweight")
        ));
    }
}
