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

package tds.assessment;

import org.joda.time.Instant;

import java.util.Objects;

/**
 * Contains information for the assessment time windows
 */
public class AssessmentWindow {
    private String windowId;
    private int windowMaxAttempts;
    private String mode;
    private int modeMaxAttempts;
    private Instant startTime;
    private Instant endTime;
    private int windowSessionType;
    private int modeSessionType;
    private String formKey;
    private String assessmentKey;

    private AssessmentWindow(AssessmentWindow.Builder builder) {
        windowId = builder.windowId;
        windowMaxAttempts = builder.windowMaxAttempts;
        mode = builder.mode;
        modeMaxAttempts = builder.modeMaxAttempts;
        startTime = builder.startTime;
        endTime = builder.endTime;
        windowSessionType = builder.windowSessionType;
        modeSessionType = builder.modeSessionType;
        formKey = builder.formKey;
        assessmentKey = builder.assessmentKey;
    }

    /**
     * Empty constructor for frameworks
     */
    private AssessmentWindow() {
    }

    /**
     * @return the window id
     */
    public String getWindowId() {
        return windowId;
    }

    /**
     * @return the max number of attempts allowed for the window
     */
    public int getWindowMaxAttempts() {
        return windowMaxAttempts;
    }

    /**
     * @return the window mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @return the max number of attempts allowed for the mode
     */
    public int getModeMaxAttempts() {
        return modeMaxAttempts;
    }

    /**
     * @return the window start time
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * @return the window end time
     */
    public Instant getEndTime() {
        return endTime;
    }

    /**
     * @return the session type associated with the window
     */
    public int getWindowSessionType() {
        return windowSessionType;
    }

    /**
     * @return the session type associated with the mode
     */
    public int getModeSessionType() {
        return modeSessionType;
    }

    /**
     * @return the form key associated with the window.  If the window is a form window
     */
    public String getFormKey() {
        return formKey;
    }

    /**
     * @return the assessment key associated with the window
     */
    public String getAssessmentKey() {
        return assessmentKey;
    }

    public static class Builder {
        private String windowId;
        private int windowMaxAttempts;
        private String mode;
        private int modeMaxAttempts;
        private Instant startTime;
        private Instant endTime;
        private int windowSessionType;
        private int modeSessionType;
        private String formKey;
        private String assessmentKey;

        public Builder withWindowId(String windowId) {
            this.windowId = windowId;
            return this;
        }

        public Builder withWindowMaxAttempts(int windowMaxAttempts) {
            this.windowMaxAttempts = windowMaxAttempts;
            return this;
        }

        public Builder withMode(String mode) {
            this.mode = mode;
            return this;
        }

        public Builder withModeMaxAttempts(int modeMaxAttempts) {
            this.modeMaxAttempts = modeMaxAttempts;
            return this;
        }

        public Builder withStartTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder withEndTime(Instant endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder withWindowSessionType(int windowSessionType) {
            this.windowSessionType = windowSessionType;
            return this;
        }

        public Builder withModeSessionType(int modeSessionType) {
            this.modeSessionType = modeSessionType;
            return this;
        }

        public Builder withFormKey(String formKey) {
            this.formKey = formKey;
            return this;
        }

        public Builder withAssessmentKey(String assessmentKey) {
            this.assessmentKey = assessmentKey;
            return this;
        }

        public AssessmentWindow build() {
            return new AssessmentWindow(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssessmentWindow that = (AssessmentWindow) o;

        if (windowMaxAttempts != that.windowMaxAttempts) return false;
        if (modeMaxAttempts != that.modeMaxAttempts) return false;
        if (windowSessionType != that.windowSessionType) return false;
        if (modeSessionType != that.modeSessionType) return false;
        if (!Objects.equals(windowId, that.windowId)) return false;
        if (!Objects.equals(formKey, that.formKey)) return false;
        return assessmentKey.equals(that.assessmentKey);
    }

    @Override
    public int hashCode() {
        int result = windowId != null ? windowId.hashCode() : 0;
        result = 31 * result + windowMaxAttempts;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + modeMaxAttempts;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + windowSessionType;
        result = 31 * result + modeSessionType;
        result = 31 * result + (formKey != null ? formKey.hashCode() : 0);
        result = 31 * result + (assessmentKey != null ? assessmentKey.hashCode() : 0);
        return result;
    }
}
