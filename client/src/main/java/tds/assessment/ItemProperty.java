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
        this(name, value, description);
        this.itemId = itemId;
    }

    public ItemProperty(String name, String value, String description) {
        this(name, value);
        this.description = description;
    }

    public ItemProperty(String name, String value) {
        this.name = name;
        this.value = value;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemProperty itemProperty = (ItemProperty) o;

        if (name != null ? !name.equals(itemProperty.name) : itemProperty.name != null) return false;
        return value != null ? value.equals(itemProperty.value) : itemProperty.value == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
