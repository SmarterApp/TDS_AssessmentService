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

import java.util.Optional;

import tds.assessment.ItemFileMetadata;

public interface ItemService {
    /**
     * Find the item by bank and stimulus key
     *
     * @param clientName the client name
     * @param bankKey     the bank key
     * @param stimulusKey the stimulus key
     * @return {@link tds.assessment.ItemFileMetadata} if found otherwise empty
     */
    Optional<ItemFileMetadata> findItemFileMetadataByStimulusKey(final String clientName, final long bankKey, final long stimulusKey);

    /**
     * Find the item by bank and item key
     *
     * @param clientName the client name
     * @param bankKey the bank key
     * @param itemKey the item key
     * @return {@link tds.assessment.ItemFileMetadata} if found otherwise empty
     */
    Optional<ItemFileMetadata> findItemFileMetadataByItemKey(final String clientName, final long bankKey, final long itemKey);
}