package tds.assessment;

public class ContentLevelSpecification {
    private boolean reportingCategory;
    private int elementType;
    private String contentLevel;
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

    private ContentLevelSpecification(){
    }

    public static final class Builder {
        private boolean reportingCategory;
        private int elementType;
        private String contentLevel;
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

        public Builder withReportingCategory(boolean reportingCategory) {
            this.reportingCategory = reportingCategory;
            return this;
        }

        public Builder withElementType(int elementType) {
            this.elementType = elementType;
            return this;
        }

        public Builder withContentLevel(String contentLevel) {
            this.contentLevel = contentLevel;
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

        public ContentLevelSpecification build() {
            ContentLevelSpecification contentLevelSpecification = new ContentLevelSpecification();
            contentLevelSpecification.adaptiveCut = this.adaptiveCut;
            contentLevelSpecification.bpWeight = this.bpWeight;
            contentLevelSpecification.startAbility = this.startAbility;
            contentLevelSpecification.maxItems = this.maxItems;
            contentLevelSpecification.strictMax = this.strictMax;
            contentLevelSpecification.minItems = this.minItems;
            contentLevelSpecification.abilityWeight = this.abilityWeight;
            contentLevelSpecification.precisionTargetNotMetWeight = this.precisionTargetNotMetWeight;
            contentLevelSpecification.precisionTargetMetWeight = this.precisionTargetMetWeight;
            contentLevelSpecification.contentLevel = this.contentLevel;
            contentLevelSpecification.precisionTarget = this.precisionTarget;
            contentLevelSpecification.scalar = this.scalar;
            contentLevelSpecification.reportingCategory = this.reportingCategory;
            contentLevelSpecification.startInfo = this.startInfo;
            contentLevelSpecification.elementType = this.elementType;
            return contentLevelSpecification;
        }
    }

    public boolean isReportingCategory() {
        return reportingCategory;
    }

    public int getElementType() {
        return elementType;
    }

    public String getContentLevel() {
        return contentLevel;
    }

    public int getMinItems() {
        return minItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public Float getAdaptiveCut() {
        return adaptiveCut;
    }

    public boolean isStrictMax() {
        return strictMax;
    }

    public float getBpWeight() {
        return bpWeight;
    }

    public Float getStartAbility() {
        return startAbility;
    }

    public Float getAbilityWeight() {
        return abilityWeight;
    }

    public Float getPrecisionTarget() {
        return precisionTarget;
    }

    public Float getPrecisionTargetMetWeight() {
        return precisionTargetMetWeight;
    }

    public Float getPrecisionTargetNotMetWeight() {
        return precisionTargetNotMetWeight;
    }

    public Float getStartInfo() {
        return startInfo;
    }

    public Float getScalar() {
        return scalar;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ContentLevelSpecification that = (ContentLevelSpecification) o;

        if (reportingCategory != that.reportingCategory) return false;
        if (elementType != that.elementType) return false;
        if (minItems != that.minItems) return false;
        if (maxItems != that.maxItems) return false;
        if (strictMax != that.strictMax) return false;
        if (Float.compare(that.bpWeight, bpWeight) != 0) return false;
        if (contentLevel != null ? !contentLevel.equals(that.contentLevel) : that.contentLevel != null) return false;
        if (adaptiveCut != null ? !adaptiveCut.equals(that.adaptiveCut) : that.adaptiveCut != null) return false;
        if (startAbility != null ? !startAbility.equals(that.startAbility) : that.startAbility != null) return false;
        if (abilityWeight != null ? !abilityWeight.equals(that.abilityWeight) : that.abilityWeight != null)
            return false;
        if (precisionTarget != null ? !precisionTarget.equals(that.precisionTarget) : that.precisionTarget != null)
            return false;
        if (precisionTargetMetWeight != null ? !precisionTargetMetWeight.equals(that.precisionTargetMetWeight) : that.precisionTargetMetWeight != null)
            return false;
        if (precisionTargetNotMetWeight != null ? !precisionTargetNotMetWeight.equals(that.precisionTargetNotMetWeight) : that.precisionTargetNotMetWeight != null)
            return false;
        if (startInfo != null ? !startInfo.equals(that.startInfo) : that.startInfo != null) return false;
        return scalar != null ? scalar.equals(that.scalar) : that.scalar == null;
    }

    @Override
    public int hashCode() {
        int result = (reportingCategory ? 1 : 0);
        result = 31 * result + elementType;
        result = 31 * result + (contentLevel != null ? contentLevel.hashCode() : 0);
        result = 31 * result + minItems;
        result = 31 * result + maxItems;
        result = 31 * result + (adaptiveCut != null ? adaptiveCut.hashCode() : 0);
        result = 31 * result + (strictMax ? 1 : 0);
        result = 31 * result + (bpWeight != +0.0f ? Float.floatToIntBits(bpWeight) : 0);
        result = 31 * result + (startAbility != null ? startAbility.hashCode() : 0);
        result = 31 * result + (abilityWeight != null ? abilityWeight.hashCode() : 0);
        result = 31 * result + (precisionTarget != null ? precisionTarget.hashCode() : 0);
        result = 31 * result + (precisionTargetMetWeight != null ? precisionTargetMetWeight.hashCode() : 0);
        result = 31 * result + (precisionTargetNotMetWeight != null ? precisionTargetNotMetWeight.hashCode() : 0);
        result = 31 * result + (startInfo != null ? startInfo.hashCode() : 0);
        result = 31 * result + (scalar != null ? scalar.hashCode() : 0);
        return result;
    }
}
