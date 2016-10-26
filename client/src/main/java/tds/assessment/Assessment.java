package tds.assessment;

import java.util.List;

/**
 * Represents admin information concerning an assessment and its individual segments (if any)
 */
public class Assessment {
    private String key;
    private String assessmentId;
    private String selectionAlgorithm;
    private float startAbility;
    private String subjectName;
    private List<Segment> segments;

    public Assessment(String key,
                      String assessmentId,
                      List<Segment> segments,
                      String selectionAlgorithm,
                      float startAbility) {
        this(key, assessmentId, segments, selectionAlgorithm, startAbility, null);
    }

    public Assessment(String key,
                      String assessmentId,
                      List<Segment> segments,
                      String selectionAlgorithm,
                      float startAbility,
                      String subjectName) {
        this.key = key;
        this.assessmentId = assessmentId;
        this.segments = segments;
        this.selectionAlgorithm = selectionAlgorithm;
        this.startAbility = startAbility;
        this.subjectName = subjectName;
    }

    /**
     * A collection of {@link Segment}s for this assessment
     * @return
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * @return key to the assessment
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the associated assessment key
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return {@code true} if the assessment is segmented
     */
    public boolean isSegmented() {
        return segments.size() > 1;
    }

    /**
     * @return the selection algorithm key for the assessment
     */
    public String getSelectionAlgorithm() {
        return selectionAlgorithm;
    }

    /**
     * @return the start ability value for the assessment
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
