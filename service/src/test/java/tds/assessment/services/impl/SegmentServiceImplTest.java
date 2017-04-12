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
        Segment assessmentSegment = new Segment("assessmentKey", Algorithm.ADAPTIVE_2);
        Segment segment = new Segment("segmentKey", Algorithm.ADAPTIVE_2);
        assessment.setSegments(Arrays.asList(assessmentSegment, segment));

        ContentLevelSpecification spec = new ContentLevelSpecification.Builder().build();
        ItemControlParameter itemControlParameter = new ItemControlParameter("bpId", "name", "value");
        ItemGroup itemGroup = new ItemGroup("group", 1, 10, 1f);
        ItemMeasurement itemMeasurement = new ItemMeasurement.Builder().build();
        Item parentItem = new Item("parent");
        Item segmentItem = new Item("segment");

        ItemProperty ignoreProperty = new ItemProperty(PROP_NAME_TYPE, "Bogus");
        ItemProperty ignoreProperty2 = new ItemProperty("bogus", OFFGADE_PROP_VALUE_PREFIX + "_Something");
        ItemProperty relevantProperty = new ItemProperty(PROP_NAME_TYPE, OFFGADE_PROP_VALUE_PREFIX + "_Something");

        when(mockAssessmentService.findAssessmentBySegmentKey("segmentKey")).thenReturn(Optional.of(assessment));
        when(mockStrandQueryRepository.findContentLevelSpeficationsBySegmentKey("segmentKey")).thenReturn(Collections.singletonList(spec));
        when(mockItemControlParametersQueryRepository.findControlParametersForSegment("segmentKey")).thenReturn(Collections.singletonList(itemControlParameter));
        when(mockItemGroupQueryRepository.findItemGroupsBySegment("segmentKey")).thenReturn(Collections.singletonList(itemGroup));
        when(mockItemQueryRepository.findItemsForSegment("assessmentKey")).thenReturn(Collections.singletonList(parentItem));
        when(mockItemQueryRepository.findItemsForSegment("segmentKey")).thenReturn(Collections.singletonList(segmentItem));
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
        assertThat(info.getParentItems()).containsExactly(parentItem);
        assertThat(info.getSegmentItems()).containsExactly(segmentItem);
        assertThat(info.getPoolFilterProperties()).containsExactly(relevantProperty);
    }
}