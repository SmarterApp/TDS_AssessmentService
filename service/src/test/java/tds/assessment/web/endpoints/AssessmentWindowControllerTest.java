package tds.assessment.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentWindowParameters;
import tds.assessment.services.AssessmentWindowService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentWindowControllerTest {
    private AssessmentWindowController controller;

    @Mock
    private AssessmentWindowService mockAssessmentWindowService;

    @Before
    public void setUp() {
        controller = new AssessmentWindowController(mockAssessmentWindowService);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindListOfAssessmentWindows() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withAssessmentKey("assessment")
            .withWindowId("windowId")
            .build();

        when(mockAssessmentWindowService.findAssessmentWindows(isA(AssessmentWindowParameters.class))).thenReturn(Collections.singletonList(window));

        ResponseEntity<List<AssessmentWindow>> response = controller.findAssessmentWindows("SBAC_PT",
            "assessment",
            false,
            25,
            50,
            75,
            100,
            "wid:formKey");

        ArgumentCaptor<AssessmentWindowParameters> assessmentWindowParametersArgumentCaptor = ArgumentCaptor.forClass(AssessmentWindowParameters.class);
        verify(mockAssessmentWindowService).findAssessmentWindows(assessmentWindowParametersArgumentCaptor.capture());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(window);

        AssessmentWindowParameters parameters = assessmentWindowParametersArgumentCaptor.getValue();
        assertThat(parameters.getAssessmentId()).isEqualTo("assessment");
        assertThat(parameters.getClientName()).isEqualTo("SBAC_PT");
        assertThat(parameters.isGuestStudent()).isFalse();
        assertThat(parameters.getShiftWindowStart()).isEqualTo(25);
        assertThat(parameters.getShiftWindowEnd()).isEqualTo(50);
        assertThat(parameters.getShiftFormStart()).isEqualTo(75);
        assertThat(parameters.getShiftFormEnd()).isEqualTo(100);
        assertThat(parameters.getFormList()).isEqualTo("wid:formKey");
    }

    @Test
    public void shouldFindListOfAssessmentWindowsWithNullArguments() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .build();

        when(mockAssessmentWindowService.findAssessmentWindows(isA(AssessmentWindowParameters.class))).thenReturn(Collections.singletonList(window));

        ResponseEntity<List<AssessmentWindow>> response = controller.findAssessmentWindows("SBAC",
            "assessment",
            false,
            null,
            null,
            null,
            null,
            null);

        ArgumentCaptor<AssessmentWindowParameters> assessmentWindowParametersArgumentCaptor = ArgumentCaptor.forClass(AssessmentWindowParameters.class);
        verify(mockAssessmentWindowService).findAssessmentWindows(assessmentWindowParametersArgumentCaptor.capture());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(window);

        AssessmentWindowParameters parameters = assessmentWindowParametersArgumentCaptor.getValue();
        assertThat(parameters.getShiftWindowStart()).isEqualTo(0);
        assertThat(parameters.getShiftWindowEnd()).isEqualTo(0);
        assertThat(parameters.getShiftFormStart()).isEqualTo(0);
        assertThat(parameters.getShiftFormEnd()).isEqualTo(0);
        assertThat(parameters.getFormList()).isNull();
    }
}
