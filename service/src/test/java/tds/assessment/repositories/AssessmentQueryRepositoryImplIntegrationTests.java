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
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Property;
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
            "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segmented test assessment
        String tblSetOfAdminSubjectsInsertSQL2 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-Mathematics-8'," +
                "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
        // Segment 1
        String tblSetOfAdminSubjectsInsertSQL2a = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG1-MATH-8'," +
                "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',NULL,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
        // Segment2
        String tblSetOfAdminSubjectsInsertSQL2b = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG2-MATH-8'," +
                "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',NULL,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";

        String tblitemPropsInsertSQL = "insert into itembank.tblitemprops (_fk_item, propname, propvalue, propdescription, _fk_adminsubject, isactive) \n" +
            "values \n" +
            "('item-99', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 1),\n" +
            "('item-1', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1),\n" +
            "('item-23', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 0),\n" +
            "('item-2', 'Language', 'Braille-ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1);";

        jdbcTemplate.update(tblClientInsertSQL);
        jdbcTemplate.update(tblSubjectInsertSQL);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(tblitemPropsInsertSQL);
    }

    @Test
    public void shouldNotFindAssessmentByKey() {
        Optional<Assessment> maybeSetOfAdminObject = repository.findAssessmentByKey("BOGUS");
        assertThat(maybeSetOfAdminObject).isNotPresent();
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
        assertThat(seg.getLanguages()).hasSize(1);
        assertThat(seg.getLanguages()).containsOnly(new Property("Language", "ENU"));
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
        assertThat(segment1.getSubject()).isEqualTo(subject);
        assertThat(segment1.getStartAbility()).isEqualTo(0);

        assertThat(segment2).isNotNull();
        assertThat(segment2.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(segment2.getKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(segment2.getSegmentId()).isEqualTo("SBAC-SEG2-MATH-8");
        assertThat(segment2.getSelectionAlgorithm()).isEqualTo("fixedform");
        assertThat(segment2.getSubject()).isEqualTo(subject);
        assertThat(segment2.getLanguages()).hasSize(2);
        assertThat(segment2.getLanguages()).contains(new Property("Language", "ENU"), new Property("Language", "Braille-ENU"));
        assertThat(segment2.getStartAbility()).isEqualTo(0);
    }
}
