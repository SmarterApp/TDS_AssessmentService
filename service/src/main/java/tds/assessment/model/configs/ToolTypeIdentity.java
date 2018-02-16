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
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ToolTypeIdentity implements Serializable {
    private String clientName;
    private String context;
    private String contextType;
    private String name;

    private ToolTypeIdentity() {
    }

    ToolTypeIdentity(final String clientName, final String context, final String contextType, final String name) {
        this.clientName = clientName;
        this.context = context;
        this.contextType = contextType;
        this.name = name;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    public String getContext() {
        return context;
    }

    @Column(name = "contexttype")
    public String getContextType() {
        return contextType;
    }

    @Column(name = "toolname")
    public String getName() {
        return name;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setContext(final String context) {
        this.context = context;
    }

    private void setContextType(final String contextType) {
        this.contextType = contextType;
    }

    private void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ToolTypeIdentity that = (ToolTypeIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        if (!context.equals(that.context)) return false;
        if (!contextType.equals(that.contextType)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + context.hashCode();
        result = 31 * result + contextType.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
