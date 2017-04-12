package tds.assessment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * A model representing a form for an {@link tds.assessment.Assessment} that uses the "fixed form" algorithm to select
 * the {@link tds.assessment.Item}s.
 */
@JsonIgnoreProperties(value="length", allowGetters = true)
public class Form {
    private String key;
    private String id;
    private String languageCode;
    private String cohort;
    private String segmentKey;
    private Long loadVersion;
    private Long updateVersion;
    private List<Item> items;

    /**
     * Private constructor for framework
     */
    private Form() {}

    private Form(Builder builder) {
        key = builder.key;
        id = builder.id;
        languageCode = builder.languageCode;
        cohort = builder.cohort;
        segmentKey = builder.segmentKey;
        loadVersion = builder.loadVersion;
        updateVersion = builder.updateVersion;
        items = builder.items;
    }

    public static class Builder {
        private String key;
        private String id;
        private String languageCode;
        private String cohort;
        private String segmentKey;
        private Long loadVersion;
        private Long updateVersion;
        private List<Item> items;


        public Builder (String key) {
            this.key = key;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withLanguage(String language) {
            this.languageCode = language;
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

        public Builder withItems(List<Item> items) {
            this.items = items;
            return this;
        }

        public Form build() {
            return new Form(this);
        }
    }

    /**
     * @return the languageCode for the form
     */
    public String getLanguageCode() {
        return languageCode;
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

    /**
     * @return The {@link tds.assessment.Item}s associated with this form
     */
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Determine the number of {@link tds.assessment.Item}s associated with this form.
     *
     * @return The number of {@link tds.assessment.Item}s in this form.  If the list is null, return 0.
     */
    public int getLength() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Form form = (Form) o;

        if (key != null ? !key.equals(form.key) : form.key != null) return false;
        if (id != null ? !id.equals(form.id) : form.id != null) return false;
        if (languageCode != null ? !languageCode.equals(form.languageCode) : form.languageCode != null) return false;
        if (cohort != null ? !cohort.equals(form.cohort) : form.cohort != null) return false;
        if (segmentKey != null ? !segmentKey.equals(form.segmentKey) : form.segmentKey != null) return false;
        if (loadVersion != null ? !loadVersion.equals(form.loadVersion) : form.loadVersion != null) return false;
        if (updateVersion != null ? !updateVersion.equals(form.updateVersion) : form.updateVersion != null)
            return false;
        return items != null ? items.equals(form.items) : form.items == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (languageCode != null ? languageCode.hashCode() : 0);
        result = 31 * result + (cohort != null ? cohort.hashCode() : 0);
        result = 31 * result + (segmentKey != null ? segmentKey.hashCode() : 0);
        result = 31 * result + (loadVersion != null ? loadVersion.hashCode() : 0);
        result = 31 * result + (updateVersion != null ? updateVersion.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
