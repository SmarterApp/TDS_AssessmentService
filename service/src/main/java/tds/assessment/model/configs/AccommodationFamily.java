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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client_accommodationfamily", catalog = "configs")
public class AccommodationFamily {
    private AccommodationFamilyIdentity accommodationFamilyIdentity;
    private String label;

    /**
     * Private constructor for frameworks
     */
    private AccommodationFamily() {
    }

    public AccommodationFamily(final String clientName, final String family) {
        this.accommodationFamilyIdentity = new AccommodationFamilyIdentity(clientName, family);
        this.label = family; // label always == family
    }

    @EmbeddedId
    public AccommodationFamilyIdentity getAccommodationFamilyIdentity() {
        return accommodationFamilyIdentity;
    }

    public String getLabel() {
        return label;
    }

    private void setAccommodationFamilyIdentity(final AccommodationFamilyIdentity accommodationFamilyIdentity) {
        this.accommodationFamilyIdentity = accommodationFamilyIdentity;
    }

    private void setLabel(final String label) {
        this.label = label;
    }
}
