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
@Table(name = "tblstimulus", schema = "itembank")
public class TblStimulus {
    private long key;
    private long bankKey;
    private String filePath;
    private String fileName;
    private Long version;
    private String id; // For hibernate compatibility - not used

    private TblStimulus(Builder builder) {
        key = builder.key;
        bankKey = builder.bankKey;
        filePath = builder.filePath;
        fileName = builder.fileName;
        version = builder.version;
    }

    public static final class Builder {
        private long key;
        private long bankKey;
        private String filePath;
        private String fileName;
        private Long version;

        public Builder() {
        }

        public Builder withKey(int key) {
            this.key = key;
            return this;
        }

        public Builder withBankKey(int bankKey) {
            this.bankKey = bankKey;
            return this;
        }

        public Builder withFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withVersion(Long version) {
            this.version = version;
            return this;
        }

        public TblStimulus build() {
            return new TblStimulus(this);
        }
    }

    @Column(name = "_efk_itskey")
    public long getKey() {
        return key;
    }

    @Column(name = "_efk_itembank")
    public long getBankKey() {
        return bankKey;
    }

    @Column(name = "filepath")
    public String getFilePath() {
        return filePath;
    }

    @Column(name = "filename")
    public String getFileName() {
        return fileName;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }

    @Id
    @Column(name = "_key")
    public String getId() {
        return String.format("%s-%s", bankKey, key);
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    public void setId(final String id) {
        this.id = id;
    }

    public void setKey(final long key) {
        this.key = key;
    }

    public void setBankKey(final long bankKey) {
        this.bankKey = bankKey;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
