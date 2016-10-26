package tds.assessment.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.repositories.AssessmentQueryRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
class AssessmentQueryRepositoryImpl implements AssessmentQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentQueryRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

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
                "S.name, \n" +
                "A.virtualtest AS assessmentKey\n" +
            "FROM itembank.tblsetofadminsubjects A \n" +
            "LEFT JOIN itembank.tblsubject S ON S._key = A._fk_Subject \n" +
            "WHERE A.virtualtest = :key OR A._key = :key \n" +
            "ORDER BY assessmentKey DESC";


        List<Map<String,Object>> rows = jdbcTemplate.queryForList(SQL, parameters);
        Assessment assessment = null;
        List<Segment> segments = new ArrayList<>();
        Optional<Assessment> maybeAssessment;

        if (rows.isEmpty()) {
            logger.debug("Did not findAssessmentByKey a result for assessment from tblsetofadminsubjects for %s", assessmentKey);
            maybeAssessment = Optional.empty();
        } else {
            /*
                The last row is the assessment row because we are ordering by `assessmentKey`.
                `assessmentKey`/virtualtest is null for the assessment row (which is last).
             */
            for (Map<String, Object> row : rows) {
                if (row.get("assessmentKey") == null) { // This is the assessment row
                    assessment = new Assessment(
                            (String) row.get("assessmentSegmentKey"),
                            (String) row.get("assessmentSegmentId"),
                            segments,
                            (String) row.get("selectionalgorithm"),
                            (float) row.get("startAbility"),
                            (String) row.get("name"));
                } else {  // This is a segment row
                    segments.add(
                            new Segment(
                                    (String) row.get("assessmentSegmentKey"),
                                    (String) row.get("assessmentSegmentId"),
                                    (String) row.get("selectionalgorithm"),
                                    (float) row.get("startAbility"),
                                    (String) row.get("assessmentKey"),
                                    (String) row.get("name"))
                    );
                }
            }

            maybeAssessment = Optional.of(assessment);
        }

        return maybeAssessment;
    }
}
