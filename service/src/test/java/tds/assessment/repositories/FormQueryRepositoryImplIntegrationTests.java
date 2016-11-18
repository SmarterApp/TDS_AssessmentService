package tds.assessment.repositories;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import tds.assessment.Form;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class FormQueryRepositoryImplIntegrationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private FormQueryRepository repository;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
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

        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(testformInsertSQL);
    }

    @Test
    public void shouldFindFormsForNonSegAssessment() {
        List<Form> forms = repository.findFormsForAssessment("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        Form form1 = null;

        for(Form form : forms) {
            if (form.getKey().equals("187-510")) {
                form1 = form;
            }
        }
        assertThat(forms).hasSize(3);
        assertThat(form1.getKey()).isEqualTo("187-510");
        assertThat(form1.getCohort()).isEqualTo("Default");
        assertThat(form1.getId()).isEqualTo("PracTest::MG4::S1::SP14");
        assertThat(form1.getLanguage()).isEqualTo("ENU");
        assertThat(form1.getLoadVersion()).isEqualTo(8233L);
        assertThat(form1.getUpdateVersion()).isNull();
        assertThat(form1.getSegmentKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
    }

    @Test
    public void shouldFindFormsForSegmentedAssessment() {
        List<Form> forms = repository.findFormsForAssessment("(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015");

        Form form1 = null;
        Form form2 = null;
        for(Form form : forms) {
            switch (form.getKey()) {
                case "187-528":
                    form1 = form;
                    break;
                case "187-531":
                    form2 = form;
                    break;
            }
        }

        // Validate forms in each segment
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
