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

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentWindowParameters;

public interface AssessmentWindowService {
    /**
     * Finds the {@link tds.assessment.AssessmentWindow}s for the specified assessmentIds
     *
     * @param clientName the client name of the TDS environment
     * @param assessmentIds a collection of assessment ids to find assessment windows for
     * @return A mapping of assessment keys to their {@link tds.assessment.AssessmentWindow}s
     */
    Map<String, List<AssessmentWindow>> findAssessmentWindowsForAssessmentIds(final String clientName, final String... assessmentIds);

    /**
     * Finds the {@link tds.assessment.AssessmentWindow AssessmentWindow} for the given {@link tds.assessment.model.AssessmentWindowParameters}
     *
     * @param assessmentWindowParameters {@link tds.assessment.model.AssessmentWindowParameters} propeties for the assessment window
     * @return list of {@link tds.assessment.AssessmentWindow} that fit the parameters
     */
    List<AssessmentWindow> findAssessmentWindows(final AssessmentWindowParameters assessmentWindowParameters);
}
