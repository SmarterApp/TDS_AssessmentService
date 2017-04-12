package tds.assessment;

/**
 * Item control parameters
 */
public class ItemControlParameter {
    private String blueprintElementId;
    private String name;
    private String value;

    //For Frameworks
    private ItemControlParameter(){
    }

    public ItemControlParameter(final String blueprintElementId, final String name, final String value) {
        this.blueprintElementId = blueprintElementId;
        this.name = name;
        this.value = value;
    }

    /**
     * @return blueprint element id
     */
    public String getBlueprintElementId() {
        return blueprintElementId;
    }

    /**
     * @return the name for the parameter
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value for the parameter
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemControlParameter parameter = (ItemControlParameter) o;

        if (blueprintElementId != null ? !blueprintElementId.equals(parameter.blueprintElementId) : parameter.blueprintElementId != null)
            return false;
        if (name != null ? !name.equals(parameter.name) : parameter.name != null) return false;
        return value != null ? value.equals(parameter.value) : parameter.value == null;
    }

    @Override
    public int hashCode() {
        int result = blueprintElementId != null ? blueprintElementId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
