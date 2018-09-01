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
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.AssessmentInfo;
import tds.assessment.ItemConstraint;
import tds.assessment.model.SegmentMetadata;

/**
 * Queries for admin subject related entities
 */
public interface AssessmentQueryRepository {
    /**
     * Finds an {@link tds.assessment.Assessment} by key. The assessment contains a collection of
     * {@link tds.assessment.Segment} objects, which is non-empty if the assessment is segmented.
     *
     * @param assessmentKey the unique key for the assessment/admin subject
     * @param clientName    the client name associated with the assessment
     * @return {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessmentByKey(final String clientName, final String assessmentKey);

    /**
     * Finds all {@link tds.assessment.AssessmentInfo}s by their keys.
     *
     * @param clientName     the client name associated with the assessment
     * @param assessmentKeys the unique keys for the assessments/admin subjects
     * @return {@link tds.assessment.Assessment} if found otherwise empty
     */
    List<AssessmentInfo> findAssessmentInfoByKeys(final String clientName, final String... assessmentKeys);

    /**
     * Finds all {@link tds.assessment.AssessmentInfo}s for the specified grade
     *
     * @param clientName the client name associated with the assessment
     * @param grade      the grade to fetch all {@link tds.assessment.AssessmentInfo} for
     * @return {@link tds.assessment.AssessmentInfo} if found otherwise empty
     */
    List<AssessmentInfo> findAssessmentInfoForGrade(final String clientName, final String grade);

    /**
     * Finds a list of {@link tds.assessment.ItemConstraint}s for the assessment
     *
     * @param clientName   the client name associated with the assessment
     * @param assessmentId the assessment id for the item's {@link tds.assessment.Assessment}
     * @return A list of item constraints for the assessment
     */
    List<ItemConstraint> findItemConstraintsForAssessment(final String clientName, final String assessmentId);

    /**
     * Finds {@link tds.assessment.model.SegmentMetadata} for the segment key
     *
     * @param segmentKey the unique segment key
     * @return {@link tds.assessment.model.SegmentMetadata} if found otherwise empty
     */
    Optional<SegmentMetadata> findSegmentMetadata(final String segmentKey);

    /**
     * Finds keys for any segments present in the assessment (if the assessment is multi-segmented)
     *
     * @param assessmentKey The key of the assessment containing the segments
     * @return The list of segment keys
     */
    List<String> findSegmentKeysByAssessmentKey(final String assessmentKey);
}
