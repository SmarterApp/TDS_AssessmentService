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
 * An interface for a service responsible for loading general item bank data
 */
public interface AssessmentItemBankLoaderService {
    /**
     * Loads the given test package's {@link tds.assessment.model.itembank.TblSubject} into the item bank
     *
     * @param testPackage The test package to load
     * @param client      The {@link tds.assessment.model.itembank.Client} object containing publisher metadata
     * @param subjectKey  The human-readable subject key value
     */
    void loadSubject(final TestPackage testPackage, final Client client, final String subjectKey);

    /**
     * Loads all {@link tds.assessment.model.itembank.TblStimulus} into the item bank
     *
     * @param testPackage The test package containing the stimuli to load
     */
    void loadTblStimuli(final TestPackage testPackage);

    /**
     * Loads all {@link tds.assessment.model.itembank.TblItem}s into the item bank
     *
     * @param testPackage          Test package containing the items to load
     * @param itemMetadataWrappers A flattened list of {@link tds.testpackage.model.Item}s, along with some additional important
     *                             metadata
     */
    void loadTblItems(final TestPackage testPackage, final List<ItemMetadataWrapper> itemMetadataWrappers);

    /**
     * Loads all {@link tds.assessment.model.itembank.TblStrand}s into the item bank
     *
     * @param blueprintElements A list of all (nested) {@link tds.testpackage.model.BlueprintElement}s
     * @param subjectKey        The human-readable subject key value
     * @param client            The {@link tds.assessment.model.itembank.Client} object containing publisher metadata
     * @param version           The version of the {@link tds.testpackage.model.TestPackage}
     * @return
     */
    Map<String, TblStrand> loadStrands(final List<BlueprintElement> blueprintElements, final String subjectKey,
                                       final Client client, final String version);


}
