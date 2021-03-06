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
 * Represents active assessment properties
 */
public class ItemProperty {
    private String itemId;
    private String name;
    private String value;
    private String description;

    public ItemProperty(String name, String value, String description, String itemId) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.itemId = itemId;
    }

    /**
     * private constructor for frameworks
     */
    private ItemProperty() {}

    /**
     * @return the id of the {@link tds.assessment.Item} the property is associated with
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @return the property name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the description for the property if present
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemProperty that = (ItemProperty) o;

        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
