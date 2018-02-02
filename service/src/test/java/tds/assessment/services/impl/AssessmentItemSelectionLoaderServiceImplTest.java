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
import java.util.UUID;
import java.util.stream.Collectors;

import tds.assessment.model.itembank.ItemMeasurementParameter;
import tds.assessment.model.itembank.ItemScoreDimension;
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

    @Captor
    private ArgumentCaptor<List<ItemScoreDimension>> itemScoreDimensionArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<ItemMeasurementParameter>> itemMeasurementParamArgumentCaptor;

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
        service.loadItemSelectionParams(mockTestPackage);

        verify(tblItemSelectionParameterRepository).save(selectionParamsArgumentCaptor.capture());

        List<TblItemSelectionParameter> savedParams = selectionParamsArgumentCaptor.getValue();
        assertThat(savedParams).hasSize(3);

        Map<String, String> savedParamMap = savedParams.stream()
            .collect(Collectors.toMap(TblItemSelectionParameter::getName, TblItemSelectionParameter::getValue));

        assertThat(savedParamMap.get("offGradeMinItemsAdministered")).isEqualTo("30");
        assertThat(savedParamMap.get("proficientPLevel")).isEqualTo("3");
        assertThat(savedParamMap.get("offGradeProbAffectProficiency")).isEqualTo("0.01");
    }

    @Test
    public void shouldLoadAdminMeasurementParameters() {
        service.loadAdminItemMeasurementParameters(mockItemMetadataMap);

        verify(itemScoreDimensionsRepository).save(itemScoreDimensionArgumentCaptor.capture());

        List<ItemScoreDimension> savedItemScoreDimensions = Lists.newArrayList(itemScoreDimensionArgumentCaptor.getValue());

        assertThat(savedItemScoreDimensions).hasSize(20);

        ItemScoreDimension savedDimension = savedItemScoreDimensions.stream()
            .filter(dim -> dim.getItemId().equalsIgnoreCase("187-1930"))
            .findFirst().get();

        assertThat(savedDimension.getKey()).isInstanceOfAny(UUID.class);
        assertThat(savedDimension.getDimension()).isEqualTo("");
        assertThat(savedDimension.getRecodeRule()).isEqualTo("");
        assertThat(savedDimension.getScorePoints()).isEqualTo(1);
        assertThat(savedDimension.getWeight()).isEqualTo(1);
        assertThat(savedDimension.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018");
        assertThat(savedDimension.getMeasurementModelKey()).isEqualTo(1);

        verify(itemMeasurementParameterRepository).save(itemMeasurementParamArgumentCaptor.capture());

        List<ItemMeasurementParameter> savedParams = itemMeasurementParamArgumentCaptor.getValue();
        assertThat(savedParams).hasSize(61);

        ItemMeasurementParameter savedParam = savedParams.stream()
            .filter(param -> param.getItemMeasurementParameterIdentity().getItemScoreDimensionKey().equals(savedDimension.getKey()))
            .findFirst().get();

        assertThat(savedParam.getParameterValue()).isEqualTo(1);
        assertThat(savedParam.getItemMeasurementParameterIdentity().getMeasurementParameterKey()).isEqualTo(0);
    }
}
