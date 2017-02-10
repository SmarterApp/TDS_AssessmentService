package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import tds.assessment.Algorithm;
import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssessmentServiceImplTest {
    private AssessmentQueryRepository assessmentQueryRepository;
    private AccommodationsQueryRepository accommodationsQueryRepository;
    private ItemQueryRepository itemQueryRepository;
    private FormQueryRepository formQueryRepository;
    private StrandQueryRepository strandQueryRepository;
    private AssessmentService service;

    @Before
    public void setUp() {
        assessmentQueryRepository = mock(AssessmentQueryRepository.class);
        itemQueryRepository = mock(ItemQueryRepository.class);
        formQueryRepository = mock(FormQueryRepository.class);
        strandQueryRepository = mock(StrandQueryRepository.class);
        accommodationsQueryRepository = mock(AccommodationsQueryRepository.class);
        service = new AssessmentServiceImpl(assessmentQueryRepository, itemQueryRepository, formQueryRepository,
            strandQueryRepository, accommodationsQueryRepository);
    }

    @Test
    public void shouldReturnAssessmentWithFixedFormSegment() {
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        Segment fixedFormSegment = new Segment(assessment.getKey(), Algorithm.FIXED_FORM);
        List<Segment> fixedFormSegments = new ArrayList<>();
        fixedFormSegments.add(fixedFormSegment);
        assessment.setSegments(fixedFormSegments);

        when(assessmentQueryRepository.findAssessmentByKey("SBAC_PT", "theKey")).thenReturn(Optional.of(assessment));
        when(formQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(strandQueryRepository.findStrands("theKey")).thenReturn(new HashSet<>());

        Optional<Assessment> maybeAssessment = service.findAssessment("SBAC_PT", "theKey");

        verify(assessmentQueryRepository).findAssessmentByKey("SBAC_PT", "theKey");
        verify(formQueryRepository).findFormsForAssessment("theKey");
        verify(itemQueryRepository).findActiveItemsProperties("theKey");
        verify(itemQueryRepository).findItemsForAssessment("theKey");
        verify(strandQueryRepository).findStrands("theKey");

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

        when(assessmentQueryRepository.findAssessmentByKey("SBAC_PT", "theKey")).thenReturn(Optional.of(assessment));
        when(formQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(strandQueryRepository.findStrands("theKey")).thenReturn(new HashSet<>());

        Optional<Assessment> maybeAssessment = service.findAssessment("SBAC_PT", "theKey");

        verify(assessmentQueryRepository).findAssessmentByKey( "SBAC_PT", "theKey");
        verify(formQueryRepository, times(0)).findFormsForAssessment("theKey");
        verify(itemQueryRepository).findActiveItemsProperties("theKey");
        verify(itemQueryRepository).findItemsForAssessment("theKey");
        verify(strandQueryRepository).findStrands("theKey");

        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }
}
