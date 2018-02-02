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
public class TblSetOfItemStrandIdentity implements Serializable {
    @NotNull
    private String itemId;

    @NotNull
    private String strandKey;

    @NotNull
    private String segmentKey;

    /**
     * Empty constructor for frameworks
     */
    private TblSetOfItemStrandIdentity() {
    }

    public TblSetOfItemStrandIdentity(final String itemId, final String strandKey, final String segmentKey) {
        this.itemId = itemId;
        this.strandKey = strandKey;
        this.segmentKey = segmentKey;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public void setStrandKey(final String strandKey) {
        this.strandKey = strandKey;
    }

    public void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    @Column(name = "_fk_item")
    public String getItemId() {
        return itemId;
    }

    @Column(name = "_fk_strand")
    public String getStrandKey() {
        return strandKey;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TblSetOfItemStrandIdentity that = (TblSetOfItemStrandIdentity) o;

        if (!itemId.equals(that.itemId)) return false;
        if (!strandKey.equals(that.strandKey)) return false;
        return segmentKey.equals(that.segmentKey);
    }

    @Override
    public int hashCode() {
        int result = itemId.hashCode();
        result = 31 * result + strandKey.hashCode();
        result = 31 * result + segmentKey.hashCode();
        return result;
    }
}
