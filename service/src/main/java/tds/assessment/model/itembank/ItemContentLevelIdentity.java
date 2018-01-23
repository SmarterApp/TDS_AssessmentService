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
public class ItemContentLevelIdentity implements Serializable {
    @NotNull
    private String segmentKey;

    @NotNull
    private String itemId;

    @NotNull
    private String contentLevel;

    public ItemContentLevelIdentity(final String segmentKey, final String itemId, final String contentLevel) {
        this.segmentKey = segmentKey;
        this.itemId = itemId;
        this.contentLevel = contentLevel;
    }

    public void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    private void setContentLevel(String contentLevel) {
        this.contentLevel = contentLevel;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    @Column(name = "_fk_item")
    public String getItemId() {
        return itemId;
    }

    public String getContentLevel() {
        return contentLevel;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemContentLevelIdentity that = (ItemContentLevelIdentity) o;

        if (!segmentKey.equals(that.segmentKey)) return false;
        if (!itemId.equals(that.itemId)) return false;
        return contentLevel.equals(that.contentLevel);
    }

    @Override
    public int hashCode() {
        int result = segmentKey.hashCode();
        result = 31 * result + itemId.hashCode();
        result = 31 * result + contentLevel.hashCode();
        return result;
    }
}
