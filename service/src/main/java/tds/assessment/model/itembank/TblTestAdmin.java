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

@Entity
@Table(name = "tbltestadmin", catalog = "itembank")
public class TblTestAdmin {
    // A constant string added to the end of each client name in the testadmin table
    private static final String TEST_ADMIN_DESCRIPTION = " administration";
    private String academicYear;
    private String key;
    private long clientKey;
    private Long version;
    private Long updateVersion;
    private String description;
    private String season;

    /**
     * Empty constructor for frameworks
     */
    private TblTestAdmin() {
    }

    private TblTestAdmin(final Builder builder) {
        this.academicYear = builder.academicYear;
        this.key = builder.key;
        this.clientKey = builder.clientKey;
        this.version = builder.version;
        this.updateVersion = builder.updateVersion;
        this.description = builder.key + TEST_ADMIN_DESCRIPTION;
        this.season = "";
    }

    public static final class Builder {
        private String academicYear;
        private String key;
        private long clientKey;
        private Long version;
        private Long updateVersion;

        public Builder() {
        }

        public Builder withAcademicYear(String academicYear) {
            this.academicYear = academicYear;
            return this;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withClientKey(long clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public Builder withVersion(Long version) {
            this.version = version;
            return this;
        }

        public Builder withUpdateVersion(Long updateVersion) {
            this.updateVersion = updateVersion;
            return this;
        }

        public TblTestAdmin build() {
            return new TblTestAdmin(this);
        }
    }

    @Column(name = "schoolyear")
    public String getAcademicYear() {
        return academicYear;
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

    @Column(name = "updateconfig")
    public Long getUpdateVersion() {
        return updateVersion;
    }

    public String getDescription() {
        return description;
    }

    public String getSeason() {
        return season;
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setAcademicYear(final String academicYear) {
        this.academicYear = academicYear;
    }

    private void setKey(final String key) {
        this.key = key;
    }

    private void setClientKey(final long clientKey) {
        this.clientKey = clientKey;
    }

    private void setVersion(final Long version) {
        this.version = version;
    }

    private void setUpdateVersion(final Long updateVersion) {
        this.updateVersion = updateVersion;
    }

    private void setDescription(final String description) {
        this.description = description;
    }

    private void setSeason(final String season) {
        this.season = season;
    }
}
