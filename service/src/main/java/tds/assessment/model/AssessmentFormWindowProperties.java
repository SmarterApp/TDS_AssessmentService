/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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
