package tds.assessment.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import tds.assessment.Strand;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class StrandQueryRepositoryImplIntegrationTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StrandQueryRepository repository;

    @Before
    public void setUp() {

        // Non-segmented, test
        final String tblSetOfAdminSubjectsInsertSQL1 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016','SBAC_PT', 'SBAC_PT-ELA','IRP-Perf-ELA-11'," +
            "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segmented test assessment
        final String tblSetOfAdminSubjectsInsertSQL2 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-Mathematics-8'," +
            "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segment 1
        final String tblSetOfAdminSubjectsInsertSQL2a = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG1-MATH-8'," +
            "0,1,4,4,1,1,NULL,NULL,2,3,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',1,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segment2
        final String tblSetOfAdminSubjectsInsertSQL2b = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG2-MATH-8'," +
            "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',2,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";

        final String insertStrandSQL =
            "INSERT INTO itembank.tbladminstrand\n" +
                "   (_fk_adminsubject, _key, _fk_strand, minitems, maxitems, adaptivecut)\n" +
                "VALUES \n" +
                "   ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 'Strand1-key', 'Strand1', 2, 7, -39.234),\n" +
                "   ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 'Strand2-key', 'Strand2', 0, 4, -37.432),\n" +
                "   ('(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015', 'Strand3-key', 'Strand3', 0, 2, NULL),\n" +
                "   ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'Strand4-key', 'Strand4', 1, 3, NULL),\n" +
                "   ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 'Strand5-key', 'Strand5', 0, 4, NULL)";

        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(insertStrandSQL);
    }

    @Test
    public void shouldFindBothStrandsForNonSegmented() {
        final String assessmentKey = "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016";
        Set<Strand> strands = repository.findStrands(assessmentKey);
        assertThat(strands).hasSize(2);

        Strand strand1 = null;
        Strand strand2 = null;

        for (Strand strand : strands) {
            if ("Strand1".equals(strand.getName())) {
                strand1 = strand;
            } else if ("Strand2".equals(strand.getName())) {
                strand2 = strand;
            }
        }

        assertThat(strand1.getKey()).isEqualTo("Strand1-key");
        assertThat(strand1.getMinItems()).isEqualTo(2);
        assertThat(strand1.getMaxItems()).isEqualTo(7);
        assertThat(strand1.getSegmentKey()).isEqualTo(assessmentKey);
        assertThat(strand1.getAdaptiveCut()).isEqualTo(-39.234F);

        assertThat(strand2.getKey()).isEqualTo("Strand2-key");
        assertThat(strand2.getMinItems()).isEqualTo(0);
        assertThat(strand2.getMaxItems()).isEqualTo(4);
        assertThat(strand2.getSegmentKey()).isEqualTo(assessmentKey);
        assertThat(strand2.getAdaptiveCut()).isEqualTo(-37.432F);
    }


    @Test
    public void shouldFindStrandsForAssessmentAndSegments() {
        final String assessmentKey = "(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015";
        Set<Strand> strands = repository.findStrands(assessmentKey);
        assertThat(strands).hasSize(3);

        Strand strand3 = null;
        Strand strand4 = null;
        Strand strand5 = null;

        for (Strand strand : strands) {
            if ("Strand3".equals(strand.getName())) {
                strand3 = strand;
            } else if ("Strand4".equals(strand.getName())) {
                strand4 = strand;
            } else if ("Strand5".equals(strand.getName())) {
                strand5 = strand;
            }
        }

        assertThat(strand3.getKey()).isEqualTo("Strand3-key");
        assertThat(strand3.getMinItems()).isEqualTo(0);
        assertThat(strand3.getMaxItems()).isEqualTo(2);
        assertThat(strand3.getSegmentKey()).isEqualTo(assessmentKey);
        assertThat(strand3.getAdaptiveCut()).isNull();
        assertThat(strand4.getKey()).isEqualTo("Strand4-key");

        assertThat(strand4.getMinItems()).isEqualTo(1);
        assertThat(strand4.getMaxItems()).isEqualTo(3);
        assertThat(strand4.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
        assertThat(strand4.getAdaptiveCut()).isNull();

        assertThat(strand5.getKey()).isEqualTo("Strand5-key");
        assertThat(strand5.getMinItems()).isEqualTo(0);
        assertThat(strand5.getMaxItems()).isEqualTo(4);
        assertThat(strand5.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(strand5.getAdaptiveCut()).isNull();
    }

    @Test
    public void shouldFindNoStrands() {
        Set<Strand> strands = repository.findStrands("AnotherKey");
        assertThat(strands).isEmpty();
    }
}
