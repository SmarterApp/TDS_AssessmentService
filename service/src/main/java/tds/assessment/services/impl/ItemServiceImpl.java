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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import tds.assessment.ItemFileMetadata;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.services.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemQueryRepository itemQueryRepository;

    @Autowired
    public ItemServiceImpl(final ItemQueryRepository itemQueryRepository) {
        this.itemQueryRepository = itemQueryRepository;
    }

    @Override
    public Optional<ItemFileMetadata> findItemFileMetadataByStimulusKey(final String clientName, final long bankKey, final long stimulusKey) {
        return itemQueryRepository.findItemFileMetadataByStimulusKey(clientName, bankKey, stimulusKey);
    }

    @Override
    public Optional<ItemFileMetadata> findItemFileMetadataByItemKey(final String clientName, final long bankKey, final long itemKey) {
        return itemQueryRepository.findItemFileMetadataByItemKey(clientName, bankKey, itemKey);
    }
}
