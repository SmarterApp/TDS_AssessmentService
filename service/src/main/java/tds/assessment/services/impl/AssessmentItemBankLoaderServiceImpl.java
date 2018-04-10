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

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.model.itembank.TestForm;
import tds.assessment.repositories.loader.itembank.ItemBankDataCommandRepository;
import tds.assessment.repositories.loader.itembank.ItemBankDataQueryRepository;
import tds.assessment.services.AffinityGroupLoaderService;
import tds.assessment.services.AssessmentFormLoaderService;
import tds.assessment.services.AssessmentItemBankGenericDataLoaderService;
import tds.assessment.services.AssessmentItemBankLoaderService;
import tds.assessment.services.AssessmentItemSelectionLoaderService;
import tds.assessment.services.AssessmentItemStimuliLoaderService;
import tds.assessment.services.AssessmentSegmentLoaderService;
import tds.common.Algorithm;
import tds.support.job.TestPackageLoadJob;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentItemBankLoaderServiceImpl implements AssessmentItemBankLoaderService {
    private final AssessmentItemBankGenericDataLoaderService assessmentItemBankGenericDataLoaderService;
    private final AssessmentItemSelectionLoaderService assessmentItemSelectionLoaderService;
    private final AssessmentItemStimuliLoaderService assessmentItemStimuliLoaderService;
    private final AssessmentFormLoaderService assessmentFormLoaderService;
    private final AssessmentSegmentLoaderService assessmentSegmentLoaderService;
    private final AffinityGroupLoaderService affinityGroupLoaderService;
    private final ItemBankDataQueryRepository itemBankDataQueryRepository;
    private final ItemBankDataCommandRepository itemBankDataCommandRepository;

    @Autowired
    public AssessmentItemBankLoaderServiceImpl(final AssessmentItemBankGenericDataLoaderService assessmentItemBankGenericDataLoaderService,
                                               final AssessmentItemSelectionLoaderService assessmentItemSelectionLoaderService,
                                               final AssessmentItemStimuliLoaderService assessmentItemStimuliLoaderService,
                                               final AssessmentFormLoaderService assessmentFormLoaderService,
                                               final AssessmentSegmentLoaderService assessmentSegmentLoaderService,
                                               final AffinityGroupLoaderService affinityGroupLoaderService,
                                               final ItemBankDataQueryRepository itemBankDataQueryRepository,
                                               final ItemBankDataCommandRepository itemBankDataCommandRepository) {
        this.assessmentItemBankGenericDataLoaderService = assessmentItemBankGenericDataLoaderService;
        this.assessmentItemSelectionLoaderService = assessmentItemSelectionLoaderService;
        this.assessmentItemStimuliLoaderService = assessmentItemStimuliLoaderService;
        this.assessmentFormLoaderService = assessmentFormLoaderService;
        this.assessmentSegmentLoaderService = assessmentSegmentLoaderService;
        this.affinityGroupLoaderService = affinityGroupLoaderService;
        this.itemBankDataQueryRepository = itemBankDataQueryRepository;
        this.itemBankDataCommandRepository = itemBankDataCommandRepository;
    }

    @Override
    public List<TestForm> loadTestPackage(final String testPackageName, final TestPackage testPackage, final Set<String> duplicateItemIds) {
        assessmentItemSelectionLoaderService.loadScoringSeedData();

        //Find client if exists. If not, we need to create one
        final Client client = itemBankDataQueryRepository.findClient(testPackage.getPublisher())
            .orElseGet(() ->itemBankDataCommandRepository.insertClient(testPackage.getPublisher())); // Insert returns Client

        final String subjectKey = client.getName() + '-' + testPackage.getSubject();
        // Contains  all items and other item metadata specified in the test package
        final Map<String, ItemMetadataWrapper> itemIdToItemMetadata = mapItemsToItemMetadata(testPackage);
        final List<ItemMetadataWrapper> itemMetadata = Lists.newArrayList(itemIdToItemMetadata.values());

        /* load_subject() */
        assessmentItemBankGenericDataLoaderService.loadSubject(testPackage, client, subjectKey);

        /* load_strands() */
        final Map<String, TblStrand> keyToStrands = assessmentItemBankGenericDataLoaderService.loadStrands(testPackage.getBlueprint(), subjectKey, client, testPackage.getVersion());

        /* load_stimulus() */
        assessmentItemBankGenericDataLoaderService.loadTblStimuli(testPackage);

        /* load_items() */
        assessmentItemBankGenericDataLoaderService.loadTblItems(testPackage, itemMetadata, duplicateItemIds);

        /* load_linkitemtostrands() */
        assessmentItemStimuliLoaderService.loadLinkItemsToStrands(itemMetadata, keyToStrands, Long.parseLong(testPackage.getVersion()));

        /* load_linkitemstostimuli() */
        assessmentItemStimuliLoaderService.loadLinkItemsToStimuli(testPackage);

        /* load_itemproperties() */
        assessmentItemStimuliLoaderService.loadItemProperties(itemMetadata);

        /* load_testadmin() */
        assessmentSegmentLoaderService.loadTestAdmin(testPackage, client);

        /* load_adminsubjects() */
        assessmentSegmentLoaderService.loadAdminSubjects(testPackage, subjectKey);

        /* load_testgrades() */
        assessmentSegmentLoaderService.loadTestGrades(testPackage);

        /* load_testcohorts() */
        assessmentSegmentLoaderService.loadTestCohorts(testPackage);

        /* load_itemselectionparm() */
        assessmentItemSelectionLoaderService.loadItemSelectionParams(testPackage);

        /* load_adminstrands() */
        assessmentItemStimuliLoaderService.loadAdminStrands(testPackage, keyToStrands);

        /* load_adminitems() */
        assessmentItemStimuliLoaderService.loadAdminItems(testPackage, itemMetadata, keyToStrands);

        /* load_adminitemmeasurementparms() */
        assessmentItemSelectionLoaderService.loadAdminItemMeasurementParameters(itemIdToItemMetadata);

        /* load_adminstimuli() */
        assessmentItemStimuliLoaderService.loadAdminStimuli(testPackage);

        /* load_adminforms() and load_adminformitems() */
        List<TestForm> testForms = assessmentFormLoaderService.loadAdminForms(testPackage);

        /* load_affinitygroups */
        affinityGroupLoaderService.loadAffinityGroups(testPackage, itemMetadata);

        return testForms;
    }

    public Set<String> findDuplicateItems(final TestPackage testPackage) {
        Map<String, ItemMetadataWrapper> itemMetadataWrappers = mapItemsToItemMetadata(testPackage);
        return assessmentItemBankGenericDataLoaderService.findDuplicateItems(testPackage, itemMetadataWrappers.values());
    }


    private static Map<String, ItemMetadataWrapper> mapItemsToItemMetadata(final TestPackage testPackage) {
        return testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                    if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                        return segment.segmentForms().stream()
                            .flatMap(form -> form.itemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .map(item -> new ItemMetadataWrapper(item, assessment.getGrades(), segment.getKey(),
                                        itemGroup.getKey(), false))
                                )
                            );
                    } else if (segment.getAlgorithmType().contains("adaptive")) {
                        return segment.pool().stream()
                            .flatMap(itemGroup -> itemGroup.items().stream()
                                .map(item -> new ItemMetadataWrapper(item, assessment.getGrades(), segment.getKey(),
                                    itemGroup.getKey(), true))
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                }
            )).collect(Collectors.toMap(itemWrapper -> itemWrapper.getItem().getKey(), itemWrapper -> itemWrapper, (a1, a2) -> a1));
    }
}
