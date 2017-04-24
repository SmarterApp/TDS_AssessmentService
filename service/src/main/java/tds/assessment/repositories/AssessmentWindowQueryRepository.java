package tds.assessment.repositories;

import java.util.List;
import java.util.Optional;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentFormWindowProperties;

/**
 * Handles data access dealing with assessment windows
 */
public interface AssessmentWindowQueryRepository {
    /**
     * Finds the assessment windows for the specified assessmentIds
     *
     * @param clientName the client name of the TDS environment
     * @param assessmentIds a collection of assessment ids to find assessment windows for
     * @return A list of assessment windows for all the ids
     */
    List<AssessmentWindow> findAssessmentWindowsForAssessmentIds(final String clientName, final String... assessmentIds);

    /**
     * Find the current active assessment windows
     *
     * @param clientName       the client name for the installation
     * @param assessmentIds    the assessment ids to fetch windows for
     * @param shiftWindowStart the days to prepend for the window
     * @param shiftWindowEnd   the days to append for the window
     * @return {@link tds.assessment.AssessmentWindow}
     */
    List<AssessmentWindow> findCurrentAssessmentWindows(final String clientName, final int shiftWindowStart, final int shiftWindowEnd, final String... assessmentIds);

    /**
     * Finds the current active assessment form windows
     *
     * @param clientName       the client name for the installation
     * @param assessmentId     assessment id
     * @param shiftWindowStart the days to shift for the start of window
     * @param shiftWindowEnd   the days to shift for the end of window
     * @param shiftFormStart   the days to shift for the start of the form
     * @param shiftFormEnd     the days to shift for the end of the form
     * @return list of current {@link tds.assessment.AssessmentWindow}
     */
    List<AssessmentWindow> findCurrentAssessmentFormWindows(final String clientName, final String assessmentId, final int shiftWindowStart, final int shiftWindowEnd, final int shiftFormStart, final int shiftFormEnd);

    /**
     * Finds the assessment form window properties
     *
     * @param clientName   client name of the installation
     * @param assessmentId assessment id
     * @return {@link tds.assessment.model.AssessmentFormWindowProperties} if found otherwise empty
     */
    Optional<AssessmentFormWindowProperties> findAssessmentFormWindowProperties(final String clientName, final String assessmentId);
}
