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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.ItemContentLevel;
import tds.assessment.model.itembank.TblAdminItem;
import tds.assessment.model.itembank.TblAdminStimulus;
import tds.assessment.model.itembank.TblAdminStrand;
import tds.assessment.model.itembank.TblItemProperty;
import tds.assessment.model.itembank.TblSetOfItemStimulus;
import tds.assessment.model.itembank.TblSetOfItemStrand;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.repositories.loader.ItemContentLevelRepository;
import tds.assessment.repositories.loader.TblAdminStimuliRepository;
import tds.assessment.repositories.loader.TblAdminStrandsRepository;
import tds.assessment.repositories.loader.TblItemPropertiesRepository;
import tds.assessment.repositories.loader.TblSetOfAdminItemsRepository;
import tds.assessment.repositories.loader.TblSetOfItemStimuliRepository;
import tds.assessment.repositories.loader.TblSetOfItemStrandsRepository;
import tds.assessment.services.AssessmentItemStimuliLoaderService;
import tds.common.Algorithm;
import tds.testpackage.model.BlueprintReference;
import tds.testpackage.model.Item;
import tds.testpackage.model.ItemScoreParameter;
import tds.testpackage.model.Property;
import tds.testpackage.model.TestPackage;

import static tds.assessment.model.BlueprintElementTypes.AFFINITY_GROUP;
import static tds.assessment.model.BlueprintElementTypes.CLAIM;
import static tds.assessment.model.BlueprintElementTypes.CLAIM_AND_TARGET_TYPES;
import static tds.assessment.model.BlueprintElementTypes.CLAIM_TYPES;
import static tds.assessment.model.BlueprintElementTypes.CONTENT_LEVEL;
import static tds.assessment.model.BlueprintElementTypes.STRAND;
import static tds.assessment.model.BlueprintElementTypes.TARGET;
import static tds.assessment.model.BlueprintElementTypes.TARGET_TYPES;
import static tds.assessment.model.itembank.TestCohortIdentity.DEFAULT_COHORT;

@Service
public class AssessmentItemStimuliLoaderServiceImpl implements AssessmentItemStimuliLoaderService {
    private static final String ITEM_TYPE_PROP_NAME = "--ITEMTYPE--";
    private static final String LANGUAGE_PROP_NAME = "Language";
    private static final String GRADE_PROP_NAME = "Grade";
    private static final double IRT_A_DEFAULT = 1;
    private static final double IRT_B_DEFAULT = -9999;
    private static final double IRT_C_DEFAULT = 0;

    private final TblAdminStrandsRepository tblAdminStrandsRepository;
    private final TblAdminStimuliRepository tblAdminStimuliRepository;
    private final ItemContentLevelRepository itemContentLevelRepository;
    private final TblSetOfAdminItemsRepository tblSetOfAdminItemsRepository;
    private final TblSetOfItemStrandsRepository tblSetOfItemStrandsRepository;
    private final TblItemPropertiesRepository tblItemPropertiesRepository;
    private final TblSetOfItemStimuliRepository tblSetOfItemStimuliRepository;

    @Autowired
    public AssessmentItemStimuliLoaderServiceImpl(final TblAdminStrandsRepository tblAdminStrandsRepository,
                                                  final TblAdminStimuliRepository tblAdminStimuliRepository,
                                                  final ItemContentLevelRepository itemContentLevelRepository,
                                                  final TblSetOfAdminItemsRepository tblSetOfAdminItemsRepository,
                                                  final TblSetOfItemStrandsRepository tblSetOfItemStrandsRepository,
                                                  final TblItemPropertiesRepository tblItemPropertiesRepository,
                                                  final TblSetOfItemStimuliRepository tblSetOfItemStimuliRepository) {
        this.tblSetOfAdminItemsRepository = tblSetOfAdminItemsRepository;
        this.tblSetOfItemStrandsRepository = tblSetOfItemStrandsRepository;
        this.tblAdminStrandsRepository = tblAdminStrandsRepository;
        this.tblAdminStimuliRepository = tblAdminStimuliRepository;
        this.itemContentLevelRepository = itemContentLevelRepository;
        this.tblItemPropertiesRepository = tblItemPropertiesRepository;
        this.tblSetOfItemStimuliRepository = tblSetOfItemStimuliRepository;
    }

    @Override
    public void loadItemProperties(final List<ItemMetadataWrapper> itemMetadata) {
        List<TblItemProperty> itemProperties = new ArrayList<>();
        // Each item requires 3 types of item properties - itemtype, grades, and languages
        itemMetadata.forEach(wrapper -> {
            final Item item = wrapper.getItem();
            // Add --ITEMTYPE-- property
            itemProperties.add(new TblItemProperty(item.getKey(), ITEM_TYPE_PROP_NAME, item.getType(), wrapper.getSegmentKey()));

            item.getPresentations().forEach(lang ->
                // Add Language properties
                itemProperties.add(new TblItemProperty(item.getKey(), LANGUAGE_PROP_NAME, lang, wrapper.getSegmentKey())));

            wrapper.getGrades().forEach(grade ->
                // Add Grade properties
                itemProperties.add(new TblItemProperty(item.getKey(), GRADE_PROP_NAME, grade.getValue(), wrapper.getSegmentKey())));
        });

        tblItemPropertiesRepository.save(itemProperties);
    }

    @Override
    public void loadAdminItems(final TestPackage testPackage, final List<ItemMetadataWrapper> itemMetadataWrappers,
                               final Map<String, TblStrand> keyToStrands) {
        List<TblAdminItem> tblSetOfAdminItems = itemMetadataWrappers.stream()
            .map(itemWrapper -> {
                    Item item = itemWrapper.getItem();

                    final double irtA = item.getItemScoreDimension().itemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().equals("a"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .max().orElse(IRT_A_DEFAULT);
                    // This appears to be the average of all item scoring parameters starting with a "b"
                    final double irtB = item.getItemScoreDimension().itemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().startsWith("b"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .average().orElse(IRT_B_DEFAULT);
                    final double irtC = item.getItemScoreDimension().itemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().equals("c"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .max().orElse(IRT_C_DEFAULT);

                    final String claimName = item.getBlueprintReferences().stream()
                        .map(bpRef -> keyToStrands.get(bpRef.getIdRef()))
                        .filter(bp -> CLAIM_AND_TARGET_TYPES.contains(bp.getType()))
                        .map(bp -> CLAIM_TYPES.contains(bp.getType())
                            ? bp.getKey()   // If this is a claim, return the key
                            : bp.getKey().substring(0, bp.getKey().indexOf('|'))) // Otherwise, if its a target, parse it out
                        .findFirst()
                        .orElseThrow(() -> new TestPackageLoaderException(
                            String.format("Cannot find matching strand/claim in test package for item %s", item.getKey())));

                    final String leafTargetKey = item.getBlueprintReferences().stream()
                        .map(bpRef -> keyToStrands.get(bpRef.getIdRef()))
                        .filter(bp -> TARGET_TYPES.contains(bp.getType())
                            && bp.isLeafTarget())
                        .map(TblStrand::getKey)
                        .findFirst()
                        .orElse(claimName); // If there's no target/contentlevel, use the strand/claim

                    // See itembank.itembvector() for reference - the bvector is a semi-colon delimited list of "b" item
                    // score dimension parameter values. If no "b" parameter is provided, use the default (-9999)
                    final String bVector = irtB == IRT_B_DEFAULT
                        ? String.format("%.15f", irtB)
                        : StringUtils.join(item.getItemScoreDimension().itemScoreParameters().stream()
                            .filter(param -> param.getMeasurementParameter().startsWith("b"))
                            .map(ItemScoreParameter::getValue)
                            .map(b -> String.format("%.15f", b))
                            .collect(Collectors.toList()), ";");

                    return new TblAdminItem.Builder(itemWrapper.getItem().getKey(), itemWrapper.getSegmentKey())
                        .withGroupId(itemWrapper.getGroupId())
                        .withItemPosition(item.position())
                        .withFieldTest(item.fieldTest())
                        .withActive(true)
                        .withIrtB(String.format("%.15f", irtB))
                        .withRequired(item.responseRequired())
                        .withBlockId("A")
                        .withClientName(testPackage.getPublisher())
                        .withResponseMimeType(MediaType.TEXT_PLAIN_VALUE)
                        .withStrandKey(leafTargetKey)
                        .withVersion(Long.parseLong(testPackage.getVersion()))
                        .withUpdatedVersion(Long.parseLong(testPackage.getVersion()))
                        .withClaimName(claimName)
                        .withIrtA((float) irtA)
                        .withIrtC((float) irtC)
                        .withIrtModel(item.getItemScoreDimension().getMeasurementModel())
                        .withNotForScoring(false)
                        // A semi-colon delimited list of the targets/content levels
                        .withTargetString(itemWrapper.isAdaptive() ? createClString(item, keyToStrands) : null)
                        .withFtWeight(1)
                        .withTestCohort(DEFAULT_COHORT)
                        .withBVector(bVector)
                        .build();
                }
            )
            .collect(Collectors.toList());

        tblSetOfAdminItemsRepository.save(tblSetOfAdminItems);
    }

    @Override
    public void loadLinkItemsToStrands(final List<ItemMetadataWrapper> itemMetadataWrappers,
                                       final Map<String, TblStrand> keyToStrands, final Long version) {
        // Inserts strand, content level, and affinity group blueprint elements to aa_itemcl
        // Get every value of the itemMetadataWrapperMap and convert it to the content level, based on the mapping to the blueprint element strand key
        List<ItemContentLevel> itemContentLevels = itemMetadataWrappers.stream()
            .flatMap(itemWrapper -> itemWrapper.getItem().getBlueprintReferences().stream()
                .flatMap(bpref -> getTargetChain(bpref.getIdRef())) // the target chain is a stream of all target/content levels
                .map(keyToStrands::get)
                .filter(bp -> CLAIM_AND_TARGET_TYPES.contains(bp.getType()) || bp.getType().equalsIgnoreCase(AFFINITY_GROUP))
                .map(bp ->
                    new ItemContentLevel(itemWrapper.getSegmentKey(), itemWrapper.getItem().getKey(), bp.getKey())
                )
            )
            .collect(Collectors.toList());

        itemContentLevelRepository.save(itemContentLevels);

        // Get each leaf target/contentlevel's key for each item that is being loaded
        List<TblSetOfItemStrand> tblSetOfItemStrands = itemMetadataWrappers.stream()
            .flatMap(wrapper -> wrapper.getItem().getBlueprintReferences().stream()
                .map(bpRef -> keyToStrands.get(bpRef.getIdRef()))
                .filter(bp -> containsTarget(wrapper.getItem().getBlueprintReferences(), keyToStrands)
                    ? CLAIM_AND_TARGET_TYPES.contains(bp.getType())
                    : bp.getType().equalsIgnoreCase(STRAND) || bp.getType().equalsIgnoreCase(CLAIM))
                .map(bp -> new TblSetOfItemStrand(wrapper.getItem().getKey(), bp.getKey(), wrapper.getSegmentKey(), version))
            )
            .collect(Collectors.toList());

        tblSetOfItemStrandsRepository.save(tblSetOfItemStrands);
    }

    @Override
    public void loadAdminStimuli(final TestPackage testPackage) {
        // Iterate over every item group in the test package and find every item group/stimuli pairing, and then map it to a TblAdminStimulus
        List<TblAdminStimulus> tblAdminStimuli = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> {
                        if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                            return segment.segmentForms().stream()
                                .flatMap(form -> form.itemGroups().stream()
                                    .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                                    .map(itemGroup -> mapStimToTblAdminStimulus(itemGroup, testPackage.getBankKey(), segment.getKey(), testPackage.getVersion())
                                    )
                                );

                        } else if (segment.getAlgorithmType().contains("adaptive")) {
                            return segment.pool().stream()
                                .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                                .map(itemGroup -> mapStimToTblAdminStimulus(itemGroup, testPackage.getBankKey(), segment.getKey(), testPackage.getVersion()));
                        } else {
                            throw new TestPackageLoaderException("Unrecognized selection algorithm");
                        }
                    }
                )
            ).collect(Collectors.toList());

        tblAdminStimuliRepository.save(tblAdminStimuli);
    }

    @Override
    public void loadAdminStrands(final TestPackage testPackage, final Map<String, TblStrand> keyToStrands) {
        // For each contentlevel (target) and strand (claim), create a tbladminstrand
        List<TblAdminStrand> adminStrands = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> segment.segmentBlueprint().stream()
                    .filter(bp -> CLAIM_AND_TARGET_TYPES.contains(keyToStrands.get(bp.getIdRef()).getType()))
                    .map(bp -> {
                            final Map<String, String> itemSelectionProperties = bp.itemSelection().stream()
                                .collect(Collectors.toMap(p -> p.getName().toLowerCase(), Property::getValue));

                            return new TblAdminStrand.Builder()
                                .withSegmentKey(segment.getKey())
                                .withStrandKey(keyToStrands.get(bp.getIdRef()).getKey())
                                .withMinItems(bp.getMinExamItems())
                                .withMaxItems(bp.getMaxExamItems())
                                .withAdaptiveCut(itemSelectionProperties.containsKey("adaptivecut")
                                    ? Float.parseFloat(itemSelectionProperties.get("adaptivecut")) : null)
                                .withStartAbility(itemSelectionProperties.containsKey("startability")
                                    ? Float.parseFloat(itemSelectionProperties.get("startability")) : null)
                                .withStartInfo(itemSelectionProperties.containsKey("startinfo")
                                    ? Float.parseFloat(itemSelectionProperties.get("startinfo")) : null)
                                .withScalar(itemSelectionProperties.containsKey("scalar")
                                    ? Float.parseFloat(itemSelectionProperties.get("scalar")) : null)
                                // If this is a segmented assessment, then loadmin/max is the segment min/maxExamItems
                                .withLoadMin(assessment.isSegmented() ? bp.getMinExamItems() : null)
                                .withLoadMax(assessment.isSegmented() ? bp.getMaxExamItems() : null)
                                .withStrictMax(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("isstrictmax", "false")))
                                .withBpWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("bpweight", "1")))
                                .withVersion(Long.parseLong(testPackage.getVersion()))
                                .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                                    ? Float.parseFloat(itemSelectionProperties.get("precisiontarget")) : null)
                                .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontargetmetweight")
                                    ? Float.parseFloat(itemSelectionProperties.get("precisiontargetmetweight")) : null)
                                .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontargetnotmetweight")
                                    ? Float.parseFloat(itemSelectionProperties.get("precisiontargetnotmetweight")) : null)
                                .withAbilityWeight(itemSelectionProperties.containsKey("abilityweight")
                                    ? Float.parseFloat(itemSelectionProperties.get("abilityweight")) : null)
                                .build();
                        }
                    )
                )
            )
            .collect(Collectors.toList());

        //TODO: More testing is necessary to determine if this is an actual issue. If it turns out fixed form segments MUST have rows in `tbladminstrand`,
        // then we must either uncomment the code below, or enforce that each item's bpref has a segmentblueprint defined for it.
        /*
        // This is purely for backwards compatibility - we need to create segment blueprints. This is because fixed form segments do not require an
        // actual segmentblueprint element for each target reference, but the TDS application expects one for every segment.
        List<TblAdminStrand> fixedFormTblAdminStrands = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType()))
                .flatMap(segment -> segment.segmentForms().stream()
                    .flatMap(form -> form.itemGroups().stream()
                        .flatMap(itemGroup -> itemGroup.items().stream()
                            .flatMap(item -> item.getBlueprintReferences().stream()
                                .map(bpref -> new TblAdminStrand.Builder()
                                    .withStrandKey(String.format("%s-%s", testPackage.getPublisher(), bpref.getIdRef()))
                                    .withSegmentKey(segment.getKey())
                                    .withMinItems(0)
                                    .withMaxItems(segment.segmentBlueprint().get(0).getMaxExamItems())
                                    .withBpWeight(1F)
                                    .build()
                                )
                            )
                        )
                    )
                )
            ).collect(Collectors.toList());

        adminStrands.addAll(fixedFormTblAdminStrands); */


        tblAdminStrandsRepository.save(adminStrands);
    }

    @Override
    public void loadLinkItemsToStimuli(final TestPackage testPackage) {
        // Iterate over every item group and create a TblSEtOfItemStimulus representing a relationship between items and stimuli
        List<TblSetOfItemStimulus> tblSetOfAdminItemStimuli = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> {
                        if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                            return segment.segmentForms().stream()
                                .flatMap(form -> form.itemGroups().stream()
                                    .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                                    .flatMap(itemGroup -> itemGroup.items().stream()
                                        .map(item -> new TblSetOfItemStimulus(item.getKey(), itemGroup.getStimulus().get().getKey(),
                                            segment.getKey(), Long.parseLong(testPackage.getVersion())))
                                    )
                                );

                        } else if (segment.getAlgorithmType().contains("adaptive")) {
                            return segment.pool().stream()
                                .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .map(item -> new TblSetOfItemStimulus(item.getKey(), itemGroup.getStimulus().get().getKey(),
                                        segment.getKey(), Long.parseLong(testPackage.getVersion())))
                                );
                        } else {
                            throw new TestPackageLoaderException("Unrecognized selection algorithm");
                        }
                    }
                )
            ).collect(Collectors.toList());

        tblSetOfItemStimuliRepository.save(tblSetOfAdminItemStimuli);
    }

    private boolean containsTarget(final List<BlueprintReference> blueprintReferences, final Map<String, TblStrand> keyToStrands) {
        // Check if any of the blueprint references refer to a target
        return blueprintReferences.stream()
            .anyMatch(bpRef -> TARGET_TYPES.contains(keyToStrands.get(bpRef.getIdRef()).getType()));
    }

    private Stream<String> getTargetChain(final String leafId) {
        if (!leafId.contains("|")) {
            return Stream.of(leafId);
        }

        List<String> targets = new ArrayList<>();
        int nextPipeIndex = 0; // Maintain an index on where the last target processed ended (pipe delimited)

        // Start at the claim level, and iterate over each target
        // e.g., 1|abc|123|xyz -> 1, 1|abc, 1|abc|123, 1|abc|123|xyz
        for (int i = 0; i < StringUtils.countMatches(leafId, "|") + 1; i++) {

            if (i == StringUtils.countMatches(leafId, "|")) {
                // At the end of the processing, no need to parse - just include the entire target string
                targets.add(leafId);
            } else {
                String target = leafId.substring(0, leafId.indexOf("|", nextPipeIndex));
                nextPipeIndex = leafId.indexOf("|", target.length()) + 1;
                targets.add(target);
            }
        }

        return targets.stream();
    }

    private static TblAdminStimulus mapStimToTblAdminStimulus(final tds.testpackage.model.ItemGroup itemGroup, final int bankKey,
                                                              final String segmentKey, final String version) {
        return new TblAdminStimulus.Builder(String.format("%s-%s", bankKey, itemGroup.getStimulus().get().getId()), segmentKey)
            .withNumItemsRequired(itemGroup.maxResponses().equalsIgnoreCase("ALL") ? -1 : Integer.parseInt(itemGroup.maxResponses()))
            .withMaxItems(itemGroup.maxItems().equalsIgnoreCase("ALL") ? -1 : Integer.parseInt(itemGroup.maxResponses()))
            .withVersion(Long.parseLong(version))
            .withUpdateVersion(Long.parseLong(version))
            .withGroupId(itemGroup.getKey())
            .build();
    }

    private static String createClString(final Item item, final Map<String, TblStrand> keyToStrands) {
        // Create a semi-colon delimited string containing all claims, targets, and affinitygroups
        return String.join(";", item.getBlueprintReferences().stream()
            .map(bpRefId -> keyToStrands.get(bpRefId.getIdRef()))
            .filter(bp -> CLAIM_AND_TARGET_TYPES.contains(bp.getType()) || bp.getType().equals(AFFINITY_GROUP))
            .map(TblStrand::getKey)
            .collect(Collectors.toList()));
    }
}
