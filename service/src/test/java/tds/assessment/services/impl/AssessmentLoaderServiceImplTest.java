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

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.repositories.ItemBankDataCommandRepository;
import tds.assessment.repositories.ItemBankDataQueryRepository;
import tds.assessment.services.AffinityGroupLoaderService;
import tds.assessment.services.AssessmentFormLoaderService;
import tds.assessment.services.AssessmentItemBankLoaderService;
import tds.assessment.services.AssessmentItemSelectionLoaderService;
import tds.assessment.services.AssessmentItemStimuliLoaderService;
import tds.assessment.services.AssessmentLoaderService;
import tds.assessment.services.AssessmentSegmentLoaderService;
import tds.common.ValidationError;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tds.assessment.model.itembank.Client.DEFAULT_HOME_PATH;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentLoaderService service;

    @Mock
    private AssessmentItemBankLoaderService assessmentItemBankLoaderService;

    @Mock
    private AssessmentItemSelectionLoaderService assessmentItemSelectionLoaderService;

    @Mock
    private AssessmentItemStimuliLoaderService assessmentItemStimuliLoaderService;

    @Mock
    private AssessmentFormLoaderService assessmentFormLoaderService;

    @Mock
    private AssessmentSegmentLoaderService assessmentSegmentLoaderService;

    @Mock
    private AffinityGroupLoaderService affinityGroupLoaderService;

    @Mock
    private ItemBankDataQueryRepository itemBankDataQueryRepository;

    @Mock
    private ItemBankDataCommandRepository itemBankDataCommandRepository;

    @Before
    public void setup() throws IOException {
        service = new AssessmentLoaderServiceImpl(assessmentItemBankLoaderService,
            affinityGroupLoaderService,
            assessmentItemSelectionLoaderService,
            assessmentItemStimuliLoaderService,
            assessmentFormLoaderService,
            assessmentSegmentLoaderService,
            itemBankDataQueryRepository,
            itemBankDataCommandRepository);
    }

    @Test
    public void shouldLoadTestPackageNewClient() {
        Map<String, TblStrand> mockTblStrandMap = ImmutableMap.of("test", random(TblStrand.class));

        when(itemBankDataQueryRepository.findClient(mockTestPackage.getPublisher())).thenReturn(Optional.empty());
        when(itemBankDataCommandRepository.insertClient("SBAC_PT")).thenReturn(new Client(1, "SBAC_PT", DEFAULT_HOME_PATH));
        when(assessmentItemBankLoaderService.loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()))).thenReturn(mockTblStrandMap);

        Optional<ValidationError> maybeError = service.loadTestPackage("V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml", mockTestPackage);
        assertThat(maybeError).isNotPresent();

        verify(assessmentItemSelectionLoaderService).loadScoringSeedData();
        verify(itemBankDataQueryRepository).findClient("SBAC_PT");
        verify(itemBankDataCommandRepository).insertClient(mockTestPackage.getPublisher());
        verify(assessmentItemBankLoaderService).loadSubject(eq(mockTestPackage), isA(Client.class), eq("SBAC_PT-MATH"));
        verify(assessmentItemBankLoaderService).loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()));
        verify(assessmentItemBankLoaderService).loadTblStimuli(mockTestPackage);
        verify(assessmentItemBankLoaderService).loadTblItems(eq(mockTestPackage), isA(List.class));
        verify(assessmentItemStimuliLoaderService).loadLinkItemsToStrands(isA(List.class), eq(mockTblStrandMap), eq(Long.parseLong(mockTestPackage.getVersion())));
        verify(assessmentItemStimuliLoaderService).loadItemProperties(isA(List.class));
        verify(assessmentSegmentLoaderService).loadTestAdmin(eq(mockTestPackage), isA(Client.class));
        verify(assessmentSegmentLoaderService).loadAdminSubjects(mockTestPackage, "SBAC_PT-MATH");
        verify(assessmentSegmentLoaderService).loadTestGrades(mockTestPackage);
        verify(assessmentSegmentLoaderService).loadTestCohorts(mockTestPackage);
        verify(assessmentItemSelectionLoaderService).loadItemSelectionParams(mockTestPackage);
        verify(assessmentItemStimuliLoaderService).loadAdminStrands(mockTestPackage, mockTblStrandMap);
        verify(assessmentItemStimuliLoaderService).loadAdminItems(eq(mockTestPackage), isA(List.class), eq(mockTblStrandMap));
        verify(assessmentItemSelectionLoaderService).loadAdminItemMeasurementParameters(isA(Map.class));
        verify(assessmentItemStimuliLoaderService).loadAdminStimuli(mockTestPackage);
        verify(assessmentFormLoaderService).loadAdminForms(mockTestPackage);
        verify(affinityGroupLoaderService).loadAffinityGroups(eq(mockTestPackage), isA(List.class));
    }

    @Test
    public void shouldLoadTestPackageOldClient() {
        Map<String, TblStrand> mockTblStrandMap = ImmutableMap.of("test", random(TblStrand.class));
        Client client = new Client(1, "SBAC_PT", DEFAULT_HOME_PATH);

        when(itemBankDataQueryRepository.findClient(mockTestPackage.getPublisher())).thenReturn(Optional.of(client));
        when(assessmentItemBankLoaderService.loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()))).thenReturn(mockTblStrandMap);

        Optional<ValidationError> maybeError = service.loadTestPackage("V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml", mockTestPackage);
        assertThat(maybeError).isNotPresent();

        verify(assessmentItemSelectionLoaderService).loadScoringSeedData();
        verify(itemBankDataQueryRepository).findClient("SBAC_PT");
        verify(itemBankDataCommandRepository).insertClient(mockTestPackage.getPublisher());
        verify(assessmentItemBankLoaderService).loadSubject(eq(mockTestPackage), isA(Client.class), eq("SBAC_PT-MATH"));
        verify(assessmentItemBankLoaderService).loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()));
        verify(assessmentItemBankLoaderService).loadTblStimuli(mockTestPackage);
        verify(assessmentItemBankLoaderService).loadTblItems(eq(mockTestPackage), isA(List.class));
        verify(assessmentItemStimuliLoaderService).loadLinkItemsToStrands(isA(List.class), eq(mockTblStrandMap), eq(Long.parseLong(mockTestPackage.getVersion())));
        verify(assessmentItemStimuliLoaderService).loadItemProperties(isA(List.class));
        verify(assessmentSegmentLoaderService).loadTestAdmin(eq(mockTestPackage), isA(Client.class));
        verify(assessmentSegmentLoaderService).loadAdminSubjects(mockTestPackage, "SBAC_PT-MATH");
        verify(assessmentSegmentLoaderService).loadTestGrades(mockTestPackage);
        verify(assessmentSegmentLoaderService).loadTestCohorts(mockTestPackage);
        verify(assessmentItemSelectionLoaderService).loadItemSelectionParams(mockTestPackage);
        verify(assessmentItemStimuliLoaderService).loadAdminStrands(mockTestPackage, mockTblStrandMap);
        verify(assessmentItemStimuliLoaderService).loadAdminItems(eq(mockTestPackage), isA(List.class), eq(mockTblStrandMap));
        verify(assessmentItemSelectionLoaderService).loadAdminItemMeasurementParameters(isA(Map.class));
        verify(assessmentItemStimuliLoaderService).loadAdminStimuli(mockTestPackage);
        verify(assessmentFormLoaderService).loadAdminForms(mockTestPackage);
        verify(affinityGroupLoaderService).loadAffinityGroups(eq(mockTestPackage), isA(List.class));
    }

    @Test
    public void shouldReturnValidationErrorForException() {
        doThrow(new TestPackageLoaderException("An exception")).when(assessmentItemSelectionLoaderService).loadScoringSeedData();
        Optional<ValidationError> maybeError = service.loadTestPackage("V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml", mockTestPackage);
        assertThat(maybeError).isPresent();
        assertThat(maybeError.get().getMessage()).contains("An exception");
    }
}
