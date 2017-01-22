package tds.assessment.services;

import java.util.List;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentWindowParameters;

public interface AssessmentWindowService {
    /**
     * Finds the {@link tds.assessment.AssessmentWindow AssessmentWindow} for the given {@link tds.config.model.AssessmentWindowParameters}
     *
     * @param assessmentWindowParameters {@link tds.assessment.model.AssessmentWindowParameters} propeties for the assessment window
     * @return list of {@link tds.assessment.AssessmentWindow} that fit the parameters
     */
    List<AssessmentWindow> findAssessmentWindows(AssessmentWindowParameters assessmentWindowParameters);
}
