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
import java.util.Map;

import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.TblStrand;
import tds.testpackage.model.TestPackage;

/**
 * An interface for a service responsible for loading item, stimuli, and strand data into the item bank
 */
public interface AssessmentItemStimuliLoaderService {
    /**
     * Loads all {@link tds.assessment.model.itembank.TblItemProperty}s into the item bank
     *
     * @param itemMetadataWrappers A flattened list of {@link tds.testpackage.model.Item}s, along with some additional important
     *                             metadata
     */
    void loadItemProperties(final List<ItemMetadataWrapper> itemMetadataWrappers);

    /**
     * Loads all {@link tds.assessment.model.itembank.TblAdminItem}s into the item bank
     *
     * @param testPackage          The test package containing the items to load
     * @param itemMetadataWrappers A flattened list of {@link tds.testpackage.model.Item}s, along with some additional important
     *                             metadata
     * @param keyToStrands         A mapping of all strand keys to their respective {@link tds.assessment.model.itembank.TblStrand}s
     */
    void loadAdminItems(final TestPackage testPackage, final List<ItemMetadataWrapper> itemMetadataWrappers,
                        final Map<String, TblStrand> keyToStrands);

    /**
     * Loads all item and strand linking {@link tds.assessment.model.itembank.TblSetOfItemStrand}s and
     * {@link tds.assessment.model.itembank.ItemContentLevel}s into the item bank
     *
     * @param itemMetadataWrappers A flattened list of {@link tds.testpackage.model.Item}s, along with some additional important
     *                             metadata
     * @param keyToStrands         A mapping of all strand keys to their respective {@link tds.assessment.model.itembank.TblStrand}s
     * @param version              The version of the test package
     */
    void loadLinkItemsToStrands(final List<ItemMetadataWrapper> itemMetadataWrappers,
                                final Map<String, TblStrand> keyToStrands, final Long version);

    /**
     * Loads all admin-stimuli linking {@link tds.assessment.model.itembank.TblAdminStimulus} into the item bank
     *
     * @param testPackage The test package containing all admin-stimuli links in the {@link tds.testpackage.model.TestPackage}
     */
    void loadAdminStimuli(final TestPackage testPackage);

    /**
     * Loads all {@link tds.assessment.model.itembank.TblAdminStrand}s into the item bank
     *
     * @param testPackage  The test package containing the admin-strand linkings
     * @param keyToStrands A mapping of all strand keys to their respective {@link tds.assessment.model.itembank.TblStrand}s
     */
    void loadAdminStrands(final TestPackage testPackage, final Map<String, TblStrand> keyToStrands);

    /**
     * Loads all item to strand linking {@link tds.assessment.model.itembank.TblSetOfItemStimulus} into the item bank
     *
     * @param testPackage The test package containing all item-to-stimuli links
     */
    void loadLinkItemsToStimuli(TestPackage testPackage);
}
