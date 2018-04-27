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
public class SegmentPropertiesIdentity implements Serializable {
    private String clientName;
    private String assessmentId;
    private String segmentId;

    /**
     * Private constructor for frameworks
     */
    private SegmentPropertiesIdentity() {
    }

    SegmentPropertiesIdentity(final String clientName, final String assessmentId, final String segmentId) {
        this.clientName = clientName;
        this.assessmentId = assessmentId;
        this.segmentId = segmentId;
    }

    @Column(name = "clientname")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "parenttest")
    public String getAssessmentId() {
        return assessmentId;
    }

    @Column(name = "segmentid")
    public String getSegmentId() {
        return segmentId;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setAssessmentId(final String assessmentId) {
        this.assessmentId = assessmentId;
    }

    private void setSegmentId(final String segmentId) {
        this.segmentId = segmentId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SegmentPropertiesIdentity that = (SegmentPropertiesIdentity) o;

        if (!clientName.equals(that.clientName)) return false;
        if (!assessmentId.equals(that.assessmentId)) return false;
        return segmentId.equals(that.segmentId);
    }

    @Override
    public int hashCode() {
        int result = clientName.hashCode();
        result = 31 * result + assessmentId.hashCode();
        result = 31 * result + segmentId.hashCode();
        return result;
    }
}
