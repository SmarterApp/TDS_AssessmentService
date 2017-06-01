package tds.assessment.repositories.impl;

import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.AssessmentInfo;
import tds.assessment.ItemConstraint;
import tds.assessment.Segment;
import tds.assessment.model.SegmentMetadata;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.common.Algorithm;
import tds.common.data.mapping.ResultSetMapperUtility;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AssessmentQueryRepositoryImplIntegrationTests {
    private static final Instant segFtStartDate = Instant.now().minus(300000);
    private static final Instant segFtEndDate = Instant.now().plus(300000);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private AssessmentQueryRepository repository;

    @Before
    public void setUp() {

        String tblClientInsertSQL = "INSERT INTO tblclient VALUES (1,'SBAC_PT',NULL,'/usr/local/tomcat/resources/tds/');";

        String tblSubjectInsertSQL = "INSERT INTO tblsubject VALUES ('ELA','','SBAC_PT-ELA',1,8185,NULL);";

        // Non-segmented, test
        String tblSetOfAdminSubjectsInsertSQL1 =
            "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016','SBAC_PT', 'SBAC_PT-ELA','IRP-Perf-ELA-11'," +
                "0,1,4,4,1,1,3,7,1,4,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,2112,1184,5,0,'Oregon',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
        // Segmented test assessment
        String tblSetOfAdminSubjectsInsertSQL2 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-Mathematics-8'," +
            "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segment 1
        String tblSetOfAdminSubjectsInsertSQL2a = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG1-MATH-8'," +
            "0,1,4,4,1,1,3,6,2,3,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',1,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segment2
        String tblSetOfAdminSubjectsInsertSQL2b = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG2-MATH-8'," +
            "0,1,4,4,1,1,7,11,1,4,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',2,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segment2 - SBAC client
        String tblSetOfAdminSubjectsSBACInsertSQL = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC', 'SBAC-ELA','SBAC-SEG2-MATH-8'," +
            "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC)SBAC-Mathematics-8-Spring-2013-2015',2,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";


        final String itemConstraintsInsertSql =
            "INSERT INTO \n" +
                "   configs.client_test_itemconstraint(clientname, testid, propname, propvalue, tooltype, toolvalue, item_in) \n" +
                "VALUES \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'ER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_ER', 0), \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'MI', 'Item Types Exclusion', 'TDS_ItemTypeExcl_MI', 0), \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'WER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_WER', 0), \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-11', 'Language', 'ENU', 'Language', 'ENU', 1), \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'ER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_ER', 0), \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'MI', 'Item Types Exclusion', 'TDS_ItemTypeExcl_MI', 0), \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'WER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_WER', 0), \n" +
                "   ('SBAC_PT', 'IRP-Perf-ELA-3', 'Language', 'ENU', 'Language', 'ENU', 1)";

        SqlParameterSource parameters = new MapSqlParameterSource("ftstartDate", ResultSetMapperUtility.mapJodaInstantToTimestamp(Instant.now()));
        final String clientTestPropertiesInsertSQL = "INSERT INTO configs.client_testproperties (clientname, testid, ftstartdate, accommodationfamily, maxopportunities, abilityslope, abilityintercept, initialabilitybysubject, prefetch, validatecompleteness, deleteUnansweredItems, label, msb, handscoreproject, subjectname) VALUES " +
            "('SBAC_PT', 'IRP-Perf-ELA-11', :ftstartDate, 'family', 99, 1.5, 2.3, 1, 2, 1, 0, 'Grade 11 ELA Perf', 1, 1234, 'ELA'), \n" +
            "('SBAC_PT', 'SBAC-Mathematics-8', :ftstartDate, 'otherFamily', 95, 5.5, 6.3, 0, 2, 1, 0, 'Grade 8 Math', 0, 4321, 'MATH');\n";

        SqlParameterSource segPropsParams = new MapSqlParameterSource("ftstartdate", ResultSetMapperUtility.mapJodaInstantToTimestamp(segFtStartDate))
            .addValue("ftenddate", ResultSetMapperUtility.mapJodaInstantToTimestamp(segFtEndDate));
        final String clientSegmentPropertiesInsertSQL =
            "INSERT INTO configs.client_segmentproperties (ispermeable, clientname, entryapproval, exitapproval, itemreview, segmentid, segmentposition, parenttest, ftstartdate, ftenddate, label, modekey) VALUES " +
                "(1, 'SBAC_PT', 0, 0, 0, 'SBAC-SEG1-MATH-8', 1, 'SBAC-Mathematics-8', :ftstartdate, :ftenddate, 'Grade 8 MATH segment', '(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015'), \n" +
                "(1, 'SBAC_PT', 0, 0, 0, 'SBAC-SEG2-MATH-8', 2, 'SBAC-Mathematics-8', :ftstartdate, :ftenddate, 'Grade 8 MATH segment', '(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015')";

        final String tblTestAdminInsertSQL =
            "INSERT INTO itembank.tbltestadmin (schoolyear, season, _key, _fk_client) " +
                "VALUES ('2112-2113', 'winter', 'SBAC_PT', 1)";

        final String testToolTypeInsertSQL = "INSERT INTO configs.client_testtooltype (clientname, context, contexttype, toolname, allowchange, rtsfieldname, isrequired, isselectable, dateentered, testmode) " +
            "VALUES ('SBAC_PT', 'IRP-Perf-ELA-11', 'TEST', 'Language', 0, 'rts', 1, 0, now(), 'ALL'), " +
            "('SBAC_PT', 'SBAC-Mathematics-8', 'TEST', 'Language', 0, 'rts', 1, 0, now(), 'ALL');";
        final String testToolInsertSQL = "INSERT INTO configs.client_testtool (clientname, context, contexttype, type, code, value, isdefault, allowcombine, testmode) " +
            "VALUES ('SBAC_PT', 'IRP-Perf-ELA-11', 'TEST', 'Language', 'ENU', 'ENU', 1, 0, 'ALL'), " +
            "('SBAC_PT', 'IRP-Perf-ELA-11', 'TEST', 'Language', 'FRN', 'FRN', 1, 0, 'ALL'), " +
            "('SBAC_PT', 'SBAC-Mathematics-8', 'TEST', 'Language', 'ENU', 'ENU', 1, 0, 'ALL');";
        final String setOfTestGradesInsertSql =
            "INSERT INTO itembank.setoftestgrades (testid, grade, _fk_adminsubject, _key) " +
                "VALUES ('IRP-Perf-ELA-11', '3', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 0x8ebc29966a52401b952e45b1a2d08e3f), " +
                "('IRP-Perf-ELA-11', '11', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 0x8ebc29966a52401b952e45b1a2d08e3e)," +
                "('SBAC-Mathematics-8', 'Kindergarden', '(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015', 0x8ebc29966a52401b952e45b1a2d08e3c)";

        jdbcTemplate.update(itemConstraintsInsertSql, new MapSqlParameterSource());
        jdbcTemplate.update(tblClientInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(tblSubjectInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1, new MapSqlParameterSource());
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2, new MapSqlParameterSource());
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a, new MapSqlParameterSource());
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b, new MapSqlParameterSource());
        jdbcTemplate.update(tblSetOfAdminSubjectsSBACInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientTestPropertiesInsertSQL, parameters);
        jdbcTemplate.update(clientSegmentPropertiesInsertSQL, segPropsParams);
        jdbcTemplate.update(tblTestAdminInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(testToolTypeInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(testToolInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(setOfTestGradesInsertSql, new MapSqlParameterSource());
    }

    @Test
    public void shouldNotFindAssessmentByKey() {
        Optional<Assessment> maybeAssessment = repository.findAssessmentByKey("SBAC", "BOGUS");
        assertThat(maybeAssessment).isNotPresent();
    }

    @Test
    public void shouldFindNonSegmentedAssessmentByKey() {
        Assessment assessment = repository.findAssessmentByKey("SBAC_PT", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016").get();
        assertThat(assessment.getKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(assessment.getSelectionAlgorithm()).isEqualTo(Algorithm.VIRTUAL);
        assertThat(assessment.getAssessmentId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(assessment.getStartAbility()).isEqualTo(0F);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.isSegmented()).isFalse();
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");
        assertThat(assessment.getAbilitySlope()).isEqualTo(1.5f);
        assertThat(assessment.getFieldTestStartDate()).isNotNull();
        assertThat(assessment.getFieldTestEndDate()).isNull();
        assertThat(assessment.getMaxOpportunities()).isEqualTo(99);
        assertThat(assessment.getAbilityIntercept()).isEqualTo(2.3f);
        assertThat(assessment.isInitialAbilityBySubject()).isTrue();
        assertThat(assessment.isMultiStageBraille()).isTrue();
        assertThat(assessment.getAcademicYear()).isEqualTo("2112-2113");
        assertThat(assessment.getHandScoreProjectId()).isEqualTo(1234);
        assertThat(assessment.getLoadVersion()).isEqualTo(2112L);
        assertThat(assessment.getUpdateVersion()).isEqualTo(1184L);
        assertThat(assessment.getContract()).isEqualTo("Oregon");
        assertThat(assessment.getType()).isEqualTo("summative");

        assertThat(assessment.getSegments().size()).isEqualTo(1);
        Segment seg = assessment.getSegments().get(0);
        assertThat(seg.getSegmentId()).isEqualTo(assessment.getAssessmentId());
        assertThat(seg.getKey()).isEqualTo(assessment.getKey());
        assertThat(seg.getStartAbility()).isEqualTo(assessment.getStartAbility());
        assertThat(seg.getAssessmentKey()).isEqualTo(assessment.getKey());
        assertThat(seg.getSubject()).isEqualTo(assessment.getSubject());
        assertThat(seg.getPosition()).isEqualTo(1);
        assertThat(seg.getMinItems()).isEqualTo(4);
        assertThat(seg.getMaxItems()).isEqualTo(4);
        assertThat(seg.getFieldTestMinItems()).isEqualTo(1);
        assertThat(seg.getFieldTestMaxItems()).isEqualTo(4);
        assertThat(seg.getSelectionAlgorithm()).isEqualTo(assessment.getSelectionAlgorithm());
    }

    @Test
    public void shouldNotFindAssessmentWrongClient() {
        Optional<Assessment> maybeAssessment = repository.findAssessmentByKey("BOGUS", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeAssessment).isNotPresent();
    }

    @Test
    public void shouldFindSegmentedAssessmentByKey() {
        final String assessmentKey = "(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015";
        final String subject = "ELA";
        Assessment assessment = repository.findAssessmentByKey("SBAC_PT", assessmentKey).get();
        assertThat(assessment.getKey()).isEqualTo(assessmentKey);
        assertThat(assessment.getSelectionAlgorithm()).isEqualTo(Algorithm.VIRTUAL);
        assertThat(assessment.getAssessmentId()).isEqualTo("SBAC-Mathematics-8");
        assertThat(assessment.getStartAbility()).isEqualTo(0F);
        assertThat(assessment.getSubject()).isEqualTo(subject);
        assertThat(assessment.isSegmented()).isTrue();
        assertThat(assessment.getSegments()).hasSize(2);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("otherFamily");
        assertThat(assessment.getAbilitySlope()).isEqualTo(5.5f);
        assertThat(assessment.getFieldTestStartDate()).isNotNull();
        assertThat(assessment.getFieldTestEndDate()).isNull();
        assertThat(assessment.getMaxOpportunities()).isEqualTo(95);
        assertThat(assessment.getAbilityIntercept()).isEqualTo(6.3f);
        assertThat(assessment.isInitialAbilityBySubject()).isFalse();
        assertThat(assessment.getPrefetch()).isEqualTo(2);
        assertThat(assessment.shouldDeleteUnansweredItems()).isFalse();
        assertThat(assessment.isValidateCompleteness()).isTrue();
        assertThat(assessment.getLabel()).isEqualTo("Grade 8 Math");
        assertThat(assessment.getAcademicYear()).isEqualTo("2112-2113");

        Segment segment1 = null;
        Segment segment2 = null;

        for (Segment segment : assessment.getSegments()) {
            if (segment.getKey().equals("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015")) {
                segment1 = segment;
            } else {
                segment2 = segment;
            }
        }

        assertThat(segment1).isNotNull();
        assertThat(segment1.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(segment1.getKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
        assertThat(segment1.getLabel()).isEqualTo("Grade 8 MATH segment");
        assertThat(segment1.getSegmentId()).isEqualTo("SBAC-SEG1-MATH-8");
        assertThat(segment1.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);
        assertThat(segment1.getPosition()).isEqualTo(1);
        assertThat(segment1.getMinItems()).isEqualTo(4);
        assertThat(segment1.getMaxItems()).isEqualTo(4);
        assertThat(segment1.getFieldTestMinItems()).isEqualTo(2);
        assertThat(segment1.getFieldTestMaxItems()).isEqualTo(3);
        assertThat(segment1.getSubject()).isEqualTo(subject);
        assertThat(segment1.getStartAbility()).isEqualTo(0);
        assertThat(segment1.getFieldTestStartDate()).isEqualTo(segFtStartDate);
        assertThat(segment1.getFieldTestEndDate()).isEqualTo(segFtEndDate);

        assertThat(segment2).isNotNull();
        assertThat(segment2.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(segment2.getKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(segment2.getLabel()).isEqualTo("Grade 8 MATH segment");
        assertThat(segment2.getSegmentId()).isEqualTo("SBAC-SEG2-MATH-8");
        assertThat(segment2.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);
        assertThat(segment2.getPosition()).isEqualTo(2);
        assertThat(segment2.getMinItems()).isEqualTo(4);
        assertThat(segment2.getMaxItems()).isEqualTo(4);
        assertThat(segment2.getFieldTestMinItems()).isEqualTo(1);
        assertThat(segment2.getFieldTestMaxItems()).isEqualTo(4);
        assertThat(segment2.getSubject()).isEqualTo(subject);
        assertThat(segment2.getStartAbility()).isEqualTo(0);
        assertThat(segment2.getFieldTestStartDate()).isEqualTo(segFtStartDate);
        assertThat(segment2.getFieldTestEndDate()).isEqualTo(segFtEndDate);
    }

    @Test
    public void shouldRetrieveFourConstraintsForAssessment() {
        final String assessmentId = "IRP-Perf-ELA-11";
        List<ItemConstraint> itemConstraints = repository.findItemConstraintsForAssessment("SBAC_PT", "IRP-Perf-ELA-11");
        assertThat(itemConstraints).hasSize(4);
        ItemConstraint languageConstraint = null;
        ItemConstraint erConstraint = null;

        for (ItemConstraint constraint : itemConstraints) {
            if (constraint.getPropertyValue().equals("ER")) {
                erConstraint = constraint;
            } else if (constraint.getPropertyName().equals("Language")) {
                languageConstraint = constraint;
            }
        }

        assertThat(languageConstraint).isNotNull();
        assertThat(erConstraint).isNotNull();

        assertThat(languageConstraint.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(languageConstraint.getPropertyName()).isEqualTo("Language");
        assertThat(languageConstraint.getPropertyValue()).isEqualTo("ENU");
        assertThat(languageConstraint.getToolType()).isEqualTo("Language");
        assertThat(languageConstraint.getToolValue()).isEqualTo("ENU");
        assertThat(languageConstraint.isInclusive()).isTrue();
        assertThat(erConstraint.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(erConstraint.getPropertyName()).isEqualTo("--ITEMTYPE--");
        assertThat(erConstraint.getPropertyValue()).isEqualTo("ER");
        assertThat(erConstraint.getToolType()).isEqualTo("Item Types Exclusion");
        assertThat(erConstraint.getToolValue()).isEqualTo("TDS_ItemTypeExcl_ER");
        assertThat(erConstraint.isInclusive()).isFalse();
    }

    @Test
    public void shouldRetrieveAssessmentsForGrade() {
        List<AssessmentInfo> assessments = repository.findAssessmentInfoForGrade("SBAC_PT", "11");
        assertThat(assessments).hasSize(1);
        AssessmentInfo assessmentInfo = assessments.get(0);
        assertThat(assessmentInfo.getId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(assessmentInfo.getMaxAttempts()).isEqualTo(99);
        assertThat(assessmentInfo.getLabel()).isEqualTo("Grade 11 ELA Perf");
        assertThat(assessmentInfo.getSubject()).isEqualTo("ELA");
        assertThat(assessmentInfo.getLanguages()).containsExactlyInAnyOrder("ENU", "FRN");
        assertThat(assessmentInfo.getGrades()).containsExactlyInAnyOrder("11");
    }

    @Test
    public void shouldRetrieveMultipleAssessments() {
        List<AssessmentInfo> assessments = repository.findAssessmentInfoByKeys("SBAC_PT",
            "(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(assessments).hasSize(2);

        AssessmentInfo assessmentInfo1 = null;
        AssessmentInfo assessmentInfo2 = null;

        for (AssessmentInfo assessmentInfo : assessments) {
            if (assessmentInfo.getKey().equals("(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015")) {
                assessmentInfo1 = assessmentInfo;
            } else if (assessmentInfo.getKey().equals("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")) {
                assessmentInfo2 = assessmentInfo;
            }
        }

        assertThat(assessmentInfo1.getId()).isEqualTo("SBAC-Mathematics-8");
        assertThat(assessmentInfo1.getMaxAttempts()).isEqualTo(95);
        assertThat(assessmentInfo1.getLabel()).isEqualTo("Grade 8 Math");
        assertThat(assessmentInfo1.getSubject()).isEqualTo("MATH");
        assertThat(assessmentInfo1.getLanguages()).containsExactly("ENU");
        assertThat(assessmentInfo1.getGrades()).containsExactly("Kindergarden");

        assertThat(assessmentInfo2.getId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(assessmentInfo2.getMaxAttempts()).isEqualTo(99);
        assertThat(assessmentInfo2.getLabel()).isEqualTo("Grade 11 ELA Perf");
        assertThat(assessmentInfo2.getSubject()).isEqualTo("ELA");
        assertThat(assessmentInfo2.getLanguages()).containsExactlyInAnyOrder("ENU", "FRN");
        assertThat(assessmentInfo2.getGrades()).containsExactlyInAnyOrder("11", "3");
    }

    @Test
    public void shouldRetrieveNoConstraintsForAssessment() {
        List<ItemConstraint> itemConstraints = repository.findItemConstraintsForAssessment("No client", "NoExam");
        assertThat(itemConstraints).isEmpty();
    }

    @Test
    public void shouldFindSegmentMetadataBySegmentKey() {
        final String segmentKey = "(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015";
        SegmentMetadata segmentMetadata = repository.findSegmentMetadata(segmentKey).get();

        assertThat(segmentMetadata.getParentKey()).isEqualTo("(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015");
        assertThat(segmentMetadata.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(segmentMetadata.getClientName()).isEqualTo("SBAC_PT");
    }

    @Test
    public void shouldReturnEmptyWhenSegmentMetadataCannotBeFound() {
        assertThat(repository.findSegmentMetadata("bogus")).isNotPresent();
    }
}
