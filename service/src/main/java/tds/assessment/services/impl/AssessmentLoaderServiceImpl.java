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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import tds.assessment.model.itembank.TestForm;
import tds.assessment.services.AssessmentConfigLoaderService;
import tds.assessment.services.AssessmentItemBankLoaderService;
import tds.assessment.services.AssessmentLoaderService;
import tds.assessment.services.AssessmentService;
import tds.common.ValidationError;
import tds.common.web.exceptions.NotFoundException;
import tds.support.job.ErrorSeverity;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentLoaderServiceImpl implements AssessmentLoaderService {
    private static final Logger log = LoggerFactory.getLogger(AssessmentLoaderServiceImpl.class);

    private final AssessmentConfigLoaderService assessmentConfigLoaderService;
    private final AssessmentItemBankLoaderService assessmentItemBankLoaderService;
    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentLoaderServiceImpl(final AssessmentConfigLoaderService assessmentConfigLoaderService,
                                       final AssessmentItemBankLoaderService assessmentItemBankLoaderService,
                                       final AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
        this.assessmentItemBankLoaderService = assessmentItemBankLoaderService;
        this.assessmentConfigLoaderService = assessmentConfigLoaderService;
    }

    /**
     * Loads a {@link tds.testpackage.model.TestPackage} into the itembank and configs tables
     *
     * @param testPackageName The file name of the test package
     * @param testPackage     The test package to load
     * @return an error, if one occurs during the creation of the assessment
     */
    @Override
    public synchronized Optional<ValidationError> loadTestPackage(final String testPackageName, final TestPackage testPackage) {

        // Delete assessments if they exist
        removeTestPackageIfPresent(testPackage);

        try {
            Set<String> duplicateItemIds = assessmentItemBankLoaderService.findDuplicateItems(testPackage);
            List<TestForm> testForms = assessmentItemBankLoaderService.loadTestPackage(testPackageName, testPackage, duplicateItemIds);
            assessmentConfigLoaderService.loadTestPackage(testPackageName, testPackage, testForms);

            if (!duplicateItemIds.isEmpty()) {
                return Optional.of(new ValidationError(ErrorSeverity.WARN.name(),
                    String.format("The following items in this test package were not loaded because they already exist in TDS: %s", duplicateItemIds)));
            }
        } catch (Exception e) {
            removeTestPackageIfPresent(testPackage);
            log.error("An error occurred while loading the test package: , Clearing the test package from TDS", e);
            return Optional.of(new ValidationError("TDS-Load", String.format("An error occurred while loading the test package %s. Message: %s",
                testPackageName, e.getMessage())));
        }

        return Optional.empty();
    }

    private void removeTestPackageIfPresent(final TestPackage testPackage) {
        try {
            testPackage.getAssessments().forEach(assessment ->
                assessmentService.removeAssessment(testPackage.getPublisher(), assessment.getKey()));
        } catch (NotFoundException e) {
            // Ignore this exception - no issue if the assessment isn't present in the system
            log.debug("Attempted to clear the assessments in the test package {} before loading, but no assessments were found presently in the system");
        }
    }

}
