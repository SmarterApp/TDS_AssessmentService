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
public class TestModeIdentity implements Serializable{
    private String assessmentKey;
    private int sessionType;

    private TestModeIdentity() {
    }

    public TestModeIdentity(final String assessmentKey, final int sessionType) {
        this.assessmentKey = assessmentKey;
        this.sessionType = sessionType;
    }


    @Column(name = "sessiontype")
    public int getSessionType() {
        return sessionType;
    }

    @Column(name = "testkey")
    public String getAssessmentKey() {
        return assessmentKey;
    }


    private void setSessionType(final int sessionType) {
        this.sessionType = sessionType;
    }

    private void setAssessmentKey(final String assessmentKey) {
        this.assessmentKey = assessmentKey;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TestModeIdentity that = (TestModeIdentity) o;
        return sessionType == that.sessionType &&
            Objects.equals(assessmentKey, that.assessmentKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(assessmentKey, sessionType);
    }
}
