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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.ContentLevelSpecification;
import tds.assessment.Item;
import tds.assessment.ItemControlParameter;
import tds.assessment.ItemGroup;
import tds.assessment.ItemMeasurement;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.SegmentItemInformation;
import tds.assessment.repositories.ItemControlParametersQueryRepository;
import tds.assessment.repositories.ItemGroupQueryRepository;
import tds.assessment.repositories.ItemMeasurementQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentService;
import tds.assessment.services.SegmentService;
import tds.common.Algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SegmentServiceImplTest {
    private static final String OFFGADE_PROP_VALUE_PREFIX = "OFFGRADE";
    private static final String PROP_NAME_TYPE = "TDSPoolFilter";

    @Mock
    private AssessmentService mockAssessmentService;

    @Mock
    private ItemGroupQueryRepository mockItemGroupQueryRepository;

    @Mock
    private ItemControlParametersQueryRepository mockItemControlParametersQueryRepository;

    @Mock
    private ItemMeasurementQueryRepository mockItemMeasurementQueryRepository;

    @Mock
    private StrandQueryRepository mockStrandQueryRepository;

    @Mock
    private ItemQueryRepository mockItemQueryRepository;

    private SegmentService segmentService;

    @Before
    public void setUp() {
        segmentService = new SegmentServiceImpl(mockAssessmentService,
            mockItemGroupQueryRepository,
            mockItemControlParametersQueryRepository,
            mockItemMeasurementQueryRepository,
            mockStrandQueryRepository,
            mockItemQueryRepository);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldReturnEmptyWhenAssessmentCannotBeFound() {
        when(mockAssessmentService.findAssessmentBySegmentKey("segmentKey")).thenReturn(Optional.empty());

        assertThat(segmentService.findSegmentItemInformation("segmentKey")).isNotPresent();
    }

    @Test
    public void shouldReturnSegmentInformation() {
        Assessment assessment = new Assessment();
        assessment.setKey("assessmentKey");
        Item otherItem = new Item("otherItem");
        Item segmentItem = new Item("segment");
        Segment assessmentSegment = new Segment("assessmentKey", Algorithm.ADAPTIVE_2);
        Segment segment = new Segment("segmentKey", Algorithm.ADAPTIVE_2);
        segment.setItems(Collections.singletonList(segmentItem));
        Segment otherSegment = new Segment("segmentKey1", Algorithm.ADAPTIVE_2);
        otherSegment.setItems(Collections.singletonList(otherItem));
        assessment.setSegments(Arrays.asList(assessmentSegment, segment, otherSegment));

        ContentLevelSpecification spec = new ContentLevelSpecification.Builder().build();
        ItemControlParameter itemControlParameter = new ItemControlParameter("bpId", "name", "value");
        ItemGroup itemGroup = new ItemGroup("group", 1, 10, 1f);
        ItemMeasurement itemMeasurement = new ItemMeasurement.Builder().build();

        ItemProperty ignoreProperty = new ItemProperty(PROP_NAME_TYPE, "Bogus", "desc", "segmentItem");
        ItemProperty ignoreProperty2 = new ItemProperty("bogus", OFFGADE_PROP_VALUE_PREFIX + "_Something", "desc", "segmentItem");
        ItemProperty relevantProperty = new ItemProperty(PROP_NAME_TYPE, OFFGADE_PROP_VALUE_PREFIX + "_Something", "desc", "segmentItem");

        when(mockAssessmentService.findAssessmentBySegmentKey("segmentKey")).thenReturn(Optional.of(assessment));
        when(mockStrandQueryRepository.findContentLevelSpecificationsBySegmentKey("segmentKey")).thenReturn(Collections.singletonList(spec));
        when(mockItemControlParametersQueryRepository.findControlParametersForSegment("segmentKey")).thenReturn(Collections.singletonList(itemControlParameter));
        when(mockItemGroupQueryRepository.findItemGroupsBySegment("segmentKey")).thenReturn(Collections.singletonList(itemGroup));
        when(mockItemMeasurementQueryRepository.findItemMeasurements("segmentKey", "assessmentKey")).thenReturn(Collections.singletonList(itemMeasurement));
        when(mockItemQueryRepository.findActiveItemsProperties("segmentKey")).thenReturn(Arrays.asList(ignoreProperty, ignoreProperty2, relevantProperty));

        Optional<SegmentItemInformation> maybeInfo = segmentService.findSegmentItemInformation("segmentKey");

        assertThat(maybeInfo).isPresent();

        SegmentItemInformation info = maybeInfo.get();

        assertThat(info.getSegment()).isEqualTo(segment);
        assertThat(info.getContentLevelSpecifications()).containsExactly(spec);
        assertThat(info.getControlParameters()).containsExactly(itemControlParameter);
        assertThat(info.getItemGroups()).containsExactly(itemGroup);
        assertThat(info.getItemMeasurements()).containsExactly(itemMeasurement);
        assertThat(info.getSiblingItems()).containsExactly(otherItem);
        assertThat(info.getSegmentItems()).containsExactly(segmentItem);
        assertThat(info.getPoolFilterProperties()).containsExactly(relevantProperty);
    }
}