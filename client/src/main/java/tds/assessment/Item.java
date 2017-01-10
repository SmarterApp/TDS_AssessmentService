package tds.assessment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A model representing an item for an {@link tds.assessment.Assessment}
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
    private Set<String> formKeys;
    private String itemFilePath;
    private String stimulusFilePath;
    private boolean isPrintable;

    /**
     * Private empty constructor for frameworks
     */
    private Item() {
    }

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

    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return The question type of the {@link tds.assessment.Item}
     */
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the key of the {@link tds.assessment.Segment} the item belongs to
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    public void setSegmentKey(String segmentKey) {
        this.segmentKey = segmentKey;
    }

    /**
     * @return the id of the item group the item belongs to
     */
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the key of the item group the item belongs to
     */
    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    /**
     * @return the position of the item in the {@link tds.assessment.Segment}
     */
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return flags whether this item is a field test item
     */
    public boolean isFieldTest() {
        return fieldTest;
    }

    public void setFieldTest(boolean fieldTest) {
        this.fieldTest = fieldTest;
    }

    /**
     * @return the strand of the item
     */
    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    /**
     * @return the collection of item properties for the items associated with this Item
     */
    public List<ItemProperty> getItemProperties() {
        return itemProperties != null ? itemProperties : new ArrayList<ItemProperty>();
    }

    public void setItemProperties(List<ItemProperty> itemProperties) {
        this.itemProperties = itemProperties;
    }

    /**
     * @return The full path and file name of the item's XML file
     */
    public String getItemFilePath() {
        return itemFilePath;
    }

    public void setItemFilePath(String itemFilePath) {
        if (itemFilePath == null) {
            itemFilePath = "";
        }

        this.itemFilePath = itemFilePath;
    }

    /**
     * @return The full path and file name of the item's stimulus file (if there is one)
     */
    public String getStimulusFilePath() {
        return stimulusFilePath;
    }

    public void setStimulusFilePath(String stimulusFilePath) {
        if (stimulusFilePath == null) {
            stimulusFilePath = "";
        }

        this.stimulusFilePath = stimulusFilePath;
    }

    /**
     * @return True if the item can be printed; otherwise false
     */
    public boolean isPrintable() {
        return isPrintable;
    }

    public void setPrintable(boolean printable) {
        isPrintable = printable;
    }

    /**
     * @return A collection of keys that identify the {@link tds.assessment.Form}s that this item is assigned to
     * <p>
     * This collection is only used for assigning Items to the correct {@link tds.assessment.Form} in a fixed-form
     * {@link tds.assessment.Segment}.
     * </p>
     */
    public Set<String> getFormKeys() {
        if (formKeys == null) {
            formKeys = new HashSet<>();
        }
        return formKeys;
    }

    public void setFormKeys(Set<String> formKeys) {
        this.formKeys = formKeys;
    }
}
