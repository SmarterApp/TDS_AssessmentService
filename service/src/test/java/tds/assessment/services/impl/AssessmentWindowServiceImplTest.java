package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentFormWindowProperties;
import tds.assessment.model.AssessmentWindowParameters;
import tds.assessment.repositories.AssessmentWindowQueryRepository;
import tds.assessment.services.AssessmentWindowService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentWindowServiceImplTest {
    private AssessmentWindowService assessmentWindowService;

    @Mock
    private AssessmentWindowQueryRepository mockAssessmentWindowQueryRepository;

    @Before
    public void setUp() {
        assessmentWindowService = new AssessmentWindowServiceImpl(mockAssessmentWindowQueryRepository);
    }

    @Test
    public void shouldReturnEmptyWindowWhenNoResultsAreFoundForGuest() {
        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(-1, "test", "assessment").build();
        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("test", "assessment", 0, 0)).thenReturn(Collections.emptyList());
        assertThat(assessmentWindowService.findAssessmentWindows(properties)).isEmpty();
    }

    @Test
    public void shouldReturnWindowForGuestWhenFound() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").build();
        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(-1, "test", "assessment").build();

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("test", "assessment", 0, 0)).thenReturn(Collections.singletonList(window));
        assertThat(assessmentWindowService.findAssessmentWindows(properties).get(0)).isEqualTo(window);
        verify(mockAssessmentWindowQueryRepository).findCurrentAssessmentWindows("test", "assessment", 0, 0);
    }

    @Test
    public void shouldReturnDistinctWindowsWhenWindowIsNotRequired() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window3 = new AssessmentWindow.Builder().withWindowId("id3").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window4 = new AssessmentWindow.Builder().withWindowId("id4").withAssessmentKey("SBAC-Mathematics-3").build();
        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8").build();

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0)).thenReturn(Arrays.asList(window, window2, window3, window4));
        List<AssessmentWindow> windows = assessmentWindowService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window, window3, window4);
    }

    @Test
    public void shouldReturnAllFormWindowsByDefault() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window3 = new AssessmentWindow.Builder().withWindowId("id3").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window4 = new AssessmentWindow.Builder().withWindowId("id4").withAssessmentKey("SBAC-Mathematics-3").build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8").build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2, window3, window4));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8")).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows = assessmentWindowService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window, window2, window3, window4);
    }

    @Test
    public void shouldReturnDistinctFormWindowsByFormKey() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("id")
            .withFormKey("formKey1")
            .withAssessmentKey("SBAC-Mathematics-8")
            .build();

        AssessmentWindow window2 = new AssessmentWindow.Builder()
            .withWindowId("id2")
            .withFormKey("formKey2")
            .withAssessmentKey("SBAC-Mathematics-8-2018")
            .build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8")
            .withFormList("formKey2")
            .build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8")).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows = assessmentWindowService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window2);
    }

    @Test
    public void shouldReturnDistinctFormWindowsByWindowIdAndFormKey() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("id")
            .withFormKey("formKey")
            .withAssessmentKey("SBAC-Mathematics-8")
            .build();

        AssessmentWindow window2 = new AssessmentWindow.Builder()
            .withWindowId("id2")
            .withFormKey("formKey")
            .withAssessmentKey("SBAC-Mathematics-8-2018")
            .build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8")
            .withFormList("id:formKey")
            .build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8")).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows = assessmentWindowService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window);
    }

}
