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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "measurementmodel", schema = "itembank")
public class MeasurementModel {
    private int modelNumber;
    private String modelName;

    public MeasurementModel(final int modelNumber, final String modelName) {
        this.modelNumber = modelNumber;
        this.modelName = modelName;
    }

    public void setModelNumber(final int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public void setModelName(final String modelName) {
        this.modelName = modelName;
    }

    @Id
    public int getModelNumber() {
        return modelNumber;
    }

    public String getModelName() {
        return modelName;
    }
}
