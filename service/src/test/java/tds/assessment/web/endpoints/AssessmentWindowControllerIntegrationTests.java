package tds.assessment.web.endpoints;

import org.joda.time.Days;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentWindowParameters;
import tds.assessment.services.AssessmentWindowService;
import tds.common.web.advice.ExceptionAdvice;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AssessmentWindowController.class)
@Import(ExceptionAdvice.class)
public class AssessmentWindowControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private AssessmentWindowService mockAssessmentWindowService;

    @Test
    public void shouldReturnAssessmentWindows() throws Exception {
        Instant startTime = Instant.now();
        Instant endTime = Instant.now().plus(Days.days(20).toStandardDuration());
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("windowId")
            .withAssessmentKey("SLA 11")
            .withMode("mode")
            .withWindowMaxAttempts(3)
            .withWindowSessionType(0)
            .withStartTime(startTime)
            .withEndTime(endTime)
            .withFormKey("formKey")
            .withModeMaxAttempts(5)
            .withModeSessionType(-1)
            .build();


        when(mockAssessmentWindowService.findAssessmentWindows(isA(AssessmentWindowParameters.class))).
            thenReturn(Collections.singletonList(window));

        String url = "/SBAC_PT/assessments/math11/windows/student/23";

        http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(1)))
            .andExpect(jsonPath("[0].windowId", is("windowId")))
            .andExpect(jsonPath("[0].assessmentKey", is("SLA 11")))
            .andExpect(jsonPath("[0].windowMaxAttempts", is(3)))
            .andExpect(jsonPath("[0].windowSessionType", is(0)))
            .andExpect(jsonPath("[0].formKey", is("formKey")))
            .andExpect(jsonPath("[0].modeMaxAttempts", is(5)))
            .andExpect(jsonPath("[0].modeSessionType", is(-1)));
    }
}
