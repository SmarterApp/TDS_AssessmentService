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
@Table(name = "client_testmode", catalog = "configs")
public class TestMode {
    private static final String DEFAULT_MODE = "online";
    private static final int DEFAULT_SESSION_TYPE = 0;

    private UUID key;
    private String clientName;
    private String assessmentId;
    private String mode;
    private String algorithm;
    private boolean segmented;
    private int sessionType;
    private String assessmentKey;

    /**
     * Private constructor for frameworks
     */
    private TestMode() {
    }


    public static final class Builder {
        private UUID key;
        private String clientName;
        private String assessmentId;
        private String mode;
        private String algorithm;
        private boolean segmented;
        private int sessionType;
        private String assessmentKey;

        public Builder() {
            this.key = UUID.randomUUID();
            this.mode = DEFAULT_MODE;
            this.sessionType = DEFAULT_SESSION_TYPE;
        }

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
            return this;
        }

        public Builder withAlgorithm(String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder withSegmented(boolean segmented) {
            this.segmented = segmented;
            return this;
        }

        public Builder withAssessmentKey(String assessmentKey) {
            this.assessmentKey = assessmentKey;
            return this;
        }

        public TestMode build() {
            TestMode testMode = new TestMode();
            testMode.clientName = this.clientName;
            testMode.assessmentId = this.assessmentId;
            testMode.key = this.key;
            testMode.algorithm = this.algorithm;
            testMode.sessionType = this.sessionType;
            testMode.segmented = this.segmented;
            testMode.mode = this.mode;
            testMode.assessmentKey = this.assessmentKey;
            return testMode;
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

    public String getMode() {
        return mode;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    @Column(name = "issegmented")
    public boolean isSegmented() {
        return segmented;
    }

    @Column(name = "sessiontype")
    public int getSessionType() {
        return sessionType;
    }

    @Column(name = "testkey")
    public String getAssessmentKey() {
        return assessmentKey;
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

    private void setMode(final String mode) {
        this.mode = mode;
    }

    private void setAlgorithm(final String algorithm) {
        this.algorithm = algorithm;
    }

    private void setSegmented(final boolean segmented) {
        this.segmented = segmented;
    }

    private void setSessionType(final int sessionType) {
        this.sessionType = sessionType;
    }

    private void setAssessmentKey(final String assessmentKey) {
        this.assessmentKey = assessmentKey;
    }
}
