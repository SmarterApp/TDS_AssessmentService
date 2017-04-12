package tds.assessment;

/**
 * Represents item measurement information
 */
public class ItemMeasurement {
    private int segmentPosition;
    private String itemKey;
    private String dimension;
    private String itemResponseTheoryModel;
    private int parameterNumber;
    private String parameterName;
    private String parameterDescription;
    private Float parameterValue;

    //Use Builder
    private ItemMeasurement(){
    }

    /**
     * @return the segment position.  If no segment then 1.  1 based
     */
    public int getSegmentPosition() {
        return segmentPosition;
    }

    /**
     * @return the item key
     */
    public String getItemKey() {
        return itemKey;
    }

    /**
     * @return the item dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * @return the theory model
     */
    public String getItemResponseTheoryModel() {
        return itemResponseTheoryModel;
    }

    /**
     * @return the parameter number in the list of parameters
     */
    public int getParameterNumber() {
        return parameterNumber;
    }

    /**
     * @return the name of the parameter
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @return the parameter description
     */
    public String getParameterDescription() {
        return parameterDescription;
    }

    /**
     * @return the parameter value
     */
    public Float getParameterValue() {
        return parameterValue;
    }


    public static final class Builder {
        private int segmentPosition;
        private String itemKey;
        private String dimension;
        private String itemResponseTheoryModel;
        private int parameterNumber;
        private String parameterName;
        private String parameterDescription;
        private Float parameterValue;

        public Builder withSegmentPosition(int segmentPosition) {
            this.segmentPosition = segmentPosition;
            return this;
        }

        public Builder withItemKey(String itemKey) {
            this.itemKey = itemKey;
            return this;
        }

        public Builder withDimension(String dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder withItemResponseTheoryModel(String itemResponseTheoryModel) {
            this.itemResponseTheoryModel = itemResponseTheoryModel;
            return this;
        }

        public Builder withParameterNumber(int parameterNumber) {
            this.parameterNumber = parameterNumber;
            return this;
        }

        public Builder withParameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public Builder withParameterDescription(String parameterDescription) {
            this.parameterDescription = parameterDescription;
            return this;
        }

        public Builder withParameterValue(Float parameterValue) {
            this.parameterValue = parameterValue;
            return this;
        }

        public ItemMeasurement build() {
            ItemMeasurement itemMeasurement = new ItemMeasurement();
            itemMeasurement.itemKey = this.itemKey;
            itemMeasurement.parameterNumber = this.parameterNumber;
            itemMeasurement.parameterValue = this.parameterValue;
            itemMeasurement.segmentPosition = this.segmentPosition;
            itemMeasurement.dimension = this.dimension;
            itemMeasurement.itemResponseTheoryModel = this.itemResponseTheoryModel;
            itemMeasurement.parameterDescription = this.parameterDescription;
            itemMeasurement.parameterName = this.parameterName;
            return itemMeasurement;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemMeasurement that = (ItemMeasurement) o;

        if (segmentPosition != that.segmentPosition) return false;
        if (parameterNumber != that.parameterNumber) return false;
        if (itemKey != null ? !itemKey.equals(that.itemKey) : that.itemKey != null) return false;
        if (dimension != null ? !dimension.equals(that.dimension) : that.dimension != null) return false;
        if (itemResponseTheoryModel != null ? !itemResponseTheoryModel.equals(that.itemResponseTheoryModel) : that.itemResponseTheoryModel != null)
            return false;
        if (parameterName != null ? !parameterName.equals(that.parameterName) : that.parameterName != null)
            return false;
        if (parameterDescription != null ? !parameterDescription.equals(that.parameterDescription) : that.parameterDescription != null)
            return false;
        return parameterValue != null ? parameterValue.equals(that.parameterValue) : that.parameterValue == null;
    }

    @Override
    public int hashCode() {
        int result = segmentPosition;
        result = 31 * result + (itemKey != null ? itemKey.hashCode() : 0);
        result = 31 * result + (dimension != null ? dimension.hashCode() : 0);
        result = 31 * result + (itemResponseTheoryModel != null ? itemResponseTheoryModel.hashCode() : 0);
        result = 31 * result + parameterNumber;
        result = 31 * result + (parameterName != null ? parameterName.hashCode() : 0);
        result = 31 * result + (parameterDescription != null ? parameterDescription.hashCode() : 0);
        result = 31 * result + (parameterValue != null ? parameterValue.hashCode() : 0);
        return result;
    }
}
