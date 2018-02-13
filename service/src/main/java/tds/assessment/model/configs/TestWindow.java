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
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "client_testwindow", catalog = "configs")
public class TestWindow {
    private static final int DEFAULT_MAX_OPPORTUNITIES = 3;
    private static final String DEFAULT_WINDOW_ID = "ANNUAL";

    private UUID key;
    private String clientName;
    private String assessmentId;
    private int numberOfOpportunities;
    private Timestamp startDate;
    private Timestamp endDate;
    private String windowId;

    private TestWindow() {
    }

    public static final class Builder {
        private UUID key;
        private String clientName;
        private String assessmentId;
        private int numberOfOpportunities;
        private Timestamp startDate;
        private Timestamp endDate;
        private String windowId;

        public Builder() {
            this.key = UUID.randomUUID();
            this.numberOfOpportunities = DEFAULT_MAX_OPPORTUNITIES;
            this.windowId = DEFAULT_WINDOW_ID;
        }

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
            return this;
        }

        public Builder withNumberOfOpportunities(int numberOfOpportunities) {
            this.numberOfOpportunities = numberOfOpportunities;
            return this;
        }

        public Builder withStartDate(Timestamp startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(Timestamp endDate) {
            this.endDate = endDate;
            return this;
        }

        public TestWindow build() {
            TestWindow testWindow = new TestWindow();
            testWindow.assessmentId = this.assessmentId;
            testWindow.endDate = this.endDate;
            testWindow.numberOfOpportunities = this.numberOfOpportunities;
            testWindow.clientName = this.clientName;
            testWindow.startDate = this.startDate;
            testWindow.key = this.key;
            testWindow.windowId = this.windowId;
            return testWindow;
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

    @Column(name = "numopps")
    public int getNumberOfOpportunities() {
        return numberOfOpportunities;
    }

    @Column(name = "startdate")
    public Timestamp getStartDate() {
        return startDate;
    }

    @Column(name = "enddate")
    public Timestamp getEndDate() {
        return endDate;
    }

    @Column(name = "windowid")
    public String getWindowId() {
        return windowId;
    }

    /*
        Do not use these setters - use the builder instead
     */
    private void setKey(final UUID key) {
        this.key = key;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setAssessmentId(final String assessmentId) {
        this.assessmentId = assessmentId;
    }

    private void setNumberOfOpportunities(final int numberOfOpportunities) {
        this.numberOfOpportunities = numberOfOpportunities;
    }

    private void setStartDate(final Timestamp startDate) {
        this.startDate = startDate;
    }

    private void setEndDate(final Timestamp endDate) {
        this.endDate = endDate;
    }

    private void setWindowId(final String windowId) {
        this.windowId = windowId;
    }
}
