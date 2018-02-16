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
public class GradeIdentity implements Serializable {
    private String gradeCode;
    private String clientName;

    /**
     * Private constructor for frameworks
     */
    private GradeIdentity() {
    }

    GradeIdentity(final String gradeCode, final String clientName) {
        this.gradeCode = gradeCode;
        this.clientName = clientName;
    }

    @Column(name = "gradecode")
    public String getGradeCode() {
        return gradeCode;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    public void setGradeCode(final String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GradeIdentity that = (GradeIdentity) o;

        if (!gradeCode.equals(that.gradeCode)) return false;
        return clientName.equals(that.clientName);
    }

    @Override
    public int hashCode() {
        int result = gradeCode.hashCode();
        result = 31 * result + clientName.hashCode();
        return result;
    }
}
