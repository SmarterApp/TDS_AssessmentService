package tds.assessment.repositories;

import java.util.List;

import tds.assessment.ItemMeasurement;

public interface ItemMeasurementQueryRepository {
    /**
     * Finds the item measurements for the segment and the overall assessment
     *
     * @param segmentKey    key for the segment
     * @param assessmentKey key for the overall parent assessment
     * @return list of {@link tds.assessment.ItemMeasurement}
     */
    List<ItemMeasurement> findItemMeasurements(final String segmentKey, final String assessmentKey);
}
