package tds.assessment;

import java.util.List;

/**
 * Segment and item information needed to do item selection
 */
public class SegmentItemInformation {
    private Segment segment;
    private List<ContentLevelSpecification> contentLevelSpecifications;
    private List<ItemGroup> itemGroups;
    private List<Item> segmentItems;
    private List<Item> parentItems;
    private List<ItemProperty> poolFilterProperties;
    List<ItemMeasurement> itemMeasurements;
    List<ItemControlParameter> controlParameters;

    private SegmentItemInformation(){
    }

    /**
     * @return the {@link tds.assessment.Segment}
     */
    public Segment getSegment() {
        return segment;
    }

    /**
     * @return associated {@link tds.assessment.ContentLevelSpecification}
     */
    public List<ContentLevelSpecification> getContentLevelSpecifications() {
        return contentLevelSpecifications;
    }

    /**
     * @return associated {@link tds.assessment.ItemGroup}
     */
    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    /**
     * @return associated list of segment {@link tds.assessment.Item}
     */
    public List<Item> getSegmentItems() {
        return segmentItems;
    }

    /**
     * @return the parent assessment's {@link tds.assessment.Item}.  If a single segmented assessment this will be empty
     */
    public List<Item> getParentItems() {
        return parentItems;
    }

    /**
     * @return the {@link tds.assessment.ItemProperty} associated with pool filters
     */
    public List<ItemProperty> getPoolFilterProperties() {
        return poolFilterProperties;
    }

    /**
     * @return {@link tds.assessment.ItemMeasurement}
     */
    public List<ItemMeasurement> getItemMeasurements() {
        return itemMeasurements;
    }

    /**
     * @return {@link tds.assessment.ItemControlParameter}
     */
    public List<ItemControlParameter> getControlParameters() {
        return controlParameters;
    }

    public static final class Builder {
        List<ItemMeasurement> itemMeasurements;
        List<ItemControlParameter> controlParameters;
        private Segment segment;
        private List<ContentLevelSpecification> contentLevelSpecifications;
        private List<ItemGroup> itemGroups;
        private List<Item> segmentItems;
        private List<Item> parentItems;
        private List<ItemProperty> poolFilterProperties;

        public Builder withSegment(Segment segment) {
            this.segment = segment;
            return this;
        }

        public Builder withContentLevelSpecifications(List<ContentLevelSpecification> contentLevelSpecifications) {
            this.contentLevelSpecifications = contentLevelSpecifications;
            return this;
        }

        public Builder withItemGroups(List<ItemGroup> itemGroups) {
            this.itemGroups = itemGroups;
            return this;
        }

        public Builder withSegmentItems(List<Item> segmentItems) {
            this.segmentItems = segmentItems;
            return this;
        }

        public Builder withParentItems(List<Item> parentItems) {
            this.parentItems = parentItems;
            return this;
        }

        public Builder withPoolFilterProperties(List<ItemProperty> poolFilterProperties) {
            this.poolFilterProperties = poolFilterProperties;
            return this;
        }

        public Builder withItemMeasurements(List<ItemMeasurement> itemMeasurements) {
            this.itemMeasurements = itemMeasurements;
            return this;
        }

        public Builder withControlParameters(List<ItemControlParameter> controlParameters) {
            this.controlParameters = controlParameters;
            return this;
        }

        public SegmentItemInformation build() {
            SegmentItemInformation segmentItemInformation = new SegmentItemInformation();
            segmentItemInformation.contentLevelSpecifications = this.contentLevelSpecifications;
            segmentItemInformation.parentItems = this.parentItems;
            segmentItemInformation.poolFilterProperties = this.poolFilterProperties;
            segmentItemInformation.segment = this.segment;
            segmentItemInformation.controlParameters = this.controlParameters;
            segmentItemInformation.itemMeasurements = this.itemMeasurements;
            segmentItemInformation.itemGroups = this.itemGroups;
            segmentItemInformation.segmentItems = this.segmentItems;
            return segmentItemInformation;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SegmentItemInformation that = (SegmentItemInformation) o;

        if (segment != null ? !segment.equals(that.segment) : that.segment != null) return false;
        if (contentLevelSpecifications != null ? !contentLevelSpecifications.equals(that.contentLevelSpecifications) : that.contentLevelSpecifications != null)
            return false;
        if (itemGroups != null ? !itemGroups.equals(that.itemGroups) : that.itemGroups != null) return false;
        if (segmentItems != null ? !segmentItems.equals(that.segmentItems) : that.segmentItems != null) return false;
        if (parentItems != null ? !parentItems.equals(that.parentItems) : that.parentItems != null) return false;
        if (poolFilterProperties != null ? !poolFilterProperties.equals(that.poolFilterProperties) : that.poolFilterProperties != null)
            return false;
        if (itemMeasurements != null ? !itemMeasurements.equals(that.itemMeasurements) : that.itemMeasurements != null)
            return false;
        return controlParameters != null ? controlParameters.equals(that.controlParameters) : that.controlParameters == null;
    }

    @Override
    public int hashCode() {
        int result = segment != null ? segment.hashCode() : 0;
        result = 31 * result + (contentLevelSpecifications != null ? contentLevelSpecifications.hashCode() : 0);
        result = 31 * result + (itemGroups != null ? itemGroups.hashCode() : 0);
        result = 31 * result + (segmentItems != null ? segmentItems.hashCode() : 0);
        result = 31 * result + (parentItems != null ? parentItems.hashCode() : 0);
        result = 31 * result + (poolFilterProperties != null ? poolFilterProperties.hashCode() : 0);
        result = 31 * result + (itemMeasurements != null ? itemMeasurements.hashCode() : 0);
        result = 31 * result + (controlParameters != null ? controlParameters.hashCode() : 0);
        return result;
    }
}
