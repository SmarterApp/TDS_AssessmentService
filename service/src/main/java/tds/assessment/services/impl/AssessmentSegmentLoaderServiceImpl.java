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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.SetOfTestGrades;
import tds.assessment.model.itembank.TblAdminSubject;
import tds.assessment.model.itembank.TblTestAdmin;
import tds.assessment.model.itembank.TestCohort;
import tds.assessment.repositories.loader.itembank.ItemBankDataCommandRepository;
import tds.assessment.repositories.loader.itembank.SetOfTestGradesRepository;
import tds.assessment.repositories.loader.itembank.TblSetOfAdminSubjectsRepository;
import tds.assessment.repositories.loader.itembank.TblTestAdminRepository;
import tds.assessment.repositories.loader.itembank.TestCohortRepository;
import tds.assessment.services.AssessmentSegmentLoaderService;
import tds.common.Algorithm;
import tds.testpackage.model.Assessment;
import tds.testpackage.model.Property;
import tds.testpackage.model.Segment;
import tds.testpackage.model.SegmentBlueprintElement;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentSegmentLoaderServiceImpl implements AssessmentSegmentLoaderService {
    /* Blueprint Element Defaults */
    private static final String DEFAULT_START_ABILITY = "0";
    private static final String DEFAULT_START_INFO = "1";
    private static final String DEFAULT_SLOPE = "1";
    private static final String DEFAULT_INTERCEPT = "1";
    private static final String DEFAULT_BLUEPRINT_WEIGHT = "5.0";
    private static final String DEFAULT_ABILITY_WEIGHT = "1";
    private static final String DEFAULT_CSET1_SIZE = "20";
    private static final String DEFAULT_CSET2_RANDOM = "1";
    private static final String DEFAULT_CSET2_INITIAL_RANDOM = "5";
    private static final String DEFAULT_COMPUTE_ABILITY_ESTIMATES = "true";
    private static final String DEFAULT_ITEM_WEIGHT = "5";
    private static final String DEFAULT_ABILITY_OFFSET = "0.0";
    private static final String DEFAULT_CSET1_ORDER = "ABILITY";
    private static final String DEFAULT_RC_ABILITY_WEIGHT = "1";
    private static final String DEFAULT_PRECISION_TARGET_MET_WEIGHT = "1";
    private static final String DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT = "1";
    private static final String DEFAULT_TERMINATION_FLAGS = "false";

    private final TblSetOfAdminSubjectsRepository tblSetOfAdminSubjectsRepository;
    private final SetOfTestGradesRepository setOfTestGradesRepository;
    private final TestCohortRepository testCohortRepository;
    private final TblTestAdminRepository tblTestAdminRepository;
    private final ItemBankDataCommandRepository itemBankDataCommandRepository;

    @Autowired
    public AssessmentSegmentLoaderServiceImpl(final TblSetOfAdminSubjectsRepository tblSetOfAdminSubjectsRepository,
                                              final SetOfTestGradesRepository setOfTestGradesRepository,
                                              final TestCohortRepository testCohortRepository,
                                              final TblTestAdminRepository tblTestAdminRepository,
                                              final ItemBankDataCommandRepository itemBankDataCommandRepository) {
        this.tblSetOfAdminSubjectsRepository = tblSetOfAdminSubjectsRepository;
        this.setOfTestGradesRepository = setOfTestGradesRepository;
        this.testCohortRepository = testCohortRepository;
        this.tblTestAdminRepository = tblTestAdminRepository;
        this.itemBankDataCommandRepository = itemBankDataCommandRepository;
    }

    @Override
    public void loadTestCohorts(final TestPackage testPackage) {
        final List<TestCohort> testCohorts = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream())
            .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType()))
            .map(segment -> new TestCohort(segment.getKey()))
            .collect(Collectors.toList());

        testCohortRepository.save(testCohorts);
    }

    @Override
    public void loadTestGrades(final TestPackage testPackage) {
        for (Assessment asssessment : testPackage.getAssessments()) {
            final List<SetOfTestGrades> testGrades = asssessment.getSegments().stream()
                .flatMap(segment -> asssessment.getGrades().stream())
                .map(grade -> new SetOfTestGrades(asssessment.getId(), asssessment.getKey(), grade.getValue()))
                .collect(Collectors.toList());

            setOfTestGradesRepository.save(testGrades);
        }
    }

    @Override
    public void loadTestAdmin(final TestPackage testPackage, final Client client) {
        TblTestAdmin testAdmin = tblTestAdminRepository.findOne(client.getName());

        if (testAdmin != null) {
            itemBankDataCommandRepository.updateTblTestAdminVersion(testAdmin.getKey(), testPackage.getVersion());
        } else {
            final TblTestAdmin tblTestAdmin = new TblTestAdmin.Builder()
                .withAcademicYear(testPackage.getAcademicYear())
                .withClientKey(client.getKey())
                .withKey(client.getName())
                .withVersion(Long.parseLong(testPackage.getVersion()))
                .build();

            tblTestAdminRepository.save(tblTestAdmin);
        }
    }

    @Override
    public void loadAdminSubjects(final TestPackage testPackage, final String subjectKey) {
        for (Assessment assessment : testPackage.getAssessments()) {
            // If the segment is multisegmented, <num of segments> + 1 inserts into the admin subjects table (one "virtual" row)
            List<TblAdminSubject> adminSubjects = new ArrayList<>();

            /**
             * Many of the properties in the "virtual" row will contain sums of its segments. For example, if seg1 has
             * a "minitems" value of 3, and "maxitems" value of 4, and seg2 has a "minitems" value of 1 and a "maxitems" value of 1,
             * the "virtual" row will contain minitems = 4 and maxitems = 5
             */
            if (assessment.isSegmented()) {
                // These four variables will hold the total counts of min/max items for all combined segments in the assessment
                int minItemCount = 0;
                int maxItemCount = 0;
                int fieldTestMinItemCount = 0;
                int fieldTestMaxItemCount = 0;

                for (Segment segment : assessment.getSegments()) {
                    final SegmentBlueprintElement segmentBpElement = segment.segmentBlueprint().stream()
                        .filter(bp -> bp.getIdRef().equals(segment.getId())) // get the actual segment blueprint (not any child claims/targets)
                        .findFirst()
                        .orElseThrow(() ->
                            new TestPackageLoaderException("No matching segment blueprint element found in the test package for id {}", segment.getId()));

                    minItemCount += segmentBpElement.getMinExamItems();
                    maxItemCount += segmentBpElement.getMinExamItems();
                    fieldTestMinItemCount += segmentBpElement.minFieldTestItems();
                    fieldTestMaxItemCount += segmentBpElement.maxFieldTestItems();

                    // Create a map to access the properties by name. Lowercasing the key to prevent casing issues
                    final Map<String, String> itemSelectionProperties = segmentBpElement.itemSelection().stream()
                        .collect(Collectors.toMap(p -> p.getName().toLowerCase(), Property::getValue));

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
                            : null)
                        .withFieldTestEndPos(itemSelectionProperties.containsKey("ftendpos")
                            ? Integer.parseInt(itemSelectionProperties.get("ftendpos"))
                            : null)
                        .withFieldTestMinItems(segmentBpElement.minFieldTestItems())
                        .withFieldTestMaxItems(segmentBpElement.maxFieldTestItems())
                        .withSelectionAlgorithm(segment.getAlgorithmType().equals("adaptive") ? Algorithm.ADAPTIVE_2.getType() : segment.getAlgorithmType())
                        .withBlueprintWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("bpweight", DEFAULT_BLUEPRINT_WEIGHT)))
                        .withAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityweight", DEFAULT_ABILITY_WEIGHT)))
                        .withCSet1Size(Integer.parseInt(itemSelectionProperties.getOrDefault("cset1size", DEFAULT_CSET1_SIZE)))
                        .withCSet2Random(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2random", DEFAULT_CSET2_RANDOM)))
                        .withCSet2InitialRandom(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2initialrandom", DEFAULT_CSET2_INITIAL_RANDOM)))
                        .withVirtualTest(assessment.getKey())
                        .withTestPosition(segment.position())
                        .withSegmented(false)
                        .withComputeAbilityEstimates(
                            Boolean.parseBoolean(itemSelectionProperties.getOrDefault("computeabilityestimates", DEFAULT_COMPUTE_ABILITY_ESTIMATES)))
                        .withVersion(Long.parseLong(testPackage.getVersion()))
                        .withItemWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("itemweight", DEFAULT_ITEM_WEIGHT)))
                        .withAbilityOffset(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityoffset", DEFAULT_ABILITY_OFFSET)))
                        .withCSet1Order(itemSelectionProperties.getOrDefault("cset1order", DEFAULT_CSET1_ORDER))
                        .withRcAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("rcabilityweight", DEFAULT_RC_ABILITY_WEIGHT)))
                        .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                            ? Float.parseFloat(itemSelectionProperties.get("precisiontarget"))
                            : null)
                        .withPrecisionTargetMetWeight(Float.parseFloat(
                            itemSelectionProperties.getOrDefault("precisiontargetmetweight", DEFAULT_PRECISION_TARGET_MET_WEIGHT)))
                        .withPrecisionTargetNotMetWeight(Float.parseFloat(
                            itemSelectionProperties.getOrDefault("precisiontargetnotmetweight", DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT)))
                        .withAdaptiveCut(itemSelectionProperties.containsKey("adaptivecut")
                            ? Float.parseFloat(itemSelectionProperties.get("adaptivecut"))
                            : null)
                        .withTooCloseses(itemSelectionProperties.containsKey("toocloseses")
                            ? Float.parseFloat(itemSelectionProperties.get("toocloseses"))
                            : null)
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
                    .withFieldTestStartPos(null)
                    .withFieldTestEndPos(null)
                    .withFieldTestMinItems(fieldTestMinItemCount)
                    .withFieldTestMaxItems(fieldTestMaxItemCount)
                    .withSelectionAlgorithm(Algorithm.VIRTUAL.getType())
                    .withBlueprintWeight(Float.parseFloat(DEFAULT_BLUEPRINT_WEIGHT))
                    .withAbilityWeight(Float.parseFloat(DEFAULT_ABILITY_WEIGHT))
                    .withCSet1Size(Integer.parseInt(DEFAULT_CSET1_SIZE))
                    .withCSet2Random(Integer.parseInt(DEFAULT_CSET2_RANDOM))
                    .withCSet2InitialRandom(Integer.parseInt(DEFAULT_CSET2_INITIAL_RANDOM))
                    .withSegmented(true)
                    .withComputeAbilityEstimates(
                        Boolean.parseBoolean(DEFAULT_COMPUTE_ABILITY_ESTIMATES))
                    .withVersion(Long.parseLong(testPackage.getVersion()))
                    .withItemWeight(Float.parseFloat(DEFAULT_ITEM_WEIGHT))
                    .withAbilityOffset(Float.parseFloat(DEFAULT_ABILITY_OFFSET))
                    .withCSet1Order(DEFAULT_CSET1_ORDER)
                    .withRcAbilityWeight(Float.parseFloat(DEFAULT_RC_ABILITY_WEIGHT))
                    .withPrecisionTarget(null)
                    .withPrecisionTargetMetWeight(Float.parseFloat(DEFAULT_PRECISION_TARGET_MET_WEIGHT))
                    .withPrecisionTargetNotMetWeight(Float.parseFloat(DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT))
                    .withAdaptiveCut(null)
                    .withTooCloseses(null)
                    .withTerminationOverallInfo(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationRCInfo(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationMinCount(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationTooClose(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withTerminationFlagsAnd(Boolean.parseBoolean(DEFAULT_TERMINATION_FLAGS))
                    .withBpMetricFunction("bp1")
                    .withTestType(testPackage.getType())
                    .build()
                );
            } else { // Not segmented
                Segment segment = assessment.getSegments().get(0);
                final SegmentBlueprintElement segmentBpElement = segment.segmentBlueprint().stream()
                    .filter(bp -> bp.getIdRef().equals(segment.getId())) // get the actual segment blueprint (not any child claims/targets)
                    .findFirst()
                    .orElseThrow(() ->
                        new TestPackageLoaderException("No matching segment blueprint element found in the test package for id {}", segment.getId()));

                // Create a map to access the properties by name. Lowercasing the key to prevent casing issues
                final Map<String, String> itemSelectionProperties = segmentBpElement.itemSelection().stream()
                    .collect(Collectors.toMap(p -> p.getName().toLowerCase(), Property::getValue));

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
                        : null)
                    .withFieldTestEndPos(itemSelectionProperties.containsKey("ftendpos")
                        ? Integer.parseInt(itemSelectionProperties.get("ftendpos"))
                        : null)
                    .withFieldTestMinItems(segmentBpElement.minFieldTestItems())
                    .withFieldTestMaxItems(segmentBpElement.maxFieldTestItems())
                    .withSelectionAlgorithm(segment.getAlgorithmType().equals("adaptive") ? Algorithm.ADAPTIVE_2.getType() : segment.getAlgorithmType())
                    .withBlueprintWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("blueprintweight", DEFAULT_BLUEPRINT_WEIGHT)))
                    .withAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityweight", DEFAULT_ABILITY_WEIGHT)))
                    .withCSet1Size(Integer.parseInt(itemSelectionProperties.getOrDefault("cset1size", DEFAULT_CSET1_SIZE)))
                    .withCSet2Random(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2random", DEFAULT_CSET2_RANDOM)))
                    .withCSet2InitialRandom(Integer.parseInt(itemSelectionProperties.getOrDefault("cset2initialrandom", DEFAULT_CSET2_INITIAL_RANDOM)))
                    .withSegmented(false)
                    .withComputeAbilityEstimates(
                        Boolean.parseBoolean(itemSelectionProperties.getOrDefault("computeabilityestimates", DEFAULT_COMPUTE_ABILITY_ESTIMATES)))
                    .withVersion(Long.parseLong(testPackage.getVersion()))
                    .withItemWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("itemweight", DEFAULT_ITEM_WEIGHT)))
                    .withAbilityOffset(Float.parseFloat(itemSelectionProperties.getOrDefault("abilityoffset", DEFAULT_ABILITY_OFFSET)))
                    .withCSet1Order(itemSelectionProperties.getOrDefault("cset1order", DEFAULT_CSET1_ORDER))
                    .withRcAbilityWeight(Float.parseFloat(itemSelectionProperties.getOrDefault("rcabilityweight", DEFAULT_RC_ABILITY_WEIGHT)))
                    .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                        ? Float.parseFloat(itemSelectionProperties.get("precisiontarget"))
                        : null)
                    .withPrecisionTargetMetWeight(Float.parseFloat(
                        itemSelectionProperties.getOrDefault("precisiontargetmetweight", DEFAULT_PRECISION_TARGET_MET_WEIGHT)))
                    .withPrecisionTargetNotMetWeight(Float.parseFloat(
                        itemSelectionProperties.getOrDefault("precisiontargetnotmetweight", DEFAULT_PRECISION_TARGET_NOT_MET_WEIGHT)))
                    .withAdaptiveCut(itemSelectionProperties.containsKey("adaptivecut")
                        ? Float.parseFloat(itemSelectionProperties.get("adaptivecut"))
                        : null)
                    .withTooCloseses(itemSelectionProperties.containsKey("toocloseses")
                        ? Float.parseFloat(itemSelectionProperties.get("toocloseses"))
                        : null)
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
}
