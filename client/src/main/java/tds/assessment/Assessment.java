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
    private List<Segment> segments = new ArrayList<>();

    private Assessment() {}

    /**
     * @return  A collection of {@link Segment}s for this assessment
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
    public String getSubject() {
        return subject;
    }

    public static final class Builder {
        private String key;
        private String assessmentId;
        private String selectionAlgorithm;
        private float startAbility;
        private String subject;
        private List<Segment> segments = new ArrayList<>();

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
            return this;
        }

        public Builder withSelectionAlgorithm(String selectionAlgorithm) {
            this.selectionAlgorithm = selectionAlgorithm;
            return this;
        }

        public Builder withStartAbility(float startAbility) {
            this.startAbility = startAbility;
            return this;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withSegments(List<Segment> segments) {
            this.segments = segments;
            return this;
        }

        public Assessment build() {
            Assessment assessment = new Assessment();
            assessment.assessmentId = this.assessmentId;
            assessment.selectionAlgorithm = this.selectionAlgorithm;
            assessment.startAbility = this.startAbility;
            assessment.key = this.key;
            assessment.subject = this.subject;
            assessment.segments = this.segments;
            return assessment;
        }
    }
}
