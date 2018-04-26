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
@Table(name = "client_grade", catalog = "configs")
public class Grade {
    private GradeIdentity gradeIdentity;
    private String gradeLabel;

    /**
     * Private constructor for frameworks
     */
    private Grade() {
    }

    public Grade(final String gradeCode, final String gradeLabel, final String clientName) {
        this.gradeIdentity = new GradeIdentity(gradeCode, clientName);
        this.gradeLabel = gradeLabel;
    }

    @EmbeddedId
    public GradeIdentity getGradeIdentity() {
        return gradeIdentity;
    }

    @Column(name = "grade")
    public String getGradeLabel() {
        return gradeLabel;
    }

    private void setGradeIdentity(final GradeIdentity gradeIdentity) {
        this.gradeIdentity = gradeIdentity;
    }

    private void setGradeLabel(final String gradeLabel) {
        this.gradeLabel = gradeLabel;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Grade grade = (Grade) o;
        return Objects.equals(gradeIdentity, grade.gradeIdentity) &&
            Objects.equals(gradeLabel, grade.gradeLabel);
    }

    @Override
    public int hashCode() {

        return Objects.hash(gradeIdentity, gradeLabel);
    }
}
