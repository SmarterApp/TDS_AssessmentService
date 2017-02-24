package tds.assessment.model;

/**
 * Contains the parameters for the assessment window lookup
 */
public class AssessmentWindowParameters {
    private long studentId;
    private String clientName;
    private String assessmentId;
    private int shiftWindowStart;
    private int shiftWindowEnd;
    private int shiftFormStart;
    private int shiftFormEnd;
    private String formList;

    private AssessmentWindowParameters(Builder builder) {
        this.studentId = builder.studentId;
        this.clientName = builder.clientName;
        this.assessmentId = builder.assessmentId;
        this.shiftWindowStart = builder.shiftWindowStart;
        this.shiftWindowEnd = builder.shiftWindowEnd;
        this.shiftFormStart = builder.shiftFormStart;
        this.shiftFormEnd = builder.shiftFormEnd;
        this.formList = builder.formList;
    }

    /**
     * @return The unique id for the student
     */
    public long getStudentId() {
        return studentId;
    }

    /**
     * @return the client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return the id for the assessment
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return the days to shift the assessment window start
     */
    public int getShiftWindowStart() {
        return shiftWindowStart;
    }

    /**
     * @return the days to shift the assessment window end
     */
    public int getShiftWindowEnd() {
        return shiftWindowEnd;
    }

    /**
     * @return the days to shift the assessment form start
     */
    public int getShiftFormStart() {
        return shiftFormStart;
    }

    /**
     * @return the days to shift the assessment form end
     */
    public int getShiftFormEnd() {
        return shiftFormEnd;
    }

    /**
     * @return the form list containing form and window information
     */
    public String getFormList() {
        return formList;
    }

    public static class Builder {
        private long studentId;
        private String clientName;
        private String assessmentId;
        private int shiftWindowStart = 0;
        private int shiftWindowEnd = 0;
        private int shiftFormStart = 0;
        private int shiftFormEnd = 0;
        private String formList;

        public Builder(long studentId, String clientName, String assessmentId) {
            this.studentId = studentId;
            this.clientName = clientName;
            this.assessmentId = assessmentId;
        }

        public Builder withShiftWindowStart(int shiftWindowStart) {
            this.shiftWindowStart = shiftWindowStart;
            return this;
        }

        public Builder withShiftWindowEnd(int shiftWindowEnd) {
            this.shiftWindowEnd = shiftWindowEnd;
            return this;
        }

        public Builder withShiftFormStart(int shiftFormStart) {
            this.shiftFormStart = shiftFormStart;
            return this;
        }

        public Builder withShiftFormEnd(int shiftFormEnd) {
            this.shiftFormEnd = shiftFormEnd;
            return this;
        }

        public Builder withFormList(String formList) {
            this.formList = formList;
            return this;
        }

        public AssessmentWindowParameters build() {
            return new AssessmentWindowParameters(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssessmentWindowParameters that = (AssessmentWindowParameters) o;

        if (studentId != that.studentId) return false;
        if (shiftWindowStart != that.shiftWindowStart) return false;
        if (shiftWindowEnd != that.shiftWindowEnd) return false;
        if (shiftFormStart != that.shiftFormStart) return false;
        if (shiftFormEnd != that.shiftFormEnd) return false;
        if (!clientName.equals(that.clientName)) return false;
        if (!assessmentId.equals(that.assessmentId)) return false;
        return formList != null ? formList.equals(that.formList) : that.formList == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (studentId ^ (studentId >>> 32));
        result = 31 * result + clientName.hashCode();
        result = 31 * result + assessmentId.hashCode();
        result = 31 * result + shiftWindowStart;
        result = 31 * result + shiftWindowEnd;
        result = 31 * result + shiftFormStart;
        result = 31 * result + shiftFormEnd;
        result = 31 * result + (formList != null ? formList.hashCode() : 0);
        return result;
    }
}
