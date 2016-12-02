package tds.assessment.repositories;

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

import tds.assessment.Algorithm;
import tds.assessment.Assessment;
import tds.assessment.ItemConstraint;
import tds.assessment.Segment;
import tds.common.data.mapping.ResultSetMapperUtility;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AssessmentQueryRepositoryImplIntegrationTests {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private AssessmentQueryRepository repository;

    private final Instant segFtStartDate = Instant.now().minus(300000);
    private final Instant segFtEndDate = Instant.now().plus(300000);

    @Before
    public void setUp() {

        String tblClientInsertSQL = "INSERT INTO tblclient VALUES (1,'SBAC_PT',NULL,'/usr/local/tomcat/resources/tds/');";

        String tblSubjectInsertSQL = "INSERT INTO tblsubject VALUES ('ELA','','SBAC_PT-ELA',1,8185,NULL);";

        // Non-segmented, test
        String tblSetOfAdminSubjectsInsertSQL1 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016','SBAC_PT', 'SBAC_PT-ELA','IRP-Perf-ELA-11'," +
            "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segmented test assessment
        String tblSetOfAdminSubjectsInsertSQL2 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-Mathematics-8'," +
                "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
        // Segment 1
        String tblSetOfAdminSubjectsInsertSQL2a = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG1-MATH-8'," +
                "0,1,4,4,1,1,NULL,NULL,2,3,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',1,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
        // Segment2
        String tblSetOfAdminSubjectsInsertSQL2b = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG2-MATH-8'," +
                "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',2,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
        // Segment2 - SBAC client
        String tblSetOfAdminSubjectsSBACInsertSQL = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC', 'SBAC-ELA','SBAC-SEG2-MATH-8'," +
            "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC)SBAC-Mathematics-8-Spring-2013-2015',2,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";


        final String itemConstraintsInsertSql =
                "INSERT INTO configs.client_test_itemconstraint" +
                        "(clientname, testid, propname, propvalue, tooltype, toolvalue, item_in) VALUES " +
                        "('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'ER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_ER', 0), \n" +
                        "('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'MI', 'Item Types Exclusion', 'TDS_ItemTypeExcl_MI', 0), \n" +
                        "('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'WER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_WER', 0), \n" +
                        "('SBAC_PT', 'IRP-Perf-ELA-11', 'Language', 'ENU', 'Language', 'ENU', 1), \n" +
                        "('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'ER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_ER', 0), \n" +
                        "('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'MI', 'Item Types Exclusion', 'TDS_ItemTypeExcl_MI', 0), \n" +
                        "('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'WER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_WER', 0), \n" +
                        "('SBAC_PT', 'IRP-Perf-ELA-3', 'Language', 'ENU', 'Language', 'ENU', 1)";

        SqlParameterSource parameters = new MapSqlParameterSource("ftstartDate", ResultSetMapperUtility.mapJodaInstantToTimestamp(Instant.now()));
        final String clientTestPropertiesInsertSQL = "INSERT INTO configs.client_testproperties (clientname, testid, ftstartdate, accommodationfamily, maxopportunities, abilityslope, abilityintercept) VALUES " +
            "('SBAC_PT', 'IRP-Perf-ELA-11', :ftstartDate, 'family', 99, 1.5, 2.3), \n" +
            "('SBAC_PT', 'SBAC-Mathematics-8', :ftstartDate, 'otherFamily', 95, 5.5, 6.3);\n";

        SqlParameterSource segPropsParams = new MapSqlParameterSource("ftstartdate", ResultSetMapperUtility.mapJodaInstantToTimestamp(segFtStartDate))
            .addValue("ftenddate", ResultSetMapperUtility.mapJodaInstantToTimestamp(segFtEndDate));
        final String clientSegmentPropertiesInsertSQL =
            "INSERT INTO configs.client_segmentproperties (ispermeable, clientname, entryapproval, exitapproval, itemreview, segmentid, segmentposition, parenttest, ftstartdate, ftenddate, label, modekey) VALUES " +
            "(1, 'SBAC_PT', 0, 0, 0, 'SBAC-SEG1-MATH-8', 1, 'SBAC-Mathematics-8', :ftstartdate, :ftenddate, 'Grade 8 MATH segment', '(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015'), \n" +
            "(1, 'SBAC_PT', 0, 0, 0, 'SBAC-SEG2-MATH-8', 2, 'SBAC-Mathematics-8', :ftstartdate, :ftenddate, 'Grade 8 MATH segment', '(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015')";

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

        Segment segment1 = null;
        Segment segment2 = null;

        for(Segment segment : assessment.getSegments()) {
            if(segment.getKey().equals("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015")) {
                segment1 = segment;
            } else {
                segment2 = segment;
            }
        }

        assertThat(segment1).isNotNull();
        assertThat(segment1.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(segment1.getKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
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
    public void shouldRetrieveNoConstraintsForAssessment() {
        List<ItemConstraint> itemConstraints = repository.findItemConstraintsForAssessment("No client", "NoExam");
        assertThat(itemConstraints).isEmpty();
    }

}
