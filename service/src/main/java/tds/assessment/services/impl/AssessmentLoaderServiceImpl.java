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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.itembank.AffinityGroup;
import tds.assessment.model.itembank.AffinityGroupIdentity;
import tds.assessment.model.itembank.AffinityGroupItem;
import tds.assessment.model.itembank.AffinityGroupItemIdentity;
import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.ItemContentLevel;
import tds.assessment.model.itembank.ItemMeasurementParameter;
import tds.assessment.model.itembank.ItemScoreDimension;
import tds.assessment.model.itembank.MeasurementModel;
import tds.assessment.model.itembank.MeasurementParameter;
import tds.assessment.model.itembank.MeasurementParameterIdentity;
import tds.assessment.model.itembank.SetOfTestGrades;
import tds.assessment.model.itembank.TblAdminItem;
import tds.assessment.model.itembank.TblAdminStimulus;
import tds.assessment.model.itembank.TblAdminStrand;
import tds.assessment.model.itembank.TblAdminSubject;
import tds.assessment.model.itembank.TblItem;
import tds.assessment.model.itembank.TblItemSelectionParameter;
import tds.assessment.model.itembank.TblSetOfItemStrand;
import tds.assessment.model.itembank.TblStimulus;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.model.itembank.TblSubject;
import tds.assessment.model.itembank.TblTestAdmin;
import tds.assessment.model.itembank.TestCohort;
import tds.assessment.model.itembank.TestForm;
import tds.assessment.model.itembank.TestFormItem;
import tds.assessment.repositories.ItemBankDataCommandRepository;
import tds.assessment.repositories.ItemBankDataQueryRepository;
import tds.assessment.repositories.loader.AffinityGroupItemRepository;
import tds.assessment.repositories.loader.AffinityGroupRepository;
import tds.assessment.repositories.loader.ItemContentLevelRepository;
import tds.assessment.repositories.loader.ItemMeasurementParameterRepository;
import tds.assessment.repositories.loader.ItemScoreDimensionsRepository;
import tds.assessment.repositories.loader.MeasurementModelRepository;
import tds.assessment.repositories.loader.MeasurementParameterRepository;
import tds.assessment.repositories.loader.SetOfTestGradesRepository;
import tds.assessment.repositories.loader.TblAdminStimuliRepository;
import tds.assessment.repositories.loader.TblAdminStrandsRepository;
import tds.assessment.repositories.loader.TblItemRepository;
import tds.assessment.repositories.loader.TblItemSelectionParameterRepository;
import tds.assessment.repositories.loader.TblSetOfAdminItemsRepository;
import tds.assessment.repositories.loader.TblSetOfAdminSubjectsRepository;
import tds.assessment.repositories.loader.TblSetOfItemStrandsRepository;
import tds.assessment.repositories.loader.TblStimuliRepository;
import tds.assessment.repositories.loader.TblStrandRepository;
import tds.assessment.repositories.loader.TblSubjectRepository;
import tds.assessment.repositories.loader.TblTestAdminRepository;
import tds.assessment.repositories.loader.TestCohortRepository;
import tds.assessment.repositories.loader.TestFormItemRepository;
import tds.assessment.repositories.loader.TestFormRepository;
import tds.assessment.services.AssessmentLoaderService;
import tds.common.Algorithm;
import tds.common.ValidationError;
import tds.testpackage.model.Assessment;
import tds.testpackage.model.BlueprintElement;
import tds.testpackage.model.Grade;
import tds.testpackage.model.Item;
import tds.testpackage.model.ItemScoreParameter;
import tds.testpackage.model.Property;
import tds.testpackage.model.Segment;
import tds.testpackage.model.SegmentBlueprintElement;
import tds.testpackage.model.Stimulus;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentLoaderServiceImpl implements AssessmentLoaderService {
    private static final Logger log = LoggerFactory.getLogger(AssessmentLoaderServiceImpl.class);

    private static final List<String> CONTENT_LEVEL_TYPES = Arrays.asList("strand", "contentlevel", "target", "claim");

    private static final List<String> ALGORITHM_PROPERTY_NAMES = Arrays.asList(
        "ftstartpos",
        "ftendpos",
        "bpweight",
        "startability",
        "startinfo",
        "cset1size",
        "cset1order",
        "cset2random",
        "cset2initialrandom",
        "abilityoffset",
        "itemweight",
        "precisiontarget",
        "adaptivecut",
        "toocloseses",
        "slope",
        "intercept",
        "abilityweight",
        "computeabilityestimates",
        "rcabilityweight",
        "precisiontargetmetweight",
        "precisiontargetnotmetweight",
        "terminationoverallinfo",
        "terminationrcinfo",
        "terminationmincount",
        "terminationtooclose",
        "terminationflagsand"
    );

    /* Blueprint Element Defaults */
    private static final String DEFAULT_START_ABILITY = "0";
    private static final String DEFAULT_START_INFO = "1";
    private static final String DEFAULT_SLOPE = "1";
    private static final String DEFAULT_INTERCEPT = "1";
    private static final Integer DEFAULT_FT_START_POS = null;
    private static final Integer DEFAULT_FT_END_POS = null;
    private static final String DEFAULT_BLUEPRINT_WEIGHT = "5.0";
    private static final String DEFAULT_ABILITY_WEIGHT = "1";
    private static final String DEFAULT_CSET1_SIZE = "20";
    private static final String DEFAULT_CSET2_RANDOM = "1";
    private static final String DEFAULT_CSET2_INITIAL_RANDOM = "5";
    private static final String DEFAULT_COMPUTE_ABILITY_ESTIMATES = "1";
    private static final String DEFAULT_ITEM_WEIGHT = "5";
    private static final String DEFAULT_ABILITY_OFFSET = "0.0";
    private static final String DEFAULT_CSET1_ORDER = "ABILITY";
    private static final String DEFAULT_RC_ABILITY_WEIGHT = "1";
    private static final Float DEFAULT_PRECISION_TARGET = null;
    private static final String DEFAULT_PRECISION_TARGET_MET_WEIGHT = "1";
    private static final String DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT = "1";
    private static final Float DEFAULT_ADAPTIVE_CUT = null;
    private static final Float DEFAULT_TOO_CLOSESES = null;
    private static final String DEFAULT_TERMINATION_FLAGS = "false";
    private static final double DEFAULT_FT_WEIGHT = 1;

    private final ItemBankDataQueryRepository itemBankDataQueryRepository;
    private final ItemBankDataCommandRepository itemBankDataCommandRepository;
    private final TblSetOfAdminSubjectsRepository tblSetOfAdminSubjectsRepository;
    private final TblStimuliRepository tblStimuliRepository;
    private final TblSetOfAdminItemsRepository tblSetOfAdminItemsRepository;
    private final TblSetOfItemStrandsRepository tblSetOfItemStrandsRepository;
    private final SetOfTestGradesRepository setOfTestGradesRepository;
    private final TestCohortRepository testCohortRepository;
    private final TblItemSelectionParameterRepository tblItemSelectionParameterRepository;
    private final TblAdminStrandsRepository tblAdminStrandsRepository;
    private final ItemScoreDimensionsRepository itemScoreDimensionsRepository;
    private final MeasurementModelRepository measurementModelRepository;
    private final MeasurementParameterRepository measurementParameterRepository;
    private final ItemMeasurementParameterRepository itemMeasurementParameterRepository;
    private final TblAdminStimuliRepository tblAdminStimuliRepository;
    private final TestFormRepository testFormRepository;
    private final TestFormItemRepository testFormItemRepository;
    private final AffinityGroupRepository affinityGroupRepository;
    private final AffinityGroupItemRepository affinityGroupItemRepository;
    private final TblSubjectRepository tblSubjectRepository;
    private final TblItemRepository tblItemRepository;
    private final ItemContentLevelRepository itemContentLevelRepository;
    private final TblStrandRepository tblStrandRepository;
    private final TblTestAdminRepository tblTestAdminRepository;

    /**
     * These measurement models and parameters are hardcoded and inserted in the load_measurementparameters stored procedure
     */
    private final List<MeasurementModel> measurementModels = ImmutableList.of(
        new MeasurementModel(1, "IRT3pln"),
        new MeasurementModel(2, "IRTPCL"),
        new MeasurementModel(3, "raw"),
        new MeasurementModel(4, "IRT3PL"),
        new MeasurementModel(5, "IRTGPC")
    );

    private final List<MeasurementParameter> measurementParameters = ImmutableList.of(
        // IRT3pln
        new MeasurementParameter(1, 0, "a", "Slope (a)"),
        new MeasurementParameter(1, 1, "b", "Difficulty (b)"),
        new MeasurementParameter(1, 2, "c", "Guessing (c)"),
        // IRTPCL
        new MeasurementParameter(2, 0, "b0", "Difficulty cut 0 (b0)"),
        new MeasurementParameter(2, 1, "b1", "Difficulty cut 1 (b1)"),
        new MeasurementParameter(2, 2, "b2", "Difficulty cut 2 (b2)"),
        new MeasurementParameter(2, 3, "b3", "Difficulty cut 3 (b3)"),
        new MeasurementParameter(2, 4, "b4", "Difficulty cut 4 (b4)"),
        new MeasurementParameter(2, 5, "b5", "Difficulty cut 5 (b5)"),
        // IRT3PL
        new MeasurementParameter(4, 0, "a", "Slope (a)"),
        new MeasurementParameter(4, 1, "b", "Difficulty (b)"),
        new MeasurementParameter(4, 2, "c", "Guessing (c)"),
        // IRTGPC
        new MeasurementParameter(5, 0, "a", "Slope (a)"),
        new MeasurementParameter(5, 1, "b0", "Difficulty cut 0 (b0)"),
        new MeasurementParameter(5, 2, "b1", "Difficulty cut 1 (b1)"),
        new MeasurementParameter(5, 3, "b2", "Difficulty cut 2 (b2)"),
        new MeasurementParameter(5, 4, "b3", "Difficulty cut 3 (b3)"),
        new MeasurementParameter(5, 5, "b4", "Difficulty cut 4 (b4)"),
        new MeasurementParameter(5, 6, "b5", "Difficulty cut 5 (b5)")
    );

    @Autowired
    public AssessmentLoaderServiceImpl(final ItemBankDataQueryRepository itemBankDataQueryRepository,
                                       final ItemBankDataCommandRepository itemBankDataCommandRepository,
                                       final TblSetOfAdminSubjectsRepository tblSetOfAdminSubjectsRepository,
                                       final TblSetOfAdminItemsRepository tblSetOfAdminItemsRepository,
                                       final TblSetOfItemStrandsRepository tblSetOfItemStrandsRepository,
                                       final SetOfTestGradesRepository setOfTestGradesRepository,
                                       final TestCohortRepository testCohortRepository,
                                       final TblItemSelectionParameterRepository tblItemSelectionParameterRepository,
                                       final TblAdminStrandsRepository tblAdminStrandsRepository,
                                       final ItemScoreDimensionsRepository itemScoreDimensionsRepository,
                                       final MeasurementModelRepository measurementModelRepository,
                                       final MeasurementParameterRepository measurementParameterRepository,
                                       final ItemMeasurementParameterRepository itemMeasurementParameterRepository,
                                       final TblAdminStimuliRepository tblAdminStimuliRepository,
                                       final TestFormRepository testFormRepository,
                                       final TestFormItemRepository testFormItemRepository,
                                       final AffinityGroupRepository affinityGroupRepository,
                                       final AffinityGroupItemRepository affinityGroupItemRepository,
                                       final TblSubjectRepository tblSubjectRepository,
                                       final TblStimuliRepository tblStimuliRepository,
                                       final TblItemRepository tblItemRepository,
                                       final ItemContentLevelRepository itemContentLevelRepository,
                                       final TblStrandRepository tblStrandRepository,
                                       final TblTestAdminRepository tblTestAdminRepository) {
        this.itemBankDataCommandRepository = itemBankDataCommandRepository;
        this.itemBankDataQueryRepository = itemBankDataQueryRepository;
        this.tblSetOfAdminSubjectsRepository = tblSetOfAdminSubjectsRepository;
        this.setOfTestGradesRepository = setOfTestGradesRepository;
        this.testCohortRepository = testCohortRepository;
        this.tblItemSelectionParameterRepository = tblItemSelectionParameterRepository;
        this.tblSetOfAdminItemsRepository = tblSetOfAdminItemsRepository;
        this.tblSetOfItemStrandsRepository = tblSetOfItemStrandsRepository;
        this.tblAdminStrandsRepository = tblAdminStrandsRepository;
        this.itemScoreDimensionsRepository = itemScoreDimensionsRepository;
        this.measurementModelRepository = measurementModelRepository;
        this.measurementParameterRepository = measurementParameterRepository;
        this.itemMeasurementParameterRepository = itemMeasurementParameterRepository;
        this.tblAdminStimuliRepository = tblAdminStimuliRepository;
        this.testFormRepository = testFormRepository;
        this.testFormItemRepository = testFormItemRepository;
        this.affinityGroupRepository = affinityGroupRepository;
        this.affinityGroupItemRepository = affinityGroupItemRepository;
        this.tblSubjectRepository = tblSubjectRepository;
        this.tblStimuliRepository = tblStimuliRepository;
        this.tblItemRepository = tblItemRepository;
        this.itemContentLevelRepository = itemContentLevelRepository;
        this.tblStrandRepository = tblStrandRepository;
        this.tblTestAdminRepository = tblTestAdminRepository;
    }

    /**
     * Loads a {@link tds.testpackage.model.TestPackage} into the itembank and configs tables
     *
     * @param testPackageName The file name of the test package
     * @param testPackage     The test package to load
     * @return an error, if one occurs during the creation of the assessment
     */
    @Override
    @Transactional
    public Optional<ValidationError> loadTestPackage(final String testPackageName, final TestPackage testPackage) {

        loadScoringSeedData();

        //Find client if exists. If not, we need to create one
        final Client client = itemBankDataQueryRepository.findClient(testPackage.getPublisher())
            .orElse(itemBankDataCommandRepository.insertClient(testPackage.getPublisher())); // Insert returns Client

        try {
            final String subjectKey = client.getName() + '-' + testPackage.getSubject();
            final String version = testPackage.getVersion();
            // Contains  all items and other item metadata specified in the test package
            final Map<String, ItemMetadataWrapper> itemIdToItemMetadata = mapItemsToItemMetadata(testPackage);

            /* load_subject() */
            loadSubject(testPackage, client, subjectKey, version);

            /* load_strands() */
            final Map<String, TblStrand> keyToStrands = loadStrands(testPackage.getBlueprint(), subjectKey, client, version);

            /* load_stimulus() */
            loadTblStimuli(testPackage);

            /* load_items() */
            loadTblItems(testPackage, itemIdToItemMetadata);

            /* load_linkitemtostrands() */
            loadLinkItemsToStrands(itemIdToItemMetadata, keyToStrands, testPackage.getVersion());

            /* load_itemproperties() */
            //TODO: Implement this once we get some more info from SBAC/AIR on other item properties
            loadItemProperties(itemIdToItemMetadata);

            /* load_testadmin() */
            loadTestAdmin(testPackage, client);

            /* load_adminsubjects() */
            loadAdminSubjects(testPackage, subjectKey);

            /* load_testgrades() */
            loadTestGrades(testPackage);

            /* load_testcohorts() */
            loadTestCohorts(testPackage);

            /* load_itemselectionparm() */
            loadItemSelectionParm(testPackage);
            
            /* load_adminstrands() */
            loadAdminStrands(testPackage, keyToStrands);

            /* load_adminitems() */
            loadAdminItems(testPackage, itemIdToItemMetadata, keyToStrands);

            /* load_adminitemmeasurementparms() */
            loadAdminItemMeasurementParameters(testPackage, itemIdToItemMetadata);

            /* load_adminstimuli() */
            loadAdminStimuli(testPackage);

            /* load_adminforms() */
            List<TestForm> testForms = loadAdminForms(testPackage);

            /* load_adminformitems() */
            loadAdminFormItems(testPackage, testForms);

            /* load_affinitygroups */
            loadAffinityGroups(testPackage, itemIdToItemMetadata);

        } catch (Exception e) {
            final String error = String.format("An error occurred while loading the test package %s. Message: %s",
                testPackageName, e.getMessage());
            log.error(error);
            return Optional.of(new ValidationError("TDS-Load", error));
        }

        return Optional.empty();
    }

    private void loadSubject(final TestPackage testPackage, final Client client, final String subjectKey, final String version) {
        final TblSubject tblSubject = new TblSubject(testPackage.getSubject(), subjectKey, client.getKey(), version);
        tblSubjectRepository.save(tblSubject);
    }

    private void loadScoringSeedData() {
        // Load the scoring measurement seed data (refer to loader_measurementparameters stored procedure in legacy)
        measurementModelRepository.save(measurementModels);
        measurementParameterRepository.save(measurementParameters);
    }

    private void loadAdminItems(final TestPackage testPackage, final Map<String, ItemMetadataWrapper> itemIdToItemMetadata,
                                final Map<String, TblStrand> keyToStrands) {
        List<TblAdminItem> tblSetOfAdminItems = itemIdToItemMetadata.values().stream()
            .map(itemWrapper -> {
                    Item item = itemWrapper.getItem();

                    final double irtA = item.getItemScoreDimension().getItemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().equals("a"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .max().orElse(1);
                    // This appears to be the average of all item scoring parameters starting with a "b"
                    final double irtB = item.getItemScoreDimension().getItemScoreParameters().stream()
                        .filter(param -> param.getMeasurementParameter().startsWith("b"))
                        .mapToDouble(ItemScoreParameter::getValue)
                        .average().orElse(-9999);
                    final double irtC = item.getItemScoreDimension().getItemScoreParameters().stream()
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
                        .withGroupId(itemWrapper.groupId)
                        .withItemPosition(item.position())
                        .withFieldTest(item.fieldTest())
                        .withActive(true)
                        .withIrtB(String.valueOf(irtB))
                        .withBlockId("A")
                        .withRequired(item.responseRequired())
                        .withClientName(testPackage.getPublisher())
                        .withResponseMimeType(MediaType.TEXT_PLAIN_VALUE)
                        .withStrandKey(leafTargetKey)
                        .withVersion(testPackage.getVersion())
                        .withUpdatedVersion(testPackage.getVersion())
                        .withClaimName(claimName)
                        .withIrtA(String.valueOf(irtA))
                        .withIrtC(String.valueOf(irtC))
                        .withIrtModel(item.getItemScoreDimension().getMeasurementModel())
                        .withNotForScoring(false)
                        .withClString(itemWrapper.isAdaptive() ? createClString(item, keyToStrands) : null)
                        .withFtWeight(DEFAULT_FT_WEIGHT)
                        .build();
                }
            )
            .collect(Collectors.toList());

        tblSetOfAdminItemsRepository.save(tblSetOfAdminItems);
    }

    private void loadAdminStrands(final TestPackage testPackage, final Map<String, TblStrand> keyToStrands) {
        // For each contentlevel (target) and strand (claim), create a tbladminstrand
        List<TblAdminStrand> adminStrands = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                    .flatMap(segment -> segment.getSegmentBlueprint().stream()
                            .filter(bp -> CONTENT_LEVEL_TYPES.contains(keyToStrands.get(bp.getIdRef()).getType()))
                            .map(bp -> {
                                    final Map<String, String> itemSelectionProperties = bp.getItemSelection().stream()
                                        .collect(Collectors.toMap(p -> p.getName().toString().toLowerCase(), Property::getValue));

                                    return new TblAdminStrand.Builder()
                                        .withSegmentKey(segment.getKey())
                                        .withStrandKey(keyToStrands.get(bp.getIdRef()).getKey())
                                        .withMinItems(bp.getMinExamItems())
                                        .withMaxItems(bp.getMaxExamItems())
                                        .withAdaptiveCut(itemSelectionProperties.containsKey("adaptivecut")
                                            ? Double.parseDouble(itemSelectionProperties.get("adaptivecut")) : null)
                                        .withStartAbility(itemSelectionProperties.containsKey("startability")
                                            ? Double.parseDouble(itemSelectionProperties.get("startability")) : null)
                                        .withStartInfo(itemSelectionProperties.containsKey("startinfo")
                                            ? Double.parseDouble(itemSelectionProperties.get("startinfo")) : null)
                                        .withScalar(itemSelectionProperties.containsKey("scalar")
                                            ? Double.parseDouble(itemSelectionProperties.get("scalar")) : null)
                                        //TODO: Investigate these two properties. Sometimes they are null, otherwise equal to min/maxItems
//                            .withLoadMin()
//                            .withLoadMax()
                                        .withStrictMax(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("isstrictmax", "false")))
                                        .withBpWeight(Integer.parseInt(itemSelectionProperties.getOrDefault("bpweight", "1")))
                                        .withVersion(testPackage.getVersion())
                                        .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                                            ? Double.parseDouble(itemSelectionProperties.get("precisiontarget")) : null)
                                        .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontargetmetweight")
                                            ? Double.parseDouble(itemSelectionProperties.get("precisiontargetmetweight")) : null)
                                        .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontargetnotmetweight")
                                            ? Double.parseDouble(itemSelectionProperties.get("precisiontargetnotmetweight")) : null)
                                        .withAbilityWeight(itemSelectionProperties.containsKey("abilityweight")
                                            ? Double.parseDouble(itemSelectionProperties.get("abilityweight")) : null)
                                        .build();
                                }
                            )
                    )
            )
            .collect(Collectors.toList());

        tblAdminStrandsRepository.save(adminStrands);
    }

    private void loadItemSelectionParm(final TestPackage testPackage) {
        // Get all the item selection parameters for CAT segments that are not in the known "algorithm property names" list
        final List<TblItemSelectionParameter> parameters = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream())
            .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.ADAPTIVE_2.getType()))
            .flatMap(segment -> segment.getSegmentBlueprint().stream()
                .filter(bpElement -> bpElement.getIdRef().equalsIgnoreCase(segment.getId()))
                .flatMap(segmentBpElement -> segmentBpElement.getItemSelection().stream()
                    .filter(param -> ALGORITHM_PROPERTY_NAMES.contains(param.getName()))
                    .map(param -> new TblItemSelectionParameter(segment.getKey(), segment.getId(), param.getName(), param.getValue()))
                )
            )
            .collect(Collectors.toList());

        tblItemSelectionParameterRepository.save(parameters);
    }

    private void loadTestCohorts(final TestPackage testPackage) {
        final List<TestCohort> testCohorts = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream())
            .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType()))
            .map(segment -> new TestCohort(segment.getKey()))
            .collect(Collectors.toList());

        testCohortRepository.save(testCohorts);
    }

    private void loadTestGrades(final TestPackage testPackage) {
        for (Assessment asssessment : testPackage.getAssessments()) {
            final List<SetOfTestGrades> testGrades = new ArrayList<>();

            for (Grade grade : asssessment.getGrades()) {
                testGrades.add(new SetOfTestGrades(asssessment.getId(), asssessment.getKey(), grade.getValue()));
            }

            asssessment.getSegments().stream()
                .flatMap(segment -> asssessment.getGrades().stream())
                .map(grade -> new SetOfTestGrades(asssessment.getId(), asssessment.getKey(), grade.getValue()))
                .forEach(testGrades::add);

            setOfTestGradesRepository.save(testGrades);
        }
    }

    private void loadItemProperties(final Map<String, ItemMetadataWrapper> itemIdToItemMetadata) {
        throw new NotImplementedException("ItemProperty load is not implemented");
    }

    private Map<String, ItemMetadataWrapper> mapItemsToItemMetadata(final TestPackage testPackage) {
        return testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                    if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                        return segment.getSegmentForms().stream()
                            .flatMap(form -> form.getItemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.getItems().stream()
                                    .map(item -> new ItemMetadataWrapper(item, testPackage.getBankKey(), segment.getKey(),
                                        itemGroup.getId(), segment.getAlgorithmType().equalsIgnoreCase(Algorithm.ADAPTIVE_2.getType())))
                                )
                            );
                    } else if (segment.getAlgorithmType().equals(Algorithm.ADAPTIVE_2.getType())) {
                        return segment.getPool().stream()
                            .flatMap(itemGroup -> itemGroup.getItems().stream()
                                .map(item -> new ItemMetadataWrapper(item, testPackage.getBankKey(), segment.getKey(),
                                    itemGroup.getId(), segment.getAlgorithmType().equalsIgnoreCase(Algorithm.ADAPTIVE_2.getType())))
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                }
            )).collect(Collectors.toMap(itemWrapper -> itemWrapper.getItem().getId(), itemWrapper -> itemWrapper));
    }

    private Map<String, TblStrand> loadStrands(final List<BlueprintElement> blueprintElements, final String subjectKey,
                                               final Client client, final String version) {
        final List<TblStrand> tblStrands = new ArrayList<>();
        final int treeLevel = 1;
        // Begin the recursive call with null parent and
        loadBlueprintElementsHelper(blueprintElements, tblStrands, client, null, subjectKey, version, treeLevel);
        tblStrandRepository.save(tblStrands);

        return tblStrands.stream()
            .collect(Collectors.toMap(TblStrand::getKey, s -> s));
    }

    private List<TblStrand> loadBlueprintElementsHelper(final List<BlueprintElement> blueprintElements,
                                                        final List<TblStrand> tblStrands,
                                                        final Client client,
                                                        final String parentKey,
                                                        final String subjectKey,
                                                        final String version,
                                                        final int treeLevel) {
        /**
         * BIG TODO:
         *  The code below assumes each blueprint element id will contain it's hierarchy embedded, as is the case in legacy.
         *  We are still waiting on feedback from SmarterBalanced, PCG, and AIR on what these blueprint elements should actually
         *  look like.
         *
         *  We need to check whether the content level/target names will contain the entire hierarchy embedded into each string.
         *  For example, in the legacy test specification packages, the strand with the key "SBAC_PT-4|OA|D|NA|NA" contains the
         *  parent keys of its hierarchy embedded within the key (delimited by pipes '|'). If this is not the case, and we should infer the
         *  string based on the name of the parent keys programmatically, the logic below will need to be updated
         */

        // Recursively create the tblStrands we will insert into the database
        for (BlueprintElement bpElement : blueprintElements) {
            final String key = client.getName() + '-' + bpElement.getId();
            // Leaf node - Let's create the strand out of this blueprint element and add it to the list we will persist
            if (bpElement.getBlueprintElements().size() == 0) {
                TblStrand tblStrand = new TblStrand.Builder()
                    .withName(bpElement.getId())
                    .withParentKey(parentKey)
                    .withKey(key)
                    .withClientKey(client.getKey())
                    .withTreeLevel(treeLevel)
                    .withVersion(version)
                    .withType(bpElement.getType())
                    .withSubjectKey(subjectKey)
                    // this is important - we want to flag this as a "leaf" target node for use later in the loading process
                    .withLeafTarget(true)
                    .build();
                tblStrands.add(tblStrand);
            } else {
                for (BlueprintElement innerBpElement : bpElement.getBlueprintElements()) {
                    // Recursively load the leaf node
                    this.loadBlueprintElementsHelper(innerBpElement.getBlueprintElements(),
                        tblStrands, client, key, subjectKey, version, treeLevel + 1);
                }
            }
        }

        return tblStrands;
    }

    private void loadTblStimuli(final TestPackage testPackage) {
        final int bankKey = testPackage.getBankKey();
        final String version = testPackage.getVersion();

        List<TblStimulus> stimuli = testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                    return segment.getSegmentForms().stream()
                        .flatMap(form -> form.getItemGroups().stream()
                            .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                            .map(itemGroup -> itemGroup.getStimulus())
                            .map(stimulus -> mapStimuliToTblStimuli(bankKey, version, stimulus)));

                } else if (segment.getAlgorithmType().equals(Algorithm.ADAPTIVE_2.getType())) {
                    return segment.getPool().stream()
                        .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                        .map(itemGroup -> itemGroup.getStimulus())
                        .map(stimulus -> mapStimuliToTblStimuli(bankKey, version, stimulus));
                } else {
                    throw new TestPackageLoaderException("Unrecognized selection algorithm");
                }
            })
        ).collect(Collectors.toList());

        tblStimuliRepository.save(stimuli);
    }

    private void loadTblItems(final TestPackage testPackage, final Map<String, ItemMetadataWrapper> itemIdToItemMetadata) {
        List<TblItem> items = itemIdToItemMetadata.values().stream()
            .map(itemWrapper -> mapItemToTblItem(testPackage.getBankKey(), testPackage.getVersion(), itemWrapper.getItem()))
            .collect(Collectors.toList());

        tblItemRepository.save(items);
    }

    // Inserts strand, content level, and affinity group blueprint elements to aa_itemcl
    private void loadLinkItemsToStrands(final Map<String, ItemMetadataWrapper> itemMetadataWrapperMap,
                                        final Map<String, TblStrand> keyToStrands, final String version) {
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

    private void loadTestAdmin(final TestPackage testPackage, final Client client) {
        TblTestAdmin testAdmin = tblTestAdminRepository.findOne(client.getName());

        if (testAdmin != null) {
            itemBankDataCommandRepository.updateTblTestAdminVersion(testAdmin.getKey(), testPackage.getVersion());
        } else {
            final TblTestAdmin tblTestAdmin = new TblTestAdmin.Builder()
                .withAcademicYear(testPackage.getAcademicYear())
                .withClientKey(client.getKey())
                .withKey(client.getName())
                .withVersion(testPackage.getVersion())
                .build();

            tblTestAdminRepository.save(tblTestAdmin);
        }
    }

    private void loadAdminSubjects(final TestPackage testPackage, final String subjectKey) {
        for (Assessment assessment : testPackage.getAssessments()) {
            // If the segment is multisegmented, <num of segments> + 1 inserts into the admin subjects table (one "virtual" row)
            boolean isSegmented = assessment.getSegments().size() > 1;
            List<TblAdminSubject> adminSubjects = new ArrayList<>();

            /**
             * Many of the properties in the "virtual" row will contain sums of its segments. For example, if seg1 has
             * a "minitems" value of 3, and "maxitems" value of 4, and seg2 has a "minitems" value of 1 and a "maxitems" value of 1,
             * the "virtual" row will contain minitems = 4 and maxitems = 5
             */
            if (isSegmented) {
                // These four variables will hold the total counts of min/max items for all combined segments in the assessment
                int minItemCount = 0;
                int maxItemCount = 0;
                int fieldTestMinItemCount = 0;
                int fieldTestMaxItemCount = 0;

                for (Segment segment : assessment.getSegments()) {
                    final SegmentBlueprintElement segmentBpElement = segment.getSegmentBlueprint().stream()
                        .filter(bp -> bp.getIdRef().equals(segment.getId())) // get the actual segment blueprint (not any child claims/targets)
                        .findFirst()
                        .orElseThrow(() ->
                            new TestPackageLoaderException("No matching segment blueprint element found in the test package for id {}", segment.getId()));

                    minItemCount += segmentBpElement.getMinExamItems();
                    maxItemCount += segmentBpElement.getMinExamItems();
                    fieldTestMinItemCount += segmentBpElement.minFieldTestItems();
                    fieldTestMaxItemCount += segmentBpElement.maxFieldTestItems();

                    // Create a map to access the properties by name. Lowercasing the key to prevent casing issues
                    final Map<String, String> itemSelectionProperties = segmentBpElement.getItemSelection().stream()
                        .collect(Collectors.toMap(p -> p.getName().toString().toLowerCase(), Property::getValue));
                    //TODO: Refactor this out
                    adminSubjects.add(new TblAdminSubject.Builder()
                        .withKey(segment.getKey())
                        .withClientName(testPackage.getPublisher())
                        .withSubjectKey(subjectKey)
                        .withId(segment.getId())
                        .withStartAbility(Float.parseFloat(itemSelectionProperties.getOrDefault("startability", DEFAULT_START_ABILITY)))
                        .withStartInfo(Float.parseFloat(itemSelectionProperties.getOrDefault("startinfo", DEFAULT_START_INFO)))
                        .withMinItems(segmentBpElement.getMinExamItems())
                        .withMaxItems(segmentBpElement.getMaxExamItems())
                        .withSlope(Float.parseFloat(itemSelectionProperties.getOrDefault("slope", DEFAULT_SLOPE)))
                        .withIntercept(Float.parseFloat(itemSelectionProperties.getOrDefault("intercept", DEFAULT_INTERCEPT)))
                        .withFieldTestStartPos(itemSelectionProperties.containsKey("ftstartpos")
                            ? Integer.parseInt(itemSelectionProperties.get("ftstartpos"))
                            : DEFAULT_FT_START_POS)
                        .withFieldTestEndPos(itemSelectionProperties.containsKey("ftendpos")
                            ? Integer.parseInt(itemSelectionProperties.get("ftendpos"))
                            : DEFAULT_FT_END_POS)
                        .withFieldTestMinItems(segmentBpElement.minFieldTestItems())
                        .withFieldTestMaxItems(segmentBpElement.maxFieldTestItems())
                        .withSelectionAlgorithm(segment.getAlgorithmType()) //TODO: We may need to map this to "fixedform" or "adaptive2"
                        .withBlueprintWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("blueprintweight", DEFAULT_BLUEPRINT_WEIGHT)))
                        .withAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityweight", DEFAULT_ABILITY_WEIGHT)))
                        .withCSet1Size(Integer.parseInt(itemSelectionProperties.getOrDefault("cset1size", DEFAULT_CSET1_SIZE)))
                        .withCSet2Random(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2random", DEFAULT_CSET2_RANDOM)))
                        .withCSet2InitialRandom(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2initialrandom", DEFAULT_CSET2_INITIAL_RANDOM)))
                        .withVirtualTest(assessment.getKey())
                        .withTestPosition(segment.position())
                        .withSegmented(false)
                        .withComputeAbilityEstimates(
                            Boolean.parseBoolean(itemSelectionProperties.getOrDefault("computeabilityestimates", DEFAULT_COMPUTE_ABILITY_ESTIMATES)))
                        .withVersion(testPackage.getVersion())
                        .withItemWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("itemweight", DEFAULT_ITEM_WEIGHT)))
                        .withAbilityOffset(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityoffset", DEFAULT_ABILITY_OFFSET)))
                        .withCSet1Order(itemSelectionProperties.getOrDefault("cset1order", DEFAULT_CSET1_ORDER))
                        .withRcAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("rcabilityweight", DEFAULT_RC_ABILITY_WEIGHT)))
                        .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                            ? Float.parseFloat(itemSelectionProperties.get("precisiontarget"))
                            : DEFAULT_PRECISION_TARGET)
                        .withPrecisionTargetMetWeight(Float.parseFloat(
                            itemSelectionProperties.getOrDefault("precisiontargetmetweight", DEFAULT_PRECISION_TARGET_MET_WEIGHT)))
                        .withPrecisionTargetNotMetWeight(Float.parseFloat(
                            itemSelectionProperties.getOrDefault("precisiontargetnotmetweight", DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT)))
                        .withAdaptiveCut(itemSelectionProperties.containsKey("adaptivecut")
                            ? Float.parseFloat(itemSelectionProperties.get("adaptivecut"))
                            : DEFAULT_ADAPTIVE_CUT)
                        .withTooCloseses(itemSelectionProperties.containsKey("toocloseses")
                            ? Float.parseFloat(itemSelectionProperties.get("toocloseses"))
                            : DEFAULT_TOO_CLOSESES)
                        .withTerminationOverallInfo(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationoverallinfo", DEFAULT_TERMINATION_FLAGS)))
                        .withTerminationRCInfo(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationrcinfo", DEFAULT_TERMINATION_FLAGS)))
                        .withTerminationMinCount(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationmincount", DEFAULT_TERMINATION_FLAGS)))
                        .withTerminationTooClose(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationtooclose", DEFAULT_TERMINATION_FLAGS)))
                        .withTerminationFlagsAnd(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationflagsand", DEFAULT_TERMINATION_FLAGS)))
                        .withBpMetricFunction("bp1")
                        .withTestType(testPackage.getType())
                        .build()
                    );
                }

                // Now add the assessment (virtual) row - this row will use all defaults for the itemselectionproperties
                // and contains a summation of the segment's min/max [fieldtest] item properties.
                adminSubjects.add(new TblAdminSubject.Builder()
                    .withKey(assessment.getKey())
                    .withClientName(testPackage.getPublisher())
                    .withSubjectKey(subjectKey)
                    .withId(assessment.getId())
                    .withStartAbility(Float.parseFloat(DEFAULT_START_ABILITY))
                    .withStartInfo(Float.parseFloat(DEFAULT_START_INFO))
                    .withMinItems(minItemCount)
                    .withMaxItems(maxItemCount)
                    .withSlope(Float.parseFloat(DEFAULT_SLOPE))
                    .withIntercept(Float.parseFloat(DEFAULT_INTERCEPT))
                    .withFieldTestStartPos(DEFAULT_FT_START_POS)
                    .withFieldTestEndPos(DEFAULT_FT_END_POS)
                    .withFieldTestMinItems(fieldTestMinItemCount)
                    .withFieldTestMaxItems(fieldTestMaxItemCount)
                    .withSelectionAlgorithm("virtual")
                    .withBlueprintWeight(Float.parseFloat(DEFAULT_BLUEPRINT_WEIGHT))
                    .withAbilityWeight(Float.parseFloat(DEFAULT_ABILITY_WEIGHT))
                    .withCSet1Size(Integer.parseInt(DEFAULT_CSET1_SIZE))
                    .withCSet2Random(Integer.parseInt(DEFAULT_CSET2_RANDOM))
                    .withCSet2InitialRandom(Integer.parseInt(DEFAULT_CSET2_INITIAL_RANDOM))
                    .withSegmented(true)
                    .withComputeAbilityEstimates(
                        Boolean.parseBoolean(DEFAULT_COMPUTE_ABILITY_ESTIMATES))
                    .withVersion(testPackage.getVersion())
                    .withItemWeight(Float.parseFloat(DEFAULT_ITEM_WEIGHT))
                    .withAbilityOffset(Float.parseFloat(DEFAULT_ABILITY_OFFSET))
                    .withCSet1Order(DEFAULT_CSET1_ORDER)
                    .withRcAbilityWeight(Float.parseFloat(DEFAULT_RC_ABILITY_WEIGHT))
                    .withPrecisionTarget(DEFAULT_PRECISION_TARGET)
                    .withPrecisionTargetMetWeight(Float.parseFloat(DEFAULT_PRECISION_TARGET_MET_WEIGHT))
                    .withPrecisionTargetNotMetWeight(Float.parseFloat(DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT))
                    .withAdaptiveCut(DEFAULT_ADAPTIVE_CUT)
                    .withTooCloseses(DEFAULT_TOO_CLOSESES)
                    .withTerminationOverallInfo(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationRCInfo(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationMinCount(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationTooClose(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationFlagsAnd(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withBpMetricFunction("bp1")
                    .withTestType(testPackage.getType())
                    .build()
                );
            } else {
                Segment segment = assessment.getSegments().get(0);
                final SegmentBlueprintElement segmentBpElement = segment.getSegmentBlueprint().stream()
                    .filter(bp -> bp.getIdRef().equals(segment.getId())) // get the actual segment blueprint (not any child claims/targets)
                    .findFirst()
                    .orElseThrow(() ->
                        new TestPackageLoaderException("No matching segment blueprint element found in the test package for id {}", segment.getId()));

                // Create a map to access the properties by name. Lowercasing the key to prevent casing issues
                final Map<String, String> itemSelectionProperties = segmentBpElement.getItemSelection().stream()
                    .collect(Collectors.toMap(p -> p.getName().toString().toLowerCase(), Property::getValue));
                //TODO: Refactor this out
                adminSubjects.add(new TblAdminSubject.Builder()
                    .withKey(segment.getKey())
                    .withClientName(testPackage.getPublisher())
                    .withSubjectKey(subjectKey)
                    .withId(segment.getId())
                    .withStartAbility(Float.parseFloat(itemSelectionProperties.getOrDefault("startability", DEFAULT_START_ABILITY)))
                    .withStartInfo(Float.parseFloat(itemSelectionProperties.getOrDefault("startinfo", DEFAULT_START_INFO)))
                    .withMinItems(segmentBpElement.getMinExamItems())
                    .withMaxItems(segmentBpElement.getMaxExamItems())
                    .withSlope(Float.parseFloat(itemSelectionProperties.getOrDefault("slope", DEFAULT_SLOPE)))
                    .withIntercept(Float.parseFloat(itemSelectionProperties.getOrDefault("intercept", DEFAULT_INTERCEPT)))
                    .withFieldTestStartPos(itemSelectionProperties.containsKey("ftstartpos")
                        ? Integer.parseInt(itemSelectionProperties.get("ftstartpos"))
                        : DEFAULT_FT_START_POS)
                    .withFieldTestEndPos(itemSelectionProperties.containsKey("ftendpos")
                        ? Integer.parseInt(itemSelectionProperties.get("ftendpos"))
                        : DEFAULT_FT_END_POS)
                    .withFieldTestMinItems(segmentBpElement.minFieldTestItems())
                    .withFieldTestMaxItems(segmentBpElement.maxFieldTestItems())
                    .withSelectionAlgorithm(segment.getAlgorithmType()) //TODO: We may need to map this to "fixedform" or "adaptive2"
                    .withBlueprintWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("blueprintweight", DEFAULT_BLUEPRINT_WEIGHT)))
                    .withAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityweight", DEFAULT_ABILITY_WEIGHT)))
                    .withCSet1Size(Integer.parseInt(itemSelectionProperties.getOrDefault("cset1size", DEFAULT_CSET1_SIZE)))
                    .withCSet2Random(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2random", DEFAULT_CSET2_RANDOM)))
                    .withCSet2InitialRandom(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2initialrandom", DEFAULT_CSET2_INITIAL_RANDOM)))
                    .withTestPosition(segment.position())
                    .withSegmented(false)
                    .withComputeAbilityEstimates(
                        Boolean.parseBoolean(itemSelectionProperties.getOrDefault("computeabilityestimates", DEFAULT_COMPUTE_ABILITY_ESTIMATES)))
                    .withVersion(testPackage.getVersion())
                    .withItemWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("itemweight", DEFAULT_ITEM_WEIGHT)))
                    .withAbilityOffset(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityoffset", DEFAULT_ABILITY_OFFSET)))
                    .withCSet1Order(itemSelectionProperties.getOrDefault("cset1order", DEFAULT_CSET1_ORDER))
                    .withRcAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("rcabilityweight", DEFAULT_RC_ABILITY_WEIGHT)))
                    .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                        ? Float.parseFloat(itemSelectionProperties.get("precisiontarget"))
                        : DEFAULT_PRECISION_TARGET)
                    .withPrecisionTargetMetWeight(Float.parseFloat(
                        itemSelectionProperties.getOrDefault("precisiontargetmetweight", DEFAULT_PRECISION_TARGET_MET_WEIGHT)))
                    .withPrecisionTargetNotMetWeight(Float.parseFloat(
                        itemSelectionProperties.getOrDefault("precisiontargetnotmetweight", DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT)))
                    .withAdaptiveCut(itemSelectionProperties.containsKey("adaptivecut")
                        ? Float.parseFloat(itemSelectionProperties.get("adaptivecut"))
                        : DEFAULT_ADAPTIVE_CUT)
                    .withTooCloseses(itemSelectionProperties.containsKey("toocloseses")
                        ? Float.parseFloat(itemSelectionProperties.get("toocloseses"))
                        : DEFAULT_TOO_CLOSESES)
                    .withTerminationOverallInfo(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationoverallinfo", DEFAULT_TERMINATION_FLAGS)))
                    .withTerminationRCInfo(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationrcinfo", DEFAULT_TERMINATION_FLAGS)))
                    .withTerminationMinCount(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationmincount", DEFAULT_TERMINATION_FLAGS)))
                    .withTerminationTooClose(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationtooclose", DEFAULT_TERMINATION_FLAGS)))
                    .withTerminationFlagsAnd(Boolean.parseBoolean(itemSelectionProperties.getOrDefault("terminationflagsand", DEFAULT_TERMINATION_FLAGS)))
                    .withBpMetricFunction("bp1")
                    .withTestType(testPackage.getType())
                    .build()
                );
            }

            tblSetOfAdminSubjectsRepository.save(adminSubjects);
        }
    }

    private void loadAdminItemMeasurementParameters(final TestPackage testPackage,
                                                    final Map<String, ItemMetadataWrapper> itemIdToItemMetadata) {
        // Create a bi-directional mapping of the measurement model strings to their keys for quick lookup
        BiMap<String, Integer> measurementModelKeys = HashBiMap.create(
            measurementModels.stream()
                .collect(Collectors.toMap(MeasurementModel::getModelName, MeasurementModel::getModelNumber))
        );

        // <ModelName>-<ParamName> -> Parameter key/num
        Map<String, Integer> modelToMeasurementParameterMap = measurementParameters.stream()
            .collect(Collectors.toMap(
                param -> measurementModelKeys.inverse().get(param.getMeasurementParameterIdentity().getMeasurementModelKey()) + "-" + param.getParameterName(),
                param -> param.getMeasurementParameterIdentity().getParameterNumber())
            );

        // Create a map for quick look up of the item dimensions
        Map<String, ItemScoreDimension> itemScoreDimensionsMap = itemIdToItemMetadata.values().stream()
            .map(wrapper -> new ItemScoreDimension.Builder()
                    //TODO: Uncomment once mike has updated with dimension attribute
//                .withDimension(wrapper.getItem().getItemScoreDimension().dimension())
                    .withScorePoints(wrapper.getItem().getItemScoreDimension().getScorePoints())
                    .withWeight(wrapper.getItem().getItemScoreDimension().getWeight())
                    .withKey(UUID.randomUUID())
                    .withSegmentKey(wrapper.getSegmentKey())
                    .withItemId(wrapper.getItemKey())
                    .withMeasurementModelKey(measurementModelKeys.get(wrapper.getItem().getItemScoreDimension().getMeasurementModel()))
                    .build()
            ).collect(Collectors.toMap(dimension -> dimension.getItemId(), Function.identity()));

        itemScoreDimensionsRepository.save(itemScoreDimensionsMap.values());

        // Stream over each item score dimension and create a list of ItemMeasurementParameters
        List<ItemMeasurementParameter> itemMeasurementParameters = itemScoreDimensionsMap.values().stream()
            .map(dimension -> itemIdToItemMetadata.get(dimension.getItemId()))
            .flatMap(wrapper -> wrapper.getItem().getItemScoreDimension().getItemScoreParameters().stream()
                .map(param ->
                    // This part is tricky - we need to get the foreign key of the parameter - which is not keyable based on on only the parameter name.
                    // This is because parameter keys can be different for different measurement model/param name combinations. For example, the param name "b1"
                    // has a param number of "1" for the IRTPCL model, while the number is "2" for the IRTGPC model. Lets look up based on model name and param num
                    new ItemMeasurementParameter(itemScoreDimensionsMap.get(wrapper.getItemKey()).getKey(),
                        modelToMeasurementParameterMap.get(wrapper.getItem().getItemScoreDimension().getMeasurementModel() + "-" + param.getMeasurementParameter()),
                        param.getValue())
                )
            )
            .collect(Collectors.toList());

        itemMeasurementParameterRepository.save(itemMeasurementParameters);
        //TODO: Update the bvector for each tblsetofadminitem - Not sure if we'll be able to use the bvector function as we are working within a transaction here
    }

    private void loadAdminStimuli(final TestPackage testPackage) {
        List<TblAdminStimulus> tblAdminStimuli = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> {
                        if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                            return segment.getSegmentForms().stream()
                                .flatMap(form -> form.getItemGroups().stream()
                                    .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                                    .map(itemGroup -> mapStimToTblAdminStimulus(itemGroup, testPackage.getBankKey(), segment.getKey(), testPackage.getVersion())
                                    )
                                );

                        } else if (segment.getAlgorithmType().equals(Algorithm.ADAPTIVE_2.getType())) {
                            return segment.getPool().stream()
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

    private List<TestForm> loadAdminForms(final TestPackage testPackage) {
        // We'll use this atomic long value to keep track of the "unique" form keys we will be generating. This key
        // used to be a required field in the old test package spec but has been removed - we are generating an "internal"
        // database key manually for backup compatibility purposes
        AtomicLong newFormKey = new AtomicLong(itemBankDataQueryRepository.generateFormKey());

        List<TestForm> testForms = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType()))
                .flatMap(segment -> segment.getSegmentForms().stream()
                    .flatMap(form -> form.getPresentations().stream()
                        .map(presentation -> new TestForm.Builder()
                            .withSegmentKey(segment.getKey())
                            .withKey(newFormKey.get())
                            .withFormId(form.getId())
                            .withLanguage(presentation)
                            .withFormKey(String.format("%s-%s", testPackage.getBankKey(), newFormKey.getAndIncrement())) // increment after fetching for the next iteration
                            .withVersion(testPackage.getVersion())
                            .withCohort(form.getCohort())
                            .build()
                        )
                    )
                )
            )
            .collect(Collectors.toList());

        testFormRepository.save(testForms);

        return testForms;
    }

    private void loadAdminFormItems(final TestPackage testPackage, final List<TestForm> testForms) {
        // formId -> testForm
        Map<String, TestForm> testFormMap = testForms.stream().collect(Collectors.toMap(TestForm::getFormId, f -> f));

        List<TestFormItem> testFormItems = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType()))
                .flatMap(segment -> segment.getSegmentForms().stream()
                    .flatMap(form -> form.getItemGroups().stream()
                        .flatMap(itemGroup -> itemGroup.getItems().stream()
                            .map(formItem ->
                                new TestFormItem.Builder(
                                    formItem.position(),
                                    segment.getKey(), String.format("%s-%s", testPackage.getBankKey(),
                                    //TODO: Replace this with .getId() on Item
                                    formItem.getId()),
                                    testFormMap.get(form.getId()).getFormKey())
                                .withFormItsKey(testFormMap.get(form.getId()).getItsKey())
                                .withActive(true)
                                .build()
                            )
                        )
                    )
                )
            ).collect(Collectors.toList());

        testFormItemRepository.save(testFormItems);
    }

    private void loadAffinityGroups(final TestPackage testPackage, final Map<String, ItemMetadataWrapper> itemIdToItemMetadata) {
        // No need for recursive searching since affinitygroups are always at the root level
        Map<String, BlueprintElement> affinityGroupBpElements = testPackage.getBlueprint().stream()
            .filter(bpElement -> bpElement.getType().equalsIgnoreCase("affinitygroup"))
            .collect(Collectors.toMap(BlueprintElement::getId, Function.identity()));

        List<AffinityGroup> affinityGroups = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> segment.getSegmentBlueprint().stream()
                    .filter(segBpElement -> affinityGroupBpElements.containsKey(segBpElement.getIdRef()))
                    .map(segBpElement -> {
                            final Map<String, String> itemSelectionProperties = segBpElement.getItemSelection().stream()
                                .collect(Collectors.toMap(p -> p.getName().toString().toLowerCase(), Property::getValue));

                            return new AffinityGroup.Builder()
                                .withAffiniyGroupId(new AffinityGroupIdentity(segment.getKey(), segBpElement.getIdRef()))
                                .withMinItems(segBpElement.getMinExamItems())
                                .withMaxItems(segBpElement.getMaxExamItems())
                                // Begin optional properties with defaults
                                .withStrictMax(itemSelectionProperties.containsKey("isstrictmax")
                                    ? Boolean.parseBoolean(itemSelectionProperties.get("isstrictmax"))
                                    : false)
                                .withWeight(itemSelectionProperties.containsKey("bpweight")
                                    ? Double.parseDouble(itemSelectionProperties.get("bpweight"))
                                    : null)
                                .withStartAbility(itemSelectionProperties.containsKey("startability")
                                    ? Double.parseDouble(itemSelectionProperties.get("startability"))
                                    : null)
                                .withStartInfo(itemSelectionProperties.containsKey("startinfo")
                                    ? Double.parseDouble(itemSelectionProperties.get("startinfo"))
                                    : null)
                                .withAbilityWeight(itemSelectionProperties.containsKey("abilityweight")
                                    ? Double.parseDouble(itemSelectionProperties.get("abilityweight"))
                                    : null)
                                .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                                    ? Double.parseDouble(itemSelectionProperties.get("precisiontarget"))
                                    : null)
                                .withPrecisionTargetMetWeight(itemSelectionProperties.containsKey("precisiontargetmetweight")
                                    ? Double.parseDouble(itemSelectionProperties.get("precisiontargetmetweight"))
                                    : null)
                                .withPrecisionTargetNotMetWeight(itemSelectionProperties.containsKey("precisiontargetnotmetweight")
                                    ? Double.parseDouble(itemSelectionProperties.get("precisiontargetnotmetweight"))
                                    : null)
                                .withVersion(testPackage.getVersion())
                                .withUpdatedVersion(testPackage.getVersion())
                                .build();
                        }
                    )
                )
            )
            .collect(Collectors.toList());

        affinityGroupRepository.save(affinityGroups);

        List<AffinityGroupItem> affinityGroupItems = itemIdToItemMetadata.values().stream()
            .flatMap(wrapper -> wrapper.getItem().getBlueprintReferences().stream()
                .filter(ref -> affinityGroupBpElements.containsKey(ref.getIdRef()))
                .map(ref -> new AffinityGroupItem(
                    new AffinityGroupItemIdentity(wrapper.getSegmentKey(), ref.getIdRef(), wrapper.getItemKey())))
            )
            .collect(Collectors.toList());

        affinityGroupItemRepository.save(affinityGroupItems);
    }

    private static TblAdminStimulus mapStimToTblAdminStimulus(final tds.testpackage.model.ItemGroup itemGroup, final int bankKey,
                                                              final String segmentKey, final String version) {
        return new TblAdminStimulus.Builder(String.format("%s-%s", bankKey, itemGroup.getStimulus().get().getId()), segmentKey)
            .withNumItemsRequired(itemGroup.maxResponses().equalsIgnoreCase("ALL") ? -1 : Integer.parseInt(itemGroup.maxResponses()))
            .withMaxItems(itemGroup.maxItems().equalsIgnoreCase("ALL") ? -1 : Integer.parseInt(itemGroup.maxResponses()))
            .withVersion(version)
            .withUpdateVersion(version)
            .withGroupId(itemGroup.getId())
            .build();
    }

    private static TblItem mapItemToTblItem(final int bankKey, final String version, final Item item) {
        final String fileName = String.format("item-%s-%s.xml", bankKey, item.getId());
        final String filePath = String.format("item-%s-%s/", bankKey, item.getId());

        return new TblItem.Builder()
            .withId(Integer.parseInt(item.getId()))
            .withItemType(item.getType())
            .withBankKey(bankKey)
            .withVersion(version)
            .withScorePoints(item.getItemScoreDimension().getScorePoints())
            .withFileName(fileName)
            .withFilePath(filePath)
            .build();
    }

    private static TblStimulus mapStimuliToTblStimuli(final int bankKey, final String version, final Optional<Stimulus> stimulus) {
        final String fileName = String.format("stim-%s-%s.xml", bankKey, stimulus.get().getId());
        final String filePath = String.format("stim-%s-%s/", bankKey, stimulus.get().getId());
        return new TblStimulus.Builder()
            .withKey(Integer.parseInt(stimulus.get().getId()))
            .withBankKey(bankKey)
            .withVersion(version)
            .withFileName(fileName)
            .withFilePath(filePath)
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


    /**
     * A private wrapper/helper class to enscapsulate various important pieces of metadata along with an item
     */
    private class ItemMetadataWrapper {
        private Item item;
        private String segmentKey;
        private int bankKey;
        private String groupId;
        private boolean adaptive;

        public ItemMetadataWrapper(final tds.testpackage.model.Item item, final int bankKey, final String segmentKey, final String groupId,
                                   final boolean adaptive) {
            this.item = item;
            this.segmentKey = segmentKey;
            this.bankKey = bankKey;
            this.groupId = groupId;
            this.adaptive = adaptive;
        }

        public Item getItem() {
            return item;
        }

        public String getSegmentKey() {
            return segmentKey;
        }

        public String getItemKey() {
            return String.format("%s-%s", bankKey, item.getId());
        }

        public String getGroupId() {
            return groupId;
        }

        public boolean isAdaptive() {
            return adaptive;
        }
    }
}
