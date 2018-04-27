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
public class AccommodationFamilyIdentity implements Serializable {
    private String clientName;
    private String family;

    /**
     * Private constructor for frameworks
     */
    private AccommodationFamilyIdentity() {
    }

    AccommodationFamilyIdentity(final String clientName, final String family) {
        this.clientName = clientName;
        this.family = family;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    public String getFamily() {
        return family;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setFamily(final String family) {
        this.family = family;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AccommodationFamilyIdentity that = (AccommodationFamilyIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        return family.equals(that.family);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + family.hashCode();
        return result;
    }
}
