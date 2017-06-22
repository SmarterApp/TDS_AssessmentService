/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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
import java.util.Set;

import tds.assessment.ContentLevelSpecification;
import tds.assessment.Strand;
import tds.assessment.repositories.StrandQueryRepository;

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

        final String insertTblStrandSQL =
            "INSERT INTO tblstrand (_fk_subject, name, _key) VALUES ('SBAC-MATH', '1-IT', 'Strand5');";

        final String insertAdminStrandSQL =
            "INSERT INTO itembank.tbladminstrand\n" +
                "   (_fk_adminsubject, _key, _fk_strand, minitems, maxitems, adaptivecut)\n" +
                "VALUES \n" +
                "   ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 'Strand1-key', 'Strand1', 2, 7, -39.234),\n" +
                "   ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 'Strand2-key', 'Strand2', 0, 4, -37.432),\n" +
                "   ('(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015', 'Strand3-key', 'Strand3', 0, 2, NULL),\n" +
                "   ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'Strand4-key', 'Strand4', 1, 3, NULL),\n" +
                "   ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 'Strand5-key', 'Strand5', 0, 4, NULL)";

        final String insertAffinityGroupSQL =
            "INSERT INTO affinitygroup (_fk_adminsubject, groupid, minitems, maxitems, isstrictmax, weight, precisiontarget, startability) " +
                "VALUES ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 'MATHG8_Test3_S2_Claim2_EE_T136', 99, 98, 1, 97.2, 96.1, 100)";

        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(insertTblStrandSQL);
        jdbcTemplate.update(insertAdminStrandSQL);
        jdbcTemplate.update(insertAffinityGroupSQL);
    }

    @Test
    public void shouldFindContentLevelSpecificationsBySegmentKey() {
       final String segmentKey = "(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015";

       List<ContentLevelSpecification> specifications = repository.findContentLevelSpecificationsBySegmentKey(segmentKey);

       assertThat(specifications).hasSize(2);

       ContentLevelSpecification affinitySpec = null;
       ContentLevelSpecification strandSpec = null;

       for(ContentLevelSpecification spec : specifications) {
           if("MATHG8_Test3_S2_Claim2_EE_T136".equals(spec.getContentLevel())) {
               affinitySpec = spec;
           } else {
               strandSpec = spec;
           }
       }

       assertThat(affinitySpec).isNotNull();
       assertThat(affinitySpec.getContentLevel()).isEqualTo("MATHG8_Test3_S2_Claim2_EE_T136");
       assertThat(affinitySpec.isReportingCategory()).isTrue();
       assertThat(affinitySpec.getElementType()).isEqualTo(2);

       assertThat(strandSpec).isNotNull();
       assertThat(strandSpec.getContentLevel()).isEqualTo("Strand5");
       assertThat(strandSpec.isReportingCategory()).isFalse();
       assertThat(strandSpec.getElementType()).isEqualTo(0);
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

        assertThat(strand1).isNotNull();
        assertThat(strand1.getKey()).isEqualTo("Strand1-key");
        assertThat(strand1.getMinItems()).isEqualTo(2);
        assertThat(strand1.getMaxItems()).isEqualTo(7);
        assertThat(strand1.getSegmentKey()).isEqualTo(assessmentKey);
        assertThat(strand1.getAdaptiveCut()).isEqualTo(-39.234F);
        assertThat(strand1.getBpWeight()).isEqualTo(1f);
        assertThat(strand1.getAbilityWeight()).isNull();
        assertThat(strand1.getPrecisionTarget()).isNull();
        assertThat(strand1.getPrecisionTargetMetWeight()).isNull();
        assertThat(strand1.getPrecisionTargetNotMetWeight()).isNull();
        assertThat(strand1.getScalar()).isNull();
        assertThat(strand1.getStartInfo()).isNull();

        assertThat(strand2).isNotNull();
        assertThat(strand2.getKey()).isEqualTo("Strand2-key");
        assertThat(strand2.getMinItems()).isEqualTo(0);
        assertThat(strand2.getMaxItems()).isEqualTo(4);
        assertThat(strand2.getSegmentKey()).isEqualTo(assessmentKey);
        assertThat(strand2.getAdaptiveCut()).isEqualTo(-37.432F);
        assertThat(strand2.getBpWeight()).isEqualTo(1f);
        assertThat(strand2.getAbilityWeight()).isNull();
        assertThat(strand2.getPrecisionTarget()).isNull();
        assertThat(strand2.getPrecisionTargetMetWeight()).isNull();
        assertThat(strand2.getPrecisionTargetNotMetWeight()).isNull();
        assertThat(strand2.getScalar()).isNull();
        assertThat(strand2.getStartInfo()).isNull();
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

        assertThat(strand3).isNotNull();
        assertThat(strand4).isNotNull();
        assertThat(strand5).isNotNull();

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
