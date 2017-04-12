package tds.assessment.services;

import java.util.Optional;

import tds.assessment.SegmentItemInformation;

/**
 * Handles segment specific actions
 */
public interface SegmentService {

    /**
     * Finds a {@link tds.assessment.SegmentItemInformation} which is used for item selection
     *
     * @param segmentKey the segment key
     * @return Optional with {@link tds.assessment.SegmentItemInformation} if found otherwise empty
     */
    Optional<SegmentItemInformation> findSegmentItemInformation(final String segmentKey);
}
