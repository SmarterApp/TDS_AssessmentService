package tds.assessment;

/**
 * Represents admin information concerning an assessment
 */
public class SetOfAdminSubject {
    private String key;
    private String assessmentId;
    private boolean segmented;
    private String selectionAlgorithm;
    private float startAbility;
    private String subjectName;

    public SetOfAdminSubject(String key,
                             String assessmentId,
                             boolean segmented,
                             String selectionAlgorithm,
                             float startAbility) {
        this(key, assessmentId, segmented, selectionAlgorithm, startAbility, null);
    }

    public SetOfAdminSubject(String key,
                             String assessmentId,
                             boolean segmented,
                             String selectionAlgorithm,
                             float startAbility,
                             String subjectName) {
        this.key = key;
        this.assessmentId = assessmentId;
        this.segmented = segmented;
        this.selectionAlgorithm = selectionAlgorithm;
        this.startAbility = startAbility;
        this.subjectName = subjectName;
    }

    /**
     * @return key to the admin subject
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
        return segmented;
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
