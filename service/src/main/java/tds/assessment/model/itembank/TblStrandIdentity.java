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
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TblStrandIdentity implements Serializable {
    private String subjectKey;
    private String name;
    private String key;

    private TblStrandIdentity() {

    }

    TblStrandIdentity(final String subjectKey, final String name, final String key) {
        this.subjectKey = subjectKey;
        this.name = name;
        this.key = key;
    }

    @Column(name = "_fk_subject")
    public String getSubjectKey() {
        return subjectKey;
    }

    public String getName() {
        return name;
    }

    @Column(name = "_key")
    public String getKey() {
        return key;
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setSubjectKey(final String subjectKey) {
        this.subjectKey = subjectKey;
    }

    private void setName(final String name) {
        this.name = name;
    }

    private void setKey(final String key) {
        this.key = key;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TblStrandIdentity that = (TblStrandIdentity) o;
        return Objects.equals(subjectKey, that.subjectKey) &&
            Objects.equals(name, that.name) &&
            Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {

        return Objects.hash(subjectKey, name, key);
    }
}
