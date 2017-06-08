package tds.assessment.services;

import java.util.Optional;

import tds.assessment.Assessment;

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
     * @param segmentKey the segment key
     * @return optional with {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessmentBySegmentKey(final String segmentKey);
}
