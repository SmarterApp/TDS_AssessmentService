package tds.assessment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.accommodation.Accommodation;

/**
 * A model representing an item for an {@link tds.assessment.Assessment}
 */
@JsonIgnoreProperties(value = "languageCode", allowGetters = true)
public class Item {
    private String id;
    private String itemType;
    private String segmentKey;
    private String groupId;
    private String groupKey;
    private String blockId;
    private int position;
    private int formPosition;
    private boolean fieldTest;
    private boolean required;
    private String strand;
    private List<ItemProperty> itemProperties = new ArrayList<>();
    private Set<String> formKeys;
    private String itemFilePath;
    private String stimulusFilePath;
    private boolean isPrintable;
    private String clientId;
    private String mimeType;
    private String contentLevel;
    private boolean notForScoring;
    private int maxScore;
    private String itemResponseTheoryModel;
    private Float itemResponseTheoryAParameter;
    private String itemResponseTheoryBParameter;
    private Float itemResponseTheoryCParameter;
    private String bVector;
    private String claims;
    private boolean active;
    private long itemKey;
    private long bankKey;

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
     * @return the block id of the item group, used to group items into blocks for the field test selection algorithm.
     * Typically "A" for most items.
     */
    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
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
     * @return The position of this item in a {@link tds.assessment.Form} that is associated to a segment that uses the
     * fixed-form selection algorithm.
     * <p>
     *     This field is only relevant for items that are displayed on a fixed-form test.
     * </p>
     */
    public int getFormPosition() {
        return formPosition;
    }

    public void setFormPosition(final int formPosition) {
        this.formPosition = formPosition;
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

    /**
     * Finds the language code of the item based on its item properties.
     *
     * @return The language code of the item if one exists in its properties - otherwise null
     */
    public String getLanguageCode() {
        for (ItemProperty property : itemProperties) {
            if (Accommodation.ACCOMMODATION_TYPE_LANGUAGE.equalsIgnoreCase(property.getName())) {
                return property.getValue();
            }
        }

        return null;
    }

    /**
     * @return Also referred to as the "itemId", an optional property defined in the test package
     */
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return The expected mimetype of this item's response
     */
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return The content level of the item - a subset of the strand
     */
    public String getContentLevel() {
        return contentLevel;
    }

    public void setContentLevel(String contentLevel) {
        this.contentLevel = contentLevel;
    }

    /**
     * @return Flag indicating whether or not this item should or should not be scored
     */
    public boolean isNotForScoring() {
        return notForScoring;
    }

    public void setNotForScoring(boolean notForScoring) {
        this.notForScoring = notForScoring;
    }

    /**
     * @return The maximum possible score for an assessment item
     */
    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return item response theory model
     */
    public String getItemResponseTheoryModel() {
        return itemResponseTheoryModel;
    }

    public void setItemResponseTheoryModel(final String itemResponseTheoryModel) {
        this.itemResponseTheoryModel = itemResponseTheoryModel;
    }

    /**
     * @return item response theory A parameter.  There are 3 parameters passed in for the statistical models (a, b, c)
     */
    public Float getItemResponseTheoryAParameter() {
        return itemResponseTheoryAParameter;
    }

    public void setItemResponseTheoryAParameter(final Float itemResponseTheoryAParameter) {
        this.itemResponseTheoryAParameter = itemResponseTheoryAParameter;
    }

    /**
     * @return item response theory B parameter.  There are 3 parameters passed in for the statistical models (a, b, c)
     */
    public String getItemResponseTheoryBParameter() {
        return itemResponseTheoryBParameter;
    }

    public void setItemResponseTheoryBParameter(final String itemResponseTheoryBParameter) {
        this.itemResponseTheoryBParameter = itemResponseTheoryBParameter;
    }

    /**
     * @return item response theory C parameter.  There are 3 parameters passed in for the statistical models (a, b, c)
     */
    public Float getItemResponseTheoryCParameter() {
        return itemResponseTheoryCParameter;
    }

    public void setItemResponseTheoryCParameter(final Float itemResponseTheoryCParameter) {
        this.itemResponseTheoryCParameter = itemResponseTheoryCParameter;
    }

    /**
     * @return item b vector
     */
    public String getbVector() {
        return bVector;
    }

    public void setbVector(final String bVector) {
        this.bVector = bVector;
    }

    /**
     * @return the claims associated with the item
     */
    public String getClaims() {
        return claims;
    }

    public void setClaims(final String claims) {
        this.claims = claims;
    }

    /**
     * @return {@code true} if the item is active
     */
    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    /**
     * @return the item key.  This is a fk to the item in the item hierarchy
     */
    public long getItemKey() {
        return itemKey;
    }

    public void setItemKey(final long itemKey) {
        this.itemKey = itemKey;
    }

    /**
     * @return the item bank key which is a fk to the bank in the item hierarchy
     */
    public long getBankKey() {
        return bankKey;
    }

    public void setBankKey(final long bankKey) {
        this.bankKey = bankKey;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Item item = (Item) o;

        if (position != item.position) return false;
        if (fieldTest != item.fieldTest) return false;
        if (required != item.required) return false;
        if (isPrintable != item.isPrintable) return false;
        if (notForScoring != item.notForScoring) return false;
        if (maxScore != item.maxScore) return false;
        if (active != item.active) return false;
        if (itemKey != item.itemKey) return false;
        if (bankKey != item.bankKey) return false;
        if (id != null ? !id.equals(item.id) : item.id != null) return false;
        if (itemType != null ? !itemType.equals(item.itemType) : item.itemType != null) return false;
        if (segmentKey != null ? !segmentKey.equals(item.segmentKey) : item.segmentKey != null) return false;
        if (groupId != null ? !groupId.equals(item.groupId) : item.groupId != null) return false;
        if (groupKey != null ? !groupKey.equals(item.groupKey) : item.groupKey != null) return false;
        if (blockId != null ? !blockId.equals(item.blockId) : item.blockId != null) return false;
        if (strand != null ? !strand.equals(item.strand) : item.strand != null) return false;
        if (itemProperties != null ? !itemProperties.equals(item.itemProperties) : item.itemProperties != null)
            return false;
        if (formKeys != null ? !formKeys.equals(item.formKeys) : item.formKeys != null) return false;
        if (itemFilePath != null ? !itemFilePath.equals(item.itemFilePath) : item.itemFilePath != null) return false;
        if (stimulusFilePath != null ? !stimulusFilePath.equals(item.stimulusFilePath) : item.stimulusFilePath != null)
            return false;
        if (clientId != null ? !clientId.equals(item.clientId) : item.clientId != null) return false;
        if (mimeType != null ? !mimeType.equals(item.mimeType) : item.mimeType != null) return false;
        if (contentLevel != null ? !contentLevel.equals(item.contentLevel) : item.contentLevel != null) return false;
        if (itemResponseTheoryModel != null ? !itemResponseTheoryModel.equals(item.itemResponseTheoryModel) : item.itemResponseTheoryModel != null)
            return false;
        if (itemResponseTheoryAParameter != null ? !itemResponseTheoryAParameter.equals(item.itemResponseTheoryAParameter) : item.itemResponseTheoryAParameter != null)
            return false;
        if (itemResponseTheoryBParameter != null ? !itemResponseTheoryBParameter.equals(item.itemResponseTheoryBParameter) : item.itemResponseTheoryBParameter != null)
            return false;
        if (itemResponseTheoryCParameter != null ? !itemResponseTheoryCParameter.equals(item.itemResponseTheoryCParameter) : item.itemResponseTheoryCParameter != null)
            return false;
        if (bVector != null ? !bVector.equals(item.bVector) : item.bVector != null) return false;
        return claims != null ? claims.equals(item.claims) : item.claims == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (segmentKey != null ? segmentKey.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (groupKey != null ? groupKey.hashCode() : 0);
        result = 31 * result + (blockId != null ? blockId.hashCode() : 0);
        result = 31 * result + position;
        result = 31 * result + (fieldTest ? 1 : 0);
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + (strand != null ? strand.hashCode() : 0);
        result = 31 * result + (itemProperties != null ? itemProperties.hashCode() : 0);
        result = 31 * result + (formKeys != null ? formKeys.hashCode() : 0);
        result = 31 * result + (itemFilePath != null ? itemFilePath.hashCode() : 0);
        result = 31 * result + (stimulusFilePath != null ? stimulusFilePath.hashCode() : 0);
        result = 31 * result + (isPrintable ? 1 : 0);
        result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (contentLevel != null ? contentLevel.hashCode() : 0);
        result = 31 * result + (notForScoring ? 1 : 0);
        result = 31 * result + maxScore;
        result = 31 * result + (itemResponseTheoryModel != null ? itemResponseTheoryModel.hashCode() : 0);
        result = 31 * result + (itemResponseTheoryAParameter != null ? itemResponseTheoryAParameter.hashCode() : 0);
        result = 31 * result + (itemResponseTheoryBParameter != null ? itemResponseTheoryBParameter.hashCode() : 0);
        result = 31 * result + (itemResponseTheoryCParameter != null ? itemResponseTheoryCParameter.hashCode() : 0);
        result = 31 * result + (bVector != null ? bVector.hashCode() : 0);
        result = 31 * result + (claims != null ? claims.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (int) (itemKey ^ (itemKey >>> 32));
        result = 31 * result + (int) (bankKey ^ (bankKey >>> 32));
        return result;
    }
}
