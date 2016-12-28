package tds.assessment.repositories;

import java.util.Set;

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
    Set<Strand> findStrands(String assessmentKey);
}
