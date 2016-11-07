package tds.assessment.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.repositories.AssessmentQueryRepository;

@Repository
class AssessmentQueryRepositoryImpl implements AssessmentQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentQueryRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AssessmentMapper assessmentMapper = new AssessmentMapper();

    @Autowired
    public AssessmentQueryRepositoryImpl(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
            List<Form> forms = findFormsForAssessment(parameters);
            maybeAssessment = assessmentMapper.mapResults(rows, forms);
        }

        return maybeAssessment;
    }

    private List<Form> findFormsForAssessment(SqlParameterSource parameters) {
        final String formsSQL =
                "SELECT \n" +
                        "    segments._key AS segmentKey, \n" +
                        "    forms._key AS `key`, \n" +
                        "    forms.formid AS id, \n" +
                        "    forms.language, \n" +
                        "    forms.loadconfig AS loadVersion, \n" +
                        "    forms.updateconfig AS updateVersion, \n" +
                        "    forms.cohort \n" +
                        "FROM itembank.tblsetofadminsubjects segments \n" +
                        "JOIN itembank.testform forms ON segments._key = forms._fk_adminsubject \n" +
                        "WHERE segments.virtualtest = :key OR segments._key = :key";


        return jdbcTemplate.query(formsSQL, parameters, (rs, row) ->
                new Form.Builder(rs.getString("key"))
                        .withSegmentKey(rs.getString("segmentKey"))
                        .withId(rs.getString("id"))
                        .withLanguage(rs.getString("language"))
                        .withCohort(rs.getString("cohort"))
                        // calling getObject() and casting to Double because .getLong() defaults to 0 if null
                        .withLoadVersion((Long) rs.getObject("loadVersion"))
                        .withUpdateVersion((Long) rs.getObject("updateVersion"))
                        .build()
        );
    }
}