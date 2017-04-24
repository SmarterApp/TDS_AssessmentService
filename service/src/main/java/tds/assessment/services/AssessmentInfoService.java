package tds.assessment.services;

import java.util.List;

import tds.assessment.AssessmentInfo;

/**
 * A service for obtaining {@link tds.assessment.AssessmentInfo} metadata
 */
public interface AssessmentInfoService {
    /**
     * Fetches the list of {@link tds.assessment.AssessmentInfo} for a given collection of assessmentKeys
     *
     * @param clientName     The client name of the TDS environment
     * @param assessmentKeys The collection of keys to obtain {@link tds.assessment.AssessmentInfo} for
     * @return A list of {@link tds.assessment.AssessmentInfo}
     */
    List<AssessmentInfo> findAssessmentInfo(final String clientName, final String... assessmentKeys);
}
