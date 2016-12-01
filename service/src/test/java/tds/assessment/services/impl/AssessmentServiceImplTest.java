package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssessmentServiceImplTest {
    private AssessmentQueryRepository assessmentQueryRepository;
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
        service = new AssessmentServiceImpl(assessmentQueryRepository, itemQueryRepository, formQueryRepository,
                strandQueryRepository);
    }

    @Test
    public void shouldReturnAssessment() {
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");

        when(assessmentQueryRepository.findAssessmentByKey("theKey", "SBAC_PT")).thenReturn(Optional.of(assessment));
        when(formQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(strandQueryRepository.findStrands("theKey")).thenReturn(new HashSet<>());
        Optional<Assessment> maybeAssessment = service.findAssessmentByKey("SBAC_PT", "theKey");
        verify(assessmentQueryRepository).findAssessmentByKey("theKey", "SBAC_PT");
        verify(formQueryRepository).findFormsForAssessment("theKey");
        verify(itemQueryRepository).findActiveItemsProperties("theKey");
        verify(itemQueryRepository).findItemsForAssessment("theKey");
        verify(strandQueryRepository).findStrands("theKey");
        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }
}
