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

import java.util.Optional;

import tds.assessment.Assessment;
import tds.common.ValidationError;
import tds.testpackage.model.TestPackage;

/**
 * Service handling interaction with assessments
 */
public interface AssessmentService {

    /**
     * Retrieves a fully populated assessment
     *
     * @param clientName    the client environment identifier
     * @param assessmentKey to the assessment
     * @return {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessment(final String clientName, final String assessmentKey);

    /**
     * Finds an assessment by the segment key including single segmented assessments
     *
     * @param segmentKey the segment key
     * @return optional with {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessmentBySegmentKey(final String segmentKey);

    /**
     * Deletes an {@link tds.assessment.Assessment} with the specified key and client name
     *
     * @param clientName the client environment identifier
     * @param key        the key of the {@link tds.assessment.Assessment}
     */
    void removeAssessment(final String clientName, final String key);
}
