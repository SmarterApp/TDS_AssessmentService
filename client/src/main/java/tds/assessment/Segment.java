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
    private int position;
    private int minItems;
    private int maxItems;
    private List<ItemProperty> languages;
    private List<Form> forms;
    private List<Item> items;

    private Segment(Builder builder) {
        key = builder.key;
        segmentId = builder.segmentId;
        selectionAlgorithm = builder.selectionAlgorithm;
        startAbility = builder.startAbility;
        subject = builder.subject;
        assessmentKey = builder.assessmentKey;
        position = builder.position;
        minItems = builder.minItems;
        maxItems = builder.maxItems;
        languages = builder.languages;
        forms = builder.forms;
        items = builder.items;
    }

    /**
     * Empty constructor for frameworks
     */
    private Segment() {}

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
    public List<ItemProperty> getLanguages() {
        return languages;
    }

    /**
     * @return the position of the segment in the {@link Assessment}
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the minimum number of items in the {@link Segment}
     */
    public int getMinItems() {
        return minItems;
    }

    /**
     * @return the minimum number of items in the {@link Segment}
     */
    public int getMaxItems() {
        return maxItems;
    }

    /**
     * @return return the forms that are a part of this assessment's {@link tds.assessment.Segment}
     */
    public List<Form> getForms() {
        return forms;
    }

    /**
     * @return return the items that a part of this assessment's {@link tds.assessment.Segment}
     */
    public List<Item> getItems() {
        return items;
    }

    public static class Builder {
        private String key;
        private String segmentId;
        private String selectionAlgorithm;
        private float startAbility;
        private String subject;
        private String assessmentKey;
        private int position;
        private int minItems;
        private int maxItems;
        private List<ItemProperty> languages;
        private List<Form> forms;
        private List<Item> items;

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

        public Builder withLanguages(List<ItemProperty> languages) {
            this.languages = languages;
            return this;
        }

        public Builder withPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder withMinItems(int minItems) {
            this.minItems = minItems;
            return this;
        }

        public Builder withMaxItems(int maxItems) {
            this.maxItems = maxItems;
            return this;
        }

        public Builder withForms(List<Form> forms) {
            this.forms = forms;
            return this;
        }

        public Builder withItems(List<Item> items) {
            this.items = items;
            return this;
        }

        public Segment build() {
            return new Segment(this);
        }
    }
}
