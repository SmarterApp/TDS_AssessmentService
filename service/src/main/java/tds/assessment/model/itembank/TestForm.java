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
@Table(name = "testform", schema = "itembank")
public class TestForm {
    private String segmentKey;
    private long key; // this refers to both "_efk_itsbank" and "_efk_itskey"
    private long itsKey; // not used - hibernate compatibility only. "key" property should be used
    private String formId;
    private String language;
    private String formKey;
    private Long version;
    private Long updateVersion;
    private String cohort;

    public static final class Builder {
        private String segmentKey;
        private long key; // this refers to both "_efk_itsbank" and "_efk_itskey"
        private String formId;
        private String language;
        private String formKey;
        private Long version;
        private Long updateVersion;
        private String cohort;

        public Builder() {
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
            return this;
        }

        public Builder withKey(long key) {
            this.key = key;
            return this;
        }

        public Builder withFormId(String formId) {
            this.formId = formId;
            return this;
        }

        public Builder withLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder withFormKey(String formKey) {
            this.formKey = formKey;
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

        public Builder withCohort(String cohort) {
            this.cohort = cohort;
            return this;
        }

        public TestForm build() {
            TestForm testForm = new TestForm();
            testForm.version = this.version;
            testForm.key = this.key;
            testForm.formId = this.formId;
            testForm.segmentKey = this.segmentKey;
            testForm.formKey = this.formKey;
            testForm.updateVersion = this.updateVersion;
            testForm.cohort = this.cohort;
            testForm.language = this.language;
            return testForm;
        }
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    private void setKey(final long key) {
        this.key = key;
    }

    private void setFormId(final String formId) {
        this.formId = formId;
    }

    private void setLanguage(final String language) {
        this.language = language;
    }

    private void setFormKey(final String formKey) {
        this.formKey = formKey;
    }

    private void setVersion(final Long version) {
        this.version = version;
    }

    private void setUpdateVersion(final Long updateVersion) {
        this.updateVersion = updateVersion;
    }

    private void setCohort(final String cohort) {
        this.cohort = cohort;
    }

    public void setItsKey(final long itsKey) {
        this.itsKey = itsKey;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    @Column(name = "_efk_itsbank")
    public long getKey() {
        return key;
    }

    @Column(name = "_efk_itskey")
    public long getItsKey() {
        return key;
    }

    @Column(name = "formid")
    public String getFormId() {
        return formId;
    }

    public String getLanguage() {
        return language;
    }

    @Id
    @Column(name = "_key")
    public String getFormKey() {
        return formKey;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }

    @Column(name = "updateconfig")
    public Long getUpdateVersion() {
        return updateVersion;
    }

    public String getCohort() {
        return cohort;
    }
}
