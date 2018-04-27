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
@Table(name = "client_testeerelationshipattribute", catalog = "configs")
public class TesteeRelationshipAttribute {
    private static final String DEFAULT_AT_LOGIN = "VERIFY";
    private TesteeRelationshipAttributeIdentity testeeRelationshipAttributeIdentity;
    private String relationshipType;
    private String artId;
    private String label;
    private String atLogin;
    private String reportName;

    /**
     * Private constructor for frameworks
     */
    private TesteeRelationshipAttribute() {
    }


    public static final class Builder {
        private TesteeRelationshipAttributeIdentity testeeRelationshipAttributeIdentity;
        private String relationshipType;
        private String artId;
        private String tdsId;
        private String label;
        private String atLogin;
        private String reportName;

        private Builder(final String clientName, final String tdsId) {
            this.testeeRelationshipAttributeIdentity = new TesteeRelationshipAttributeIdentity(clientName, tdsId);
        }

        public Builder withRelationshipType(String relationshipType) {
            this.relationshipType = relationshipType;
            return this;
        }

        public Builder withArtId(String artId) {
            this.artId = artId;
            return this;
        }

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withAtLogin(String atLogin) {
            this.atLogin = atLogin;
            return this;
        }

        public Builder withReportName(String reportName) {
            this.reportName = reportName;
            return this;
        }

        public TesteeRelationshipAttribute build() {
            TesteeRelationshipAttribute testeeRelationshipAttribute = new TesteeRelationshipAttribute();
            testeeRelationshipAttribute.artId = this.artId;
            testeeRelationshipAttribute.relationshipType = this.relationshipType;
            testeeRelationshipAttribute.reportName = this.reportName;
            testeeRelationshipAttribute.atLogin = this.atLogin;
            testeeRelationshipAttribute.testeeRelationshipAttributeIdentity = this.testeeRelationshipAttributeIdentity;
            testeeRelationshipAttribute.label = this.label;
            return testeeRelationshipAttribute;
        }
    }

    @EmbeddedId
    public TesteeRelationshipAttributeIdentity getTesteeRelationshipAttributeIdentity() {
        return testeeRelationshipAttributeIdentity;
    }

    @Column(name = "relationshiptype")
    public String getRelationshipType() {
        return relationshipType;
    }

    @Column(name = "rtsname")
    public String getArtId() {
        return artId;
    }

    public String getLabel() {
        return label;
    }

    @Column(name = "atlogin")
    public String getAtLogin() {
        return atLogin;
    }

    @Column(name = "reportname")
    public String getReportName() {
        return reportName;
    }

    private void setTesteeRelationshipAttributeIdentity(final TesteeRelationshipAttributeIdentity testeeRelationshipAttributeIdentity) {
        this.testeeRelationshipAttributeIdentity = testeeRelationshipAttributeIdentity;
    }

    private void setRelationshipType(final String relationshipType) {
        this.relationshipType = relationshipType;
    }

    private void setArtId(final String artId) {
        this.artId = artId;
    }

    private void setLabel(final String label) {
        this.label = label;
    }

    private void setAtLogin(final String atLogin) {
        this.atLogin = atLogin;
    }

    private void setReportName(final String reportName) {
        this.reportName = reportName;
    }
}
