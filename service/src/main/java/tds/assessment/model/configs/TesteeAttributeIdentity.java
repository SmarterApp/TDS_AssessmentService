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

@Embeddable
public class TesteeAttributeIdentity implements Serializable {
    private String tdsAttributeId;
    private String clientName;

    /**
     * Private constructor for frameworks
     */
    private TesteeAttributeIdentity() {
    }

    TesteeAttributeIdentity(final String tdsAttributeId, final String clientName) {
        this.tdsAttributeId = tdsAttributeId;
        this.clientName = clientName;
    }

    @Column(name = "tds_id")
    public String getTdsAttributeId() {
        return tdsAttributeId;
    }

    public void setTdsAttributeId(final String tdsAttributeId) {
        this.tdsAttributeId = tdsAttributeId;
    }

    @Column(name = "clientname")
    private String getClientName() {
        return clientName;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TesteeAttributeIdentity that = (TesteeAttributeIdentity) o;

        if (!tdsAttributeId.equals(that.tdsAttributeId)) return false;
        return clientName.equals(that.clientName);
    }

    @Override
    public int hashCode() {
        int result = tdsAttributeId.hashCode();
        result = 31 * result + clientName.hashCode();
        return result;
    }
}
