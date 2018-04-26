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
public class LanguageIdentity implements Serializable {
    private String clientName;
    private String languageCode;

    /**
     * Private constructor for frameworks
     */
    private LanguageIdentity() {
    }

    LanguageIdentity(final String clientName, final String languageCode) {
        this.clientName = clientName;
        this.languageCode = languageCode;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "languagecode")
    public String getLanguageCode() {
        return languageCode;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setLanguageCode(final String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final LanguageIdentity that = (LanguageIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        return languageCode.equals(that.languageCode);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + languageCode.hashCode();
        return result;
    }
}
