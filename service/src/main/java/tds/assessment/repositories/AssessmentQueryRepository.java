package tds.assessment.repositories;

import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.AssessmentInfo;
import tds.assessment.ItemConstraint;

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
     * Finds an {@link tds.assessment.Assessment} by a segment key
     *
     * @param segmentKey the segment's key
     * @return a {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessmentBySegmentKey(final String segmentKey);
}
