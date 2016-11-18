package tds.assessment.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AssessmentQueryRepositoryImplIntegrationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AssessmentQueryRepository repository;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

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

        final String tblItemPropsInsertSQL = "insert into itembank.tblitemprops (_fk_item, propname, propvalue, propdescription, _fk_adminsubject, isactive) \n" +
                "values \n" +
                "('item-99', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 1),\n" +
                "('item-1', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1),\n" +
                "('item-23', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 0),\n" +
                "('item-2', 'Language', 'Braille-ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1);";

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

        jdbcTemplate.update(itemConstraintsInsertSql);
        jdbcTemplate.update(tblClientInsertSQL);
        jdbcTemplate.update(tblSubjectInsertSQL);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(tblItemPropsInsertSQL);
    }

    @Test
    public void shouldNotFindAssessmentByKey() {
        Optional<Assessment> maybeAssessment = repository.findAssessmentByKey("BOGUS");
        assertThat(maybeAssessment).isNotPresent();
    }

    @Test
    public void shouldFindNonSegmentedAssessmentByKey() {
        Optional<Assessment> maybeAssessment = repository.findAssessmentByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeAssessment.get().getKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeAssessment.get().getSelectionAlgorithm()).isEqualTo("virtual");
        assertThat(maybeAssessment.get().getAssessmentId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(maybeAssessment.get().getStartAbility()).isEqualTo(0F);
        assertThat(maybeAssessment.get().getSubject()).isEqualTo("ELA");
        assertThat(maybeAssessment.get().isSegmented()).isFalse();
        assertThat(maybeAssessment.get().getSegments().size()).isEqualTo(1);
        Segment seg = maybeAssessment.get().getSegments().get(0);
        assertThat(seg.getSegmentId()).isEqualTo(maybeAssessment.get().getAssessmentId());
        assertThat(seg.getKey()).isEqualTo(maybeAssessment.get().getKey());
        assertThat(seg.getStartAbility()).isEqualTo(maybeAssessment.get().getStartAbility());
        assertThat(seg.getAssessmentKey()).isEqualTo(maybeAssessment.get().getKey());
        assertThat(seg.getSubject()).isEqualTo(maybeAssessment.get().getSubject());
        assertThat(seg.getPosition()).isEqualTo(1);
        assertThat(seg.getMinItems()).isEqualTo(4);
        assertThat(seg.getMaxItems()).isEqualTo(4);
        assertThat(seg.getFieldTestMinItems()).isEqualTo(1);
        assertThat(seg.getFieldTestMaxItems()).isEqualTo(4);
        assertThat(seg.getLanguages()).hasSize(1);
        assertThat(seg.getLanguages()).containsOnly(new ItemProperty("Language", "ENU"));
        assertThat(seg.getSelectionAlgorithm()).isEqualTo(maybeAssessment.get().getSelectionAlgorithm());
    }

    @Test
    public void shouldFindSegmentedAssessmentByKey() {
        final String assessmentKey = "(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015";
        final String subject = "ELA";
        Optional<Assessment> maybeAssessment = repository.findAssessmentByKey(assessmentKey);
        assertThat(maybeAssessment.get().getKey()).isEqualTo(assessmentKey);
        assertThat(maybeAssessment.get().getSelectionAlgorithm()).isEqualTo("virtual");
        assertThat(maybeAssessment.get().getAssessmentId()).isEqualTo("SBAC-Mathematics-8");
        assertThat(maybeAssessment.get().getStartAbility()).isEqualTo(0F);
        assertThat(maybeAssessment.get().getSubject()).isEqualTo(subject);
        assertThat(maybeAssessment.get().isSegmented()).isTrue();
        assertThat(maybeAssessment.get().getSegments()).hasSize(2);

        Segment segment1 = null;
        Segment segment2 = null;

        for(Segment segment : maybeAssessment.get().getSegments()) {
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
        assertThat(segment1.getSelectionAlgorithm()).isEqualTo("fixedform");
        assertThat(segment1.getPosition()).isEqualTo(1);
        assertThat(segment1.getMinItems()).isEqualTo(4);
        assertThat(segment1.getMaxItems()).isEqualTo(4);
        assertThat(segment1.getFieldTestMinItems()).isEqualTo(2);
        assertThat(segment1.getFieldTestMaxItems()).isEqualTo(3);
        assertThat(segment1.getSubject()).isEqualTo(subject);
        assertThat(segment1.getStartAbility()).isEqualTo(0);

        assertThat(segment2).isNotNull();
        assertThat(segment2.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(segment2.getKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(segment2.getSegmentId()).isEqualTo("SBAC-SEG2-MATH-8");
        assertThat(segment2.getSelectionAlgorithm()).isEqualTo("fixedform");
        assertThat(segment2.getPosition()).isEqualTo(2);
        assertThat(segment2.getMinItems()).isEqualTo(4);
        assertThat(segment2.getMaxItems()).isEqualTo(4);
        assertThat(segment2.getFieldTestMinItems()).isEqualTo(1);
        assertThat(segment2.getFieldTestMaxItems()).isEqualTo(4);
        assertThat(segment2.getSubject()).isEqualTo(subject);
        assertThat(segment2.getLanguages()).hasSize(2);
        assertThat(segment2.getLanguages()).contains(new ItemProperty("Language", "ENU"), new ItemProperty("Language", "Braille-ENU"));
        assertThat(segment2.getStartAbility()).isEqualTo(0);
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
                continue;
            } else if (constraint.getPropertyName().equals("Language")) {
                languageConstraint = constraint;
                continue;
            }
        }

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
