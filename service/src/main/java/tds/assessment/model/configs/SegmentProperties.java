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

package tds.assessment.model.configs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client_segmentproperties", catalog = "configs")
public class SegmentProperties {
    private SegmentPropertiesIdentity segmentPropertiesIdentity;
    private int permeable;
    private boolean entryApprovalRequired;
    private boolean exitApprovalRequired;
    private boolean itemReviewRequired;
    private int position;
    private String label;
    private String assessmentKey;

    private SegmentProperties() {
    }


    public static final class Builder {
        private SegmentPropertiesIdentity segmentPropertiesIdentity;
        private int permeable;
        private boolean entryApprovalRequired;
        private boolean exitApprovalRequired;
        private boolean itemReviewRequired;
        private int position;
        private String label;
        private String assessmentKey;

        public Builder(final String clientName, final String assessmentId, final String segmentId) {
            this.segmentPropertiesIdentity = new SegmentPropertiesIdentity(clientName, assessmentId, segmentId);
        }

        public Builder withPermeable(int permeable) {
            this.permeable = permeable;
            return this;
        }

        public Builder withEntryApprovalRequired(boolean entryApprovalRequired) {
            this.entryApprovalRequired = entryApprovalRequired;
            return this;
        }

        public Builder withExitApprovalRequired(boolean exitApprovalRequired) {
            this.exitApprovalRequired = exitApprovalRequired;
            return this;
        }

        public Builder withItemReviewRequired(boolean itemReviewRequired) {
            this.itemReviewRequired = itemReviewRequired;
            return this;
        }

        public Builder withPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withAssessmentKey(String assessmentKey) {
            this.assessmentKey = assessmentKey;
            return this;
        }

        public SegmentProperties build() {
            SegmentProperties segmentProperties = new SegmentProperties();
            segmentProperties.assessmentKey = this.assessmentKey;
            segmentProperties.itemReviewRequired = this.itemReviewRequired;
            segmentProperties.label = this.label;
            segmentProperties.segmentPropertiesIdentity = this.segmentPropertiesIdentity;
            segmentProperties.position = this.position;
            segmentProperties.permeable = this.permeable;
            segmentProperties.entryApprovalRequired = this.entryApprovalRequired;
            segmentProperties.exitApprovalRequired = this.exitApprovalRequired;
            return segmentProperties;
        }
    }

    @EmbeddedId
    public SegmentPropertiesIdentity getSegmentPropertiesIdentity() {
        return segmentPropertiesIdentity;
    }

    @Column(name = "ispermeable")
    public int isPermeable() {
        return permeable;
    }

    @Column(name = "entryapproval", columnDefinition = "INT")
    public boolean isEntryApprovalRequired() {
        return entryApprovalRequired;
    }

    @Column(name = "exitapproval", columnDefinition = "INT")
    public boolean isExitApprovalRequired() {
        return exitApprovalRequired;
    }

    @Column(name = "itemreview")
    public boolean isItemReviewRequired() {
        return itemReviewRequired;
    }

    @Column(name = "segmentposition")
    public int getPosition() {
        return position;
    }

    public String getLabel() {
        return label;
    }

    @Column(name = "modekey")
    public String getAssessmentKey() {
        return assessmentKey;
    }

    private void setSegmentPropertiesIdentity(final SegmentPropertiesIdentity segmentPropertiesIdentity) {
        this.segmentPropertiesIdentity = segmentPropertiesIdentity;
    }

    private void setPermeable(final int permeable) {
        this.permeable = permeable;
    }

    private void setEntryApprovalRequired(final boolean entryApprovalRequired) {
        this.entryApprovalRequired = entryApprovalRequired;
    }

    private void setExitApprovalRequired(final boolean exitApprovalRequired) {
        this.exitApprovalRequired = exitApprovalRequired;
    }

    private void setItemReviewRequired(final boolean itemReviewRequired) {
        this.itemReviewRequired = itemReviewRequired;
    }

    private void setPosition(final int position) {
        this.position = position;
    }

    private void setLabel(final String label) {
        this.label = label;
    }

    private void setAssessmentKey(final String assessmentKey) {
        this.assessmentKey = assessmentKey;
    }
}
