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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A row representing a form cohort - used for linking certain forms to other forms during multi-segmented fixed-form assessments
 */
@Entity
@Table(name = "testcohort", catalog = "itembank")
public class TestCohort {
    private static final float DEFAULT_ITEM_RATIO = 1.0F;
    private TestCohortIdentity testCohortIdentity;
    private float itemRatio;

    /**
     * Empty constructor for frameworks
     */
    private TestCohort() {
    }

    public TestCohort(final String segmentKey) {
        this.testCohortIdentity = new TestCohortIdentity(segmentKey);
        this.itemRatio = DEFAULT_ITEM_RATIO;
    }

    public void setTestCohortIdentity(final TestCohortIdentity testCohortIdentity) {
        this.testCohortIdentity = testCohortIdentity;
    }

    public void setItemRatio(final float itemRatio) {
        this.itemRatio = itemRatio;
    }

    @Column(name = "itemratio")
    public float getItemRatio() {
        return itemRatio;
    }

    @EmbeddedId
    public TestCohortIdentity getTestCohortIdentity() {
        return testCohortIdentity;
    }
}
