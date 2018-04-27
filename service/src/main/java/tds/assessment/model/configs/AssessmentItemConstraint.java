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

package tds.assessment.model.configs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "client_test_itemconstraint", catalog = "configs")
public class AssessmentItemConstraint {
    private AssessmentItemConstraintIdentity assessmentItemConstraintIdentity;
    private String toolType;
    private String toolValue;

    private AssessmentItemConstraint() {
    }

    public static final class Builder {
        private AssessmentItemConstraintIdentity assessmentItemConstraintIdentity;
        private String toolType;
        private String toolValue;

        public Builder(final String clientName, final String assessmentId, final String propertyName,
                       final String propertyValue, final boolean itemIn) {
            this.assessmentItemConstraintIdentity = new AssessmentItemConstraintIdentity(clientName, assessmentId, propertyName,
                propertyValue, itemIn);
        }

        public Builder withToolType(String toolType) {
            this.toolType = toolType;
            return this;
        }

        public Builder withToolValue(String toolValue) {
            this.toolValue = toolValue;
            return this;
        }

        public AssessmentItemConstraint build() {
            AssessmentItemConstraint assessmentItemConstraint = new AssessmentItemConstraint();
            assessmentItemConstraint.toolType = this.toolType;
            assessmentItemConstraint.toolValue = this.toolValue;
            assessmentItemConstraint.assessmentItemConstraintIdentity = this.assessmentItemConstraintIdentity;
            return assessmentItemConstraint;
        }
    }

    @EmbeddedId
    public AssessmentItemConstraintIdentity getAssessmentItemConstraintIdentity() {
        return assessmentItemConstraintIdentity;
    }

    @Column(name = "tooltype")
    public String getToolType() {
        return toolType;
    }

    @Column(name = "toolvalue")
    public String getToolValue() {
        return toolValue;
    }

    /*
        Do not use these setters - use the builder instead
     */
    private void setAssessmentItemConstraintIdentity(final AssessmentItemConstraintIdentity assessmentItemConstraintIdentity) {
        this.assessmentItemConstraintIdentity = assessmentItemConstraintIdentity;
    }

    private void setToolType(final String toolType) {
        this.toolType = toolType;
    }

    private void setToolValue(final String toolValue) {
        this.toolValue = toolValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AssessmentItemConstraint that = (AssessmentItemConstraint) o;
        return Objects.equals(assessmentItemConstraintIdentity, that.assessmentItemConstraintIdentity) &&
            Objects.equals(toolType, that.toolType) &&
            Objects.equals(toolValue, that.toolValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(assessmentItemConstraintIdentity, toolType, toolValue);
    }
}
