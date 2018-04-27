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
@Table(name = "client_testformproperties", catalog = "configs")
public class AssessmentFormProperties {
    private AssessmentFormPropertiesIdentity assessmentFormPropertiesIdentity;
    private String languageCode;
    private String formId;
    private String segmentId;
    private String segmentKey;

    /**
     * Private constructor for frameworks
     */
    private AssessmentFormProperties() {
    }

    public static final class Builder {
        private AssessmentFormPropertiesIdentity assessmentFormPropertiesIdentity;
        private String languageCode;
        private String formId;
        private String segmentId;
        private String segmentKey;

        public Builder(final String clientName, final String formKey) {
            this.assessmentFormPropertiesIdentity = new AssessmentFormPropertiesIdentity(clientName, formKey);
        }

        public Builder withLanguageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public Builder withFormId(String formId) {
            this.formId = formId;
            return this;
        }

        public Builder withSegmentId(String segmentId) {
            this.segmentId = segmentId;
            return this;
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
            return this;
        }

        public AssessmentFormProperties build() {
            AssessmentFormProperties assessmentFormProperties = new AssessmentFormProperties();
            assessmentFormProperties.formId = this.formId;
            assessmentFormProperties.languageCode = this.languageCode;
            assessmentFormProperties.assessmentFormPropertiesIdentity = this.assessmentFormPropertiesIdentity;
            assessmentFormProperties.segmentKey = this.segmentKey;
            assessmentFormProperties.segmentId = this.segmentId;
            return assessmentFormProperties;
        }
    }

    @EmbeddedId
    public AssessmentFormPropertiesIdentity getAssessmentFormPropertiesIdentity() {
        return assessmentFormPropertiesIdentity;
    }

    @Column(name = "language")
    public String getLanguageCode() {
        return languageCode;
    }

    @Column(name = "formid")
    public String getFormId() {
        return formId;
    }

    @Column(name = "testid")
    public String getSegmentId() {
        return segmentId;
    }

    @Column(name = "testkey")
    public String getSegmentKey() {
        return segmentKey;
    }

    /*
        Do not use these setters - use the builder instead
     */
    private void setAssessmentFormPropertiesIdentity(final AssessmentFormPropertiesIdentity assessmentFormPropertiesIdentity) {
        this.assessmentFormPropertiesIdentity = assessmentFormPropertiesIdentity;
    }

    private void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }

    private void setFormId(final String formId) {
        this.formId = formId;
    }

    private void setSegmentId(final String segmentId) {
        this.segmentId = segmentId;
    }

    private void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }
}
