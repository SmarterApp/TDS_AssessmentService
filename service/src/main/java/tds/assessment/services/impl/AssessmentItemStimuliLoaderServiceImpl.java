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

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.ItemContentLevel;
import tds.assessment.model.itembank.TblAdminItem;
import tds.assessment.model.itembank.TblAdminStimulus;
import tds.assessment.model.itembank.TblAdminStrand;
import tds.assessment.model.itembank.TblSetOfItemStrand;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.repositories.loader.ItemContentLevelRepository;
import tds.assessment.repositories.loader.TblAdminStimuliRepository;
import tds.assessment.repositories.loader.TblAdminStrandsRepository;
import tds.assessment.repositories.loader.TblSetOfAdminItemsRepository;
import tds.assessment.repositories.loader.TblSetOfItemStrandsRepository;
import tds.assessment.services.AssessmentItemStimuliLoaderService;
import tds.common.Algorithm;
import tds.testpackage.model.Item;
import tds.testpackage.model.ItemScoreParameter;
import tds.testpackage.model.Property;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentItemStimuliLoaderServiceImpl implements AssessmentItemStimuliLoaderService {
    private static final List<String> CONTENT_LEVEL_TYPES = Arrays.asList("strand", "contentlevel", "target", "claim");

    private final TblAdminStrandsRepository tblAdminStrandsRepository;
    private final TblAdminStimuliRepository tblAdminStimuliRepository;
    private final ItemContentLevelRepository itemContentLevelRepository;
    private final TblSetOfAdminItemsRepository tblSetOfAdminItemsRepository;
    private final TblSetOfItemStrandsRepository tblSetOfItemStrandsRepository;

    @Autowired
    public AssessmentItemStimuliLoaderServiceImpl(final TblAdminStrandsRepository tblAdminStrandsRepository,
                                                  final TblAdminStimuliRepository tblAdminStimuliRepository,
                                                  final ItemContentLevelRepository itemContentLevelRepository,
                                                  final TblSetOfAdminItemsRepository tblSetOfAdminItemsRepository,
                                                  final TblSetOfItemStrandsRepository tblSetOfItemStrandsRepository) {
        this.tblSetOfAdminItemsRepository = tblSetOfAdminItemsRepository;
        this.tblSetOfItemStrandsRepository = tblSetOfItemStrandsRepository;
        this.tblAdminStrandsRepository = tblAdminStrandsRepository;
        this.tblAdminStimuliRepository = tblAdminStimuliRepository;
        this.itemContentLevelRepository = itemContentLevelRepository;
    }

    @Override
    public void loadItemProperties(final Map<String, ItemMetadataWrapper> itemIdToItemMetadata) {
        throw new NotImplementedException("ItemProperty load is not implemented");
    }

    @Override
    public void loadAdminItems(final TestPackage testPackage, final Map<String, ItemMetadataWrapper> itemIdToItemMetadata,
                                final Map<String, TblStrand> keyToStrands) {
        List<TblAdminItem> tblSetOfAdminItems = itemIdToItemMetadata.values().stream()
            .map(itemWrapper -> {
                    Item item = itemWrapper.getItem();

                    final double irtA = item.getItemScoreDimension().itemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().equals("a"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .max().orElse(1);
                    // This appears to be the average of all item scoring parameters starting with a "b"
                    final double irtB = item.getItemScoreDimension().itemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().startsWith("b"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .average().orElse(-9999);
                    final double irtC = item.getItemScoreDimension().itemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().equals("c"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .max().orElse(0);

                    final String claimName = item.getBlueprintReferences().stream()
                        .map(bpRef -> keyToStrands.get(bpRef.getIdRef()))
                        .filter(bp -> bp.getType().equals("strand") || bp.getType().equals("claim"))
                        .map(bp -> bp.getKey())
                        .findFirst()
                        .orElseThrow(() -> new TestPackageLoaderException(
                            String.format("Cannot find matching strand/claim in test package for item %s", item.getId())));

                    final String leafTargetKey = item.getBlueprintReferences().stream()
                        .map(bpRef -> keyToStrands.get(bpRef.getIdRef()))
                        .filter(bp -> (bp.getType().equals("contentlevel") || bp.getType().equals("target"))
                            && bp.isLeafTarget())
                        .map(bp -> bp.getKey())
                        .findFirst()
                        .orElseThrow(() -> new TestPackageLoaderException(
                            String.format("Cannot find matching target/contentlevel in test package for item %s", item.getId())));

                    return new TblAdminItem.Builder(itemWrapper.getItemKey(), itemWrapper.getSegmentKey())
                        .withGroupId(itemWrapper.getGroupId())
                        .withItemPosition(item.position())
                        .withFieldTest(item.fieldTest())
                        .withActive(true)
                        .withIrtB(String.valueOf(irtB))
                        .withBlockId("A")
                        .withRequired(item.responseRequired())
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
                        .withClString(itemWrapper.isAdaptive() ? createClString(item, keyToStrands) : null)
                        .withFtWeight(1)
                        .build();
                }
            )
            .collect(Collectors.toList());

        tblSetOfAdminItemsRepository.save(tblSetOfAdminItems);
    }

    @Override
    public void loadLinkItemsToStrands(final Map<String, ItemMetadataWrapper> itemMetadataWrapperMap,
                                       final Map<String, TblStrand> keyToStrands, final Long version) {
        // Inserts strand, content level, and affinity group blueprint elements to aa_itemcl
        // Get every value of the itemMetadataWrapperMap and convert it to the content level, based on the mapping to the blueprint element strand key
        List<ItemContentLevel> itemContentLevels = itemMetadataWrapperMap.values().stream()
            .flatMap(itemWrapper -> itemWrapper.getItem().getBlueprintReferences().stream()
                .map(bpref -> keyToStrands.get(bpref.getIdRef()))
                .filter(bp -> CONTENT_LEVEL_TYPES.contains(bp.getType()))
                .map(bp ->
                    new ItemContentLevel(itemWrapper.getSegmentKey(), itemWrapper.getItemKey(), bp.getKey())
                )
            )
            .collect(Collectors.toList());

        itemContentLevelRepository.save(itemContentLevels);

        // Get each leaf target/contentlevel's key for each item that is being loaded
        List<TblSetOfItemStrand> tblSetOfItemStrands = itemMetadataWrapperMap.values().stream()
            .flatMap(wrapper -> wrapper.getItem().getBlueprintReferences().stream()
                .map(bpRef -> keyToStrands.get(bpRef.getIdRef()))
                .filter(bp -> bp.isLeafTarget())
                .map(bp -> new TblSetOfItemStrand(wrapper.getItemKey(), bp.getKey(), wrapper.getSegmentKey(), version))
            )
            .collect(Collectors.toList());

        tblSetOfItemStrandsRepository.save(tblSetOfItemStrands);
    }

    @Override
    public void loadAdminStimuli(final TestPackage testPackage) {
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

                        } else if (segment.getAlgorithmType().equals(Algorithm.ADAPTIVE_2.getType())) {
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
                            .filter(bp -> CONTENT_LEVEL_TYPES.contains(keyToStrands.get(bp.getIdRef()).getType()))
                            .map(bp -> {
                                    final Map<String, String> itemSelectionProperties = bp.itemSelection().stream()
                                        .collect(Collectors.toMap(p -> p.getName().toString().toLowerCase(), Property::getValue));

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
                                        //TODO: Investigate these two properties. Sometimes they are null, otherwise equal to min/maxItems
//                            .withLoadMin()
//                            .withLoadMax()
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

        tblAdminStrandsRepository.save(adminStrands);
    }

    private static TblAdminStimulus mapStimToTblAdminStimulus(final tds.testpackage.model.ItemGroup itemGroup, final int bankKey,
                                                              final String segmentKey, final String version) {
        return new TblAdminStimulus.Builder(String.format("%s-%s", bankKey, itemGroup.getStimulus().get().getId()), segmentKey)
            .withNumItemsRequired(itemGroup.maxResponses().equalsIgnoreCase("ALL") ? -1 : Integer.parseInt(itemGroup.maxResponses()))
            .withMaxItems(itemGroup.maxItems().equalsIgnoreCase("ALL") ? -1 : Integer.parseInt(itemGroup.maxResponses()))
            .withVersion(Long.parseLong(version))
            .withUpdateVersion(Long.parseLong(version))
            .withGroupId(itemGroup.getId())
            .build();
    }

    private static String createClString(final Item item, final Map<String, TblStrand> keyToStrands) {
        // Create a semi-colon delimited string containing all claims, targets, and affinitygroups
        return String.join(";", item.getBlueprintReferences().stream()
            .map(bpRefId -> keyToStrands.get(bpRefId.getIdRef()))
            .filter(bp -> CONTENT_LEVEL_TYPES.contains(bp.getType()) || bp.getType().equals("affinitygroup"))
            .map(bp -> bp.getKey())
            .collect(Collectors.toList()));
    }
}
