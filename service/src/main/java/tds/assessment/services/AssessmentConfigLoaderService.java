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
import java.util.Optional;

import tds.assessment.model.itembank.TestForm;
import tds.common.ValidationError;
import tds.testpackage.model.TestPackage;

/**
 * An interface for a service responsible for loading data into the TDS "configs" database
 */
public interface AssessmentConfigLoaderService {
    /**
     * Loads test package data into the TDS configs database
     *
     * @param testPackageName The name of the test package
     * @param testPackage     The {@link tds.testpackage.model.TestPackage} to load
     * @param testForms       A list of {@link tds.assessment.model.itembank.TestForm}s present in the item bank
     */
    void loadTestPackage(final String testPackageName, final TestPackage testPackage,
                         final List<TestForm> testForms);
}
