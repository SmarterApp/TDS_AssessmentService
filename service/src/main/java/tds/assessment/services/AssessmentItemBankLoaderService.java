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
import java.util.Set;

import tds.assessment.model.itembank.TestForm;
import tds.testpackage.model.TestPackage;

public interface AssessmentItemBankLoaderService {
    /**
     * Loads a test package into the itembank database
     *
     * @param testPackageName The name of the test package
     * @param testPackage     The {@link tds.testpackage.model.TestPackage} to load
     * @return A list of test forms generated from the test packages' segment forms
     */
    List<TestForm> loadTestPackage(final String testPackageName, final TestPackage testPackage, final Set<String> duplicateItemIds);

    /**
     * Finds the items in the test package that already exist in the TDS itembank database
     *
     * @param testPackage The test package containing the items to check for
     * @return The {@link Set} of duplicate item ids
     */
    Set<String> findDuplicateItems(final TestPackage testPackage);
}
