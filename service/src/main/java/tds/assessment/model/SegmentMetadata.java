package tds.assessment.model;

/**
 * Represents metadata for a single segment
 */
public class SegmentMetadata {
    private final String segmentKey;
    private final String parentKey;
    private final String clientName;

    public SegmentMetadata(final String segmentKey, final String parentKey, final String clientName) {
        this.segmentKey = segmentKey;
        this.parentKey = parentKey;
        this.clientName = clientName;
    }

    /**
     * @return key for the segment
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    /**
     * @return assessment key if part of a multi segmented assessment.  Single segment assessments will be null
     */
    public String getParentKey() {
        return parentKey;
    }

    /**
     * @return client name associated with the segment
     */
    public String getClientName() {
        return clientName;
    }
}
