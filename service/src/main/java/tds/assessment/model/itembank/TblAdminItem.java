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
@Table(name = "tblsetofadminsubjects", schema = "itembank")
public class TblAdminItem {
    private TblAdminItemIdentifier tblAdminItemIdentifier;
    private String groupKey;
    private String groupId;
    private int itemPosition;
    private boolean fieldTest;
    private boolean active;
    private String irtB;
    private String blockId;
    private boolean required;
    private boolean printable;
    private String clientName;
    private String responseMimeType;
    private int testCohort;
    private String strandKey;
    private String version;
    private String updatedVersion;
    private String claimName;
    private String irtA;
    private String irtC;
    private String irtModel;
    private String bVector;
    private boolean notForScoring;
    private String clString;
    private String irt2A;
    private String irt2B;
    private String irt2C;
    private String irt2Model;
    private double ftWeight;

    public static final class Builder {
        private TblAdminItemIdentifier tblAdminItemIdentifier;
        private String groupId;
        private int itemPosition;
        private boolean fieldTest;
        private boolean active;
        private String irtB;
        private String blockId;
        private boolean required;
        private boolean printable;
        private String clientName;
        private String responseMimeType;
        private int testCohort;
        private String strandKey;
        private String version;
        private String updatedVersion;
        private String claimName;
        private String irtA;
        private String irtC;
        private String irtModel;
        private String bVector;
        private boolean notForScoring;
        private String clString;
        private String irt2A;
        private String irt2B;
        private String irt2C;
        private String irt2Model;
        private double ftWeight;

        public Builder(String itemId, String segmentKey) {
            this.tblAdminItemIdentifier = new TblAdminItemIdentifier(itemId, segmentKey);
        }

        public Builder withGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
            return this;
        }

        public Builder withFieldTest(boolean fieldTest) {
            this.fieldTest = fieldTest;
            return this;
        }

        public Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder withIrtB(String irtB) {
            this.irtB = irtB;
            return this;
        }

        public Builder withBlockId(String blockId) {
            this.blockId = blockId;
            return this;
        }

        public Builder withRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder withPrintable(boolean printable) {
            this.printable = printable;
            return this;
        }

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withResponseMimeType(String responseMimeType) {
            this.responseMimeType = responseMimeType;
            return this;
        }

        public Builder withTestCohort(int testCohort) {
            this.testCohort = testCohort;
            return this;
        }

        public Builder withStrandKey(String strandKey) {
            this.strandKey = strandKey;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withUpdatedVersion(String updatedVersion) {
            this.updatedVersion = updatedVersion;
            return this;
        }

        public Builder withClaimName(String claimName) {
            this.claimName = claimName;
            return this;
        }

        public Builder withIrtA(String irtA) {
            this.irtA = irtA;
            return this;
        }

        public Builder withIrtC(String irtC) {
            this.irtC = irtC;
            return this;
        }

        public Builder withIrtModel(String irtModel) {
            this.irtModel = irtModel;
            return this;
        }

        public Builder withBVector(String bVector) {
            this.bVector = bVector;
            return this;
        }

        public Builder withNotForScoring(boolean notForScoring) {
            this.notForScoring = notForScoring;
            return this;
        }

        public Builder withClString(String clString) {
            this.clString = clString;
            return this;
        }

        public Builder withIrt2A(String irt2A) {
            this.irt2A = irt2A;
            return this;
        }

        public Builder withIrt2B(String irt2B) {
            this.irt2B = irt2B;
            return this;
        }

        public Builder withIrt2C(String irt2C) {
            this.irt2C = irt2C;
            return this;
        }

        public Builder withIrt2Model(String irt2Model) {
            this.irt2Model = irt2Model;
            return this;
        }

        public Builder withFtWeight(double ftWeight) {
            this.ftWeight = ftWeight;
            return this;
        }

        public TblAdminItem build() {
            TblAdminItem tblAdminItem = new TblAdminItem();
            tblAdminItem.tblAdminItemIdentifier = this.tblAdminItemIdentifier;
            tblAdminItem.updatedVersion = this.updatedVersion;
            tblAdminItem.irtA = this.irtA;
            tblAdminItem.irtC = this.irtC;
            tblAdminItem.clientName = this.clientName;
            tblAdminItem.bVector = this.bVector;
            tblAdminItem.ftWeight = this.ftWeight;
            tblAdminItem.irtModel = this.irtModel;
            tblAdminItem.notForScoring = this.notForScoring;
            tblAdminItem.responseMimeType = this.responseMimeType;
            tblAdminItem.groupId = this.groupId;
            tblAdminItem.irtB = this.irtB;
            tblAdminItem.irt2A = this.irt2A;
            tblAdminItem.claimName = this.claimName;
            tblAdminItem.printable = this.printable;
            tblAdminItem.fieldTest = this.fieldTest;
            tblAdminItem.version = this.version;
            tblAdminItem.irt2Model = this.irt2Model;
            tblAdminItem.itemPosition = this.itemPosition;
            tblAdminItem.clString = this.clString;
            tblAdminItem.strandKey = this.strandKey;
            tblAdminItem.required = this.required;
            tblAdminItem.irt2C = this.irt2C;
            tblAdminItem.testCohort = this.testCohort;
            tblAdminItem.active = this.active;
            tblAdminItem.blockId = this.blockId;
            tblAdminItem.irt2B = this.irt2B;
            tblAdminItem.groupKey = String.format("%s_%s", this.groupId, this.blockId);
            return tblAdminItem;
        }
    }

    @EmbeddedId
    public TblAdminItemIdentifier getTblAdminItemIdentifier() {
        return tblAdminItemIdentifier;
    }

    public String getGroupId() {
        return groupId;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    @Column(name = "isfieldtest")
    public boolean isFieldTest() {
        return fieldTest;
    }

    @Column(name = "isactive")
    public boolean isActive() {
        return active;
    }

    @Column(name = "irt_b")
    public String getIrtB() {
        return irtB;
    }

    public String getBlockId() {
        return blockId;
    }

    @Column(name = "isrequired")
    public boolean isRequired() {
        return required;
    }

    @Column(name = "isprintable")
    public boolean isPrintable() {
        return printable;
    }

    @Column(name = "_fk_testadmin")
    public String getClientName() {
        return clientName;
    }

    public String getResponseMimeType() {
        return responseMimeType;
    }

    public int getTestCohort() {
        return testCohort;
    }

    @Column(name = "_fk_strand")
    public String getStrandKey() {
        return strandKey;
    }

    @Column(name = "loadconfig")
    public String getVersion() {
        return version;
    }

    @Column(name = "updateconfig")
    public String getUpdatedVersion() {
        return updatedVersion;
    }

    public String getGroupKey() {
        return groupKey;
    }

    @Column(name = "strandname")
    public String getClaimName() {
        return claimName;
    }

    @Column(name = "irt_a")
    public String getIrtA() {
        return irtA;
    }

    @Column(name = "irt_c")
    public String getIrtC() {
        return irtC;
    }

    @Column(name = "irt_model")
    public String getIrtModel() {
        return irtModel;
    }

    @Column(name = "bvector")
    public String getbVector() {
        return bVector;
    }

    public boolean isNotForScoring() {
        return notForScoring;
    }

    public String getClString() {
        return clString;
    }

    @Column(name = "irt2_a")
    public String getIrt2A() {
        return irt2A;
    }

    @Column(name = "irt2_b")
    public String getIrt2B() {
        return irt2B;
    }

    @Column(name = "irt2_c")
    public String getIrt2C() {
        return irt2C;
    }

    @Column(name = "irt2_model")
    public String getIrt2Model() {
        return irt2Model;
    }

    public double getFtWeight() {
        return ftWeight;
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setTblAdminItemIdentifier(final TblAdminItemIdentifier tblAdminItemIdentifier) {
        this.tblAdminItemIdentifier = tblAdminItemIdentifier;
    }

    private void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    private void setItemPosition(final int itemPosition) {
        this.itemPosition = itemPosition;
    }

    private void setFieldTest(final boolean fieldTest) {
        this.fieldTest = fieldTest;
    }

    private void setActive(final boolean active) {
        this.active = active;
    }

    private void setIrtB(final String irtB) {
        this.irtB = irtB;
    }

    private void setBlockId(final String blockId) {
        this.blockId = blockId;
    }

    private void setRequired(final boolean required) {
        this.required = required;
    }

    private void setPrintable(final boolean printable) {
        this.printable = printable;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setResponseMimeType(final String responseMimeType) {
        this.responseMimeType = responseMimeType;
    }

    private void setTestCohort(final int testCohort) {
        this.testCohort = testCohort;
    }

    private void setStrandKey(final String strandKey) {
        this.strandKey = strandKey;
    }

    private void setVersion(final String version) {
        this.version = version;
    }

    private void setUpdatedVersion(final String updatedVersion) {
        this.updatedVersion = updatedVersion;
    }

    private void setClaimName(final String claimName) {
        this.claimName = claimName;
    }

    private void setIrtA(final String irtA) {
        this.irtA = irtA;
    }

    private void setIrtC(final String irtC) {
        this.irtC = irtC;
    }

    private void setIrtModel(final String irtModel) {
        this.irtModel = irtModel;
    }

    private void setbVector(final String bVector) {
        this.bVector = bVector;
    }

    private void setNotForScoring(final boolean notForScoring) {
        this.notForScoring = notForScoring;
    }

    private void setClString(final String clString) {
        this.clString = clString;
    }

    private void setIrt2A(final String irt2A) {
        this.irt2A = irt2A;
    }

    private void setIrt2B(final String irt2B) {
        this.irt2B = irt2B;
    }

    private void setIrt2C(final String irt2C) {
        this.irt2C = irt2C;
    }

    private void setIrt2Model(final String irt2Model) {
        this.irt2Model = irt2Model;
    }

    private void setFtWeight(final double ftWeight) {
        this.ftWeight = ftWeight;
    }

    private void setGroupKey(final String groupKey) {
        this.groupKey = groupKey;
    }
}
