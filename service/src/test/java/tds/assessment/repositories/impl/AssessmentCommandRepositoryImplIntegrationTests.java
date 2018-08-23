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
    private static final String TEST_ASSESSMENT_KEY2 = "(SBAC_PT)SBAC-Perf-ELA-8-Fall-2017-2018";

    @Autowired
    private AssessmentCommandRepository commandRepository;

    @Autowired
    private AssessmentQueryRepository queryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("classpath:(SBAC_PT)SBAC-IRP-CAT-ELA-3-Summer-2015-2016.xml")
    private Resource assessmentToRemove;

    @Value("classpath:(SBAC_PT)SBAC-Perf-ELA-8-Fall-2017-2018.xml")
    private Resource otherAssessment;

    @Before
    public void setup() throws IOException, InterruptedException {
        // Add the tblclient
        jdbcTemplate.execute("INSERT INTO itembank.tblclient (_key, name, description, homepath) VALUES (2, 'SBAC_PT', NULL, '/usr/local/tomcat/resources/tds/');");

        // Loads the test package
        jdbcTemplate.execute(String.format("CALL itembank.loader_main('%s')", Files.toString(assessmentToRemove.getFile(), Charsets.UTF_8)));
        jdbcTemplate.execute(String.format("CALL itembank.loader_main('%s')", Files.toString(otherAssessment.getFile(), Charsets.UTF_8)));

    }

    @Test
    public void shouldRemoveCATAssessmentSuccessfully() {
        // Make sure both loaded assessments exist
        Optional<Assessment> assessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(assessment.isPresent()).isTrue();
        Optional<Assessment> assessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(assessment2.isPresent()).isTrue();

        // Remove only the CAT assessment
        commandRepository.removeAssessmentData(TEST_CLIENT_NAME, assessment.get());

        // The second assessment's data should not be affected by the delete
        Optional<Assessment> retAssessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(retAssessment.isPresent()).isFalse();
        Optional<Assessment> retAssessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(retAssessment2.isPresent()).isTrue();
        assertThat(assessment2).isEqualTo(retAssessment2);
    }

    @Test
    public void shouldRemoveCATAssessmentFromItembankSuccessfully() {
        // Make sure both loaded assessments exist
        Optional<Assessment> assessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(assessment.isPresent()).isTrue();
        Optional<Assessment> assessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(assessment2.isPresent()).isTrue();

        // Remove only the CAT assessment
        commandRepository.removeItemBankAssessmentData(TEST_ASSESSMENT_KEY);

        // The second assessment's data should not be affected by the delete
        Optional<Assessment> retAssessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(retAssessment.isPresent()).isFalse();
        Optional<Assessment> retAssessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(retAssessment2.isPresent()).isTrue();
        assertThat(assessment2).isEqualTo(retAssessment2);
    }

    @Test
    public void shouldRemoveFixedFormAssessmentSuccessfully() {
        // Make sure both loaded assessments exist
        Optional<Assessment> assessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(assessment.isPresent()).isTrue();
        Optional<Assessment> assessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(assessment2.isPresent()).isTrue();

        // Remove only the CAT assessment
        commandRepository.removeAssessmentData(TEST_CLIENT_NAME, assessment2.get());

        // The second assessment's data should not be affected by the delete
        Optional<Assessment> retAssessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(retAssessment.isPresent()).isTrue();
        assertThat(assessment).isEqualTo(retAssessment);
        Optional<Assessment> retAssessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(retAssessment2.isPresent()).isFalse();
    }

    @Test
    public void shouldNotDeleteForWildcard() {
        // Make sure both loaded assessments exist
        Optional<Assessment> assessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(assessment.isPresent()).isTrue();
        Optional<Assessment> assessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(assessment2.isPresent()).isTrue();

        // Remove only the CAT assessment
        commandRepository.removeAssessmentData("*", assessment2.get());

        // The second assessment's data should not be affected by the delete
        Optional<Assessment> retAssessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(retAssessment.isPresent()).isTrue();
        assertThat(assessment).isEqualTo(retAssessment);
        Optional<Assessment> retAssessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(retAssessment2.isPresent()).isTrue();
        assertThat(assessment2).isEqualTo(retAssessment2);
    }

    @Test
    public void shouldNotDeleteForMismatchedClientname() {
        // Make sure both loaded assessments exist
        Optional<Assessment> assessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(assessment.isPresent()).isTrue();
        Optional<Assessment> assessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(assessment2.isPresent()).isTrue();

        // Remove only the CAT assessment
        commandRepository.removeAssessmentData("AnotherClient", assessment2.get());

        // The second assessment's data should not be affected by the delete
        Optional<Assessment> retAssessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(retAssessment.isPresent()).isTrue();
        assertThat(assessment).isEqualTo(retAssessment);
        Optional<Assessment> retAssessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(retAssessment2.isPresent()).isTrue();
        assertThat(assessment2).isEqualTo(retAssessment2);
    }

    @Test
    public void shouldDeleteBothAssessments() {
        // Make sure both loaded assessments exist
        Optional<Assessment> assessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(assessment.isPresent()).isTrue();
        Optional<Assessment> assessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(assessment2.isPresent()).isTrue();

        // Remove only the CAT assessment
        commandRepository.removeAssessmentData(TEST_CLIENT_NAME, assessment.get());
        commandRepository.removeAssessmentData(TEST_CLIENT_NAME, assessment2.get());

        // The second assessment's data should not be affected by the delete
        Optional<Assessment> retAssessment = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY);
        assertThat(retAssessment.isPresent()).isFalse();
        Optional<Assessment> retAssessment2 = queryRepository.findAssessmentByKey(TEST_CLIENT_NAME, TEST_ASSESSMENT_KEY2);
        assertThat(retAssessment2.isPresent()).isFalse();
    }
}
