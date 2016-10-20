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

import tds.assessment.SetOfAdminSubject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AdminSubjectQueryRepositoryImplIntegrationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminSubjectQueryRepository repository;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String tblClientInsertSQL = "INSERT INTO tblclient VALUES (1,'SBAC_PT',NULL,'/usr/local/tomcat/resources/tds/');";

        String tblSubjectInsertSQL = "INSERT INTO tblsubject VALUES ('ELA','','SBAC_PT-ELA',1,8185,NULL);";

        String tblSetOfAdminSubjectsInsertSQL = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016','SBAC_PT', 'SBAC_PT-ELA','IRP-Perf-ELA-11'," +
            "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";

        jdbcTemplate.update(tblClientInsertSQL);
        jdbcTemplate.update(tblSubjectInsertSQL);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL);
    }

    @Test
    public void shouldNotFindAdminSubjectByKey() {
        Optional<SetOfAdminSubject> maybeSetOfAdminObject = repository.findByKey("BOGUS");
        assertThat(maybeSetOfAdminObject).isNotPresent();
    }

    @Test
    public void shouldFindAdminSubjectByKey() {
        Optional<SetOfAdminSubject> maybeSetOfAdminObject = repository.findByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeSetOfAdminObject.get().getKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeSetOfAdminObject.get().getSelectionAlgorithm()).isEqualTo("virtual");
        assertThat(maybeSetOfAdminObject.get().getAssessmentId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(maybeSetOfAdminObject.get().getStartAbility()).isEqualTo(0F);
        assertThat(maybeSetOfAdminObject.get().getSubjectName()).isEqualTo("ELA");
    }
}
