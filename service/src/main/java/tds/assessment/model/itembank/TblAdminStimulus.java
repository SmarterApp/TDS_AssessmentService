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
@Table(name = "tbladminstimulus", schema = "itembank")
public class TblAdminStimulus {
    private TblAdminStimulusIdentifier tblAdminStimulusIdentifier;
    private int numItemsRequired;
    private int maxItems;
    private Long version;
    private Long updateVersion;
    private String groupId;

    public static final class Builder {
        private TblAdminStimulusIdentifier tblAdminStimulusIdentifier;
        private int numItemsRequired;
        private int maxItems;
        private Long version;
        private Long updateVersion;
        private String groupId;

        public Builder(String stimulusKey, String segmentKey) {
            this.tblAdminStimulusIdentifier = new TblAdminStimulusIdentifier(stimulusKey, segmentKey);
        }

        public Builder withNumItemsRequired(int numItemsRequired) {
            this.numItemsRequired = numItemsRequired;
            return this;
        }

        public Builder withMaxItems(int maxItems) {
            this.maxItems = maxItems;
            return this;
        }

        public Builder withVersion(Long version) {
            this.version = version;
            return this;
        }

        public Builder withUpdateVersion(Long updateVersion) {
            this.updateVersion = updateVersion;
            return this;
        }

        public Builder withGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public TblAdminStimulus build() {
            TblAdminStimulus tblAdminStimulus = new TblAdminStimulus();
            tblAdminStimulus.maxItems = this.maxItems;
            tblAdminStimulus.numItemsRequired = this.numItemsRequired;
            tblAdminStimulus.tblAdminStimulusIdentifier = this.tblAdminStimulusIdentifier;
            tblAdminStimulus.groupId = this.groupId;
            tblAdminStimulus.version = this.version;
            tblAdminStimulus.updateVersion = this.updateVersion;
            return tblAdminStimulus;
        }
    }

    @EmbeddedId
    public TblAdminStimulusIdentifier getTblAdminStimulusIdentifier() {
        return tblAdminStimulusIdentifier;
    }

    @Column(name = "numitemsrequired")
    public int getNumItemsRequired() {
        return numItemsRequired;
    }

    @Column(name = "maxitems")
    public int getMaxItems() {
        return maxItems;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }

    @Column(name = "updateconfig")
    public Long getUpdateVersion() {
        return updateVersion;
    }

    @Column(name = "groupid")
    public String getGroupId() {
        return groupId;
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setTblAdminStimulusIdentifier(final TblAdminStimulusIdentifier tblAdminStimulusIdentifier) {
        this.tblAdminStimulusIdentifier = tblAdminStimulusIdentifier;
    }

    private void setNumItemsRequired(final int numItemsRequired) {
        this.numItemsRequired = numItemsRequired;
    }

    private void setMaxItems(final int maxItems) {
        this.maxItems = maxItems;
    }

    private void setVersion(final Long version) {
        this.version = version;
    }

    private void setUpdateVersion(final Long updateVersion) {
        this.updateVersion = updateVersion;
    }

    private void setGroupId(final String groupId) {
        this.groupId = groupId;
    }
}
