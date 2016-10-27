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
import java.util.ArrayList;
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
        Assessment subject = new Assessment("theKey", "assessment", new ArrayList<>(), "alg", 50F);

        when(service.findAssessmentByKey("theKey")).thenReturn(Optional.of(subject));
        ResponseEntity<Assessment> response = controller.findAssessment("theKey");
        verify(service).findAssessmentByKey("theKey");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getKey()).isEqualTo("theKey");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowSubjectByKeyCannotBeFound() {
        when(service.findAssessmentByKey("theKey")).thenReturn(Optional.empty());
        controller.findAssessment("theKey");
    }
}
