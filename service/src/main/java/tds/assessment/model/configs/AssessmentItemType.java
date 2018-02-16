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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "client_test_itemtypes", catalog = "configs")
public class AssessmentItemType {
    private AssessmentItemTypeIdentity assessmentItemTypeIdentity;

    /**
     * Private constructor for frameworks
     */
    private AssessmentItemType() {
    }

    public AssessmentItemType(final String clientName, final String assessmentId, final String itemType) {
        this.assessmentItemTypeIdentity = new AssessmentItemTypeIdentity(clientName, assessmentId, itemType);
    }

    @EmbeddedId
    public AssessmentItemTypeIdentity getAssessmentItemTypeIdentity() {
        return assessmentItemTypeIdentity;
    }

    private void setAssessmentItemTypeIdentity(final AssessmentItemTypeIdentity assessmentItemTypeIdentity) {
        this.assessmentItemTypeIdentity = assessmentItemTypeIdentity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AssessmentItemType that = (AssessmentItemType) o;
        return Objects.equals(assessmentItemTypeIdentity, that.assessmentItemTypeIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentItemTypeIdentity);
    }
}
