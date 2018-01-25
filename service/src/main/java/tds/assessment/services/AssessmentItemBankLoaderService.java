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

package tds.assessment.services;

import java.util.List;
import java.util.Map;

import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.TblStrand;
import tds.testpackage.model.BlueprintElement;
import tds.testpackage.model.TestPackage;

/**
 * A service responsible for loading general item bank data
 */
public interface AssessmentItemBankLoaderService {
    void loadSubject(TestPackage testPackage, Client client, String subjectKey);

    void loadTblStimuli(TestPackage testPackage);

    Map<String, TblStrand> loadStrands(List<BlueprintElement> blueprintElements, String subjectKey,
                                       Client client, String version);

    void loadTblItems(TestPackage testPackage, Map<String, ItemMetadataWrapper> itemIdToItemMetadata);
}
