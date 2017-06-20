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

import tds.assessment.ContentLevelSpecification;
import tds.assessment.Strand;

/**
 * Repository for reading {@link tds.assessment.Strand} data
 */
public interface StrandQueryRepository {

    /**
     * Retrieves the list of {@link tds.assessment.Strand}s for the {@link tds.assessment.Assessment}
     *
     * @param assessmentKey the key of the {@link tds.assessment.Assessment}
     * @return the list of the strands for the assessment and its segments
     */
    Set<Strand> findStrands(final String assessmentKey);

    /**
     * Finds the content level specifications by segment key
     *
     * @param segmentKey the segment key
     * @return list of {@link tds.assessment.ContentLevelSpecification}
     */
    List<ContentLevelSpecification> findContentLevelSpecificationsBySegmentKey(final String segmentKey);
}
