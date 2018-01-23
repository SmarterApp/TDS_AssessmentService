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
public class TestFormItemIdentity implements Serializable {
    private int itemPosition;

    @NotNull
    private String segmentKey;

    @NotNull
    private String itemId;

    @NotNull
    private String formKey;

    public TestFormItemIdentity(final int itemPosition, final String segmentKey, final String itemId, final String formKey) {
        this.itemPosition = itemPosition;
        this.segmentKey = segmentKey;
        this.itemId = itemId;
        this.formKey = formKey;
    }

    public void setItemPosition(final int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public void setFormKey(final String formKey) {
        this.formKey = formKey;
    }

    @Column(name = "_fk_item")
    public String getItemId() {
        return itemId;
    }

    @Column(name = "formposition")
    public int getItemPosition() {
        return itemPosition;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    @Column(name = "_fk_testform")
    public String getFormKey() {
        return formKey;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TestFormItemIdentity that = (TestFormItemIdentity) o;

        if (itemPosition != that.itemPosition) return false;
        if (!segmentKey.equals(that.segmentKey)) return false;
        if (!itemId.equals(that.itemId)) return false;
        return formKey.equals(that.formKey);
    }

    @Override
    public int hashCode() {
        int result = itemPosition;
        result = 31 * result + segmentKey.hashCode();
        result = 31 * result + itemId.hashCode();
        result = 31 * result + formKey.hashCode();
        return result;
    }
}
