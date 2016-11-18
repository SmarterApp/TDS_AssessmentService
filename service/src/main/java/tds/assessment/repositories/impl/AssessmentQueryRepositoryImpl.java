package tds.assessment.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.repositories.AssessmentQueryRepository;

@Repository
class AssessmentQueryRepositoryImpl implements AssessmentQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentQueryRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AssessmentMapper assessmentMapper = new AssessmentMapper();

    @Autowired
    public AssessmentQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Assessment> findAssessmentByKey(final String assessmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey);

        String SQL =
            "SELECT \n" +
                "A._key AS assessmentSegmentKey, \n" +
                "testid AS assessmentSegmentId, \n" +
                "selectionalgorithm AS selectionAlgorithm, \n" +
                "startAbility, \n" +
                "A.testposition AS segmentPosition, \n" +
                "A.minItems, \n" +
                "A.maxItems, \n" +
                "A.ftminitems AS fieldTestMinItems, \n" +
                "A.ftmaxitems AS fieldTestMaxItems, \n" +
                "S.name AS subject, \n" +
                "A.virtualtest AS assessmentKey, \n" +
                "P.propname, \n" +
                "P.propvalue, \n" +
                "P.propdescription \n" +
            "FROM itembank.tblsetofadminsubjects A \n" +
            "LEFT JOIN itembank.tblsubject S ON S._key = A._fk_Subject \n" +
            "LEFT JOIN itembank.tblitemprops P ON P.isactive = 1 and propname = 'Language' AND P._fk_AdminSubject = A._key \n" +
            "WHERE A.virtualtest = :key OR A._key = :key \n" +
            "ORDER BY assessmentKey DESC";

        List<Map<String,Object>> rows = jdbcTemplate.queryForList(SQL, parameters);
        Optional<Assessment> maybeAssessment = Optional.empty();

        if (rows.isEmpty()) {
            logger.debug("Did not findAssessmentByKey a result for assessment from tblsetofadminsubjects for %s", assessmentKey);
        } else {
            maybeAssessment = assessmentMapper.mapResults(rows);
        }

        return maybeAssessment;
    }

    @Override
    public List<ItemConstraint> findItemConstraintsForAssessment(final String clientName, final String assessmentId) {
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
