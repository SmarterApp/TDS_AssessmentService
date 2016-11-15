package tds.assessment;

/**
 * Created by emunoz on 11/8/16.
 */
public class ItemConstraint {
    private String assessmentId;
    private String propertyName;
    private String propertyValue;
    private String toolType;
    private String toolValue;
    private boolean inclusive;

    /**
     * Private constructor for framework
     */
    private ItemConstraint() {}

    private ItemConstraint(Builder builder) {
        this.assessmentId = builder.assessmentId;
        this.propertyValue = builder.propertyValue;
        this.inclusive = builder.inclusive;
        this.propertyName = builder.propertyName;
        this.toolType = builder.toolType;
        this.toolValue = builder.toolValue;
    }

    /**
     * @return The type of tool the constraint applies to
     */
    public String getToolType() {
        return toolType;
    }

    /**
     * @return the value of the tool the constraint applies to
     */
    public String getToolValue() {
        return toolValue;
    }

    /**
     * @return the id of the {@link tds.assessment.Assessment} the constraint applies to
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return the property name of the constraint
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @return the property value of the constraint
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * @return flag that dictates whether the item constraint is an inclusionary or exclusionary rule
     */
    public boolean isInclusive() {
        return inclusive;
    }

    public static class Builder {
        private String assessmentId;
        private String propertyName;
        private String propertyValue;
        private String toolType;
        private String toolValue;
        private boolean inclusive;

        public Builder() {
        }

        public Builder withAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
            return this;
        }

        public Builder withPropertyName(String propertyName) {
            this.propertyName = propertyName;
            return this;
        }

        public Builder withPropertyValue(String propertyValue) {
            this.propertyValue = propertyValue;
            return this;
        }

        public Builder withToolType(String toolType) {
            this.toolType = toolType;
            return this;
        }

        public Builder withToolValue(String toolValue) {
            this.toolValue = toolValue;
            return this;
        }

        public Builder withInclusive(boolean inclusive) {
            this.inclusive = inclusive;
            return this;
        }

        public ItemConstraint build() {
            return new ItemConstraint(this);
        }
    }
}
