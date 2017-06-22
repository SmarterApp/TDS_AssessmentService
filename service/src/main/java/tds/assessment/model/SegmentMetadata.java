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
 * Represents metadata for a single segment
 */
public class SegmentMetadata {
    private final String segmentKey;
    private final String parentKey;
    private final String clientName;

    public SegmentMetadata(final String segmentKey, final String parentKey, final String clientName) {
        this.segmentKey = segmentKey;
        this.parentKey = parentKey;
        this.clientName = clientName;
    }

    /**
     * @return key for the segment
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    /**
     * @return assessment key if part of a multi segmented assessment.  Single segment assessments will be null
     */
    public String getParentKey() {
        return parentKey;
    }

    /**
     * @return client name associated with the segment
     */
    public String getClientName() {
        return clientName;
    }
}
