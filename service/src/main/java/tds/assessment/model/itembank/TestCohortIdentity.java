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

package tds.assessment.model.itembank;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class TestCohortIdentity implements Serializable {

    public static final int DEFAULT_COHORT = 1;
    private int cohort; //Unused as default is always provided - only here for Hibernate compatibility

    @NotNull
    private String segmentKey;

    /**
     * Empty constructor for frameworks
     */
    private TestCohortIdentity() {
    }

    TestCohortIdentity(final String segmentKey) {
        this.segmentKey = segmentKey;
        cohort = DEFAULT_COHORT;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    public void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    public int getCohort() {
        return cohort;
    }

    public void setCohort(final int cohort) {
        this.cohort = cohort;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TestCohortIdentity that = (TestCohortIdentity) o;

        return segmentKey.equals(that.segmentKey);
    }

    @Override
    public int hashCode() {
        int result = cohort;
        result = 31 * result + (segmentKey != null ? segmentKey.hashCode() : 0);
        return result;
    }
}
