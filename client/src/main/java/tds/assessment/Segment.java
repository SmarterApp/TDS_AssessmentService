package tds.assessment;

import java.util.List;

/**
 * Represents admin information concerning a segment
 */
public class Segment {
    private String key;
    private String segmentId;
    private String selectionAlgorithm;
    private float startAbility;
    private String subjectName;
    private String assessmentKey;

    public Segment(String key,
                      String segmentId,
                      String selectionAlgorithm,
                      float startAbility,
                      String assessmentKey) {
        this(key, segmentId, selectionAlgorithm, startAbility, assessmentKey, null);
    }

    public Segment(String key,
                      String segmentId,
                      String selectionAlgorithm,
                      float startAbility,
                      String assessmentKey,
                      String subjectName) {
        this.key = key;
        this.segmentId = segmentId;
        this.selectionAlgorithm = selectionAlgorithm;
        this.startAbility = startAbility;
        this.assessmentKey = assessmentKey;
        this.subjectName = subjectName;
    }

    /**
     * @return key to the segment
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the key to the assessment this segment is a part of
     */
    public String getAssessmentKey() {
        return assessmentKey;
    }

    /**
     * @return the associated segment id
     */
    public String getSegmentId() {
        return segmentId;
    }

    /**
     * @return the selection algorithm key for the segment
     */
    public String getSelectionAlgorithm() {
        return selectionAlgorithm;
    }

    /**
     * @return the start ability value for the segment
     */
    public float getStartAbility() {
        return startAbility;
    }

    /**
     * @return the subject name - this can be null
     */
    public String getSubjectName() {
        return subjectName;
    }
}
