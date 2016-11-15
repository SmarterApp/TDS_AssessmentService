package tds.assessment;

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
    private boolean active;
    private boolean required;
    private String strand;

    private Item(Builder builder) {
        this.groupKey = builder.groupKey;
        this.segmentKey = builder.segmentKey;
        this.itemType = builder.itemType;
        this.groupId = builder.groupId;
        this.strand = builder.strand;
        this.required = builder.required;
        this.active = builder.active;
        this.fieldTest = builder.fieldTest;
        this.id = builder.id;
        this.position = builder.position;
    }

    public static class Builder {
        private String id;
        private String itemType;
        private String segmentKey;
        private String groupId;
        private String groupKey;
        private int position;
        private boolean fieldTest;
        private boolean active;
        private boolean required;
        private String strand;

        public Builder(String id) {
            this.id = id;
        }


        public Builder withItemType(String itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
            return this;
        }

        public Builder withGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withGroupKey(String groupKey) {
            this.groupKey = groupKey;
            return this;
        }

        public Builder withPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder withFieldTest(boolean fieldTest) {
            this.fieldTest = fieldTest;
            return this;
        }

        public Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder withRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder withStrand(String strand) {
            this.strand = strand;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
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
     * @return flags whether the item is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return the strand of the item
     */
    public String getStrand() {
        return strand;
    }

}
