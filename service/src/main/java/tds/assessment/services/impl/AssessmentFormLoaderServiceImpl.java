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

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import tds.assessment.model.configs.AssessmentFormProperties;
import tds.assessment.model.itembank.TestForm;
import tds.assessment.model.itembank.TestFormItem;
import tds.assessment.repositories.loader.configs.AssessmentFormPropertiesRepository;
import tds.assessment.repositories.loader.itembank.ItemBankDataQueryRepository;
import tds.assessment.repositories.loader.itembank.TestFormItemRepository;
import tds.assessment.repositories.loader.itembank.TestFormRepository;
import tds.assessment.services.AssessmentFormLoaderService;
import tds.common.Algorithm;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentFormLoaderServiceImpl implements AssessmentFormLoaderService {
    private final TestFormRepository testFormRepository;
    private final TestFormItemRepository testFormItemRepository;
    private final ItemBankDataQueryRepository itemBankDataQueryRepository;
    private final AssessmentFormPropertiesRepository assessmentFormPropertiesRepository;

    @Autowired
    public AssessmentFormLoaderServiceImpl(final TestFormRepository testFormRepository,
                                           final TestFormItemRepository testFormItemRepository,
                                           final ItemBankDataQueryRepository itemBankDataQueryRepository,
                                           final AssessmentFormPropertiesRepository assessmentFormPropertiesRepository) {
        this.testFormRepository = testFormRepository;
        this.testFormItemRepository = testFormItemRepository;
        this.itemBankDataQueryRepository = itemBankDataQueryRepository;
        this.assessmentFormPropertiesRepository = assessmentFormPropertiesRepository;
    }

    @Override
    public List<TestForm> loadAdminForms(final TestPackage testPackage) {
        // We'll use this atomic long value to keep track of the "unique" form keys we will be generating. This key
        // used to be a required field in the old test package spec but has been removed - we are generating an "internal"
        // database key manually for backup compatibility purposes
        AtomicLong newFormKey = new AtomicLong(itemBankDataQueryRepository.generateFormKey());

        // Map each SegmentForm to a "testform" - we will generate the form key manually
        List<TestForm> testForms = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType()))
                .flatMap(segment -> segment.segmentForms().stream()
                    .flatMap(form -> form.getPresentations().stream()
                        .map(presentation -> new TestForm.Builder()
                            .withSegmentKey(segment.getKey())
                            .withSegmentId(segment.getId())
                            .withKey(newFormKey.get())
                            .withFormId(form.getId())
                            .withLanguage(presentation.getCode())
                            .withFormKey(String.format("%s-%s", testPackage.getBankKey(), newFormKey.getAndIncrement())) // increment after fetching for the next iteration
                            .withVersion(Long.parseLong(testPackage.getVersion()))
                            .withCohort(form.getCohort())
                            .build()
                        )
                    )
                )
            )
            .collect(Collectors.toList());

        testFormRepository.save(testForms);

        loadAdminFormItems(testPackage, testForms);

        return testForms;
    }

    @Override
    public void loadAssessmentFormProperties(final TestPackage testPackage, final List<TestForm> testForms) {
        // formId -> Assessment - we need some assessment-level properties for each test form
        List<AssessmentFormProperties> assessmentFormProperties = testForms.stream()
            .map(form -> new AssessmentFormProperties.Builder(testPackage.getPublisher(), form.getFormKey())
                .withLanguageCode(form.getLanguage())
                .withFormId(form.getFormId())
                .withSegmentId(form.getSegmentId())
                .withSegmentKey(form.getSegmentKey())
                .build())
            .collect(Collectors.toList());

        assessmentFormPropertiesRepository.save(assessmentFormProperties);
    }

    private void loadAdminFormItems(final TestPackage testPackage, final List<TestForm> testForms) {
        // formId -> testForm
        Map<String, TestForm> testFormMap = testForms.stream().collect(Collectors.toMap(TestForm::getFormId, Function.identity()));

        // Map each item to its "testform"
        List<TestFormItem> testFormItems = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .filter(segment -> segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType()))
                .flatMap(segment -> segment.segmentForms().stream()
                    .flatMap(form -> form.itemGroups().stream()
                        .flatMap(itemGroup -> itemGroup.items().stream()
                            .map(formItem ->
                                new TestFormItem.Builder(formItem.position(), segment.getKey(), formItem.getKey(),
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
}
