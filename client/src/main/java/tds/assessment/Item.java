package tds.assessment;

import java.util.ArrayList;
import java.util.List;

/**
 * A model representing an item for an {@link Assessment}
 */
public class Item {
    private String id;
    private String itemType;
    private String segmentKey;
    private String groupId;
    private String groupKey;
    private int position;
    private boolean fieldTest;
    private boolean required;
    private String strand;
    private List<ItemProperty> itemProperties;

    /**
     * Private empty constructor for frameworks
     */
    private Item() {}

    public Item(String id) {
        this.id = id;
    }

    /**
     * @return the item id
     */
    public String getId() {
        return id;
    }

    /**
     * @return flag that dictates whether an item answer is required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @return The question type of the {@link tds.assessment.Item}
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @return the key of the {@link tds.assessment.Segment} the item belongs to
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    /**
     * @return the id of the item group the item belongs to
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return the key of the item group the item belongs to
     */
    public String getGroupKey() {
        return groupKey;
    }

    /**
     * @return the position of the item in the {@link tds.assessment.Segment}
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return  flags whether this item is a field test item
     */
    public boolean isFieldTest() {
        return fieldTest;
    }

    /**
     * @return the strand of the item
     */
    public String getStrand() {
        return strand;
    }

    /**
     * @return the
     */
    public List<ItemProperty> getItemProperties() {
        return itemProperties != null ? itemProperties : new ArrayList<ItemProperty>();
    }


    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setSegmentKey(String segmentKey) {
        this.segmentKey = segmentKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setFieldTest(boolean fieldTest) {
        this.fieldTest = fieldTest;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public void setItemProperties(List<ItemProperty> itemProperties) {
        this.itemProperties = itemProperties;
    }
}
