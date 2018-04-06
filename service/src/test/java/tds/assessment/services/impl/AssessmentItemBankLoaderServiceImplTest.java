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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.model.itembank.TestForm;
import tds.assessment.repositories.loader.itembank.ItemBankDataCommandRepository;
import tds.assessment.repositories.loader.itembank.ItemBankDataQueryRepository;
import tds.assessment.services.AffinityGroupLoaderService;
import tds.assessment.services.AssessmentConfigLoaderService;
import tds.assessment.services.AssessmentFormLoaderService;
import tds.assessment.services.AssessmentItemBankGenericDataLoaderService;
import tds.assessment.services.AssessmentItemBankLoaderService;
import tds.assessment.services.AssessmentItemSelectionLoaderService;
import tds.assessment.services.AssessmentItemStimuliLoaderService;
import tds.assessment.services.AssessmentLoaderService;
import tds.assessment.services.AssessmentSegmentLoaderService;
import tds.assessment.services.AssessmentService;
import tds.common.ValidationError;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tds.assessment.model.itembank.Client.DEFAULT_HOME_PATH;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentItemBankLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentItemBankLoaderService service;

    @Mock
    private AssessmentItemBankGenericDataLoaderService assessmentItemBankGenericDataLoaderService;

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
    public void setup() {
        service = new AssessmentItemBankLoaderServiceImpl(assessmentItemBankGenericDataLoaderService,
            assessmentItemSelectionLoaderService,
            assessmentItemStimuliLoaderService,
            assessmentFormLoaderService,
            assessmentSegmentLoaderService,
            affinityGroupLoaderService,
            itemBankDataQueryRepository,
            itemBankDataCommandRepository);
    }

    @Test
    public void shouldLoadTestPackageNewClient() {
        Map<String, TblStrand> mockTblStrandMap = ImmutableMap.of("test", random(TblStrand.class));

        when(itemBankDataQueryRepository.findClient(mockTestPackage.getPublisher())).thenReturn(Optional.empty());
        when(itemBankDataCommandRepository.insertClient("SBAC_PT")).thenReturn(new Client(1, "SBAC_PT", DEFAULT_HOME_PATH));
        when(assessmentItemBankGenericDataLoaderService.loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()))).thenReturn(mockTblStrandMap);

        List<TestForm> testForms = service.loadTestPackage("V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml", mockTestPackage, new HashSet<>());
        assertThat(testForms).isEmpty();

        verify(assessmentItemSelectionLoaderService).loadScoringSeedData();
        verify(itemBankDataQueryRepository).findClient("SBAC_PT");
        verify(itemBankDataCommandRepository).insertClient(mockTestPackage.getPublisher());
        verify(assessmentItemBankGenericDataLoaderService).loadSubject(eq(mockTestPackage), isA(Client.class), eq("SBAC_PT-MATH"));
        verify(assessmentItemBankGenericDataLoaderService).loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()));
        verify(assessmentItemBankGenericDataLoaderService).loadTblStimuli(mockTestPackage);
        verify(assessmentItemBankGenericDataLoaderService).loadTblItems(eq(mockTestPackage), isA(List.class), isA(Set.class));
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
        when(assessmentItemBankGenericDataLoaderService.loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()))).thenReturn(mockTblStrandMap);

        List<TestForm> testForms = service.loadTestPackage("V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml", mockTestPackage, new HashSet<>());
        assertThat(testForms).isEmpty();

        verify(assessmentItemSelectionLoaderService).loadScoringSeedData();
        verify(itemBankDataQueryRepository).findClient("SBAC_PT");
        verify(itemBankDataCommandRepository, never()).insertClient(mockTestPackage.getPublisher());
        verify(assessmentItemBankGenericDataLoaderService).loadSubject(eq(mockTestPackage), isA(Client.class), eq("SBAC_PT-MATH"));
        verify(assessmentItemBankGenericDataLoaderService).loadStrands(eq(mockTestPackage.getBlueprint()), eq("SBAC_PT-MATH"),
            isA(Client.class), eq(mockTestPackage.getVersion()));
        verify(assessmentItemBankGenericDataLoaderService).loadTblStimuli(mockTestPackage);
        verify(assessmentItemBankGenericDataLoaderService).loadTblItems(eq(mockTestPackage), isA(List.class), isA(Set.class));
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
}
