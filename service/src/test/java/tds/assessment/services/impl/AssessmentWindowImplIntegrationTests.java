package tds.assessment.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentFormWindowProperties;
import tds.assessment.model.AssessmentWindowParameters;
import tds.assessment.repositories.AssessmentWindowQueryRepository;
import tds.assessment.services.AssessmentWindowService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
This test is here to verify caching is configured in the application.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(properties = "tds.cache.enabled = true")
public class AssessmentWindowImplIntegrationTests {
    @MockBean
    private AssessmentWindowQueryRepository mockAssessmentWindowQueryRepository;

    @Autowired
    private AssessmentWindowService assessmentWindowService;

    @Test
    public void shouldReturnCachedAssessmentWindow() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8-2018").build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(false, "SBAC_PT", "SBAC-Mathematics-8").build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8")).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows1 = assessmentWindowService.findAssessmentWindows(properties);
        List<AssessmentWindow> windows2 = assessmentWindowService.findAssessmentWindows(properties);

        assertThat(windows1).containsExactly(window, window2);
        assertThat(windows2).containsExactly(window, window2);
        verify(mockAssessmentWindowQueryRepository, times(1)).findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0);
        verify(mockAssessmentWindowQueryRepository, times(1)).findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8");
    }
}
