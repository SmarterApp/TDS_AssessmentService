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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "client_tooldependencies", catalog = "configs")
public class ToolDependency {
    private static final String DEFAULT_TEST_MODE = "ALL";
    private UUID key;
    private String context;
    private String contextType;
    private String ifType;
    private String ifValue;
    private String thenType;
    private String thenValue;
    private boolean defaultValue;
    private String clientName;
    private String testMode;

    private ToolDependency() {
    }

    public static final class Builder {
        private UUID key;
        private String context;
        private String contextType;
        private String ifType;
        private String ifValue;
        private String thenType;
        private String thenValue;
        private boolean defaultValue;
        private String clientName;
        private String testMode;

        public Builder() {
            this.key = UUID.randomUUID();
            this.testMode = DEFAULT_TEST_MODE;
        }

        public Builder withContext(String context) {
            this.context = context;
            return this;
        }

        public Builder withContextType(String contextType) {
            this.contextType = contextType;
            return this;
        }

        public Builder withIfType(String ifType) {
            this.ifType = ifType;
            return this;
        }

        public Builder withIfValue(String ifValue) {
            this.ifValue = ifValue;
            return this;
        }

        public Builder withThenType(String thenType) {
            this.thenType = thenType;
            return this;
        }

        public Builder withThenValue(String thenValue) {
            this.thenValue = thenValue;
            return this;
        }

        public Builder withDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withTestMode(String testMode) {
            this.testMode = testMode;
            return this;
        }

        public ToolDependency build() {
            ToolDependency toolDependency = new ToolDependency();
            toolDependency.ifType = this.ifType;
            toolDependency.ifValue = this.ifValue;
            toolDependency.thenValue = this.thenValue;
            toolDependency.thenType = this.thenType;
            toolDependency.context = this.context;
            toolDependency.contextType = this.contextType;
            toolDependency.defaultValue = this.defaultValue;
            toolDependency.testMode = this.testMode;
            toolDependency.key = this.key;
            toolDependency.clientName = this.clientName;
            return toolDependency;
        }
    }

    @Id
    @Column(name = "_key", columnDefinition = "VARBINARY(16)")
    public UUID getKey() {
        return key;
    }

    public String getContext() {
        return context;
    }

    @Column(name = "contexttype")
    public String getContextType() {
        return contextType;
    }

    @Column(name = "iftype")
    public String getIfType() {
        return ifType;
    }

    @Column(name = "ifvalue")
    public String getIfValue() {
        return ifValue;
    }

    @Column(name = "thentype")
    public String getThenType() {
        return thenType;
    }

    @Column(name = "thenvalue")
    public String getThenValue() {
        return thenValue;
    }

    @Column(name = "isdefault")
    public boolean isDefaultValue() {
        return defaultValue;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "testmode")
    public String getTestMode() {
        return testMode;
    }

    private void setKey(final UUID key) {
        this.key = key;
    }

    private void setContext(final String context) {
        this.context = context;
    }

    private void setIfType(final String ifType) {
        this.ifType = ifType;
    }

    private void setIfValue(final String ifValue) {
        this.ifValue = ifValue;
    }

    private void setThenType(final String thenType) {
        this.thenType = thenType;
    }

    private void setThenValue(final String thenValue) {
        this.thenValue = thenValue;
    }

    private void setDefaultValue(final boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setTestMode(final String testMode) {
        this.testMode = testMode;
    }

    private void setContextType(final String contextType) {
        this.contextType = contextType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ToolDependency that = (ToolDependency) o;
        return Objects.equals(context, that.context) &&
            Objects.equals(contextType, that.contextType) &&
            Objects.equals(ifType, that.ifType) &&
            Objects.equals(ifValue, that.ifValue) &&
            Objects.equals(thenType, that.thenType) &&
            Objects.equals(thenValue, that.thenValue) &&
            Objects.equals(clientName, that.clientName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(context, contextType, ifType, ifValue, thenType, thenValue, clientName);
    }
}
