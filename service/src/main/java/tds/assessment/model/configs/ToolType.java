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
import java.sql.Timestamp;

@Entity
@Table(name = "client_testtooltype", catalog = "configs")
public class ToolType {
    private final static String DEFAULT_TEST_MODE = "ALL";
    private ToolTypeIdentity toolTypeIdentity;
    private boolean allowChange;
    private boolean tideSelectable;
    private String artFieldName;
    private boolean required;
    private boolean tideSelectableBySubject;
    private boolean selectable;
    private boolean visible;
    private boolean studentControlled;
    private int sortOrder;
    private Timestamp dateEntered;
    private String dependsOnToolType;
    private boolean functional;
    private String testMode;

    /**
     * Private constructor for frameworks
     */
    private ToolType() {
    }

    public static final class Builder {
        private ToolTypeIdentity toolTypeIdentity;
        private boolean allowChange;
        private boolean tideSelectable;
        private String artFieldName;
        private boolean required;
        private boolean tideSelectableBySubject;
        private boolean selectable;
        private boolean visible;
        private boolean studentControlled;
        private int sortOrder;
        private Timestamp dateEntered;
        private String dependsOnToolType;
        private boolean functional;
        private String testMode;

        public Builder(final String clientName, final String context, final String contextType, final String name) {
            this.toolTypeIdentity = new ToolTypeIdentity(clientName, context, contextType, name);
            this.testMode = DEFAULT_TEST_MODE;
        }

        public Builder withAllowChange(boolean allowChange) {
            this.allowChange = allowChange;
            return this;
        }

        public Builder withTideSelectable(boolean tideSelectable) {
            this.tideSelectable = tideSelectable;
            return this;
        }

        public Builder withArtFieldName(String artFieldName) {
            this.artFieldName = artFieldName;
            return this;
        }

        public Builder withRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder withTideSelectableBySubject(boolean tideSelectableBySubject) {
            this.tideSelectableBySubject = tideSelectableBySubject;
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

        public Builder withStudentControlled(boolean studentControlled) {
            this.studentControlled = studentControlled;
            return this;
        }

        public Builder withSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder withDateEntered(Timestamp dateEntered) {
            this.dateEntered = dateEntered;
            return this;
        }

        public Builder withDependsOnToolType(String dependsOnToolType) {
            this.dependsOnToolType = dependsOnToolType;
            return this;
        }

        public Builder withFunctional(boolean functional) {
            this.functional = functional;
            return this;
        }

        public Builder withTestMode(String testMode) {
            this.testMode = testMode;
            return this;
        }

        public ToolType build() {
            ToolType toolType = new ToolType();
            toolType.sortOrder = this.sortOrder;
            toolType.tideSelectable = this.tideSelectable;
            toolType.required = this.required;
            toolType.selectable = this.selectable;
            toolType.studentControlled = this.studentControlled;
            toolType.dateEntered = this.dateEntered;
            toolType.artFieldName = this.artFieldName;
            toolType.visible = this.visible;
            toolType.allowChange = this.allowChange;
            toolType.tideSelectableBySubject = this.tideSelectableBySubject;
            toolType.dependsOnToolType = this.dependsOnToolType;
            toolType.toolTypeIdentity = this.toolTypeIdentity;
            toolType.functional = this.functional;
            toolType.testMode = this.testMode;
            return toolType;
        }
    }

    @EmbeddedId
    public ToolTypeIdentity getToolTypeIdentity() {
        return toolTypeIdentity;
    }

    @Column(name = "allowchange")
    public boolean isAllowChange() {
        return allowChange;
    }

    @Column(name = "tideselectable")
    public boolean isTideSelectable() {
        return tideSelectable;
    }

    @Column(name = "rtsfieldname")
    public String getArtFieldName() {
        return artFieldName;
    }

    @Column(name = "isrequired")
    public boolean isRequired() {
        return required;
    }

    @Column(name = "tideselectablebysubject")
    public boolean isTideSelectableBySubject() {
        return tideSelectableBySubject;
    }

    @Column(name = "isselectable")
    public boolean isSelectable() {
        return selectable;
    }

    @Column(name = "isvisible")
    public boolean isVisible() {
        return visible;
    }

    @Column(name = "studentcontrol")
    public boolean isStudentControlled() {
        return studentControlled;
    }

    @Column(name = "sortorder")
    public int getSortOrder() {
        return sortOrder;
    }

    @Column(name = "dateentered")
    public Timestamp getDateEntered() {
        return dateEntered;
    }

    @Column(name = "dependsontooltype")
    public String getDependsOnToolType() {
        return dependsOnToolType;
    }

    @Column(name = "isfunctional")
    public boolean isFunctional() {
        return functional;
    }

    @Column(name = "testmode")
    public String getTestMode() {
        return testMode;
    }

    private void setToolTypeIdentity(final ToolTypeIdentity toolTypeIdentity) {
        this.toolTypeIdentity = toolTypeIdentity;
    }

    private void setAllowChange(final boolean allowChange) {
        this.allowChange = allowChange;
    }

    private void setTideSelectable(final boolean tideSelectable) {
        this.tideSelectable = tideSelectable;
    }

    private void setArtFieldName(final String artFieldName) {
        this.artFieldName = artFieldName;
    }

    private void setRequired(final boolean required) {
        this.required = required;
    }

    private void setTideSelectableBySubject(final boolean tideSelectableBySubject) {
        this.tideSelectableBySubject = tideSelectableBySubject;
    }

    private void setSelectable(final boolean selectable) {
        this.selectable = selectable;
    }

    private void setVisible(final boolean visible) {
        this.visible = visible;
    }

    private void setStudentControlled(final boolean studentControlled) {
        this.studentControlled = studentControlled;
    }

    private void setSortOrder(final int sortOrder) {
        this.sortOrder = sortOrder;
    }

    private void setDateEntered(final Timestamp dateEntered) {
        this.dateEntered = dateEntered;
    }

    private void setDependsOnToolType(final String dependsOnToolType) {
        this.dependsOnToolType = dependsOnToolType;
    }

    private void setFunctional(final boolean functional) {
        this.functional = functional;
    }

    private void setTestMode(final String testMode) {
        this.testMode = testMode;
    }
}
