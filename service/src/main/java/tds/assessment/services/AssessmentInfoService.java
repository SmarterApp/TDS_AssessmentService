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

import tds.assessment.AssessmentInfo;

/**
 * A service for obtaining {@link tds.assessment.AssessmentInfo} metadata
 */
public interface AssessmentInfoService {
    /**
     * Fetches the list of {@link tds.assessment.AssessmentInfo} for a given collection of assessmentKeys
     *
     * @param clientName     The client name of the TDS environment
     * @param assessmentKeys The collection of keys to obtain {@link tds.assessment.AssessmentInfo} for
     * @return A list of {@link tds.assessment.AssessmentInfo}
     */
    List<AssessmentInfo> findAssessmentInfo(final String clientName, final String... assessmentKeys);

    /**
     * Finds all {@link tds.assessment.AssessmentInfo}s for the specified grade
     *
     * @param clientName the client name associated with the assessment
     * @param grade      the grade to fetch all {@link tds.assessment.AssessmentInfo} for
     * @return {@link tds.assessment.AssessmentInfo} if found otherwise empty
     */
    List<AssessmentInfo> findAssessmentInfoForGrade(final String clientName, final String grade);
}
