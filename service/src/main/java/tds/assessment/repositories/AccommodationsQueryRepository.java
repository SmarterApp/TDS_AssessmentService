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

import java.util.List;
import java.util.Set;

import tds.accommodation.Accommodation;
import tds.accommodation.AccommodationDependency;

/**
 * Data access repository for accommodations
 */
public interface AccommodationsQueryRepository {
    /**
     * Finds the accommodations for an assessment without segments
     *
     * @param assessmentKey the key for the assessment
     * @param languageCodes the included language codes for the accommodations
     * @return List of {@link tds.accommodation.Accommodation}
     */
    List<Accommodation> findAssessmentAccommodationsByKey(final String assessmentKey, final Set<String> languageCodes);

    /**
     * Finds the accommodations for an assessment id an client
     *
     * @param clientName   the client name for the accommodations
     * @param assessmentId the assessment id for the accommodations
     * @return list of {@link tds.accommodation.Accommodation}
     */
    List<Accommodation> findAssessmentAccommodationsById(final String clientName, final String assessmentId);
    
    /**
     * Finds the accommodation dependencies for an assessment id and client
     *
     * @param clientName   the client name for the accommodation dependencies
     * @param assessmentId the assessment id for the accommodation dependencies
     * @return list of {@link tds.accommodation.AccommodationDependency}
     */
    List<AccommodationDependency> findAssessmentAccommodationDependencies(final String clientName, final String assessmentId);
}
