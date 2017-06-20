/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.assessment.repositories.impl;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.AssessmentInfo;
import tds.assessment.ItemConstraint;
import tds.assessment.model.SegmentMetadata;
import tds.assessment.repositories.AssessmentQueryRepository;

@Repository
class AssessmentQueryRepositoryImpl implements AssessmentQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentQueryRepositoryImpl.class);
    private static final Splitter COMMA_SPLITTER = Splitter.on(",");

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final AssessmentMapper assessmentMapper = new AssessmentMapper();
    private static final RowMapper<AssessmentInfo> assessmentInfoRowMapper = new AssessmentInfoRowMapper();
    private static final String ASSESSMENT_SELECT_SQL = "SELECT \n" +
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
        "   SP.label AS segmentLabel, \n" +
        "   A.blueprintWeight, \n" +
        "   A.itemWeight, \n" +
        "   A.abilityOffset, \n " +
        "   A.cset1size, \n " +
        "   A.cset1order, \n " +
        "   A.cset2random, \n " +
        "   A.cset2initialrandom, \n " +
        "   A.startAbility, \n " +
        "   A.startInfo, \n " +
        "   A.slope, \n " +
        "   A.intercept, \n " +
        "   A.bpmetricfunction as adaptiveVersion, \n " +
        "   A.abilityWeight, \n " +
        "   A.rcAbilityWeight, \n " +
        "   A.precisionTarget, \n " +
        "   A.precisionTargetMetWeight, \n " +
        "   A.precisionTargetNotMetWeight, \n " +
        "   A.adaptiveCut, \n " +
        "   A.tooCloseSEs, \n " +
        "   A.terminationOverallInfo, \n " +
        "   A.terminationRCInfo, \n " +
        "   A.terminationMinCount, \n " +
        "   A.terminationTooClose, \n " +
        "   A.terminationFlagsAnd \n ";

    private static final String ASSESSMENT_INFO_SELECT = "SELECT \n" +
        "   a._key AS assessmentKey, \n" +
        "   a.testid AS assessmentId, \n" +
        "   tp.subjectname AS subject, \n" +
        "   tp.label AS assessmentLabel, \n" +
        "   GROUP_CONCAT(DISTINCT tool.code) languages, \n" +
        "   GROUP_CONCAT(DISTINCT g.grade) grades, \n" +
        "   tp.maxopportunities AS maxAttempts \n" +
        "FROM \n" +
        "   itembank.tblsetofadminsubjects a \n" +
        "JOIN configs.client_testproperties tp \n" +
        "   ON a.testid = tp.testid \n" +
        "LEFT JOIN configs.client_testtool tool \n" +
        "   ON tool.context = tp.testid \n" +
        "LEFT JOIN itembank.setoftestgrades g \n" +
        "   ON g.testid = tp.testid \n";

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
            ASSESSMENT_SELECT_SQL +
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
    public List<AssessmentInfo> findAssessmentInfoByKeys(final String clientName, final String... assessmentKeys) {
        final SqlParameterSource parameters = new MapSqlParameterSource("keys", Arrays.asList(assessmentKeys))
            .addValue("clientName", clientName);

        final String SQL =
            ASSESSMENT_INFO_SELECT +
                "WHERE \n" +
                "   tp.isselectable = 1 \n" +
                "   AND tp.clientname = :clientName \n" +
                "   AND tool.clientname = :clientName \n" +
                "   AND tool.type = 'Language' \n" +
                "   AND tool.contexttype = 'TEST' \n" +
                "   AND a._key IN (:keys) \n" +
                "GROUP BY \n" +
                "   assessmentKey, assessmentId, subject, assessmentLabel, maxAttempts";

        return jdbcTemplate.query(SQL, parameters, assessmentInfoRowMapper);
    }

    @Override
    public List<AssessmentInfo> findAssessmentInfoForGrade(final String clientName, final String grade) {
        final SqlParameterSource parameters = new MapSqlParameterSource("grade", grade)
            .addValue("clientName", clientName);

        final String SQL =
            ASSESSMENT_INFO_SELECT +
                "WHERE \n" +
                "   tp.isselectable = 1 \n" +
                "   AND tp.clientname = :clientName \n" +
                "   AND tool.clientname = :clientName \n" +
                "   AND tool.type = 'Language' \n" +
                "   AND tool.contexttype = 'TEST' \n" +
                "   AND g.grade = :grade \n" +
                "GROUP BY \n" +
                "   assessmentKey, assessmentId, subject, assessmentLabel, maxAttempts";

        return jdbcTemplate.query(SQL, parameters, assessmentInfoRowMapper);
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

    @Override
    public Optional<SegmentMetadata> findSegmentMetadata(final String segmentKey) {
        final SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey);

        final String SQL = "SELECT \n" +
            "  _key, \n" +
            "  virtualtest, \n" +
            "  _fk_testadmin \n" +
            "FROM \n" +
            "  tblsetofadminsubjects \n" +
            "WHERE \n" +
            "  _key = :segmentKey";

        Optional<SegmentMetadata> maybeSegmentMetadata;
        try {
            maybeSegmentMetadata = Optional.of(jdbcTemplate.queryForObject(SQL, parameters, (resultSet, i) -> new SegmentMetadata(
                resultSet.getString("_key"),
                resultSet.getString("virtualtest"),
                resultSet.getString("_fk_testadmin")
            )));
        } catch (EmptyResultDataAccessException e) {
            maybeSegmentMetadata = Optional.empty();
        }

        return maybeSegmentMetadata;
    }

    private static class AssessmentInfoRowMapper implements RowMapper<AssessmentInfo> {
        @Override
        public AssessmentInfo mapRow(final ResultSet rs, final int i) throws SQLException {
            return new AssessmentInfo.Builder()
                .withKey(rs.getString("assessmentKey"))
                .withId(rs.getString("assessmentId"))
                .withLabel(rs.getString("assessmentLabel"))
                .withSubject(rs.getString("subject"))
                .withMaxAttempts(rs.getInt("maxAttempts"))
                .withLanguages(Arrays.asList(rs.getString("languages").split(",")))
                .withGrades(rs.getString("grades") != null
                    ? COMMA_SPLITTER.splitToList(rs.getString("grades"))
                    : new ArrayList<>())
                .build();
        }
    }
}
