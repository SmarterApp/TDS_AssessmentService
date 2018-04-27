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

import java.util.List;

import tds.testpackage.model.Grade;
import tds.testpackage.model.Item;

/**
 * A wrapper/helper class to enscapsulate various important pieces of metadata along with an item
 */
public class ItemMetadataWrapper {
    private final Item item;
    private final String segmentKey;
    private final String groupId;
    private final boolean adaptive;
    private final List<Grade> grades;

    public ItemMetadataWrapper(final tds.testpackage.model.Item item, final List<Grade> grades, final String segmentKey, final String groupId,
                               final boolean adaptive) {
        this.item = item;
        this.segmentKey = segmentKey;
        this.grades = grades;
        this.groupId = groupId;
        this.adaptive = adaptive;
    }

    /**
     * @return The {@link tds.testpackage.model.Item} wrapped by this object
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return The segment key the item corresponds to
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    /**
     * @return The item group id the {@link tds.testpackage.model.Item} corresponds to
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return A flag indicating whether the item corresponds to an adaptive segment
     */
    public boolean isAdaptive() {
        return adaptive;
    }

    /**
     * @return The list of grades this item is eligible for
     */
    public List<Grade> getGrades() {
        return grades;
    }
}