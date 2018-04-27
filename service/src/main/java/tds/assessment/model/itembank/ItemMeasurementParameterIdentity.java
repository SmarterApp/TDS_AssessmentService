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
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ItemMeasurementParameterIdentity implements Serializable {
    @NotNull
    private UUID itemScoreDimensionKey;

    @NotNull
    private int measurementParameterKey;

    /**
     * Empty constructor for frameworks
     */
    private ItemMeasurementParameterIdentity() {
    }

    ItemMeasurementParameterIdentity(final UUID itemScoreDimensionKey, final int measurementParameterKey) {
        this.itemScoreDimensionKey = itemScoreDimensionKey;
        this.measurementParameterKey = measurementParameterKey;
    }

    private void setItemScoreDimensionKey(final UUID itemScoreDimensionKey) {
        this.itemScoreDimensionKey = itemScoreDimensionKey;
    }

    private void setMeasurementParameterKey(final int measurementParameterKey) {
        this.measurementParameterKey = measurementParameterKey;
    }

    @Column(name = "_fk_itemscoredimension", columnDefinition = "VARBINARY(16)")
    public UUID getItemScoreDimensionKey() {
        return itemScoreDimensionKey;
    }

    @Column(name = "_fk_measurementparameter")
    public int getMeasurementParameterKey() {
        return measurementParameterKey;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemMeasurementParameterIdentity that = (ItemMeasurementParameterIdentity) o;

        if (measurementParameterKey != that.measurementParameterKey) return false;
        return itemScoreDimensionKey.equals(that.itemScoreDimensionKey);
    }

    @Override
    public int hashCode() {
        int result = itemScoreDimensionKey.hashCode();
        result = 31 * result + measurementParameterKey;
        return result;
    }
}
