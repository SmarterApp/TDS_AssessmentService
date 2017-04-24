package tds.assessment.services;

import java.util.List;
import java.util.Map;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentWindowParameters;

public interface AssessmentWindowService {
    /**
     * Finds the {@link tds.assessment.AssessmentWindow}s for the specified assessmentIds
     *
     * @param clientName the client name of the TDS environment
     * @param assessmentIds a collection of assessment ids to find assessment windows for
     * @return A mapping of assessment keys to their {@link tds.assessment.AssessmentWindow}s
     */
    Map<String, List<AssessmentWindow>> findAssessmentWindowsForAssessmentIds(final String clientName, final String... assessmentIds);

    /**
     * Finds the {@link tds.assessment.AssessmentWindow AssessmentWindow} for the given {@link tds.assessment.model.AssessmentWindowParameters}
     *
     * @param assessmentWindowParameters {@link tds.assessment.model.AssessmentWindowParameters} propeties for the assessment window
     * @return list of {@link tds.assessment.AssessmentWindow} that fit the parameters
     */
    List<AssessmentWindow> findAssessmentWindows(final AssessmentWindowParameters assessmentWindowParameters);
}
