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

@Entity
@Table(name = "client_testgrades", catalog = "configs")
public class AssessmentGrade {
    private AssessmentGradeIdentity assessmentGradeIdentity;
    private boolean requireEnrollment;

    /**
     * Private constructor for frameworks
     */
    private AssessmentGrade() {
    }

    public AssessmentGrade(final String clientName, final String assessmentId, final String grade) {
        this.assessmentGradeIdentity = new AssessmentGradeIdentity(clientName, assessmentId, grade);
        this.requireEnrollment = false;
    }

    @EmbeddedId
    public AssessmentGradeIdentity getAssessmentGradeIdentity() {
        return assessmentGradeIdentity;
    }

    @Column(name = "requireenrollment")
    public boolean isRequireEnrollment() {
        return requireEnrollment;
    }

    private void setAssessmentGradeIdentity(final AssessmentGradeIdentity assessmentGradeIdentity) {
        this.assessmentGradeIdentity = assessmentGradeIdentity;
    }

    private void setRequireEnrollment(final boolean requireEnrollment) {
        this.requireEnrollment = requireEnrollment;
    }
}
