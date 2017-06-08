package tds.assessment.repositories;

import java.util.List;
import java.util.Optional;

import tds.assessment.Item;
import tds.assessment.ItemFileMetadata;
import tds.assessment.ItemProperty;

/**
 * Repository for reading {@link tds.assessment.ItemProperty} data
 */
public interface ItemQueryRepository {

    /**
     * Finds the list of item properties for the assessment
     *
     * @param assessmentKey the assessment key for the item's {@link tds.assessment.Assessment}
     * @return A list of item properties for the assessment
     */
    List<ItemProperty> findActiveItemsProperties(final String assessmentKey);

    /**
     * Finds the list of {@link tds.assessment.Item}s for the assessment
     *
     * @param assessmentKey the assessment key for the item's {@link tds.assessment.Assessment}
     * @return A list of items for the assessment
     */
    List<Item> findItemsForAssessment(final String assessmentKey);

    /**
     * Find the item by bank and stimulus key
     *
     * @param clientName  the client name
     * @param bankKey     the bank key
     * @param stimulusKey the stimulus key
     * @return {@link tds.assessment.ItemFileMetadata} if found otherwise empty
     */
    Optional<ItemFileMetadata> findItemFileMetadataByStimulusKey(final String clientName, final long bankKey, final long stimulusKey);

    /**
     * Find the item by bank and item key
     *
     * @param clientName the client name
     * @param bankKey    the bank key
     * @param itemKey    the item key
     * @return {@link tds.assessment.ItemFileMetadata} if found otherwise empty
     */
    Optional<ItemFileMetadata> findItemFileMetadataByItemKey(final String clientName, final long bankKey, final long itemKey);

    /**
     * Finds items associated with the segment key
     *
     * @param segmentKey the segment key
     * @return list of {@link tds.assessment.Item}
     */
    List<Item> findItemsForSegment(final String segmentKey);
}
