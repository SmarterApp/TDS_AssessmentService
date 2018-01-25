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

import tds.testpackage.model.Item;

/**
 * A wrapper/helper class to enscapsulate various important pieces of metadata along with an item
 */
public class ItemMetadataWrapper {
    private Item item;
    private String segmentKey;
    private int bankKey;
    private String groupId;
    private boolean adaptive;

    public ItemMetadataWrapper(final tds.testpackage.model.Item item, final int bankKey, final String segmentKey, final String groupId,
                               final boolean adaptive) {
        this.item = item;
        this.segmentKey = segmentKey;
        this.bankKey = bankKey;
        this.groupId = groupId;
        this.adaptive = adaptive;
    }

    public Item getItem() {
        return item;
    }

    public String getSegmentKey() {
        return segmentKey;
    }

    public String getItemKey() {
        return String.format("%s-%s", bankKey, item.getId());
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean isAdaptive() {
        return adaptive;
    }
}