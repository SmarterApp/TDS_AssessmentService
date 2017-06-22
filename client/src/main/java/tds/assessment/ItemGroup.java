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

/**
 * Represents item grouping information
 */
public class ItemGroup {
    private String groupId;
    private int requiredItemCount;
    private int maxItems;
    private float bpWeight;

    private ItemGroup() {
    }

    /**
     * @param groupId           item group id
     * @param requiredItemCount the minimum item count
     * @param maxItems          the max number of items
     * @param bpWeight          the blueprint weight
     */
    public ItemGroup(final String groupId, final int requiredItemCount, final int maxItems, final float bpWeight) {
        this.groupId = groupId;
        this.requiredItemCount = requiredItemCount;
        this.maxItems = maxItems;
        this.bpWeight = bpWeight;
    }

    /**
     * @return the item group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return the required item count for the group
     */
    public int getRequiredItemCount() {
        return requiredItemCount;
    }

    /**
     * @return max number of items for the group
     */
    public int getMaxItems() {
        return maxItems;
    }

    /**
     * @return the blueprint weight
     */
    public float getBpWeight() {
        return bpWeight;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemGroup itemGroup = (ItemGroup) o;

        if (requiredItemCount != itemGroup.requiredItemCount) return false;
        if (maxItems != itemGroup.maxItems) return false;
        if (Float.compare(itemGroup.bpWeight, bpWeight) != 0) return false;
        return groupId != null ? groupId.equals(itemGroup.groupId) : itemGroup.groupId == null;
    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + requiredItemCount;
        result = 31 * result + maxItems;
        result = 31 * result + (bpWeight != +0.0f ? Float.floatToIntBits(bpWeight) : 0);
        return result;
    }
}
