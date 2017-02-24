package tds.assessment.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.common.Algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(properties = "tds.cache.enabled = true")
public class AssessmentServiceImplIntegrationTests {
    @MockBean
    private AssessmentQueryRepository assessmentQueryRepository;

    @Autowired
    private AssessmentServiceImpl service;

    @Test
    public void shouldReturnCachedAssessment() {
        final String clientName = "SBAC_PT";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        Segment fixedFormSegment = new Segment(assessment.getKey(), Algorithm.FIXED_FORM);
        assessment.setSegments(Collections.singletonList(fixedFormSegment));

        when(assessmentQueryRepository.findAssessmentByKey(clientName, assessment.getKey())).thenReturn(Optional.of(assessment));
        Optional<Assessment> maybeAssessment1 = service.findAssessment(clientName, assessment.getKey());
        // Get the cached value - only a single repo call should result from the two service calls
        Optional<Assessment> maybeAssessment2 = service.findAssessment(clientName, assessment.getKey());
        verify(assessmentQueryRepository, times(1)).findAssessmentByKey(clientName, assessment.getKey());

        assertThat(maybeAssessment1).isEqualTo(maybeAssessment2);
    }
}
