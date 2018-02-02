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

/**
 * A row representing a subject, with a link to its client/publisher
 */
@Entity
@Table(name = "tblsubject", schema = "itembank")
public class TblSubject {
    private String name;
    private String key;
    private long clientKey;
    private Long version;
    private String grade;

    /**
     * Private constructor for frameworks
     */
    private TblSubject() {
    }

    public TblSubject(final String name, final String key, final long clientKey, final Long version) {
        this.name = name;
        this.key = key;
        this.clientKey = clientKey;
        this.version = version;
        this.grade = "";
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setClientKey(final long clientKey) {
        this.clientKey = clientKey;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    @Id
    @Column(name = "_key")
    public String getKey() {
        return key;
    }

    @Column(name = "_fk_client")
    public long getClientKey() {
        return clientKey;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }
}
