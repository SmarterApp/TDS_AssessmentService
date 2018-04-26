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
public class AssessmentFormPropertiesIdentity implements Serializable {
    private String clientName;
    private String formKey;

    /**
     * Private constructor for frameworks
     */
    private AssessmentFormPropertiesIdentity() {
    }

    AssessmentFormPropertiesIdentity(final String clientName, final String formKey) {
        this.clientName = clientName;
        this.formKey = formKey;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "_efk_testform")
    public String getFormKey() {
        return formKey;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setFormKey(final String formKey) {
        this.formKey = formKey;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AssessmentFormPropertiesIdentity that = (AssessmentFormPropertiesIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        return formKey.equals(that.formKey);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + formKey.hashCode();
        return result;
    }
}
