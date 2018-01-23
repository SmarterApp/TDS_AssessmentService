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
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "affinitygroup", schema = "itembank")
public class AffinityGroup {
    private AffinityGroupIdentity affinityGroupId;
    private int minItems;
    private int maxItems;
    private boolean strictMax;
    private double weight;
    private String version;
    private String updatedVersion;
    private Double startAbility;
    private Double startInfo;
    private Double abilityWeight;
    private Double precisionTarget;
    private Double precisionTargetMetWeight;
    private Double precisionTargetNotMetWeight;

    public static final class Builder {
        private AffinityGroupIdentity affinityGroupId;
        private int minItems;
        private int maxItems;
        private boolean strictMax;
        private double weight;
        private String version;
        private String updatedVersion;
        private Double startAbility;
        private Double startInfo;
        private Double abilityWeight;
        private Double precisionTarget;
        private Double precisionTargetMetWeight;
        private Double precisionTargetNotMetWeight;

        public Builder() {
        }

        public Builder withAffiniyGroupId(AffinityGroupIdentity affiniyGroupId) {
            this.affinityGroupId = affinityGroupId;
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

        public Builder withStrictMax(boolean strictMax) {
            this.strictMax = strictMax;
            return this;
        }

        public Builder withWeight(double weight) {
            this.weight = weight;
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

        public Builder withStartAbility(Double startAbility) {
            this.startAbility = startAbility;
            return this;
        }

        public Builder withStartInfo(Double startInfo) {
            this.startInfo = startInfo;
            return this;
        }

        public Builder withAbilityWeight(Double abilityWeight) {
            this.abilityWeight = abilityWeight;
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

        public AffinityGroup build() {
            AffinityGroup affinityGroup = new AffinityGroup();
            affinityGroup.affinityGroupId = this.affinityGroupId;
            affinityGroup.minItems = this.minItems;
            affinityGroup.precisionTarget = this.precisionTarget;
            affinityGroup.precisionTargetNotMetWeight = this.precisionTargetNotMetWeight;
            affinityGroup.maxItems = this.maxItems;
            affinityGroup.version = this.version;
            affinityGroup.startInfo = this.startInfo;
            affinityGroup.precisionTargetMetWeight = this.precisionTargetMetWeight;
            affinityGroup.updatedVersion = this.updatedVersion;
            affinityGroup.strictMax = this.strictMax;
            affinityGroup.abilityWeight = this.abilityWeight;
            affinityGroup.weight = this.weight;
            affinityGroup.startAbility = this.startAbility;
            return affinityGroup;
        }
    }

    @EmbeddedId
    public AffinityGroupIdentity getAffinityGroupId() {
        return affinityGroupId;
    }

    public int getMinItems() {
        return minItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    @Column(name = "isstrictmax")
    public boolean isStrictMax() {
        return strictMax;
    }

    public double getWeight() {
        return weight;
    }

    @Column(name = "loadconfig")
    public String getVersion() {
        return version;
    }

    @Column(name = "updateconfig")
    public String getUpdatedVersion() {
        return updatedVersion;
    }

    public Double getStartAbility() {
        return startAbility;
    }

    public Double getStartInfo() {
        return startInfo;
    }

    public Double getAbilityWeight() {
        return abilityWeight;
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


    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setAffinityGroupId(final AffinityGroupIdentity affinityGroupId) {
        this.affinityGroupId = affinityGroupId;
    }

    private void setMinItems(final int minItems) {
        this.minItems = minItems;
    }

    private void setMaxItems(final int maxItems) {
        this.maxItems = maxItems;
    }

    private void setStrictMax(final boolean strictMax) {
        this.strictMax = strictMax;
    }

    private void setWeight(final double weight) {
        this.weight = weight;
    }

    private void setVersion(final String version) {
        this.version = version;
    }

    private void setUpdatedVersion(final String updatedVersion) {
        this.updatedVersion = updatedVersion;
    }

    private void setStartAbility(final Double startAbility) {
        this.startAbility = startAbility;
    }

    private void setStartInfo(final Double startInfo) {
        this.startInfo = startInfo;
    }

    private void setAbilityWeight(final Double abilityWeight) {
        this.abilityWeight = abilityWeight;
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

}
