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

import tds.assessment.model.itembank.TestForm;
import tds.assessment.services.AssessmentConfigLoaderService;
import tds.assessment.services.AssessmentConfigSeedDataLoaderService;
import tds.assessment.services.AssessmentFormLoaderService;
import tds.assessment.services.AssessmentItemBankLoaderService;
import tds.assessment.services.AssessmentItemConfigLoaderService;
import tds.assessment.services.AssessmentSegmentConfigLoaderService;
import tds.assessment.services.AssessmentService;
import tds.assessment.services.AssessmentToolConfigService;

import static io.github.benas.randombeans.api.EnhancedRandom.randomListOf;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentConfigLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentConfigLoaderService service;

    @Mock
    private AssessmentConfigSeedDataLoaderService assessmentConfigSeedDataLoaderService;

    @Mock
    private AssessmentSegmentConfigLoaderService assessmentSegmentConfigLoaderService;

    @Mock
    private AssessmentFormLoaderService assessmentFormLoaderService;

    @Mock
    private AssessmentItemConfigLoaderService assessmentItemConfigLoaderService;

    @Mock
    private AssessmentToolConfigService assessmentToolConfigService;

    @Before
    public void setup() {
        service = new AssessmentConfigLoaderServiceImpl(assessmentConfigSeedDataLoaderService, assessmentSegmentConfigLoaderService,
            assessmentFormLoaderService, assessmentItemConfigLoaderService, assessmentToolConfigService);
    }

    @Test
    public void shouldLoadTestPackageSuccessfully() {
        final String testPackageName = "SBAC-TestPackage";
        final List<TestForm> mockTestForms = randomListOf(2, TestForm.class);
        service.loadTestPackage(testPackageName, mockTestPackage, mockTestForms);

        verify(assessmentConfigSeedDataLoaderService).loadSeedData(mockTestPackage);
        verify(assessmentSegmentConfigLoaderService).loadAssessmentProperties(mockTestPackage);
        verify(assessmentSegmentConfigLoaderService).loadTestMode(mockTestPackage);
        verify(assessmentSegmentConfigLoaderService).loadSegmentProperties(mockTestPackage);
        verify(assessmentFormLoaderService).loadAssessmentFormProperties(mockTestPackage, mockTestForms);
        verify(assessmentSegmentConfigLoaderService).loadTestWindow(mockTestPackage);
        verify(assessmentItemConfigLoaderService).loadItemTypes(mockTestPackage);
        verify(assessmentItemConfigLoaderService).loadItemConstraints(mockTestPackage);
        verify(assessmentToolConfigService).loadTools(mockTestPackage);
    }
}
