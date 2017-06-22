/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemConstraint that = (ItemConstraint) o;

        if (inclusive != that.inclusive) return false;
        if (assessmentId != null ? !assessmentId.equals(that.assessmentId) : that.assessmentId != null) return false;
        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) return false;
        if (propertyValue != null ? !propertyValue.equals(that.propertyValue) : that.propertyValue != null)
            return false;
        if (toolType != null ? !toolType.equals(that.toolType) : that.toolType != null) return false;
        return toolValue != null ? toolValue.equals(that.toolValue) : that.toolValue == null;
    }

    @Override
    public int hashCode() {
        int result = assessmentId != null ? assessmentId.hashCode() : 0;
        result = 31 * result + (propertyName != null ? propertyName.hashCode() : 0);
        result = 31 * result + (propertyValue != null ? propertyValue.hashCode() : 0);
        result = 31 * result + (toolType != null ? toolType.hashCode() : 0);
        result = 31 * result + (toolValue != null ? toolValue.hashCode() : 0);
        result = 31 * result + (inclusive ? 1 : 0);
        return result;
    }
}
