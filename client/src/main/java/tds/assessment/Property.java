package tds.assessment;

/**
 * Represents active assessment properties
 */
public class Property {
    private String name;
    private String value;
    private String description;

    public Property(String name, String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
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

        Property property = (Property) o;

        if (name != null ? !name.equals(property.name) : property.name != null) return false;
        return value != null ? value.equals(property.value) : property.value == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
