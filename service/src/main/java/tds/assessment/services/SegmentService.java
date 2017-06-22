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

package tds.assessment.services;

import java.util.Optional;

import tds.assessment.SegmentItemInformation;

/**
 * Handles segment specific actions
 */
public interface SegmentService {

    /**
     * Finds a {@link tds.assessment.SegmentItemInformation} which is used for item selection
     *
     * @param segmentKey the segment key
     * @return Optional with {@link tds.assessment.SegmentItemInformation} if found otherwise empty
     */
    Optional<SegmentItemInformation> findSegmentItemInformation(final String segmentKey);
}
