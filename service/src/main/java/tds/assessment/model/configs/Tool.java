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

package tds.assessment.model.configs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client_testtool", catalog = "configs")
public class Tool {
    private static final String DEFAULT_TEST_MODE = "ALL";

    private ToolIdentity toolIdentity;
    private String value;
    private boolean defaultValue;
    private boolean allowCombine;
    private String valueDescription;
    private int sortOrder;
    private String testMode;

    /**
     * Private constructor for frameworks
     */
    private Tool() {
    }


    public static final class Builder {
        private ToolIdentity toolIdentity;
        private String value;
        private boolean defaultValue;
        private boolean allowCombine;
        private String valueDescription;
        private int sortOrder;
        private String testMode;

        public Builder(final String clientName, final String context, final String contextType, final String type,
                        final String code) {
            this.toolIdentity = new ToolIdentity(clientName, context, contextType, type, code);
            this.testMode =  DEFAULT_TEST_MODE;

        }

        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Builder withDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder withAllowCombine(boolean allowCombine) {
            this.allowCombine = allowCombine;
            return this;
        }

        public Builder withValueDescription(String valueDescription) {
            this.valueDescription = valueDescription;
            return this;
        }

        public Builder withSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder withTestMode(String testMode) {
            this.testMode = testMode;
            return this;
        }

        public Tool build() {
            Tool tool = new Tool();
            tool.defaultValue = this.defaultValue;
            tool.toolIdentity = this.toolIdentity;
            tool.valueDescription = this.valueDescription;
            tool.sortOrder = this.sortOrder;
            tool.value = this.value;
            tool.allowCombine = this.allowCombine;
            tool.testMode = this.testMode;
            return tool;
        }
    }

    @EmbeddedId
    public ToolIdentity getToolIdentity() {
        return toolIdentity;
    }

    public String getValue() {
        return value;
    }

    @Column(name = "isdefault")
    public boolean isDefaultValue() {
        return defaultValue;
    }

    @Column(name = "allowcombine")
    public boolean isAllowCombine() {
        return allowCombine;
    }

    @Column(name = "valuedescription")
    public String getValueDescription() {
        return valueDescription;
    }

    @Column(name = "sortorder")
    public int getSortOrder() {
        return sortOrder;
    }

    @Column(name = "testmode")
    public String getTestMode() {
        return testMode;
    }

    private void setToolIdentity(final ToolIdentity toolIdentity) {
        this.toolIdentity = toolIdentity;
    }

    private void setValue(final String value) {
        this.value = value;
    }

    private void setDefaultValue(final boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    private void setAllowCombine(final boolean allowCombine) {
        this.allowCombine = allowCombine;
    }

    private void setValueDescription(final String valueDescription) {
        this.valueDescription = valueDescription;
    }

    private void setSortOrder(final int sortOrder) {
        this.sortOrder = sortOrder;
    }

    private void setTestMode(final String testMode) {
        this.testMode = testMode;
    }
}
