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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import tds.assessment.model.configs.AssessmentFormProperties;
import tds.assessment.model.itembank.TestForm;
import tds.assessment.model.itembank.TestFormItem;
import tds.assessment.repositories.loader.configs.AssessmentFormPropertiesRepository;
import tds.assessment.repositories.loader.itembank.ItemBankDataQueryRepository;
import tds.assessment.repositories.loader.itembank.TestFormItemRepository;
import tds.assessment.repositories.loader.itembank.TestFormRepository;
import tds.assessment.services.AssessmentFormLoaderService;

import static io.github.benas.randombeans.api.EnhancedRandom.randomListOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentFormLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentFormLoaderService service;

    @Mock
    private TestFormRepository testFormRepository;

    @Mock
    private TestFormItemRepository testFormItemRepository;

    @Mock
    private ItemBankDataQueryRepository itemBankDataQueryRepository;

    @Mock
    private AssessmentFormPropertiesRepository assessmentFormPropertiesRepository;

    @Captor
    private ArgumentCaptor<List<TestForm>> testFormArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TestFormItem>> testFormItemsArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<AssessmentFormProperties>> assessmentFormPropertiesCaptor;

    @Before
    public void setup() {
        service = new AssessmentFormLoaderServiceImpl(testFormRepository, testFormItemRepository,
            itemBankDataQueryRepository, assessmentFormPropertiesRepository);
    }

    @Test
    public void shouldLoadAdminFormsAndFormItems() {
        int formId = 1580662250;
        service.loadAdminForms(mockTestPackage);
        verify(testFormRepository).save(testFormArgumentCaptor.capture());

        List<TestForm> savedTestForms = testFormArgumentCaptor.getValue();
        assertThat(savedTestForms).hasSize(1);
        TestForm retTestForm = savedTestForms.get(0);
        assertThat(retTestForm.getFormKey()).isEqualTo(String.format("187-%d", formId));
        assertThat(retTestForm.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-Perf-MATH-11-2017-2018");
        assertThat(retTestForm.getItsKey()).isEqualTo(formId);
        assertThat(retTestForm.getKey()).isEqualTo(formId);
        assertThat(retTestForm.getLanguage()).isEqualTo("ENU");
        assertThat(retTestForm.getFormId()).isEqualTo("IRP::MathG11::Perf::SP15::ENU");
        assertThat(retTestForm.getCohort()).isEqualTo("Default");

        verify(testFormItemRepository).save(testFormItemsArgumentCaptor.capture());
        List<TestFormItem> savedTestFormItems = testFormItemsArgumentCaptor.getValue();
        assertThat(savedTestFormItems).hasSize(2);

        TestFormItem formItem = savedTestFormItems.get(0);
        assertThat(formItem.isActive()).isTrue();
        assertThat(formItem.getFormItsKey()).isEqualTo(formId);
        assertThat(formItem.getTestFormItemIdentity().getItemPosition()).isEqualTo(1);
        assertThat(formItem.getTestFormItemIdentity().getFormKey()).isEqualTo(String.format("187-%d", formId));
        assertThat(formItem.getTestFormItemIdentity().getItemId()).isEqualTo("187-1434");
    }

    @Test
    public void shouldLoadAssessmentFormPropertiesSuccessfully() {
        List<TestForm> mockTestForms = randomListOf(2, TestForm.class);
        service.loadAssessmentFormProperties(mockTestPackage, mockTestForms);
        verify(assessmentFormPropertiesRepository).save(assessmentFormPropertiesCaptor.capture());
        List<AssessmentFormProperties> savedAssessmentFormProperties = assessmentFormPropertiesCaptor.getValue();
        assertThat(savedAssessmentFormProperties).hasSize(2);
        AssessmentFormProperties savedFormProperties = savedAssessmentFormProperties.get(0);
        assertThat(savedFormProperties.getFormId()).isEqualTo(mockTestForms.get(0).getFormId());
        assertThat(savedFormProperties.getSegmentId()).isEqualTo(mockTestForms.get(0).getSegmentId());
        assertThat(savedFormProperties.getSegmentKey()).isEqualTo(mockTestForms.get(0).getSegmentKey());
        assertThat(savedFormProperties.getLanguageCode()).isEqualTo(mockTestForms.get(0).getLanguage());
        assertThat(savedFormProperties.getAssessmentFormPropertiesIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedFormProperties.getAssessmentFormPropertiesIdentity().getFormKey()).isEqualTo(mockTestForms.get(0).getFormKey());
    }
}
