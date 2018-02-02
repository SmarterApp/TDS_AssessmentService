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

@Embeddable
public class MeasurementParameterIdentity implements Serializable {
    @NotNull
    private int measurementModelKey;

    @NotNull
    private int parameterNumber;

    /**
     * Empty constructor for frameworks
     */
    private MeasurementParameterIdentity() {
    }

    public MeasurementParameterIdentity(final int measurementModelKey, final int parameterNumber) {
        this.measurementModelKey = measurementModelKey;
        this.parameterNumber = parameterNumber;
    }

    public void setMeasurementModelKey(final int measurementModelKey) {
        this.measurementModelKey = measurementModelKey;
    }

    public void setParameterNumber(final int parameterNumber) {
        this.parameterNumber = parameterNumber;
    }

    @Column(name = "_fk_measurementmodel")
    public int getMeasurementModelKey() {
        return measurementModelKey;
    }

    @Column(name = "parmnum")
    public int getParameterNumber() {
        return parameterNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MeasurementParameterIdentity that = (MeasurementParameterIdentity) o;

        if (measurementModelKey != that.measurementModelKey) return false;
        return parameterNumber == that.parameterNumber;
    }

    @Override
    public int hashCode() {
        int result = measurementModelKey;
        result = 31 * result + parameterNumber;
        return result;
    }
}
