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
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Timestamp;

@Embeddable
public class TimeWindowIdentity implements Serializable {
    private static final String DEFAULT_WINDOW_ID = "ANNUAL";
    private String clientName;
    private String windowId;

    /**
     * Private constructor for frameworks
     */
    private TimeWindowIdentity() {
    }

    TimeWindowIdentity(final String clientName) {
        this.clientName = clientName;
        this.windowId = DEFAULT_WINDOW_ID;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "windowid")
    public String getWindowId() {
        return windowId;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setWindowId(final String windowId) {
        this.windowId = windowId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TimeWindowIdentity that = (TimeWindowIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        return windowId.equals(that.windowId);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + windowId.hashCode();
        return result;
    }
}
