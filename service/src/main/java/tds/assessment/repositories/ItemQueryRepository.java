package tds.assessment.repositories;

import java.util.List;

import tds.assessment.Item;
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
}
