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

@Entity
@Table(name = "client", catalog = "configs")
public class Client {
    private String name;
    private boolean internationalize;
    private String defaultLanguage;

    /**
     * Private constructor for frameworks
     */
    private Client() {
    }

    public Client(final String name) {
        this.name = name;
        this.internationalize = true;
        this.defaultLanguage = "ENU";
    }

    @Id
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isInternationalize() {
        return internationalize;
    }

    public void setInternationalize(final boolean internationalize) {
        this.internationalize = internationalize;
    }

    @Column(name = "defaultlanguage")
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(final String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}
