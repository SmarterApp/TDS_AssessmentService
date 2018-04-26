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
public class AssessmentGradeIdentity implements Serializable {
    private String clientName;
    private String assessmentId;
    private String grade;

    private AssessmentGradeIdentity() {
    }

    AssessmentGradeIdentity(final String clientName, final String assessmentId, final String grade) {
        this.clientName = clientName;
        this.assessmentId = assessmentId;
        this.grade = grade;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "testid")
    public String getAssessmentId() {
        return assessmentId;
    }

    public String getGrade() {
        return grade;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setAssessmentId(final String assessmentId) {
        this.assessmentId = assessmentId;
    }

    private void setGrade(final String grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AssessmentGradeIdentity that = (AssessmentGradeIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        if (!assessmentId.equals(that.assessmentId)) return false;
        return grade.equals(that.grade);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + assessmentId.hashCode();
        result = 31 * result + grade.hashCode();
        return result;
    }
}
