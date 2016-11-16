package tds.assessment.repositories;

import java.util.List;
import java.util.Map;

import tds.assessment.ItemProperty;

/**
 * Repository for reading {@link tds.assessment.ItemProperty} data
 */
public interface ItemPropertyQueryRepository {

    /**
     *  Finds a list of item properties for the assessment
     *
     * @param segmentKey the segment key for the item's {@link tds.assessment.Segment}
     * @return A list of item properties for the assessment
     */
    List<ItemProperty> findActiveItemsProperties(String segmentKey);
}
