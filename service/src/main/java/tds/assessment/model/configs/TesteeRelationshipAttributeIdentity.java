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
import java.util.Objects;

@Embeddable
public class TesteeRelationshipAttributeIdentity implements Serializable {
    private String clientName;
    private String tdsId;

    /**
     * Private constructor for frameworks
     */
    private TesteeRelationshipAttributeIdentity() {
    }

    TesteeRelationshipAttributeIdentity(final String clientName, final String tdsId) {
        this.clientName = clientName;
        this.tdsId = tdsId;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "tds_id")
    public String getTdsId() {
        return tdsId;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setTdsId(final String tdsId) {
        this.tdsId = tdsId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TesteeRelationshipAttributeIdentity that = (TesteeRelationshipAttributeIdentity) o;
        return Objects.equals(clientName, that.clientName) &&
            Objects.equals(tdsId, that.tdsId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientName, tdsId);
    }
}
