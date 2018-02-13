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
@Table(name = "client_testproperties", catalog = "configs")
public class AssessmentProperties {
    private static final int DEFAULT_MAX_OPPORTUNITIES = 3;
    private AssessmentPropertiesIdentity assessmentPropertiesIdentity;
    private int maxOpportunities;
    private String label;
    private boolean scoreByTds;
    private String subjectName;
    private String accommodationFamily;
    private String gradeLabel;

    /**
     * Private constructor for frameworks
     */
    private AssessmentProperties() {
    }


    public static final class Builder {
        private AssessmentPropertiesIdentity assessmentPropertiesIdentity;
        private int maxOpportunities;
        private String label;
        private boolean scoreByTds;
        private String subjectName;
        private String gradeLabel;

        public Builder(final String clientName, final String assessmentId) {
            this.assessmentPropertiesIdentity = new AssessmentPropertiesIdentity(clientName, assessmentId);
            this.maxOpportunities = DEFAULT_MAX_OPPORTUNITIES;
        }

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withScoreByTds(boolean scoreByTds) {
            this.scoreByTds = scoreByTds;
            return this;
        }

        public Builder withSubjectName(String subjectName) {
            this.subjectName = subjectName;
            return this;
        }

        public Builder withGradeLabel(String gradeLabel) {
            this.gradeLabel = gradeLabel;
            return this;
        }

        public AssessmentProperties build() {
            AssessmentProperties assessmentProperties = new AssessmentProperties();
            assessmentProperties.scoreByTds = this.scoreByTds;
            assessmentProperties.subjectName = this.subjectName;
            assessmentProperties.label = this.label;
            assessmentProperties.gradeLabel = this.gradeLabel;
            assessmentProperties.assessmentPropertiesIdentity = this.assessmentPropertiesIdentity;
            assessmentProperties.maxOpportunities = this.maxOpportunities;
            assessmentProperties.accommodationFamily = subjectName; // accommodation family always == subjectCode
            return assessmentProperties;
        }
    }

    @EmbeddedId
    public AssessmentPropertiesIdentity getAssessmentPropertiesIdentity() {
        return assessmentPropertiesIdentity;
    }

    @Column(name = "maxopportunities")
    public int getMaxOpportunities() {
        return maxOpportunities;
    }

    public String getLabel() {
        return label;
    }

    @Column(name = "scorebytds")
    public boolean isScoreByTds() {
        return scoreByTds;
    }

    @Column(name = "subjectname")
    public String getSubjectName() {
        return subjectName;
    }

    @Column(name = "accommodationfamily")
    public String getAccommodationFamily() {
        return accommodationFamily;
    }

    @Column(name = "gradetext")
    public String getGradeLabel() {
        return gradeLabel;
    }

    /*
        Do not use the setters below - use the builder
     */
    private void setAssessmentPropertiesIdentity(final AssessmentPropertiesIdentity assessmentPropertiesIdentity) {
        this.assessmentPropertiesIdentity = assessmentPropertiesIdentity;
    }

    private void setMaxOpportunities(final int maxOpportunities) {
        this.maxOpportunities = maxOpportunities;
    }

    private void setLabel(final String label) {
        this.label = label;
    }

    private void setScoreByTds(final boolean scoreByTds) {
        this.scoreByTds = scoreByTds;
    }

    private void setSubjectName(final String subjectName) {
        this.subjectName = subjectName;
    }

    private void setAccommodationFamily(final String accommodationFamily) {
        this.accommodationFamily = accommodationFamily;
    }

    private void setGradeLabel(final String gradeLabel) {
        this.gradeLabel = gradeLabel;
    }
}
