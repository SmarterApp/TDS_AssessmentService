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

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.repositories.AssessmentCommandRepository;
import tds.assessment.repositories.AssessmentQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AssessmentCommandRepositoryImplIntegrationTests {
    private static final String TEST_CLIENT_NAME = "SBAC_PT";
    private static final String TEST_ASSESSMENT_KEY = "(SBAC_PT)SBAC-IRP-CAT-ELA-3-Summer-2015-2016";

    @Autowired
    private AssessmentCommandRepository commandRepository;

    @Autowired
    private AssessmentQueryRepository queryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("classpath:(SBAC_PT)SBAC-IRP-CAT-ELA-3-Summer-2015-2016.xml")
    private Resource res;

    @Before
    public void setup() throws IOException {
        // Loads the test package
        jdbcTemplate.execute(String.format("CALL itembank.loader_main('%s')", Files.toString(res.getFile(), Charsets.UTF_8)));
    }

    @Test
    public void testRepository() {
        Optional<Assessment> assessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(assessment.isPresent()).isTrue();

        commandRepository.removeAssessmentData(TEST_CLIENT_NAME, assessment.get());

        Optional<Assessment> retAssessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(retAssessment.isPresent()).isFalse();
    }
}
