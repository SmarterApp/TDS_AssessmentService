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
public class AssessmentItemTypeIdentity implements Serializable {
    private String clientName;
    private String assessmentId;
    private String itemType;

    /**
     * Private constructor for frameworks
     */
    private AssessmentItemTypeIdentity() {
    }

    AssessmentItemTypeIdentity(final String clientName, final String assessmentId, final String itemType) {
        this.clientName = clientName;
        this.assessmentId = assessmentId;
        this.itemType = itemType;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "testid")
    public String getAssessmentId() {
        return assessmentId;
    }

    @Column(name = "itemtype")
    public String getItemType() {
        return itemType;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setAssessmentId(final String assessmentId) {
        this.assessmentId = assessmentId;
    }

    private void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AssessmentItemTypeIdentity that = (AssessmentItemTypeIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        if (!assessmentId.equals(that.assessmentId)) return false;
        return itemType.equals(that.itemType);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + assessmentId.hashCode();
        result = 31 * result + itemType.hashCode();
        return result;
    }
}
