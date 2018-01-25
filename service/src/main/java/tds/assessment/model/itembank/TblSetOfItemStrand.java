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

@Entity
@Table(name = "tblsetofitemstrands")
public class TblSetOfItemStrand {
    private TblSetOfItemStrandIdentity tblSetOfItemStrandIdentity;
    private Long version;

    public TblSetOfItemStrand(final String itemId, final String strandKey, final String segmentKey, final Long version) {
        this.tblSetOfItemStrandIdentity = new TblSetOfItemStrandIdentity(itemId, strandKey, segmentKey);
        this.version = version;
    }

    public void setTblSetOfItemStrandIdentity(final TblSetOfItemStrandIdentity tblSetOfItemStrandIdentity) {
        this.tblSetOfItemStrandIdentity = tblSetOfItemStrandIdentity;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }

    @EmbeddedId
    public TblSetOfItemStrandIdentity getTblSetOfItemStrandIdentity() {
        return tblSetOfItemStrandIdentity;
    }
}
