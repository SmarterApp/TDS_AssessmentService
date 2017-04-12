package tds.assessment.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.ItemConstraint;
import tds.assessment.repositories.AssessmentQueryRepository;

@Repository
class AssessmentQueryRepositoryImpl implements AssessmentQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentQueryRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final AssessmentMapper assessmentMapper = new AssessmentMapper();

    @Autowired
    public AssessmentQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Assessment> findAssessmentByKey(final String clientName, final String assessmentKey) {
        /*
        This method fetches the base assessment object
        1. itembank.tblsetofadminsubjects - contains the structure of the assessment and segment along with some metadata
        2. configs.client_testproperties - contains client specific information for the assessment
        3. configs.client_segmentproperties - contains client specific information for segments (if the assessment is segmented)
        4. itembank.tblsubject - subject information for the assessment (i.e. MATH, ELA)
         */

        SqlParameterSource parameters = new MapSqlParameterSource("key", assessmentKey)
            .addValue("clientName", clientName);

        String SQL =
            "SELECT \n" +
                "   A._key AS assessmentSegmentKey, \n" +
                "   A.testid AS assessmentSegmentId, \n" +
                "   A.selectionalgorithm AS selectionAlgorithm, \n" +
                "   A.startAbility, \n" +
                "   A.testposition AS segmentPosition, \n" +
                "   A.minItems, \n" +
                "   A.maxItems, \n" +
                "   A.ftminitems AS fieldTestMinItems, \n" +
                "   A.ftmaxitems AS fieldTestMaxItems, \n" +
                "   A.ftstartpos AS fieldTestStartPosition, \n" +
                "   A.ftendpos AS fieldTestEndPosition, \n" +
                "   S.name AS subject, \n" +
                "   A.virtualtest AS assessmentKey, \n" +
                "   A.contract, \n" +
                "   A.testtype AS assessmentType, \n" +
                "   A.loadconfig AS loadVersion, \n" +
                "   A.updateconfig AS updateVersion, \n" +
                "   CT.ftstartdate, \n" +
                "   CT.ftenddate, \n" +
                "   CT.accommodationfamily, \n" +
                "   CT.maxopportunities, \n" +
                "   CT.abilityslope, \n" +
                "   CT.abilityintercept, \n" +
                "   CT.initialabilitybysubject, \n" +
                "   CT.validatecompleteness AS validateCompleteness, \n" +
                "   CT.prefetch, \n" +
                "   CT.label, \n" +
                "   CT.deleteUnansweredItems, \n" +
                "   CT.handscoreproject AS handScored, \n " +
                "   CT.msb AS multiStageBraille, \n" +
                "   TA.schoolyear AS academicYear, \n" +
                "   SP.ftstartdate AS segFieldTestStartDate, \n" +
                "   SP.ftenddate AS segFieldTestEndDate, \n" +
                "   SP.label AS segmentLabel \n" +
                "FROM \n" +
                "   itembank.tblsetofadminsubjects A \n" +
                "JOIN \n" +
                "   configs.client_testproperties CT \n" +
                "   ON CT.testid = A.testid \n " +
                "   OR CT.testid = (\n" +
                "       SELECT parentTsa.testid \n" +
                "       FROM itembank.tblsetofadminsubjects tsa \n" +
                "       JOIN itembank.tblsetofadminsubjects parentTsa ON tsa.virtualtest = parentTsa._key \n" +
                "       WHERE tsa._key = A._key \n" +
                "   ) \n" +
                "LEFT JOIN \n" +
                "   configs.client_segmentproperties SP \n" +
                "   ON SP.segmentid = A.testid \n" +
                "   AND SP.clientName = :clientName \n" +
                "JOIN \n" +
                "   itembank.tblclient CL \n" +
                "   ON (CL.name = SP.clientname OR CL.name = CT.clientname) \n" +
                "LEFT JOIN \n" +
                "   itembank.tbltestadmin TA \n" +
                "   ON TA._fk_client = CL._key \n" +
                "LEFT JOIN \n" +
                "   itembank.tblsubject S \n" +
                "   ON S._key = A._fk_Subject \n" +
                "WHERE \n" +
                "   (A.virtualtest = :key OR A._key = :key) \n" +
                "   AND (CT.clientname = :clientName OR SP.clientname = :clientName) \n" +
                "ORDER BY  \n" +
                "   assessmentKey DESC, \n" +
                "   A.testPosition";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL, parameters);
        Optional<Assessment> maybeAssessment = Optional.empty();

        if (rows.isEmpty()) {
            logger.debug("Could not find an Assessment in tblsetofadminsubjects using key = '%s'", assessmentKey);
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
