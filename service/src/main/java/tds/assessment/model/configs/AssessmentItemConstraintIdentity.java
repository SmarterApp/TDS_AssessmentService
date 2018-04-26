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
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AssessmentItemConstraintIdentity implements Serializable {
    private String clientName;
    private String assessmentId;
    private String propertyName;
    private String propertyValue;
    private boolean itemIn;

    private AssessmentItemConstraintIdentity() {
    }

    AssessmentItemConstraintIdentity(final String clientName, final String assessmentId, final String propertyName,
                                            final String propertyValue, final boolean itemIn) {
        this.clientName = clientName;
        this.assessmentId = assessmentId;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.itemIn = itemIn;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "testid")
    public String getAssessmentId() {
        return assessmentId;
    }

    @Column(name = "propname")
    public String getPropertyName() {
        return propertyName;
    }

    @Column(name = "propvalue")
    public String getPropertyValue() {
        return propertyValue;
    }

    @Column(name = "item_in")
    public boolean isItemIn() {
        return itemIn;
    }

    /*
        Do not use these setters - use the builder instead
    */
    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setAssessmentId(final String assessmentId) {
        this.assessmentId = assessmentId;
    }

    private void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    private void setPropertyValue(final String propertyValue) {
        this.propertyValue = propertyValue;
    }

    private void setItemIn(final boolean itemIn) {
        this.itemIn = itemIn;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AssessmentItemConstraintIdentity that = (AssessmentItemConstraintIdentity) o;

        if (itemIn != that.itemIn) return false;
        if (!clientName.equals(that.clientName)) return false;
        if (!assessmentId.equals(that.assessmentId)) return false;
        if (!propertyName.equals(that.propertyName)) return false;
        return propertyValue.equals(that.propertyValue);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + assessmentId.hashCode();
        result = 31 * result + propertyName.hashCode();
        result = 31 * result + propertyValue.hashCode();
        result = 31 * result + (itemIn ? 1 : 0);
        return result;
    }
}
