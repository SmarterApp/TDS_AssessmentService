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