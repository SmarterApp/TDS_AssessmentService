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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import tds.assessment.model.itembank.TestForm;
import tds.assessment.services.AssessmentConfigLoaderService;
import tds.assessment.services.AssessmentConfigSeedDataLoaderService;
import tds.assessment.services.AssessmentFormLoaderService;
import tds.assessment.services.AssessmentItemConfigLoaderService;
import tds.assessment.services.AssessmentSegmentConfigLoaderService;
import tds.assessment.services.AssessmentToolConfigService;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentConfigLoaderServiceImpl implements AssessmentConfigLoaderService {
    private static final Logger log = LoggerFactory.getLogger(AssessmentConfigLoaderServiceImpl.class);

    private final AssessmentConfigSeedDataLoaderService assessmentConfigSeedDataLoaderService;
    private final AssessmentSegmentConfigLoaderService assessmentSegmentConfigLoaderService;
    private final AssessmentFormLoaderService assessmentFormLoaderService;
    private final AssessmentItemConfigLoaderService assessmentItemConfigLoaderService;
    private final AssessmentToolConfigService assessmentToolConfigService;

    @Autowired
    public AssessmentConfigLoaderServiceImpl(final AssessmentConfigSeedDataLoaderService assessmentConfigSeedDataLoaderService,
                                             final AssessmentSegmentConfigLoaderService assessmentSegmentConfigLoaderService, final AssessmentFormLoaderService assessmentFormLoaderService, final AssessmentItemConfigLoaderService assessmentItemConfigLoaderService, final AssessmentToolConfigService assessmentToolConfigService) {
        this.assessmentConfigSeedDataLoaderService = assessmentConfigSeedDataLoaderService;
        this.assessmentSegmentConfigLoaderService = assessmentSegmentConfigLoaderService;
        this.assessmentFormLoaderService = assessmentFormLoaderService;
        this.assessmentItemConfigLoaderService = assessmentItemConfigLoaderService;
        this.assessmentToolConfigService = assessmentToolConfigService;
    }

    @Override
    @Transactional
    public void loadTestPackage(final String testPackageName, final TestPackage testPackage, final List<TestForm> testForms) {

        assessmentConfigSeedDataLoaderService.loadSeedData(testPackage);

        assessmentSegmentConfigLoaderService.loadAssessmentProperties(testPackage);
        assessmentSegmentConfigLoaderService.loadTestMode(testPackage);
        assessmentSegmentConfigLoaderService.loadSegmentProperties(testPackage);

        assessmentFormLoaderService.loadAssessmentFormProperties(testPackage, testForms);
        assessmentSegmentConfigLoaderService.loadTestWindow(testPackage);

        // TODO: Insert into client_testeligibility if necessary (may not be)
        assessmentItemConfigLoaderService.loadItemTypes(testPackage);
        assessmentItemConfigLoaderService.loadItemConstraints(testPackage);
        assessmentToolConfigService.loadTools(testPackage);
    }


}
