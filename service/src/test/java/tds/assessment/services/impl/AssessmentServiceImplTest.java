package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentService;
import tds.common.Algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentServiceImplTest {
    @Mock
    private AssessmentQueryRepository mockAssessmentQueryRepository;

    @Mock
    private AccommodationsQueryRepository mockAccommodationsQueryRepository;

    @Mock
    private ItemQueryRepository mockItemQueryRepository;

    @Mock
    private FormQueryRepository mockFormQueryRepository;

    @Mock
    private StrandQueryRepository mockStrandQueryRepository;
    
    private AssessmentService service;

    @Before
    public void setUp() {
        service = new AssessmentServiceImpl(mockAssessmentQueryRepository, mockItemQueryRepository, mockFormQueryRepository,
            mockStrandQueryRepository, mockAccommodationsQueryRepository);
    }

    @Test
    public void shouldReturnAssessmentWithFixedFormSegment() {
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        Segment fixedFormSegment = new Segment(assessment.getKey(), Algorithm.FIXED_FORM);
        List<Segment> fixedFormSegments = new ArrayList<>();
        fixedFormSegments.add(fixedFormSegment);
        assessment.setSegments(fixedFormSegments);

        when(mockAssessmentQueryRepository.findAssessmentByKey("SBAC_PT", "theKey")).thenReturn(Optional.of(assessment));
        when(mockFormQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(mockItemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(mockItemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(mockStrandQueryRepository.findStrands("theKey")).thenReturn(new HashSet<>());

        Optional<Assessment> maybeAssessment = service.findAssessment("SBAC_PT", "theKey");

        verify(mockAssessmentQueryRepository).findAssessmentByKey("SBAC_PT", "theKey");
        verify(mockFormQueryRepository).findFormsForAssessment("theKey");
        verify(mockItemQueryRepository).findActiveItemsProperties("theKey");
        verify(mockItemQueryRepository).findItemsForAssessment("theKey");
        verify(mockStrandQueryRepository).findStrands("theKey");

        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }

    @Test
    public void shouldReturnAssessmentWithAdaptiveSegment() {
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        Segment fixedFormSegment = new Segment(assessment.getKey(), Algorithm.ADAPTIVE_2);
        List<Segment> fixedFormSegments = new ArrayList<>();
        fixedFormSegments.add(fixedFormSegment);
        assessment.setSegments(fixedFormSegments);

        when(mockAssessmentQueryRepository.findAssessmentByKey("SBAC_PT", "theKey")).thenReturn(Optional.of(assessment));
        when(mockFormQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(mockItemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(mockItemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(mockStrandQueryRepository.findStrands("theKey")).thenReturn(new HashSet<>());

        Optional<Assessment> maybeAssessment = service.findAssessment("SBAC_PT", "theKey");

        verify(mockAssessmentQueryRepository).findAssessmentByKey( "SBAC_PT", "theKey");
        verify(mockFormQueryRepository, times(0)).findFormsForAssessment("theKey");
        verify(mockItemQueryRepository).findActiveItemsProperties("theKey");
        verify(mockItemQueryRepository).findItemsForAssessment("theKey");
        verify(mockStrandQueryRepository).findStrands("theKey");

        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }
}
