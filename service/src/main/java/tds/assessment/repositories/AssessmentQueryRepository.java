package tds.assessment.repositories;

import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
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
     * Finds a list of {@link tds.assessment.ItemConstraint}s for the assessment
     *
     * @param clientName the client name associated with the assessment
     * @param assessmentId the assessment id for the item's {@link tds.assessment.Assessment}
     * @return A list of item constraints for the assessment
     */
    List<ItemConstraint> findItemConstraintsForAssessment(final String clientName, final String assessmentId);

}
