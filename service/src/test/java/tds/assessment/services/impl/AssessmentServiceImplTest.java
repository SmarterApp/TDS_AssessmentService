/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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
import tds.assessment.model.SegmentMetadata;
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.assessment.repositories.AssessmentCommandRepository;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.GradesQueryRepository;
import tds.assessment.repositories.ItemGroupQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentService;
import tds.common.Algorithm;
import tds.common.web.exceptions.NotFoundException;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentServiceImplTest {
    @Mock
    private AssessmentQueryRepository mockAssessmentQueryRepository;

    @Mock
    private AssessmentCommandRepository mockAssessmentCommandRepository;

    @Mock
    private AccommodationsQueryRepository mockAccommodationsQueryRepository;

    @Mock
    private ItemQueryRepository mockItemQueryRepository;

    @Mock
    private FormQueryRepository mockFormQueryRepository;

    @Mock
    private StrandQueryRepository mockStrandQueryRepository;

    @Mock
    private GradesQueryRepository mockGradesQueryRepository;

    @Mock
    private ItemGroupQueryRepository mockItemGroupQueryRepository;

    private AssessmentService service;

    @Before
    public void setUp() {
        service = new AssessmentServiceImpl(mockAssessmentQueryRepository, mockAssessmentCommandRepository, mockItemQueryRepository, mockFormQueryRepository,
            mockStrandQueryRepository, mockAccommodationsQueryRepository, mockGradesQueryRepository, mockItemGroupQueryRepository);
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
        when(mockGradesQueryRepository.findGrades("theKey")).thenReturn(new ArrayList<>());

        Optional<Assessment> maybeAssessment = service.findAssessment("SBAC_PT", "theKey");

        verify(mockAssessmentQueryRepository).findAssessmentByKey("SBAC_PT", "theKey");
        verify(mockFormQueryRepository).findFormsForAssessment("theKey");
        verify(mockItemQueryRepository).findActiveItemsProperties("theKey");
        verify(mockItemQueryRepository).findItemsForAssessment("theKey");
        verify(mockStrandQueryRepository).findStrands("theKey");
        verify(mockGradesQueryRepository).findGrades("theKey");
        verify(mockItemGroupQueryRepository).findItemGroupsBySegment("theKey");

        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }

    @Test
    public void shouldReturnAssessmentWithAdaptiveSegment() {
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        Segment adaptiveSegment = new Segment("theKey", Algorithm.ADAPTIVE_2);
        Segment adaptiveSegment2 = new Segment("segmentKey", Algorithm.ADAPTIVE_2);
        List<Segment> segments = new ArrayList<>();
        segments.add(adaptiveSegment);
        segments.add(adaptiveSegment2);
        assessment.setSegments(segments);

        when(mockAssessmentQueryRepository.findAssessmentByKey("SBAC_PT", "theKey")).thenReturn(Optional.of(assessment));
        when(mockFormQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(mockItemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(mockItemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(mockStrandQueryRepository.findStrands("theKey")).thenReturn(new HashSet<>());
        when(mockGradesQueryRepository.findGrades("theKey")).thenReturn(new ArrayList<>());

        Optional<Assessment> maybeAssessment = service.findAssessment("SBAC_PT", "theKey");

        verify(mockAssessmentQueryRepository).findAssessmentByKey("SBAC_PT", "theKey");
        verify(mockFormQueryRepository, times(0)).findFormsForAssessment("theKey");
        verify(mockItemQueryRepository).findActiveItemsProperties("theKey");
        verify(mockItemQueryRepository).findItemsForAssessment("theKey");
        verify(mockStrandQueryRepository).findStrands("theKey");
        verify(mockGradesQueryRepository).findGrades("theKey");
        verify(mockItemGroupQueryRepository).findItemGroupsBySegment("theKey");

        verify(mockItemGroupQueryRepository).findItemGroupsBySegment("segmentKey");

        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }

    @Test
    public void shouldReturnMultiSegmentedAssessmentBySegmentKey() {
        SegmentMetadata metadata = new SegmentMetadata("segmentKey", "parentKey", "clientName");
        Assessment assessment = new Assessment();

        when(mockAssessmentQueryRepository.findSegmentMetadata("segmentKey")).thenReturn(Optional.of(metadata));
        when(mockAssessmentQueryRepository.findAssessmentByKey("clientName", "parentKey")).thenReturn(Optional.of(assessment));

        assertThat(service.findAssessmentBySegmentKey("segmentKey").get()).isEqualTo(assessment);

        verify(mockAssessmentQueryRepository).findSegmentMetadata("segmentKey");
    }

    @Test
    public void shouldReturnSingleSegmentedAssessmentBySegmentKey() {
        SegmentMetadata metadata = new SegmentMetadata("segmentKey", null, "clientName");
        Assessment assessment = new Assessment();

        when(mockAssessmentQueryRepository.findSegmentMetadata("segmentKey")).thenReturn(Optional.of(metadata));
        when(mockAssessmentQueryRepository.findAssessmentByKey("clientName", "segmentKey")).thenReturn(Optional.of(assessment));

        assertThat(service.findAssessmentBySegmentKey("segmentKey").get()).isEqualTo(assessment);

        verify(mockAssessmentQueryRepository).findSegmentMetadata("segmentKey");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenSegmentMetadataCannotBeFound() {
        when(mockAssessmentQueryRepository.findSegmentMetadata("segmentKey")).thenReturn(Optional.empty());

        service.findAssessmentBySegmentKey("segmentKey");
    }

    @Test
    public void shouldRemoveAssessmentUnsafeDelete() {
        final String key = "assessmentKey";
        service.removeAssessment("SBAC_PT", false, key);
        verify(mockAssessmentQueryRepository).findSegmentKeysByAssessmentKey(key);
        verify(mockAssessmentCommandRepository).removeItemBankAssessmentData(key);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowForNoAssessmentFoundRemoveAssessment() {
        when(mockAssessmentQueryRepository.findAssessmentByKey("SBAC_PT", "noAssessment"))
            .thenReturn(Optional.empty());
        service.removeAssessment("SBAC_PT", true, "noAssessment");
    }
}
