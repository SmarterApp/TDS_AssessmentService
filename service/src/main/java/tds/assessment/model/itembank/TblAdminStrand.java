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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbladminstrand", schema = "itembank")
public class TblAdminStrand {
    private String key;
    private String segmentKey;
    private String strandKey;
    private int minItems;
    private int maxItems;
    private Double adaptiveCut;
    private Double startAbility;
    private Double startInfo;
    private Double scalar;
    private Integer loadMin;
    private Integer loadMax;
    private boolean strictMax;
    private Integer bpWeight;
    private String version;
    private Double precisionTarget;
    private Double precisionTargetMetWeight;
    private Double precisionTargetNotMetWeight;
    private Double abilityWeight;

    public static final class Builder {
        private String key;
        private String segmentKey;
        private String strandKey;
        private int minItems;
        private int maxItems;
        private Double adaptiveCut;
        private Double startAbility;
        private Double startInfo;
        private Double scalar;
        private Integer loadMin;
        private Integer loadMax;
        private boolean strictMax;
        private Integer bpWeight;
        private String version;
        private Double precisionTarget;
        private Double precisionTargetMetWeight;
        private Double precisionTargetNotMetWeight;
        private Double abilityWeight;

        public Builder() {
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
            return this;
        }

        public Builder withStrandKey(String strandKey) {
            this.strandKey = strandKey;
            return this;
        }

        public Builder withMinItems(int minItems) {
            this.minItems = minItems;
            return this;
        }

        public Builder withMaxItems(int maxItems) {
            this.maxItems = maxItems;
            return this;
        }

        public Builder withAdaptiveCut(Double adaptiveCut) {
            this.adaptiveCut = adaptiveCut;
            return this;
        }

        public Builder withStartAbility(Double startAbility) {
            this.startAbility = startAbility;
            return this;
        }

        public Builder withStartInfo(Double startInfo) {
            this.startInfo = startInfo;
            return this;
        }

        public Builder withScalar(Double scalar) {
            this.scalar = scalar;
            return this;
        }

        public Builder withLoadMin(Integer loadMin) {
            this.loadMin = loadMin;
            return this;
        }

        public Builder withLoadMax(Integer loadMax) {
            this.loadMax = loadMax;
            return this;
        }

        public Builder withStrictMax(boolean strictMax) {
            this.strictMax = strictMax;
            return this;
        }

        public Builder withBpWeight(Integer bpWeight) {
            this.bpWeight = bpWeight;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withPrecisionTarget(Double precisionTarget) {
            this.precisionTarget = precisionTarget;
            return this;
        }

        public Builder withPrecisionTargetMetWeight(Double precisionTargetMetWeight) {
            this.precisionTargetMetWeight = precisionTargetMetWeight;
            return this;
        }

        public Builder withPrecisionTargetNotMetWeight(Double precisionTargetNotMetWeight) {
            this.precisionTargetNotMetWeight = precisionTargetNotMetWeight;
            return this;
        }

        public Builder withAbilityWeight(Double abilityWeight) {
            this.abilityWeight = abilityWeight;
            return this;
        }

        public TblAdminStrand build() {
            TblAdminStrand tblAdminStrand = new TblAdminStrand();
            tblAdminStrand.key = String.format("%s-%s", this.segmentKey, this.strandKey);
            tblAdminStrand.precisionTargetNotMetWeight = this.precisionTargetNotMetWeight;
            tblAdminStrand.version = this.version;
            tblAdminStrand.adaptiveCut = this.adaptiveCut;
            tblAdminStrand.startAbility = this.startAbility;
            tblAdminStrand.maxItems = this.maxItems;
            tblAdminStrand.precisionTarget = this.precisionTarget;
            tblAdminStrand.minItems = this.minItems;
            tblAdminStrand.bpWeight = this.bpWeight;
            tblAdminStrand.strictMax = this.strictMax;
            tblAdminStrand.segmentKey = this.segmentKey;
            tblAdminStrand.precisionTargetMetWeight = this.precisionTargetMetWeight;
            tblAdminStrand.loadMin = this.loadMin;
            tblAdminStrand.startInfo = this.startInfo;
            tblAdminStrand.loadMax = this.loadMax;
            tblAdminStrand.strandKey = this.strandKey;
            tblAdminStrand.scalar = this.scalar;
            tblAdminStrand.abilityWeight = this.abilityWeight;
            return tblAdminStrand;
        }
    }

    @Id
    @Column(name = "_key")
    public String getKey() {
        return key;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    @Column(name = "_fk_strand")
    public String getStrandKey() {
        return strandKey;
    }

    public int getMinItems() {
        return minItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public Double getAdaptiveCut() {
        return adaptiveCut;
    }

    public Double getStartAbility() {
        return startAbility;
    }

    public Double getStartInfo() {
        return startInfo;
    }

    public Double getScalar() {
        return scalar;
    }

    public Integer getLoadMin() {
        return loadMin;
    }

    public Integer getLoadMax() {
        return loadMax;
    }

    @Column(name = "isstrictmax")
    public boolean isStrictMax() {
        return strictMax;
    }

    public Integer getBpWeight() {
        return bpWeight;
    }

    @Column(name = "loadconfig")
    public String getVersion() {
        return version;
    }

    public Double getPrecisionTarget() {
        return precisionTarget;
    }

    public Double getPrecisionTargetMetWeight() {
        return precisionTargetMetWeight;
    }

    public Double getPrecisionTargetNotMetWeight() {
        return precisionTargetNotMetWeight;
    }

    public Double getAbilityWeight() {
        return abilityWeight;
    }


    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    private void setStrandKey(final String strandKey) {
        this.strandKey = strandKey;
    }

    private void setMinItems(final int minItems) {
        this.minItems = minItems;
    }

    private void setMaxItems(final int maxItems) {
        this.maxItems = maxItems;
    }

    private void setAdaptiveCut(final Double adaptiveCut) {
        this.adaptiveCut = adaptiveCut;
    }

    private void setStartAbility(final Double startAbility) {
        this.startAbility = startAbility;
    }

    private void setStartInfo(final Double startInfo) {
        this.startInfo = startInfo;
    }

    private void setScalar(final Double scalar) {
        this.scalar = scalar;
    }

    private void setLoadMin(final Integer loadMin) {
        this.loadMin = loadMin;
    }

    private void setLoadMax(final Integer loadMax) {
        this.loadMax = loadMax;
    }

    private void setStrictMax(final boolean strictMax) {
        this.strictMax = strictMax;
    }

    private void setBpWeight(final Integer bpWeight) {
        this.bpWeight = bpWeight;
    }

    private void setVersion(final String version) {
        this.version = version;
    }

    private void setPrecisionTarget(final Double precisionTarget) {
        this.precisionTarget = precisionTarget;
    }

    private void setPrecisionTargetMetWeight(final Double precisionTargetMetWeight) {
        this.precisionTargetMetWeight = precisionTargetMetWeight;
    }

    private void setPrecisionTargetNotMetWeight(final Double precisionTargetNotMetWeight) {
        this.precisionTargetNotMetWeight = precisionTargetNotMetWeight;
    }

    private void setAbilityWeight(final Double abilityWeight) {
        this.abilityWeight = abilityWeight;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
