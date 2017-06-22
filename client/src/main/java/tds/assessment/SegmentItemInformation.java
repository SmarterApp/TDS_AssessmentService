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

import java.util.ArrayList;
import java.util.List;

/**
 * Segment and item information needed to do item selection
 */
public class SegmentItemInformation {
    private Segment segment;
    private List<ContentLevelSpecification> contentLevelSpecifications = new ArrayList<>();
    private List<ItemGroup> itemGroups = new ArrayList<>();
    private List<Item> segmentItems = new ArrayList<>();
    private List<Item> siblingItems = new ArrayList<>();
    private List<ItemProperty> poolFilterProperties = new ArrayList<>();
    List<ItemMeasurement> itemMeasurements = new ArrayList<>();
    List<ItemControlParameter> controlParameters = new ArrayList<>();

    //For frameworks
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
     * @return the assessment's {@link tds.assessment.Item}.  If a single segmented assessment this will be empty
     */
    public List<Item> getSiblingItems() {
        return siblingItems;
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
        private List<Item> siblingItems;
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

        public Builder withSiblingItems(List<Item> siblingItems) {
            this.siblingItems = siblingItems;
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
            segmentItemInformation.siblingItems = this.siblingItems;
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
        if (siblingItems != null ? !siblingItems.equals(that.siblingItems) : that.siblingItems != null) return false;
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
        result = 31 * result + (siblingItems != null ? siblingItems.hashCode() : 0);
        result = 31 * result + (poolFilterProperties != null ? poolFilterProperties.hashCode() : 0);
        result = 31 * result + (itemMeasurements != null ? itemMeasurements.hashCode() : 0);
        result = 31 * result + (controlParameters != null ? controlParameters.hashCode() : 0);
        return result;
    }
}
