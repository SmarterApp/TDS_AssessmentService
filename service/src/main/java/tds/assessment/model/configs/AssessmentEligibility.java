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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "client_testeligibility", catalog = "configs")
public class AssessmentEligibility {
    private UUID key;
    private String clientName;
    private String assessmentId;
    private String artId;
    private boolean disables;
    private String artValue;
    private long entityTypeKey;
    private String eligibilityTypeKey;

    private AssessmentEligibility() {
    }

    public static final class Builder {
        private UUID key;
        private String clientName;
        private String assessmentId;
        private String artId;
        private boolean disables;
        private String artValue;
        private long entityTypeKey;
        private String eligibilityTypeKey;

        public Builder() {
            this.key = UUID.randomUUID();
            this.disables = false;
        }

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
            return this;
        }

        public Builder withArtId(String artId) {
            this.artId = artId;
            return this;
        }

        public Builder withDisables(boolean disables) {
            this.disables = disables;
            return this;
        }

        public Builder withArtValue(String artValue) {
            this.artValue = artValue;
            return this;
        }

        public Builder withEntityTypeKey(long entityTypeKey) {
            this.entityTypeKey = entityTypeKey;
            return this;
        }

        public Builder withEligibilityTypeKey(String eligibilityTypeKey) {
            this.eligibilityTypeKey = eligibilityTypeKey;
            return this;
        }

        public AssessmentEligibility build() {
            AssessmentEligibility assessmentEligibility = new AssessmentEligibility();
            assessmentEligibility.eligibilityTypeKey = this.eligibilityTypeKey;
            assessmentEligibility.disables = this.disables;
            assessmentEligibility.key = this.key;
            assessmentEligibility.artValue = this.artValue;
            assessmentEligibility.assessmentId = this.assessmentId;
            assessmentEligibility.entityTypeKey = this.entityTypeKey;
            assessmentEligibility.clientName = this.clientName;
            assessmentEligibility.artId = this.artId;
            return assessmentEligibility;
        }
    }

    @Id
    @Column(name = "_key", columnDefinition = "VARBINARY(16)")
    public UUID getKey() {
        return key;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "testid")
    public String getAssessmentId() {
        return assessmentId;
    }

    @Column(name = "rtsname")
    public String getArtId() {
        return artId;
    }

    @Column(name = "disables")
    public boolean isDisables() {
        return disables;
    }

    @Column(name = "rtsvalue")
    public String getArtValue() {
        return artValue;
    }

    @Column(name = "_efk_entitytype")
    public long getEntityTypeKey() {
        return entityTypeKey;
    }

    @Column(name = "eligibilitytype")
    public String getEligibilityTypeKey() {
        return eligibilityTypeKey;
    }

    private void setKey(final UUID key) {
        this.key = key;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setAssessmentId(final String assessmentId) {
        this.assessmentId = assessmentId;
    }

    private void setArtId(final String artId) {
        this.artId = artId;
    }

    private void setDisables(final boolean disables) {
        this.disables = disables;
    }

    private void setArtValue(final String artValue) {
        this.artValue = artValue;
    }

    private void setEntityTypeKey(final long entityTypeKey) {
        this.entityTypeKey = entityTypeKey;
    }

    private void setEligibilityTypeKey(final String eligibilityTypeKey) {
        this.eligibilityTypeKey = eligibilityTypeKey;
    }


}
