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
import java.sql.Timestamp;

@Entity
@Table(name = "client_timewindow", catalog = "configs")
public class TimeWindow {
    private TimeWindowIdentity timeWindowIdentity;
    private Timestamp startDate;
    private Timestamp endDate;

    /**
     * Private constructor for frameworks
     */
    private TimeWindow() {
    }

    public TimeWindow(final String clientName, final Timestamp startDate, final Timestamp endDate) {
        this.timeWindowIdentity = new TimeWindowIdentity(clientName);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @EmbeddedId
    public TimeWindowIdentity getTimeWindowIdentity() {
        return timeWindowIdentity;
    }

    @Column(name = "startdate")
    public Timestamp getStartDate() {
        return startDate;
    }

    @Column(name = "enddate")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setTimeWindowIdentity(final TimeWindowIdentity timeWindowIdentity) {
        this.timeWindowIdentity = timeWindowIdentity;
    }

    public void setStartDate(final Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(final Timestamp endDate) {
        this.endDate = endDate;
    }
}
