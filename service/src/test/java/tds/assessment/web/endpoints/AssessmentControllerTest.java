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


        when(service.findAssessmentByKey(clientName, assessmentKey)).thenReturn(Optional.of(assessment));
        ResponseEntity<Assessment> response = controller.findAssessment(clientName, assessmentKey);
        verify(service).findAssessmentByKey(clientName, assessmentKey);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getKey()).isEqualTo("theKey");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowSubjectByKeyCannotBeFound() {
        final String clientName = "SBAC_PT";
        final String assessmentKey = "theKey";
        when(service.findAssessmentByKey(clientName, assessmentKey)).thenReturn(Optional.empty());
        controller.findAssessment(clientName, assessmentKey);
    }
}
