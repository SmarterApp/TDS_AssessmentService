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

    private Strand() {}

    private Strand (Builder builder) {
        this.name = builder.name;
        this.key = builder.key;
        this.segmentKey = builder.segmentKey;
        this.minItems = builder.minItems;
        this.maxItems = builder.maxItems;
        this.adaptiveCut = builder.adaptiveCut;
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

    public static class Builder {
        private String name;
        private String key;
        private String segmentKey;
        private int minItems;
        private int maxItems;
        private Float adaptiveCut;

        public Builder() {
        }

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

        public Strand build() {
            return new Strand(this);
        }
    }
}
