package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import tds.common.cache.CacheType;

@Service
public class SegmentServiceImpl implements SegmentService {
    private final AssessmentService assessmentService;
    private final ItemGroupQueryRepository itemGroupQueryRepository;
    private final ItemControlParametersQueryRepository itemControlParametersQueryRepository;
    private final ItemMeasurementQueryRepository itemMeasurementQueryRepository;
    private final StrandQueryRepository strandQueryRepository;
    private final ItemQueryRepository itemQueryRepository;

    private static final String OFF_GRADE_PROP_VALUE_PREFIX = "OFFGRADE";
    private static final String PROP_NAME_TYPE = "TDSPoolFilter";

    @Autowired
    public SegmentServiceImpl(final AssessmentService assessmentService,
                              final ItemGroupQueryRepository itemGroupQueryRepository,
                              final ItemControlParametersQueryRepository itemControlParametersQueryRepository,
                              final ItemMeasurementQueryRepository itemMeasurementQueryRepository,
                              final StrandQueryRepository strandQueryRepository,
                              final ItemQueryRepository itemQueryRepository) {
        this.assessmentService = assessmentService;
        this.itemGroupQueryRepository = itemGroupQueryRepository;
        this.itemControlParametersQueryRepository = itemControlParametersQueryRepository;
        this.itemMeasurementQueryRepository = itemMeasurementQueryRepository;
        this.strandQueryRepository = strandQueryRepository;
        this.itemQueryRepository = itemQueryRepository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public Optional<SegmentItemInformation> findSegmentItemInformation(final String segmentKey) {
        //This method is a port of ItemSelectionDLL.AA_GetSegment2_SP

        Optional<Assessment> maybeAssessment = assessmentService.findAssessmentBySegmentKey(segmentKey);
        if (!maybeAssessment.isPresent()) {
            return Optional.empty();
        }

        Assessment assessment = maybeAssessment.get();
        Segment segment = assessment.getSegment(segmentKey);

        List<ContentLevelSpecification> specifications = strandQueryRepository.findContentLevelSpecificationsBySegmentKey(segmentKey);
        List<ItemGroup> itemGroups = itemGroupQueryRepository.findItemGroupsBySegment(segmentKey);
        List<Item> segmentItems = segment.getItems();
        List<Item> siblingItems = new ArrayList<>();

        if (assessment.isSegmented()) {
            Set<String> segmentItemIds = segment.getItems().stream()
                .map(Item::getId)
                .collect(Collectors.toSet());

            for (Segment seg : assessment.getSegments()) {
                if(seg.getKey().equals(segment.getKey())) continue;

                List<Item> items = seg.getItems().stream()
                    .filter(item -> !segmentItemIds.contains(item.getId()))
                    .collect(Collectors.toList());

                siblingItems.addAll(items);
            }
        }

        List<ItemMeasurement> itemMeasurements = itemMeasurementQueryRepository.findItemMeasurements(segmentKey, assessment.getKey());
        List<ItemControlParameter> controlParameters = itemControlParametersQueryRepository.findControlParametersForSegment(segmentKey);

        List<ItemProperty> properties = itemQueryRepository.findActiveItemsProperties(segmentKey);

        List<ItemProperty> segmentProperties = properties.stream()
            .filter(itemProperty ->
                itemProperty.getValue().startsWith(OFF_GRADE_PROP_VALUE_PREFIX)
                    && PROP_NAME_TYPE.equals(itemProperty.getName()))
            .collect(Collectors.toList());

        return Optional.of(new SegmentItemInformation.Builder()
            .withSegment(segment)
            .withItemGroups(itemGroups)
            .withSiblingItems(siblingItems)
            .withSegmentItems(segmentItems)
            .withItemMeasurements(itemMeasurements)
            .withControlParameters(controlParameters)
            .withPoolFilterProperties(segmentProperties)
            .withContentLevelSpecifications(specifications)
            .build());
    }
}
