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

import tds.accommodation.Accommodation;

/**
 * Configuration accommodations
 */
public interface AccommodationsService {
    /**
     * Finds the assessment accommodations for an assessment key
     *
     * @param assessmentKey the assessment's key
     * @return list of {@link tds.accommodation.Accommodation}
     */
    List<Accommodation> findAccommodationsByAssessmentKey(final String clientName, final String assessmentKey);

    /**
     * Finds the assessment accommodations for an assessment id
     *
     * @param clientName   the client name
     * @param assessmentId the id for the assessment
     * @return
     */
    List<Accommodation> findAccommodationsByAssessmentId(final String clientName, final String assessmentId);
}
