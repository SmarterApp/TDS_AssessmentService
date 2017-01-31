package tds.assessment.repositories;

import java.util.List;
import java.util.Set;

import tds.accommodation.Accommodation;

/**
 * Data access repository for accommodations
 */
public interface AccommodationsQueryRepository {
    /**
     * Finds the accommodations for an assessment without segments
     *
     * @param assessmentKey the key for the assessment
     * @param languageCodes the included language codes for the accommodations
     * @return List of {@link tds.accommodation.Accommodation}
     */
    List<Accommodation> findAssessmentAccommodationsByKey(String assessmentKey, Set<String> languageCodes);

    /**
     * Finds the accommodations for an assessment id an client
     *
     * @param clientName   the client name for the accommodations
     * @param assessmentId the assessment id for the accommodations
     * @return list of {@link tds.accommodation.Accommodation}
     */
    List<Accommodation> findAssessmentAccommodationsById(String clientName, String assessmentId);
}
