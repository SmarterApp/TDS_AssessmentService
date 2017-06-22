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

/**
 * A repository for retrieving assessment grades
 */
public interface GradesQueryRepository {
    /**
     * Finds all eligible grades for the {@link tds.assessment.Assessment}
     *
     * @param assessmentKey The key of the {@link tds.assessment.Assessment} to find eligible grades for
     * @return The list of eligible grades for the assessment
     */
    List<String> findGrades(final String assessmentKey);
}
