package tds.assessment.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.services.AssessmentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssessmentServiceImplTest {
    private AssessmentQueryRepository queryRepository;
    private AssessmentService service;

    @Before
    public void setUp() {
        queryRepository = mock(AssessmentQueryRepository.class);
        service = new AssessmentServiceImpl(queryRepository);
    }

    @After
    public void tearDown() {}

    @Test
    public void shouldReturnAssessment() {
        Assessment assessment = new Assessment.Builder().withKey("theKey").build();

        when(queryRepository.findAssessmentByKey("theKey")).thenReturn(Optional.of(assessment));
        Optional<Assessment> maybeAssessment = service.findAssessmentByKey("theKey");
        verify(queryRepository).findAssessmentByKey("theKey");

        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }
}
