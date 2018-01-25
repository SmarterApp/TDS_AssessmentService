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
import java.util.UUID;

@Entity
@Table(name = "itemscoredimension", schema = "itembank")
public class ItemScoreDimension {
    private String dimension;
    private String recodeRule;
    private int scorePoints;
    private float weight;
    private UUID key;
    private String segmentKey;
    private String itemId;
    private int measurementModelKey;

    public static final class Builder {
        private String dimension;
        private String recodeRule;
        private int scorePoints;
        private float weight;
        private UUID key;
        private String segmentKey;
        private String itemId;
        private int measurementModelKey;

        public Builder() {
        }

        public Builder withDimension(String dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder withRecodeRule(String recodeRule) {
            this.recodeRule = recodeRule;
            return this;
        }

        public Builder withScorePoints(int scorePoints) {
            this.scorePoints = scorePoints;
            return this;
        }

        public Builder withWeight(float weight) {
            this.weight = weight;
            return this;
        }

        public Builder withKey(UUID key) {
            this.key = key;
            return this;
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
            return this;
        }

        public Builder withItemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder withMeasurementModelKey(int measurementModelKey) {
            this.measurementModelKey = measurementModelKey;
            return this;
        }

        public ItemScoreDimension build() {
            ItemScoreDimension itemScoreDimension = new ItemScoreDimension();
            itemScoreDimension.dimension = this.dimension;
            itemScoreDimension.weight = this.weight;
            itemScoreDimension.key = this.key;
            itemScoreDimension.scorePoints = this.scorePoints;
            itemScoreDimension.measurementModelKey = this.measurementModelKey;
            itemScoreDimension.recodeRule = this.recodeRule;
            itemScoreDimension.itemId = this.itemId;
            itemScoreDimension.segmentKey = this.segmentKey;
            return itemScoreDimension;
        }
    }

    public String getDimension() {
        return dimension;
    }

    @Column(name = "recoderule")
    public String getRecodeRule() {
        return recodeRule;
    }

    @Column(name = "scorepoints")
    public int getScorePoints() {
        return scorePoints;
    }

    public float getWeight() {
        return weight;
    }

    @Id
    @Column(name = "_key", columnDefinition = "VARBINARY(16)")
    public UUID getKey() {
        return key;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    @Column(name = "_fk_item")
    public String getItemId() {
        return itemId;
    }

    @Column(name = "_fk_measurementmodel")
    public int getMeasurementModelKey() {
        return measurementModelKey;
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setDimension(final String dimension) {
        this.dimension = dimension;
    }

    private void setRecodeRule(final String recodeRule) {
        this.recodeRule = recodeRule;
    }

    private void setScorePoints(final int scorePoints) {
        this.scorePoints = scorePoints;
    }

    private void setWeight(final float weight) {
        this.weight = weight;
    }

    private void setKey(final UUID key) {
        this.key = key;
    }

    private void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    private void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    private void setMeasurementModelKey(final int measurementModelKey) {
        this.measurementModelKey = measurementModelKey;
    }
}
