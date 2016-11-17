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
     * @param assessmentKey to the assessment
     * @return {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessmentByKey(String assessmentKey);
}
