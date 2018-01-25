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
import javax.persistence.Transient;

@Entity
@Table(name = "tblstrand", schema = "itembank")
public class TblStrand {
    private String subjectKey;
    private String name;
    private String key;
    private String parentKey;
    private long clientKey;
    private int treeLevel;
    private Long version;
    private String type;
    private boolean leafTarget;

    private TblStrand(Builder builder) {
        subjectKey = builder.subjectKey;
        name = builder.name;
        key = builder.key;
        parentKey = builder.parentKey;
        clientKey = builder.clientKey;
        treeLevel = builder.treeLevel;
        version = builder.version;
        type = builder.type;
        leafTarget = builder.leafTarget;
    }

    public static final class Builder {
        private String subjectKey;
        private String name;
        private String key;
        private String parentKey;
        private long clientKey;
        private int treeLevel;
        private Long version;
        private String type;
        private boolean leafTarget;

        public Builder() {
        }

        public Builder withSubjectKey(String subjectKey) {
            this.subjectKey = subjectKey;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withParentKey(String parentKey) {
            this.parentKey = parentKey;
            return this;
        }

        public Builder withClientKey(long clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public Builder withTreeLevel(int treeLevel) {
            this.treeLevel = treeLevel;
            return this;
        }

        public Builder withVersion(Long version) {
            this.version = version;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withLeafTarget(boolean leafTarget) {
            this.leafTarget = leafTarget;
            return this;
        }

        public TblStrand build() {
            return new TblStrand(this);
        }
    }

    @Column(name = "_fk_subject")
    public String getSubjectKey() {
        return subjectKey;
    }

    public String getName() {
        return name;
    }

    @Id
    @Column(name = "_key")
    public String getKey() {
        return key;
    }

    @Column(name = "_fk_parent")
    public String getParentKey() {
        return parentKey;
    }

    @Column(name = "_fk_client")
    public long getClientKey() {
        return clientKey;
    }

    @Column(name = "treelevel")
    public int getTreeLevel() {
        return treeLevel;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }

    @Transient
    public String getType() {
        return type;
    }

    @Transient
    public boolean isLeafTarget() {
        return leafTarget;
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

    private void setParentKey(final String parentKey) {
        this.parentKey = parentKey;
    }

    private void setClientKey(final long clientKey) {
        this.clientKey = clientKey;
    }

    private void setTreeLevel(final int treeLevel) {
        this.treeLevel = treeLevel;
    }

    private void setVersion(final Long version) {
        this.version = version;
    }

    private void setType(final String type) {
        this.type = type;
    }

    private void setLeafTarget(final boolean leafTarget) {
        this.leafTarget = leafTarget;
    }
}
