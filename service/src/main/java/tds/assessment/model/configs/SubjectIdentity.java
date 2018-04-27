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
public class SubjectIdentity implements Serializable {
    private String subject;
    private String clientName;

    /**
     * Private constructor for frameworks
     */
    private SubjectIdentity() {
    }

    SubjectIdentity(final String subject, final String clientName) {
        this.subject = subject;
        this.clientName = clientName;
    }

    public String getSubject() {
        return subject;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SubjectIdentity that = (SubjectIdentity) o;

        if (!subject.equals(that.subject)) return false;
        return clientName.equals(that.clientName);
    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + clientName.hashCode();
        return result;
    }
}
