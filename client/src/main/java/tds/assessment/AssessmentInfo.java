package tds.assessment;

import java.util.ArrayList;
import java.util.List;

/**
 * An object containing assessment metadata
 */
public class AssessmentInfo {
    private String key;
    private String id;
    private String subject;
    private String label;
    private int maxAttempts;
    private List<String> grades = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<AssessmentWindow> assessmentWindows = new ArrayList<>();

    /**
     * Empty constructor for frameworks
     */
    private AssessmentInfo() {}

    public AssessmentInfo(Builder builder) {
        this.maxAttempts = builder.maxAttempts;
        this.assessmentWindows = builder.assessmentWindows;
        this.id = builder.id;
        this.label = builder.label;
        this.subject = builder.subject;
        this.grades = builder.grades;
        this.languages = builder.languages;
        this.key = builder.key;
    }

    public static final class Builder {
        private String key;
        private String id;
        private String subject;
        private String label;
        private int maxAttempts;
        private List<String> grades;
        private List<String> languages;
        private List<AssessmentWindow> assessmentWindows;

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder withGrades(List<String> grades) {
            this.grades = grades;
            return this;
        }

        public Builder withLanguages(List<String> languages) {
            this.languages = languages;
            return this;
        }

        public Builder withAssessmentWindows(List<AssessmentWindow> assessmentWindows) {
            this.assessmentWindows = assessmentWindows;
            return this;
        }

        public static Builder fromAssessmentInfo(AssessmentInfo assessmentInfo) {
            return new AssessmentInfo.Builder()
                .withMaxAttempts(assessmentInfo.maxAttempts)
                .withId(assessmentInfo.id)
                .withKey(assessmentInfo.key)
                .withSubject(assessmentInfo.subject)
                .withLabel(assessmentInfo.label)
                .withAssessmentWindows(assessmentInfo.assessmentWindows)
                .withLanguages(assessmentInfo.languages)
                .withGrades(assessmentInfo.grades);
        }

        public AssessmentInfo build() {
            return new AssessmentInfo(this);
        }
    }

    /**
     * @return The key of the assessment
     */
    public String getKey() {
        return key;
    }

    /**
     * @return The id of the assessment
     */
    public String getId() {
        return id;
    }

    /**
     * @return The subject of the assessment
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return The label of the assessment, displayed in the UI
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return The maximum number of exam attempts
     */
    public int getMaxAttempts() {
        return maxAttempts;
    }

    /**
     * @return The list of eligible grades for the assessments
     */
    public List<String> getGrades() {
        return grades;
    }

    /**
     * @return The list of languages for the assessment
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * @return The assessment windows for the assessment
     */
    public List<AssessmentWindow> getAssessmentWindows() {
        return assessmentWindows;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AssessmentInfo that = (AssessmentInfo) o;

        if (maxAttempts != that.maxAttempts) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (grades != null ? !grades.equals(that.grades) : that.grades != null) return false;
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) return false;
        return assessmentWindows != null ? assessmentWindows.equals(that.assessmentWindows) : that.assessmentWindows == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + maxAttempts;
        result = 31 * result + (grades != null ? grades.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (assessmentWindows != null ? assessmentWindows.hashCode() : 0);
        return result;
    }
}
