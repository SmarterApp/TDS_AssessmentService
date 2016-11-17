package tds.assessment.repositories;

import java.util.List;

import tds.assessment.Form;

/**
 * Repository responsible for reading {@link tds.assessment.Form} data
 */
public interface FormQueryRepository {

    /**
     * Finds a list of forms for the assessment
     *
     * @param assessmentKey the assessment key for the form's {@link tds.assessment.Assessment}
     * @return  a list of forms for the assessment
     */
    List<Form> findFormsForAssessment(final String assessmentKey);
}
