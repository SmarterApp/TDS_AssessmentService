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
import java.util.Objects;

@Entity
@Table(name = "client_language", catalog = "configs")
public class Language {
    private LanguageIdentity languageIdentity;
    private String languageLabel;

    /**
     * Private constructor for frameworks
     */
    private Language() {
    }

    public Language(final String clientName, final String languageCode, final String languageLabel) {
        this.languageIdentity = new LanguageIdentity(clientName, languageCode);
        this.languageLabel = languageLabel;
    }

    @EmbeddedId
    public LanguageIdentity getLanguageIdentity() {
        return languageIdentity;
    }

    @Column(name = "language")
    public String getLanguageLabel() {
        return languageLabel;
    }

    private void setLanguageIdentity(final LanguageIdentity languageIdentity) {
        this.languageIdentity = languageIdentity;
    }

    private void setLanguageLabel(final String languageLabel) {
        this.languageLabel = languageLabel;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Language language = (Language) o;
        return Objects.equals(languageIdentity, language.languageIdentity) &&
            Objects.equals(languageLabel, language.languageLabel);
    }

    @Override
    public int hashCode() {

        return Objects.hash(languageIdentity, languageLabel);
    }
}
