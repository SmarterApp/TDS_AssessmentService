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
    private String subject;
    private String assessmentKey;
    private List<Property> languages;

    private Segment(Builder builder) {
        key = builder.key;
        segmentId = builder.segmentId;
        selectionAlgorithm = builder.selectionAlgorithm;
        startAbility = builder.startAbility;
        subject = builder.subject;
        assessmentKey = builder.assessmentKey;
        languages = builder.languages;
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
    public String getSubject() {
        return subject;
    }

    /**
     * @return languages associated with the segment
     */
    public List<Property> getLanguages() {
        return languages;
    }

    public static class Builder {
        private String key;
        private String segmentId;
        private String selectionAlgorithm;
        private float startAbility;
        private String subject;
        private String assessmentKey;
        private List<Property> languages;

        public Builder(String key) {
            this.key = key;
        }

        public Builder withSegmentId(String segmentId) {
            this.segmentId = segmentId;
            return this;
        }

        public Builder withSelectionAlgorithm(String selectionAlgorithm) {
            this.selectionAlgorithm = selectionAlgorithm;
            return this;
        }

        public Builder withStartAbility(float ability) {
            this.startAbility = ability;
            return this;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withAssessmentKey(String assessmentKey) {
            this.assessmentKey = assessmentKey;
            return this;
        }

        public Builder withLanguages(List<Property> languages) {
            this.languages = languages;
            return this;
        }

        public Segment build() {
            return new Segment(this);
        }
    }
}
