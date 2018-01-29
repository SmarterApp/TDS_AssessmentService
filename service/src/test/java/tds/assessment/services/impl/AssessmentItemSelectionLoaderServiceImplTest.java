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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import tds.assessment.model.itembank.TblItemSelectionParameter;
import tds.assessment.repositories.loader.ItemMeasurementParameterRepository;
import tds.assessment.repositories.loader.ItemScoreDimensionsRepository;
import tds.assessment.repositories.loader.MeasurementModelRepository;
import tds.assessment.repositories.loader.MeasurementParameterRepository;
import tds.assessment.repositories.loader.TblItemSelectionParameterRepository;
import tds.assessment.services.AssessmentItemSelectionLoaderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentItemSelectionLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentItemSelectionLoaderService service;

    @Mock
    private ItemScoreDimensionsRepository itemScoreDimensionsRepository;

    @Mock
    private MeasurementModelRepository measurementModelRepository;

    @Mock
    private MeasurementParameterRepository measurementParameterRepository;

    @Mock
    private ItemMeasurementParameterRepository itemMeasurementParameterRepository;

    @Mock
    private TblItemSelectionParameterRepository tblItemSelectionParameterRepository;

    @Captor
    private ArgumentCaptor<List<TblItemSelectionParameter>> selectionParamsArgumentCaptor;

    @Before
    public void setup() {
        service = new AssessmentItemSelectionLoaderServiceImpl(itemScoreDimensionsRepository,
            measurementModelRepository, measurementParameterRepository, itemMeasurementParameterRepository, tblItemSelectionParameterRepository);
    }

    @Test
    public void shouldInsertScoringSeedData() {
        service.loadScoringSeedData();
        verify(measurementModelRepository).save(isA(Set.class));
        verify(measurementParameterRepository).save(isA(Set.class));
    }

    @Test
    public void shouldLoadItemSelectionParameters() {
        service.loadItemSelectionParm(mockTestPackage);

        verify(tblItemSelectionParameterRepository).save(selectionParamsArgumentCaptor.capture());

        List<TblItemSelectionParameter> savedParams = selectionParamsArgumentCaptor.getValue();
        assertThat(savedParams).hasSize(3);

        Map<String, String> savedParamMap = savedParams.stream()
            .collect(Collectors.toMap(TblItemSelectionParameter::getName, TblItemSelectionParameter::getValue));

        assertThat(savedParamMap.get("offGradeMinItemsAdministered")).isEqualTo("30");
        assertThat(savedParamMap.get("proficientPLevel")).isEqualTo("3");
        assertThat(savedParamMap.get("offGradeProbAffectProficiency")).isEqualTo("0.01");
    }

//    @Test
//    public void shouldLoadAdminMeasurementParameters() {
//        List<Item> item = randomListOf(5, Item.class);
//        Map<String, ItemMetadataWrapper> mockItemWrapperMap = mockItemWrappers.stream()
//            .collect(Collectors.toMap(ItemMetadataWrapper::getItemKey, Function.identity()));
//        service.loadAdminItemMeasurementParameters(mockItemWrapperMap);
//    }
}