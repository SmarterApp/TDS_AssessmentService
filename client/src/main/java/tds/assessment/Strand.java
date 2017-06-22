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

package tds.assessment;

/**
 * Represents a strand grouping of items and contains metadata for segment pool selection
 */
public class Strand {
    private String name;
    private String key;
    private String segmentKey;
    private int minItems;
    private int maxItems;
    private Float adaptiveCut;
    private boolean strictMax;
    private float bpWeight;
    private Float startAbility;
    private Float abilityWeight;
    private Float precisionTarget;
    private Float precisionTargetMetWeight;
    private Float precisionTargetNotMetWeight;
    private Float startInfo;
    private Float scalar;

    /**
     * Used by frameworks
     */
    private Strand() {}

    private Strand (Builder builder) {
        this.name = builder.name;
        this.key = builder.key;
        this.segmentKey = builder.segmentKey;
        this.minItems = builder.minItems;
        this.maxItems = builder.maxItems;
        this.adaptiveCut = builder.adaptiveCut;
        this.strictMax = builder.strictMax;
        this.bpWeight = builder.bpWeight;
        this.startAbility = builder.startAbility;
        this.abilityWeight = builder.abilityWeight;
        this.precisionTarget = builder.precisionTarget;
        this.precisionTargetMetWeight = builder.precisionTargetMetWeight;
        this.precisionTargetNotMetWeight = builder.precisionTargetNotMetWeight;
        this.startInfo = builder.startInfo;
        this.scalar = builder.scalar;
    }

    /**
     * @return the strand name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the strand key - typical format is {segmentKey}-{strandName}
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the key to the parent {@link tds.assessment.Segment}
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    /**
     * @return the minimum number of items allowed for this strand
     */
    public int getMinItems() {
        return minItems;
    }

    /**
     * @return the maximum number of items allowed for this strand
     */
    public int getMaxItems() {
        return maxItems;
    }

    /**
     * @return an optional (nullable) value used by the adaptive algorithm engine.
     */
    public Float getAdaptiveCut() {
        return adaptiveCut;
    }

    /**
     * @return {@code true} if there is a strict max
     */
    public boolean isStrictMax() {
        return strictMax;
    }

    /**
     * @return the blueprint weight
     */
    public float getBpWeight() {
        return bpWeight;
    }

    /**
     * @return start ability associated with the strand
     */
    public Float getStartAbility() {
        return startAbility;
    }

    /**
     * @return ability weight
     */
    public Float getAbilityWeight() {
        return abilityWeight;
    }

    /**
     * @return precision target
     */
    public Float getPrecisionTarget() {
        return precisionTarget;
    }

    /**
     * @return precision target met weight
     */
    public Float getPrecisionTargetMetWeight() {
        return precisionTargetMetWeight;
    }

    /**
     * @return precision target not met weight
     */
    public Float getPrecisionTargetNotMetWeight() {
        return precisionTargetNotMetWeight;
    }

    /**
     * @return start information
     */
    public Float getStartInfo() {
        return startInfo;
    }

    public Float getScalar() {
        return scalar;
    }

    public static final class Builder {
        private String name;
        private String key;
        private String segmentKey;
        private int minItems;
        private int maxItems;
        private Float adaptiveCut;
        private boolean strictMax;
        private float bpWeight;
        private Float startAbility;
        private Float abilityWeight;
        private Float precisionTarget;
        private Float precisionTargetMetWeight;
        private Float precisionTargetNotMetWeight;
        private Float startInfo;
        private Float scalar;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
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

        public Builder withAdaptiveCut(Float adaptiveCut) {
            this.adaptiveCut = adaptiveCut;
            return this;
        }

        public Builder withStrictMax(boolean strictMax) {
            this.strictMax = strictMax;
            return this;
        }

        public Builder withBpWeight(float bpWeight) {
            this.bpWeight = bpWeight;
            return this;
        }

        public Builder withStartAbility(Float startAbility) {
            this.startAbility = startAbility;
            return this;
        }

        public Builder withAbilityWeight(Float abilityWeight) {
            this.abilityWeight = abilityWeight;
            return this;
        }

        public Builder withPrecisionTarget(Float precisionTarget) {
            this.precisionTarget = precisionTarget;
            return this;
        }

        public Builder withPrecisionTargetMetWeight(Float precisionTargetMetWeight) {
            this.precisionTargetMetWeight = precisionTargetMetWeight;
            return this;
        }

        public Builder withPrecisionTargetNotMetWeight(Float precisionTargetNotMetWeight) {
            this.precisionTargetNotMetWeight = precisionTargetNotMetWeight;
            return this;
        }

        public Builder withStartInfo(Float startInfo) {
            this.startInfo = startInfo;
            return this;
        }

        public Builder withScalar(Float scalar) {
            this.scalar = scalar;
            return this;
        }

        public Strand build() {
            return new Strand(this);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Strand strand = (Strand) o;

        if (minItems != strand.minItems) return false;
        if (maxItems != strand.maxItems) return false;
        if (name != null ? !name.equals(strand.name) : strand.name != null) return false;
        if (key != null ? !key.equals(strand.key) : strand.key != null) return false;
        if (segmentKey != null ? !segmentKey.equals(strand.segmentKey) : strand.segmentKey != null) return false;
        return adaptiveCut != null ? adaptiveCut.equals(strand.adaptiveCut) : strand.adaptiveCut == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (segmentKey != null ? segmentKey.hashCode() : 0);
        result = 31 * result + minItems;
        result = 31 * result + maxItems;
        result = 31 * result + (adaptiveCut != null ? adaptiveCut.hashCode() : 0);
        return result;
    }
}
