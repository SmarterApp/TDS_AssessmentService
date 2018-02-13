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

package tds.assessment.services;

import java.util.List;

import tds.assessment.model.itembank.TestForm;
import tds.testpackage.model.TestPackage;

/**
 * An interface for a service responsible for loading form data into the item bank
 */
public interface AssessmentFormLoaderService {
    /**
     * Loads all {@link tds.assessment.model.itembank.TestForm}s and {@link tds.assessment.model.itembank.TestFormItem}s
     * present in the test package into the item bank
     *
     * @param testPackage The test package containing the form data to load
     * @return The list of saved forms
     */
    List<TestForm> loadAdminForms(final TestPackage testPackage);

    /**
     * Loads all {@link tds.assessment.model.configs.AssessmentFormProperties} for each segment form in the test package
     *
     * @param testPackage The test package containing the form property data to load
     * @param testForms   A list of {@link tds.assessment.model.itembank.TestForm}s present in the test package
     */
    void loadAssessmentFormProperties(final TestPackage testPackage, final List<TestForm> testForms);
}
