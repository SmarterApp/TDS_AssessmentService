package tds.assessment.model;

/**
 * Represents assessment form window properties
 */
public class AssessmentFormWindowProperties {
    private boolean requireForm;
    private boolean requireIfFormExists;
    private String formField;
    private boolean requireFormWindow;

    public AssessmentFormWindowProperties(boolean requireForm,
                                          boolean requireIfFormExists,
                                          String formField,
                                          boolean requireFormWindow) {
        this.requireForm = requireForm;
        this.requireIfFormExists = requireIfFormExists;
        this.formField = formField;
        this.requireFormWindow = requireFormWindow;
    }

    /**
     * @return is the form required
     */
    public boolean isRequireForm() {
        return requireForm;
    }

    /**
     * @return if the form exists also require the form
     */
    public boolean isRequireIfFormExists() {
        return requireIfFormExists;
    }

    /**
     * @return the form field to use when accessing the package information
     */
    public String getFormField() {
        return formField;
    }

    /**
     * @return true if the window is also required with the form
     */
    public boolean isRequireFormWindow() {
        return requireFormWindow;
    }
}
