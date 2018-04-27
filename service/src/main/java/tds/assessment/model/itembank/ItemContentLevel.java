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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "aa_itemcl", catalog = "itembank")
public class ItemContentLevel {
    private ItemContentLevelIdentity itemContentLevelIdentity;

    /**
     * Empty constructor for frameworks
     */
    private ItemContentLevel() {
    }

    public ItemContentLevel(final String segmentKey, final String itemId, final String contentLevel) {
        this.itemContentLevelIdentity = new ItemContentLevelIdentity(segmentKey, itemId, contentLevel);
    }

    public void setItemContentLevelIdentity(final ItemContentLevelIdentity itemContentLevelIdentity) {
        this.itemContentLevelIdentity = itemContentLevelIdentity;
    }

    @EmbeddedId
    public ItemContentLevelIdentity getItemContentLevelIdentity() {
        return itemContentLevelIdentity;
    }
}
