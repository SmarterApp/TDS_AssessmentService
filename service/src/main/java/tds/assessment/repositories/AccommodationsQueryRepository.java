package tds.assessment.repositories;

import java.util.List;
import java.util.Set;

import tds.accommodation.Accommodation;
import tds.accommodation.Dependency;

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
    
    /**
     * Finds the accommodation dependencies for an assessment id and clienet
     *
     * @param clientName   the client name for the accommodation dependencies
     * @param assessmentId the assessment id for the accommodation dependencies
     * @return list of {@link tds.accommodation.Dependency}
     */
    List<Dependency> findAssessmentAccommodationDependencies(String clientName, String assessmentId);
}
