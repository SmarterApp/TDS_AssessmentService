package tds.assessment;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents admin information concerning an assessment and its individual segments (if any)
 */
public class Assessment {
    private String key;
    private String assessmentId;
    private String selectionAlgorithm;
    private float startAbility;
    private String subject;
    private List<ItemConstraint> itemConstraints;
    private List<Segment> segments = new ArrayList<>();

    public Assessment() {}

    public List<ItemConstraint> getItemConstraints() {
        return itemConstraints;
    }

    public void setItemConstraints(List<ItemConstraint> itemConstraints) {
        this.itemConstraints = itemConstraints;
    }

    /**
     * @return  A collection of {@link Segment}s for this assessment
     */
    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    /**
     * @return key to the assessment
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the associated assessment key
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
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

    public void setSelectionAlgorithm(String selectionAlgorithm) {
        this.selectionAlgorithm = selectionAlgorithm;
    }

    /**
     * @return the start ability value for the assessment
     */
    public float getStartAbility() {
        return startAbility;
    }

    public void setStartAbility(float startAbility) {
        this.startAbility = startAbility;
    }

    /**
     * @return the subject name - this can be null
     */
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
