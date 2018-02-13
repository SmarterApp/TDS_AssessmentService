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
@Table(name = "tblsetofitemstimuli", catalog = "itembank")
public class TblSetOfItemStimulus {
    private TblSetOfItemStimulusIdentity tblSetOfItemStimulusIdentity;
    private Long version;

    /**
     * private constructor for frameworks
     */
    private TblSetOfItemStimulus() {}

    public TblSetOfItemStimulus(final String itemKey, final String stimulusKey, final String segmentKey, final Long version) {
        this.tblSetOfItemStimulusIdentity = new TblSetOfItemStimulusIdentity(itemKey, stimulusKey, segmentKey);
        this.version = version;
    }

    @EmbeddedId
    public TblSetOfItemStimulusIdentity getTblSetOfItemStimulusIdentity() {
        return tblSetOfItemStimulusIdentity;
    }

    public void setTblSetOfItemStimulusIdentity(final TblSetOfItemStimulusIdentity tblSetOfItemStimulusIdentity) {
        this.tblSetOfItemStimulusIdentity = tblSetOfItemStimulusIdentity;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
