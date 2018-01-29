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

import tds.assessment.model.itembank.TestForm;
import tds.assessment.model.itembank.TestFormItem;
import tds.assessment.repositories.ItemBankDataQueryRepository;
import tds.assessment.repositories.loader.TestFormItemRepository;
import tds.assessment.repositories.loader.TestFormRepository;
import tds.assessment.services.AssessmentFormLoaderService;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Captor
    private ArgumentCaptor<List<TestForm>> testFormArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TestFormItem>> testFormItemsArgumentCaptor;

    @Before
    public void setup() {
        service = new AssessmentFormLoaderServiceImpl(testFormRepository, testFormItemRepository,
            itemBankDataQueryRepository);
    }

    @Test
    public void shouldLoadAdminFormsAndFormItems() {
        when(itemBankDataQueryRepository.generateFormKey()).thenReturn(2112L);

        service.loadAdminForms(mockTestPackage);

        verify(itemBankDataQueryRepository).generateFormKey();
        verify(testFormRepository).save(testFormArgumentCaptor.capture());

        List<TestForm> savedTestForms = testFormArgumentCaptor.getValue();
        assertThat(savedTestForms).hasSize(1);
        TestForm retTestForm = savedTestForms.get(0);
        assertThat(retTestForm.getFormKey()).isEqualTo("187-2112");
        assertThat(retTestForm.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-Perf-MATH-11-2017-2018");
        assertThat(retTestForm.getItsKey()).isEqualTo(2112);
        assertThat(retTestForm.getKey()).isEqualTo(2112);
        assertThat(retTestForm.getLanguage()).isEqualTo("ENU");
        assertThat(retTestForm.getFormId()).isEqualTo("IRP::MathG11::Perf::SP15");
        assertThat(retTestForm.getCohort()).isEqualTo("Default");

        verify(testFormItemRepository).save(testFormItemsArgumentCaptor.capture());
        List<TestFormItem> savedTestFormItems = testFormItemsArgumentCaptor.getValue();
        assertThat(savedTestFormItems).hasSize(2);

        TestFormItem formItem = savedTestFormItems.get(0);
        assertThat(formItem.isActive()).isTrue();
        assertThat(formItem.getFormItsKey()).isEqualTo(2112);
        assertThat(formItem.getTestFormItemIdentity().getItemPosition()).isEqualTo(1);
        assertThat(formItem.getTestFormItemIdentity().getFormKey()).isEqualTo("187-2112");
        assertThat(formItem.getTestFormItemIdentity().getItemId()).isEqualTo("187-1434");
    }
}
