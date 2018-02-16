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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.itembank.TestForm;
import tds.assessment.services.AssessmentConfigLoaderService;
import tds.assessment.services.AssessmentItemBankLoaderService;
import tds.assessment.services.AssessmentLoaderService;
import tds.assessment.services.AssessmentService;
import tds.common.ValidationError;
import tds.common.web.exceptions.NotFoundException;

import static io.github.benas.randombeans.api.EnhancedRandom.randomListOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentLoaderService service;

    @Mock
    private AssessmentConfigLoaderService assessmentConfigLoaderService;

    @Mock
    private AssessmentItemBankLoaderService assessmentItemBankLoaderService;

    @Mock
    private AssessmentService assessmentService;

    @Before
    public void setup() {
        service = new AssessmentLoaderServiceImpl(assessmentConfigLoaderService, assessmentItemBankLoaderService, assessmentService);
    }

    @Test
    public void shouldLoadAssessmentSuccessfullyFirstTime() {
        final String testPackageName = "V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml";
        doThrow(NotFoundException.class).when(assessmentService).removeAssessment(mockTestPackage.getPublisher(), mockTestPackage.getAssessments().get(0).getKey());

        List<TestForm> mockTestForms = randomListOf(2, TestForm.class);
        when(assessmentItemBankLoaderService.loadTestPackage(testPackageName, mockTestPackage)).thenReturn(mockTestForms);
        Optional<ValidationError> maybeError = service.loadTestPackage(testPackageName, mockTestPackage);
        assertThat(maybeError).isNotPresent();

        verify(assessmentItemBankLoaderService).loadTestPackage(testPackageName, mockTestPackage);
        verify(assessmentConfigLoaderService).loadTestPackage(testPackageName, mockTestPackage, mockTestForms);
    }

    @Test
    public void shouldClearTestPackageWhenErrorOccurs() {
        final String testPackageName = "V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml";

        List<TestForm> mockTestForms = randomListOf(2, TestForm.class);
        when(assessmentItemBankLoaderService.loadTestPackage(testPackageName, mockTestPackage)).thenReturn(mockTestForms);
        doThrow(NotFoundException.class).when(assessmentService).removeAssessment(mockTestPackage.getPublisher(), mockTestPackage.getAssessments().get(0).getKey());
        doThrow(TestPackageLoaderException.class).when(assessmentConfigLoaderService).loadTestPackage(testPackageName, mockTestPackage, mockTestForms);

        Optional<ValidationError> maybeError = service.loadTestPackage(testPackageName, mockTestPackage);
        assertThat(maybeError).isPresent();

        verify(assessmentItemBankLoaderService).loadTestPackage(testPackageName, mockTestPackage);
        verify(assessmentConfigLoaderService).loadTestPackage(testPackageName, mockTestPackage, mockTestForms);
        verify(assessmentService, times(2)).removeAssessment(mockTestPackage.getPublisher()
            , mockTestPackage.getAssessments().get(0).getKey());
    }

    @Test
    public void shouldDeleteAllAssessmentsIfPresentBeforeLoading() {
        final String testPackageName = "V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml";

        List<TestForm> mockTestForms = randomListOf(2, TestForm.class);
        when(assessmentItemBankLoaderService.loadTestPackage(testPackageName, mockTestPackage)).thenReturn(mockTestForms);
        Optional<ValidationError> maybeError = service.loadTestPackage(testPackageName, mockTestPackage);
        assertThat(maybeError).isNotPresent();

        verify(assessmentItemBankLoaderService).loadTestPackage(testPackageName, mockTestPackage);
        verify(assessmentConfigLoaderService).loadTestPackage(testPackageName, mockTestPackage, mockTestForms);
        verify(assessmentService).removeAssessment(mockTestPackage.getPublisher()
            , mockTestPackage.getAssessments().get(0).getKey());
        verify(assessmentService).removeAssessment(mockTestPackage.getPublisher()
            , mockTestPackage.getAssessments().get(1).getKey());
    }
}
