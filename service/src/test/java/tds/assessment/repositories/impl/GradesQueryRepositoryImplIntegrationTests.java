package tds.assessment.repositories.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import tds.assessment.repositories.GradesQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class GradesQueryRepositoryImplIntegrationTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GradesQueryRepository repository;

    @Before
    public void setUp() {

        final String setOfTestGradesInsertSql =
            "INSERT INTO itembank.setoftestgrades (testid, grade, _fk_adminsubject, _key) " +
                "VALUES ('IRP-Perf-ELA-11', '3', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 0x8ebc29966a52401b952e45b1a2d08e3f), " +
                "('IRP-Perf-ELA-11', '11', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 0x8ebc29966a52401b952e45b1a2d08e3e)," +
                "('IRP-Perf-ELA-11', 'Kindergarden', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 0x8ebc29966a52401b952e45b1a2d08e3c)";

        jdbcTemplate.update(setOfTestGradesInsertSql);
    }

    @Test
    public void shouldFindMultipleGradesForAssessmentKey() {
        List<String> grades = repository.findGrades("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(grades).hasSize(3);

        // should be ordered
    }

}
