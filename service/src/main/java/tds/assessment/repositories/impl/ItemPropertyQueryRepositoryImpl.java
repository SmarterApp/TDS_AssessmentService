package tds.assessment.repositories.impl;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import tds.assessment.ItemProperty;
import tds.assessment.repositories.ItemPropertyQueryRepository;

@Repository
public class ItemPropertyQueryRepositoryImpl implements ItemPropertyQueryRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemPropertyQueryRepositoryImpl(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ItemProperty> findActiveItemsProperties(String segmentKey) {
        final SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey);

        final String SQL =
            "SELECT \n" +
            "   _fk_item AS itemId, \n" +
            "   propname AS name, \n" +
            "   propvalue AS value, \n" +
            "   propdescription AS description \n" +
            "FROM \n" +
            "   itembank.tblitemprops \n" +
            "WHERE \n" +
            "   _fk_AdminSubject = :segmentKey AND \n" +
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
