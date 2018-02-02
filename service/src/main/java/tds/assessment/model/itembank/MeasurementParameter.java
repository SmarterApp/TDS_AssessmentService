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

/**
 * A row containing known measurement parameters for each scoring {@link tds.assessment.model.itembank.MeasurementModel}
 *
 * Note: As of 02/01/2018, these parameters are only provided via seed data
 */
@Entity
@Table(name = "measurementparameter", schema = "itembank")
public class MeasurementParameter {
    private MeasurementParameterIdentity measurementParameterIdentity;
    private String parameterName;
    private String parameterDescription;

    /**
     * Empty constructor for frameworks
     */
    private MeasurementParameter() {
    }

    public MeasurementParameter(final int measurementModelKey, final int parameterNumber, final String parameterName, final String parameterDescription) {
        this.measurementParameterIdentity = new MeasurementParameterIdentity(measurementModelKey, parameterNumber);
        this.parameterName = parameterName;
        this.parameterDescription = parameterDescription;
    }

    public void setMeasurementParameterIdentity(final MeasurementParameterIdentity measurementParameterIdentity) {
        this.measurementParameterIdentity = measurementParameterIdentity;
    }

    public void setParameterName(final String parameterName) {
        this.parameterName = parameterName;
    }

    public void setParameterDescription(final String parameterDescription) {
        this.parameterDescription = parameterDescription;
    }

    @Column(name = "parmname")
    public String getParameterName() {
        return parameterName;
    }

    @Column(name = "parmdescription")
    public String getParameterDescription() {
        return parameterDescription;
    }

    @EmbeddedId
    public MeasurementParameterIdentity getMeasurementParameterIdentity() {
        return measurementParameterIdentity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MeasurementParameter that = (MeasurementParameter) o;

        if (!measurementParameterIdentity.equals(that.measurementParameterIdentity)) return false;
        if (!parameterName.equals(that.parameterName)) return false;
        return parameterDescription != null ? parameterDescription.equals(that.parameterDescription) : that.parameterDescription == null;
    }

    @Override
    public int hashCode() {
        int result = measurementParameterIdentity.hashCode();
        result = 31 * result + parameterName.hashCode();
        result = 31 * result + (parameterDescription != null ? parameterDescription.hashCode() : 0);
        return result;
    }
}
