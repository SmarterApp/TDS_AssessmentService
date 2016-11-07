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
                "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',1,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
        // Segment2
        String tblSetOfAdminSubjectsInsertSQL2b = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG2-MATH-8'," +
                "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',2,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";

        String tblitemPropsInsertSQL = "insert into itembank.tblitemprops (_fk_item, propname, propvalue, propdescription, _fk_adminsubject, isactive) \n" +
            "values \n" +
            "('item-99', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 1),\n" +
            "('item-1', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1),\n" +
            "('item-23', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 0),\n" +
            "('item-2', 'Language', 'Braille-ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1);";

        String testformInsertSQL =
                "INSERT INTO itembank.testform\n" +
                        "   (_fk_adminsubject, _efk_itsbank, _efk_itskey, formid, language, _key, itsid, iteration, loadconfig, updateconfig, cohort)\n" +
                        "VALUES \n" +
                        "   ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 528, 528, 'PracTest::MG8::S1::SP14', 'ENU', '187-528', NULL, 0, 8233, 8234, 'Default'),\n" +
                        "   ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 529, 529, 'PracTest::MG8::S1::SP14::Braille', 'ENU-Braille', '187-529', NULL, 0, 8233, 8234, 'Default'),\n" +
                        "   ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 530, 530, 'PracTest::MG8::S1::SP14::ESN', 'ESN', '187-530', NULL, 0, 8233, 8234, 'Default'),\n" +
                        "   ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 531, 531, 'PracTest::MG8::S2::SP14', 'ENU', '187-531', NULL, 0, 8233, NULL, 'Default'),\n" +
                        "   ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 532, 532, 'PracTest::MG8::S2::SP14::Braille', 'ENU-Braille', '187-532', NULL, 0, 8233, NULL, 'Default'),\n" +
                        "   ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 533, 533, 'PracTest::MG8::S2::SP14::ESN', 'ESN', '187-533', NULL, 0, 8233, NULL, 'Default'), \n " +
                        "   ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016',510, 510,'PracTest::MG4::S1::SP14','ENU','187-510',NULL,0,8233,NULL,'Default'), \n" +
                        "   ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016',511, 511,'PracTest::MG4::S1::SP14::Braille','ENU-Braille','187-511',NULL,0,8233,NULL,'Default'),\n" +
                        "   ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016',512, 512,'PracTest::MG4::S1::SP14::ESN','ESN','187-512',NULL,0,8233,NULL,'Default')\n";


        jdbcTemplate.update(tblClientInsertSQL);
        jdbcTemplate.update(tblSubjectInsertSQL);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(tblitemPropsInsertSQL);
        jdbcTemplate.update(testformInsertSQL);
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
        assertThat(seg.getLanguages()).hasSize(1);
        assertThat(seg.getLanguages()).containsOnly(new Property("Language", "ENU"));
        assertThat(seg.getSelectionAlgorithm()).isEqualTo(maybeAssessment.get().getSelectionAlgorithm());

        Form form1 = null;

        for(Form form : seg.getForms()) {
            if (form.getKey().equals("187-510")) {
                form1 = form;
            }
        }
        assertThat(seg.getForms()).hasSize(3);
        assertThat(form1.getKey()).isEqualTo("187-510");
        assertThat(form1.getCohort()).isEqualTo("Default");
        assertThat(form1.getId()).isEqualTo("PracTest::MG4::S1::SP14");
        assertThat(form1.getLanguage()).isEqualTo("ENU");
        assertThat(form1.getLoadVersion()).isEqualTo(8233L);
        assertThat(form1.getUpdateVersion()).isNull();
        assertThat(form1.getSegmentKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
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
        assertThat(segment2.getSubject()).isEqualTo(subject);
        assertThat(segment2.getLanguages()).hasSize(2);
        assertThat(segment2.getLanguages()).contains(new Property("Language", "ENU"), new Property("Language", "Braille-ENU"));
        assertThat(segment2.getStartAbility()).isEqualTo(0);

        List<Form> formsSeg1 = segment1.getForms();
        assertThat(formsSeg1.size()).isEqualTo(3);
        List<Form> formsSeg2 = segment2.getForms();
        assertThat(formsSeg2.size()).isEqualTo(3);

        List<Form> allSegments = new ArrayList<>();
        allSegments.addAll(formsSeg1);
        allSegments.addAll(formsSeg2);
        Form form1 = null;
        Form form2 = null;
        for(Form form : allSegments) {
            switch (form.getKey()) {
                case "187-528":
                    form1 = form;
                    break;
                case "187-531":
                    form2 = form;
                    break;
            }
        }

        assertThat(form1.getKey()).isEqualTo("187-528");
        assertThat(form1.getCohort()).isEqualTo("Default");
        assertThat(form1.getId()).isEqualTo("PracTest::MG8::S1::SP14");
        assertThat(form1.getLanguage()).isEqualTo("ENU");
        assertThat(form1.getLoadVersion()).isEqualTo(8233L);
        assertThat(form1.getUpdateVersion()).isEqualTo(8234L);
        assertThat(form1.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
        assertThat(form2.getKey()).isEqualTo("187-531");
        assertThat(form2.getCohort()).isEqualTo("Default");
        assertThat(form2.getId()).isEqualTo("PracTest::MG8::S2::SP14");
        assertThat(form2.getLanguage()).isEqualTo("ENU");
        assertThat(form2.getLoadVersion()).isEqualTo(8233L);
        assertThat(form2.getUpdateVersion()).isNull();
        assertThat(form2.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
    }

}
