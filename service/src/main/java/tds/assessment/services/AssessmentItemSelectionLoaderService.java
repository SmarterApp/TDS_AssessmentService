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

import java.util.Map;

import tds.assessment.model.ItemMetadataWrapper;
import tds.testpackage.model.TestPackage;

/**
 * An interface for a service responsible for loading item selection data into the item bank
 */
public interface AssessmentItemSelectionLoaderService {
    /**
     * Loads scoring measurement seed data into the item bank
     */
    void loadSeedData(final TestPackage testPackage);

    /**
     * Loads {@link tds.assessment.model.itembank.ItemMeasurementParameter}s into the item bank
     *
     * @param itemIdToItemMetadata As mapping of all item wrappers to their item keys in the test package
     */
    void loadAdminItemMeasurementParameters(final Map<String, ItemMetadataWrapper> itemIdToItemMetadata);

    /**
     * Loads all {@link tds.assessment.model.itembank.TblItemSelectionParameter}s into the item bank
     *
     * @param testPackage The test package containing the item and selection parameter mappings
     */
    void loadItemSelectionParams(final TestPackage testPackage);
}
