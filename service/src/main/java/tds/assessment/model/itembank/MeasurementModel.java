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

import tds.assessment.repositories.ItemBankDataCommandRepository;
import tds.assessment.repositories.ItemBankDataQueryRepository;
import tds.assessment.repositories.loader.AffinityGroupItemRepository;
import tds.assessment.repositories.loader.AffinityGroupRepository;
import tds.assessment.repositories.loader.ItemContentLevelRepository;
import tds.assessment.repositories.loader.ItemMeasurementParameterRepository;
import tds.assessment.repositories.loader.ItemScoreDimensionsRepository;
import tds.assessment.repositories.loader.MeasurementModelRepository;
import tds.assessment.repositories.loader.MeasurementParameterRepository;
import tds.assessment.repositories.loader.SetOfTestGradesRepository;
import tds.assessment.repositories.loader.TblAdminStimuliRepository;
import tds.assessment.repositories.loader.TblAdminStrandsRepository;
import tds.assessment.repositories.loader.TblItemRepository;
import tds.assessment.repositories.loader.TblItemSelectionParameterRepository;
import tds.assessment.repositories.loader.TblSetOfAdminItemsRepository;
import tds.assessment.repositories.loader.TblSetOfAdminSubjectsRepository;
import tds.assessment.repositories.loader.TblSetOfItemStrandsRepository;
import tds.assessment.repositories.loader.TblStimuliRepository;
import tds.assessment.repositories.loader.TblStrandRepository;
import tds.assessment.repositories.loader.TblSubjectRepository;
import tds.assessment.repositories.loader.TblTestAdminRepository;
import tds.assessment.repositories.loader.TestCohortRepository;
import tds.assessment.repositories.loader.TestFormItemRepository;
import tds.assessment.repositories.loader.TestFormRepository;

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
    @Column(name = "modelnumber")
    public int getModelNumber() {
        return modelNumber;
    }

    @Column(name = "modelname")
    public String getModelName() {
        return modelName;
    }
}
