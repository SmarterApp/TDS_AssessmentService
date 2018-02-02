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

package tds.assessment.repositories;

import java.util.Optional;

import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.TblTestAdmin;

public interface ItemBankDataQueryRepository {
    /**
     * Returns a {@link tds.assessment.model.itembank.Client} if one exists for the given client name
     *
     * @param clientName The name of the {@link tds.assessment.model.itembank.Client}
     * @return The {@link tds.assessment.model.itembank.Client} if one exists
     */
    Optional<Client>  findClient(final String clientName);

    /**
     * @return The current max form key + 1
     */
    long generateFormKey();
}
