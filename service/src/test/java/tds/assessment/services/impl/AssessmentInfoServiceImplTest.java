package tds.assessment.services.impl;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tds.assessment.AssessmentInfo;
import tds.assessment.AssessmentWindow;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.services.AssessmentInfoService;
import tds.assessment.services.AssessmentWindowService;

import static io.github.benas.randombeans.api.EnhancedRandom.randomListOf;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentInfoServiceImplTest {
    private AssessmentInfoService assessmentInfoService;

    @Mock
    private AssessmentQueryRepository mockAssessmentQueryRepository;

    @Mock
    private AssessmentWindowService mockAssessmentWindowService;

    @Before
    public void setup() {
        assessmentInfoService = new AssessmentInfoServiceImpl(mockAssessmentQueryRepository, mockAssessmentWindowService);
    }

    @Test
    public void shouldReturnAssessmentInfos() {
        List<AssessmentInfo> assessmentInfoList = randomListOf(2, AssessmentInfo.class);
        List<AssessmentWindow> assessmentWindows1 = randomListOf(2, AssessmentWindow.class);
        List<AssessmentWindow> assessmentWindows2 = randomListOf(2, AssessmentWindow.class);
        Map<String, List<AssessmentWindow>> assessmentWindowsMap = ImmutableMap.of(
            assessmentInfoList.get(0).getKey(), assessmentWindows1,
            assessmentInfoList.get(1).getKey(), assessmentWindows2
        );

        when(mockAssessmentQueryRepository.findAssessmentInfoByKeys("SBAC_PT", assessmentInfoList.get(0).getKey(), assessmentInfoList.get(1).getKey()))
            .thenReturn(assessmentInfoList);
        when(mockAssessmentWindowService.findAssessmentWindowsForAssessmentIds(any(), Matchers.<String>anyVararg()))
            .thenReturn(assessmentWindowsMap);
        List<AssessmentInfo> assessmentInfos = assessmentInfoService.findAssessmentInfo("SBAC_PT", assessmentInfoList.get(0).getKey(), assessmentInfoList.get(1).getKey());

        assertThat(assessmentInfos).hasSize(2);
        AssessmentInfo retInfo1 = assessmentInfos.get(0);
        AssessmentInfo retInfo2 = assessmentInfos.get(1);

        assertThat(retInfo1.getId()).isEqualTo(assessmentInfoList.get(0).getId());
        assertThat(retInfo1.getKey()).isEqualTo(assessmentInfoList.get(0).getKey());
        assertThat(retInfo1.getSubject()).isEqualTo(assessmentInfoList.get(0).getSubject());
        assertThat(retInfo1.getAssessmentWindows()).isEqualTo(assessmentWindows1);
        assertThat(retInfo1.getLanguages()).isEqualTo(assessmentInfoList.get(0).getLanguages());
        assertThat(retInfo1.getMaxAttempts()).isEqualTo(assessmentInfoList.get(0).getMaxAttempts());
        assertThat(retInfo1.getGrades()).isEqualTo(assessmentInfoList.get(0).getGrades());

        assertThat(retInfo2.getId()).isEqualTo(assessmentInfoList.get(1).getId());
        assertThat(retInfo2.getKey()).isEqualTo(assessmentInfoList.get(1).getKey());
        assertThat(retInfo2.getSubject()).isEqualTo(assessmentInfoList.get(1).getSubject());
        assertThat(retInfo2.getAssessmentWindows()).isEqualTo(assessmentWindows2);
        assertThat(retInfo2.getLanguages()).isEqualTo(assessmentInfoList.get(1).getLanguages());
        assertThat(retInfo2.getMaxAttempts()).isEqualTo(assessmentInfoList.get(1).getMaxAttempts());
        assertThat(retInfo2.getGrades()).isEqualTo(assessmentInfoList.get(1).getGrades());
    }

    @Test
    public void shouldReturnAssessmentInfosForGrade() {
        List<AssessmentInfo> assessmentInfoList = randomListOf(2, AssessmentInfo.class);
        List<AssessmentWindow> assessmentWindows1 = randomListOf(2, AssessmentWindow.class);
        List<AssessmentWindow> assessmentWindows2 = randomListOf(2, AssessmentWindow.class);
        final String grade = "3";
        Map<String, List<AssessmentWindow>> assessmentWindowsMap = ImmutableMap.of(
            assessmentInfoList.get(0).getKey(), assessmentWindows1,
            assessmentInfoList.get(1).getKey(), assessmentWindows2
        );

        when(mockAssessmentQueryRepository.findAssessmentInfoForGrade("SBAC_PT", grade))
            .thenReturn(assessmentInfoList);
        when(mockAssessmentWindowService.findAssessmentWindowsForAssessmentIds(any(), Matchers.<String>anyVararg()))
            .thenReturn(assessmentWindowsMap);
        List<AssessmentInfo> assessmentInfos = assessmentInfoService.findAssessmentInfoForGrade("SBAC_PT", grade);

        assertThat(assessmentInfos).hasSize(2);
        AssessmentInfo retInfo1 = assessmentInfos.get(0);
        AssessmentInfo retInfo2 = assessmentInfos.get(1);

        assertThat(retInfo1.getId()).isEqualTo(assessmentInfoList.get(0).getId());
        assertThat(retInfo1.getKey()).isEqualTo(assessmentInfoList.get(0).getKey());
        assertThat(retInfo1.getSubject()).isEqualTo(assessmentInfoList.get(0).getSubject());
        assertThat(retInfo1.getAssessmentWindows()).isEqualTo(assessmentWindows1);
        assertThat(retInfo1.getLanguages()).isEqualTo(assessmentInfoList.get(0).getLanguages());
        assertThat(retInfo1.getMaxAttempts()).isEqualTo(assessmentInfoList.get(0).getMaxAttempts());
        assertThat(retInfo1.getGrades()).isEqualTo(assessmentInfoList.get(0).getGrades());

        assertThat(retInfo2.getId()).isEqualTo(assessmentInfoList.get(1).getId());
        assertThat(retInfo2.getKey()).isEqualTo(assessmentInfoList.get(1).getKey());
        assertThat(retInfo2.getSubject()).isEqualTo(assessmentInfoList.get(1).getSubject());
        assertThat(retInfo2.getAssessmentWindows()).isEqualTo(assessmentWindows2);
        assertThat(retInfo2.getLanguages()).isEqualTo(assessmentInfoList.get(1).getLanguages());
        assertThat(retInfo2.getMaxAttempts()).isEqualTo(assessmentInfoList.get(1).getMaxAttempts());
        assertThat(retInfo2.getGrades()).isEqualTo(assessmentInfoList.get(1).getGrades());
    }

    @Test
    public void shouldReturnEmptyList() {
        List<AssessmentInfo> assessmentInfoList = randomListOf(2, AssessmentInfo.class);
        when(mockAssessmentQueryRepository.findAssessmentInfoByKeys("SBAC_PT", assessmentInfoList.get(0).getKey(), assessmentInfoList.get(1).getKey()))
            .thenReturn(new ArrayList<>());
        when(mockAssessmentWindowService.findAssessmentWindowsForAssessmentIds(any(), Matchers.<String>anyVararg()))
            .thenReturn(new HashMap<>());
        List<AssessmentInfo> assessmentInfos = assessmentInfoService.findAssessmentInfo("SBAC_PT", assessmentInfoList.get(0).getKey(), assessmentInfoList.get(1).getKey());
        assertThat(assessmentInfos).isEmpty();
    }
}
