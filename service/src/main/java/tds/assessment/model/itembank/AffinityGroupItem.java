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

/**
 * An item belonging to a specific {@link tds.assessment.model.itembank.AffinityGroup}
 */
@Entity
@Table(name = "affinitygroupitem", catalog = "itembank")
public class AffinityGroupItem {
    private AffinityGroupItemIdentity affinityGroupItemIdentity;


    /**
     * Empty constructor for frameworks
     */
    private AffinityGroupItem() {
    }

    public AffinityGroupItem(final String segmentKey, final String affinityGroupId, final String itemId) {
        this.affinityGroupItemIdentity = new AffinityGroupItemIdentity(segmentKey, affinityGroupId, itemId);
    }

    public void setAffinityGroupItemIdentity(final AffinityGroupItemIdentity affinityGroupItemIdentity) {
        this.affinityGroupItemIdentity = affinityGroupItemIdentity;
    }

    @EmbeddedId
    public AffinityGroupItemIdentity getAffinityGroupItemIdentity() {
        return affinityGroupItemIdentity;
    }
}
