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

public class TestToolDefaults {
    private String name;
    private String artFieldName;
    private boolean allowChange;
    private boolean required;
    private boolean selectable;
    private boolean visible;
    private boolean studentControl;
    private boolean functional;
    private boolean allowMultipleOptions;
    private String dependsOnToolType;

    public static final class Builder {
        private String name;
        private String artFieldName;
        private boolean allowChange;
        private boolean required;
        private boolean selectable;
        private boolean visible;
        private boolean studentControl;
        private boolean functional;
        private boolean allowMultipleOptions;
        private String dependsOnToolType;

        public Builder(final String name) {
            this.name = name;
        }

        public Builder withArtFieldName(String artFieldName) {
            this.artFieldName = artFieldName;
            return this;
        }

        public Builder withAllowChange(boolean allowChange) {
            this.allowChange = allowChange;
            return this;
        }

        public Builder withRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder withSelectable(boolean selectable) {
            this.selectable = selectable;
            return this;
        }

        public Builder withVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder withStudentControl(boolean studentControl) {
            this.studentControl = studentControl;
            return this;
        }

        public Builder withFunctional(boolean functional) {
            this.functional = functional;
            return this;
        }

        public Builder withAllowMultipleOptions(boolean allowMultipleOptions) {
            this.allowMultipleOptions = allowMultipleOptions;
            return this;
        }

        public Builder withDependsOnToolType(String dependsOnToolType) {
            this.dependsOnToolType = dependsOnToolType;
            return this;
        }

        public TestToolDefaults build() {
            TestToolDefaults testToolDefaults = new TestToolDefaults();
            testToolDefaults.required = this.required;
            testToolDefaults.allowChange = this.allowChange;
            testToolDefaults.visible = this.visible;
            testToolDefaults.selectable = this.selectable;
            testToolDefaults.dependsOnToolType = this.dependsOnToolType;
            testToolDefaults.artFieldName = this.artFieldName;
            testToolDefaults.name = this.name;
            testToolDefaults.studentControl = this.studentControl;
            testToolDefaults.functional = this.functional;
            testToolDefaults.allowMultipleOptions = this.allowMultipleOptions;
            return testToolDefaults;
        }
    }

    public String getName() {
        return name;
    }

    public String getArtFieldName() {
        return artFieldName;
    }

    public boolean isAllowChange() {
        return allowChange;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isStudentControl() {
        return studentControl;
    }

    public String getDependsOnToolType() {
        return dependsOnToolType;
    }

    public boolean isFunctional() {
        return functional;
    }

    public boolean isAllowMultipleOptions() {
        return allowMultipleOptions;
    }
}
