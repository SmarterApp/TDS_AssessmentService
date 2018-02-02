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
 * A row representing generic item metadata (not linked to any specific segment or assessment)
 */
@Entity
@Table(name = "tblitemprops", schema = "itembank")
public class TblItemProperty {
    private TblItemPropertyIdentity tblItemPropertyIdentity;
    private boolean active;

    /**
     * Empty constructor for frameworks
     */
    private TblItemProperty() {
    }

    public TblItemProperty(final String itemId, final String name, final String value, final String segmentKey) {
        this.tblItemPropertyIdentity = new TblItemPropertyIdentity(itemId, name, value, segmentKey);
        this.active = true;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public void setTblItemPropertyIdentity(final TblItemPropertyIdentity tblItemPropertyIdentity) {
        this.tblItemPropertyIdentity = tblItemPropertyIdentity;
    }

    @Column(name = "isactive")
    public boolean isActive() {
        return active;
    }

    @EmbeddedId
    public TblItemPropertyIdentity getTblItemPropertyIdentity() {
        return tblItemPropertyIdentity;
    }
}
