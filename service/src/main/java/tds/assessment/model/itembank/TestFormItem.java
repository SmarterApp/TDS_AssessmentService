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
 * A row representing an item in a {@link tds.assessment.model.itembank.TestForm}
 */
@Entity
@Table(name = "testformitem", catalog = "itembank")
public class TestFormItem {
    private TestFormItemIdentity testFormItemIdentity;
    private long formItsKey;
    private boolean active;

    /**
     * Empty constructor for frameworks
     */
    private TestFormItem() {
    }

    public static final class Builder {
        private TestFormItemIdentity testFormItemIdentity;
        private long formItsKey;
        private boolean active;

        public Builder(final int itemPosition, final String segmentKey, final String itemId, final String formKey) {
            this.testFormItemIdentity = new TestFormItemIdentity(itemPosition, segmentKey, itemId, formKey);
        }

        public Builder withFormItsKey(long formItsKey) {
            this.formItsKey = formItsKey;
            return this;
        }

        public Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public TestFormItem build() {
            TestFormItem testFormItem = new TestFormItem();
            testFormItem.formItsKey = this.formItsKey;
            testFormItem.testFormItemIdentity = this.testFormItemIdentity;
            testFormItem.active = this.active;
            return testFormItem;
        }
    }

    public void setTestFormItemIdentity(final TestFormItemIdentity testFormItemIdentity) {
        this.testFormItemIdentity = testFormItemIdentity;
    }

    public void setFormItsKey(final long formItsKey) {
        this.formItsKey = formItsKey;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    @EmbeddedId
    public TestFormItemIdentity getTestFormItemIdentity() {
        return testFormItemIdentity;
    }

    @Column(name = "_efk_itsformkey")
    public long getFormItsKey() {
        return formItsKey;
    }

    @Column(name = "isactive")
    public boolean isActive() {
        return active;
    }
}
