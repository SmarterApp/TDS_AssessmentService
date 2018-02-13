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
@Table(name = "client_testeeattribute", catalog = "configs")
public class TesteeAttribute {
    private TesteeAttributeIdentity testeeAttributeIdentity;
    private String artId;
    private String reportName;
    private String type;
    private String label;
    private String atLogin;
    private int sortOrder;
    private boolean latencySite;
    private boolean showOnProctor;

    /**
     * Private constructor for frameworks
     */
    private TesteeAttribute() {
    }


    public static final class Builder {
        private TesteeAttributeIdentity testeeAttributeIdentity;
        private String artId;
        private String reportName;
        private String type;
        private String label;
        private String atLogin;
        private int sortOrder;
        private boolean latencySite;
        private boolean showOnProctor;

        public Builder(final String tdsAttributeId, final String clientName) {
            this.testeeAttributeIdentity = new TesteeAttributeIdentity(tdsAttributeId, clientName);
        }

        public Builder withTesteeAttributeIdentity(TesteeAttributeIdentity testeeAttributeIdentity) {
            this.testeeAttributeIdentity = testeeAttributeIdentity;
            return this;
        }

        public Builder withArtId(String artId) {
            this.artId = artId;
            return this;
        }

        public Builder withReportName(String reportName) {
            this.reportName = reportName;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
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

        public Builder withSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder withLatencySite(boolean latencySite) {
            this.latencySite = latencySite;
            return this;
        }

        public Builder withShowOnProctor(boolean showOnProctor) {
            this.showOnProctor = showOnProctor;
            return this;
        }

        public TesteeAttribute build() {
            TesteeAttribute testeeAttribute = new TesteeAttribute();
            testeeAttribute.reportName = this.reportName;
            testeeAttribute.atLogin = this.atLogin;
            testeeAttribute.latencySite = this.latencySite;
            testeeAttribute.showOnProctor = this.showOnProctor;
            testeeAttribute.type = this.type;
            testeeAttribute.artId = this.artId;
            testeeAttribute.label = this.label;
            testeeAttribute.testeeAttributeIdentity = this.testeeAttributeIdentity;
            testeeAttribute.sortOrder = this.sortOrder;
            return testeeAttribute;
        }
    }


    @EmbeddedId
    public TesteeAttributeIdentity getTesteeAttributeIdentity() {
        return testeeAttributeIdentity;
    }

    @Column(name = "rtsname")
    public String getArtId() {
        return artId;
    }

    @Column(name = "reportname")
    public String getReportName() {
        return reportName;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    @Column(name = "atlogin")
    public String getAtLogin() {
        return atLogin;
    }

    @Column(name = "sortorder")
    public int getSortOrder() {
        return sortOrder;
    }

    @Column(name = "islatencysite")
    public boolean isLatencySite() {
        return latencySite;
    }

    @Column(name = "showonproctor")
    public boolean isShowOnProctor() {
        return showOnProctor;
    }

    private void setTesteeAttributeIdentity(final TesteeAttributeIdentity testeeAttributeIdentity) {
        this.testeeAttributeIdentity = testeeAttributeIdentity;
    }

    private void setArtId(final String artId) {
        this.artId = artId;
    }

    private void setReportName(final String reportName) {
        this.reportName = reportName;
    }

    private void setType(final String type) {
        this.type = type;
    }

    private void setLabel(final String label) {
        this.label = label;
    }

    private void setAtLogin(final String atLogin) {
        this.atLogin = atLogin;
    }

    private void setSortOrder(final int sortOrder) {
        this.sortOrder = sortOrder;
    }

    private void setLatencySite(final boolean latencySite) {
        this.latencySite = latencySite;
    }

    private void setShowOnProctor(final boolean showOnProctor) {
        this.showOnProctor = showOnProctor;
    }
}
