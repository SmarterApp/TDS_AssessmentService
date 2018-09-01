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

package tds.assessment.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.services.AssessmentService;
import tds.common.web.exceptions.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentControllerTest {
    private AssessmentController controller;
    @Mock
    private AssessmentService service;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        controller = new AssessmentController(service);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindAssessmentByKey() {
        final String clientName = "SBAC_PT";
        final String assessmentKey = "theKey";
        Assessment assessment = new Assessment();
        assessment.setKey(assessmentKey);


        when(service.findAssessment(clientName, assessmentKey)).thenReturn(Optional.of(assessment));
        ResponseEntity<Assessment> response = controller.findAssessment(clientName, assessmentKey);
        verify(service).findAssessment(clientName, assessmentKey);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getKey()).isEqualTo("theKey");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowSubjectByKeyCannotBeFound() {
        final String clientName = "SBAC_PT";
        final String assessmentKey = "theKey";
        when(service.findAssessment(clientName, assessmentKey)).thenReturn(Optional.empty());
        controller.findAssessment(clientName, assessmentKey);
    }

    @Test
    public void shouldRemoveAssessment() {
        final String clientName = "SBAC_PT";
        final String assessmentKey = "theKey";
        controller.removeAssessment(clientName, assessmentKey);
        verify(service).removeAssessment(clientName, true, assessmentKey);
    }
}
