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

package tds.assessment.model.itembank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "setoftestgrades", schema = "itembank")
public class SetOfTestGrades {
    private UUID gradeKey;
    private String id;
    private String key;
    private String grade;
    private boolean requireEnrollment;

    public SetOfTestGrades(final String id, final String key, final String grade) {
        this.id = id;
        this.key = key;
        this.grade = grade;
        this.gradeKey = UUID.randomUUID();
        this.requireEnrollment = false;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
    }

    public void setGradeKey(final UUID gradeKey) {
        this.gradeKey = gradeKey;
    }

    public void setRequireEnrollment(final boolean requireEnrollment) {
        this.requireEnrollment = requireEnrollment;
    }

    @Column(name = "testid")
    public String getId() {
        return id;
    }

    @Column(name = "_fk_adminsubject")
    public String getKey() {
        return key;
    }

    public String getGrade() {
        return grade;
    }

    @Column(name = "requireenrollment")
    public boolean isRequireEnrollment() {
        return requireEnrollment;
    }

    @Id
    @Column(name = "_key", columnDefinition = "VARBINARY(16)")
    public UUID getGradeKey() {
        return gradeKey;
    }
}
