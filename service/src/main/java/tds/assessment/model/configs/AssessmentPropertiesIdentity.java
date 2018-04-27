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
import java.util.Objects;

@Embeddable
public class AssessmentPropertiesIdentity implements Serializable {
    private String clientName;
    private String assessmentId;

    /**
     * Private constructor for frameworks
     */
    private AssessmentPropertiesIdentity() {
    }

    AssessmentPropertiesIdentity(final String clientName, final String assessmentId) {
        this.clientName = clientName;
        this.assessmentId = assessmentId;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "testid")
    public String getAssessmentId() {
        return assessmentId;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setAssessmentId(final String assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AssessmentPropertiesIdentity that = (AssessmentPropertiesIdentity) o;
        return Objects.equals(clientName, that.clientName) &&
            Objects.equals(assessmentId, that.assessmentId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientName, assessmentId);
    }
}
