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

import tds.assessment.model.ItemMetadataWrapper;
import tds.testpackage.model.TestPackage;

/**
 * An interface for a service responsible for handling the loading of {@link tds.assessment.model.itembank.AffinityGroup}s
 */
public interface AffinityGroupLoaderService {
    /**
     * Loads all {@link tds.assessment.model.itembank.AffinityGroup}s present in the test package into the item bank
     *
     * @param testPackage          The test package to load
     * @param itemMetadataWrappers A flattened list of {@link tds.testpackage.model.Item}s, along with some additional important
     *                             metadata
     */
    void loadAffinityGroups(final TestPackage testPackage, final List<ItemMetadataWrapper> itemMetadataWrappers);
}
