package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Optional;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.services.AdminSubjectService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AdminSubjectServiceImplIntegrationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminSubjectService adminSubjectService;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Test
    public void shouldNotFindAdminSubjectByKey() {
        Optional<SetOfAdminSubject> maybeSetOfAdminObject = adminSubjectService.findSetOfAdminByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeSetOfAdminObject).isNotPresent();
    }

    @Test
    public void shouldFindAdminSubjectByKey() {
        insertRecord();
        Optional<SetOfAdminSubject> maybeSetOfAdminObject = adminSubjectService.findSetOfAdminByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeSetOfAdminObject.get().getKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeSetOfAdminObject.get().getSelectionAlgorithm()).isEqualTo("virtual");
        assertThat(maybeSetOfAdminObject.get().getAssessmentId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(maybeSetOfAdminObject.get().getStartAbility()).isEqualTo(0F);
    }

    private void insertRecord() {
        String SQL = "INSERT INTO itembank.tblsetofadminsubjects VALUES (" +
            "'(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016'," +
            "'SBAC_PT'," +
            "'SBAC_PT-ELA'," +
            "'IRP-Perf-ELA-11'," +
            "0," +
            "1," +
            "4," +
            "4," +
            "1," +
            "1," +
            "NULL," +
            "NULL," +
            "0," +
            "0," +
            "NULL," +
            "'virtual'," +
            "NULL," +
            "5," +
            "1," +
            "20," +
            "1," +
            "5," +
            "NULL," +
            "NULL," +
            "1," +
            "1," +
            "8185," +
            "8185," +
            "5," +
            "0," +
            "'SBAC_PT'" +
            ",NULL," +
            "'ABILITY'," +
            "NULL," +
            "1," +
            "NULL," +
            "1," +
            "1," +
            "NULL," +
            "NULL," +
            "0," +
            "0," +
            "0," +
            "0," +
            "0," +
            "'bp1'," +
            "NULL," +
            "NULL," +
            "'summative');";

        jdbcTemplate.update(SQL, new HashMap<>());
    }
}
