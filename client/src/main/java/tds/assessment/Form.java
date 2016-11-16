package tds.assessment;

/**
 * A model representing a form for an {@link tds.assessment.Assessment}
 */
public class Form {
    private String key;
    private String id;
    private String language;
    private String cohort;
    private String segmentKey;
    private Long loadVersion;
    private Long updateVersion;

    /**
     * Private constructor for framework
     */
    private Form() {}

    private Form(Builder builder) {
        key = builder.key;
        id = builder.id;
        language = builder.language;
        cohort = builder.cohort;
        segmentKey = builder.segmentKey;
        loadVersion = builder.loadVersion;
        updateVersion = builder.updateVersion;
    }

    public static class Builder {
        private String key;
        private String id;
        private String language;
        private String cohort;
        private String segmentKey;
        private Long loadVersion;
        private Long updateVersion;


        public Builder (String key) {
            this.key = key;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder withCohort(String cohort) {
            this.cohort = cohort;
            return this;
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
            return this;
        }

        public Builder withLoadVersion(Long loadVersion) {
            this.loadVersion = loadVersion;
            return this;
        }

        public Builder withUpdateVersion(Long updateVersion) {
            this.updateVersion = updateVersion;
            return this;
        }

        public Form build() {
            return new Form(this);
        }
    }

    /**
     * @return the language for the form
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return the key of the form (ex: 187-582)
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the id of the form
     */
    public String getId() {
        return id;
    }

    /**
     * @return the cohort of the form
     */
    public String getCohort() {
        return cohort;
    }

    /**
     * @return the key of the segment this form is a child of
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    /**
     * @return the originally loaded version of this form
     */
    public Long getLoadVersion() {
        return loadVersion;
    }

    /**
     * @return an optional "update" version of the form
     */
    public Long getUpdateVersion() {
        return updateVersion;
    }

}
