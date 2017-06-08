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
