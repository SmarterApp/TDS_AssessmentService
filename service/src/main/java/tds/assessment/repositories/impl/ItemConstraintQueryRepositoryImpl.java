package tds.assessment.repositories.impl;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tds.assessment.ItemConstraint;
import tds.assessment.repositories.ItemConstraintQueryRepository;

@Repository
public class ItemConstraintQueryRepositoryImpl implements ItemConstraintQueryRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemConstraintQueryRepositoryImpl(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ItemConstraint> findItemConstraints(String clientName, String assessmentId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("clientName", clientName);
        parameters.put("assessmentId", assessmentId);

        final String SQL =
            "SELECT \n" +
            "   clientname,\n" +
            "   testid AS assessmentId, \n" +
            "   propname,\n" +
            "   propvalue,\n" +
            "   tooltype,\n" +
            "   toolvalue,\n" +
            "   item_in \n" +
            "FROM \n" +
            "   configs.client_test_itemconstraint \n" +
            "WHERE \n" +
            "   clientname = :clientName AND \n" +
            "   testid = :assessmentId";

        return jdbcTemplate.query(SQL, parameters, (rs, row) ->
            new ItemConstraint.Builder()
                .withInclusive(rs.getBoolean("item_in"))
                .withPropertyName(rs.getString("propname"))
                .withPropertyValue(rs.getString("propvalue"))
                .withToolType(rs.getString("tooltype"))
                .withToolValue(rs.getString("toolvalue"))
                .withAssessmentId(rs.getString("assessmentId"))
                .build()
        );
    }
}
