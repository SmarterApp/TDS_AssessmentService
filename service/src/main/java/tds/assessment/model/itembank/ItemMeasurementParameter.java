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
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "itemmeasurementparameter", schema = "itembank")
public class ItemMeasurementParameter {
    private ItemMeasurementParameterIdentifier itemMeasurementParameterIdentifier;
    private double parameterValue;

    public ItemMeasurementParameter(final UUID itemScoreDimensionKey, final int measurementParmaeterKey, final double parameterValue) {
        this.itemMeasurementParameterIdentifier = new ItemMeasurementParameterIdentifier(itemScoreDimensionKey, measurementParmaeterKey);
        this.parameterValue = parameterValue;
    }

    public void setItemMeasurementParameterIdentifier(final ItemMeasurementParameterIdentifier itemMeasurementParameterIdentifier) {
        this.itemMeasurementParameterIdentifier = itemMeasurementParameterIdentifier;
    }

    public void setParameterValue(final double parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Column(name = "parmvalue")
    public double getParameterValue() {
        return parameterValue;
    }

    @EmbeddedId
    public ItemMeasurementParameterIdentifier getItemMeasurementParameterIdentifier() {
        return itemMeasurementParameterIdentifier;
    }
}
