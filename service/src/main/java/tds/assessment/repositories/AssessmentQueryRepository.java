package tds.assessment.repositories;

import tds.assessment.Assessment;
import tds.assessment.Segment;

import java.util.Optional;

/**
 * Queries for admin subject related entities
 */
public interface AssessmentQueryRepository {
    /**
     * Finds an {@link tds.assessment.Assessment} by key. The assessment contains a collection of
     * {@link tds.assessment.Segment} objects, which is non-empty if the assessment is segmented.
     *
     * @param assessmentKey the unique key for the assessment/admin subject
     * @return {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessmentByKey(String assessmentKey);
}
