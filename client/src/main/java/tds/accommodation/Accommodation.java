package tds.accommodation;

/**
 * Represents an Assessment accommodation
 */
public class Accommodation {
    private int segmentPosition;
    private boolean disableOnGuestSession;
    private int toolTypeSortOrder;
    private int toolValueSortOrder;
    private String toolMode;
    private String type;
    private String value;
    private String code;
    private boolean defaultAccommodation;
    private boolean allowCombine;
    private boolean allowChange;
    private boolean functional;
    private boolean selectable;
    private boolean visible;
    private boolean studentControl;
    private boolean entryControl;
    private String dependsOnToolType;
    private String typeMode;
    private String segmentKey;
    private int typeTotal;

    public static final String ACCOMMODATION_TYPE_LANGUAGE = "Language";

    /**
     * Used by Jackson Framework
     */
    private Accommodation() {
    }

    /**
     * @return the segment position which relates to this accommodation.  Zero if non segmented assessment
     */
    public int getSegmentPosition() {
        return segmentPosition;
    }

    /**
     * @return {@code true} if accommodation should be disabled during guest session
     */
    public boolean isDisableOnGuestSession() {
        return disableOnGuestSession;
    }

    /**
     * @return used to sort by type
     */
    public int getToolTypeSortOrder() {
        return toolTypeSortOrder;
    }

    /**
     * @return used to sort by value
     */
    public int getToolValueSortOrder() {
        return toolValueSortOrder;
    }

    /**
     * @return the tool mode
     */
    public String getToolMode() {
        return toolMode;
    }

    /**
     * @return the type of the accommodation
     */
    public String getType() {
        return type;
    }

    /**
     * @return the value of the accommodation
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the code of the accommodation
     */
    public String getCode() {
        return code;
    }

    /**
     * @return {@code true} if it can be combined
     */
    public boolean isAllowCombine() {
        return allowCombine;
    }

    /**
     * @return {@code true} if the accommodation is functional
     */
    public boolean isFunctional() {
        return functional;
    }

    /**
     * @return {@code true} if the accommodation can be selected
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * @return {@code true} if the accommodation is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @return {@code true} if the student can control the accommodation
     */
    public boolean isStudentControl() {
        return studentControl;
    }

    /**
     * @return {@code true} if the entry can be controlled
     */
    public boolean isEntryControl() {
        return entryControl;
    }

    /**
     * @return the tool type that this accommodation depends on
     */
    public String getDependsOnToolType() {
        return dependsOnToolType;
    }

    /**
     * @return {@code true} if this is the default accommodation
     */
    public boolean isDefaultAccommodation() {
        return defaultAccommodation;
    }

    /**
     * @return the type mode
     */
    public String getTypeMode() {
        return typeMode;
    }

    /**
     * @return {@code true} allow changes
     */
    public boolean isAllowChange() {
        return allowChange;
    }

    /**
     * @return the segment key if segmented accommodation otherwise null
     */
    public String getSegmentKey() {
        return segmentKey;
    }

    public int getTypeTotal() {
        return typeTotal;
    }

    public static final class Builder {
        private int segmentPosition;
        private boolean disableOnGuestSession;
        private int toolTypeSortOrder;
        private int toolValueSortOrder;
        private String toolMode;
        private String accommodationType;
        private String accommodationValue;
        private String accommodationCode;
        private boolean allowCombine;
        private boolean functional;
        private boolean selectable;
        private boolean visible;
        private boolean studentControl;
        private boolean entryControl;
        private String dependsOnToolType;
        private boolean defaultAccommodation;
        private String typeMode;
        private boolean allowChange;
        private String segmentKey;
        private int typeTotal;

        public Builder withSegmentPosition(int segmentPosition) {
            this.segmentPosition = segmentPosition;
            return this;
        }

        public Builder withDisableOnGuestSession(boolean disableOnGuestSession) {
            this.disableOnGuestSession = disableOnGuestSession;
            return this;
        }

        public Builder withToolTypeSortOrder(int toolTypeSortOrder) {
            this.toolTypeSortOrder = toolTypeSortOrder;
            return this;
        }

        public Builder withToolValueSortOrder(int toolValueSortOrder) {
            this.toolValueSortOrder = toolValueSortOrder;
            return this;
        }

        public Builder withToolMode(String toolMode) {
            this.toolMode = toolMode;
            return this;
        }

        public Builder withAccommodationType(String accType) {
            this.accommodationType = accType;
            return this;
        }

        public Builder withAccommodationValue(String accValue) {
            this.accommodationValue = accValue;
            return this;
        }

        public Builder withAccommodationCode(String accCode) {
            this.accommodationCode = accCode;
            return this;
        }

        public Builder withAllowCombine(boolean allowCombine) {
            this.allowCombine = allowCombine;
            return this;
        }

        public Builder withFunctional(boolean functional) {
            this.functional = functional;
            return this;
        }

        public Builder withSelectable(boolean selectable) {
            this.selectable = selectable;
            return this;
        }

        public Builder withVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder withStudentControl(boolean studentControl) {
            this.studentControl = studentControl;
            return this;
        }

        public Builder withEntryControl(boolean entryControl) {
            this.entryControl = entryControl;
            return this;
        }

        public Builder withDependsOnToolType(String dependsOnToolType) {
            this.dependsOnToolType = dependsOnToolType;
            return this;
        }

        public Builder withDefaultAccommodation(boolean defaultAccommodation) {
            this.defaultAccommodation = defaultAccommodation;
            return this;
        }

        public Builder withTypeMode(String typeMode) {
            this.typeMode = typeMode;
            return this;
        }

        public Builder withAllowChange(boolean allowChange) {
            this.allowChange = allowChange;
            return this;
        }

        public Builder withSegmentKey(String segmentKey) {
            this.segmentKey = segmentKey;
            return this;
        }

        public Builder withTypeTotal(int typeTotal) {
            this.typeTotal = typeTotal;
            return this;
        }

        public Accommodation build() {
            Accommodation accommodation = new Accommodation();
            accommodation.toolValueSortOrder = this.toolValueSortOrder;
            accommodation.segmentPosition = this.segmentPosition;
            accommodation.value = this.accommodationValue;
            accommodation.selectable = this.selectable;
            accommodation.dependsOnToolType = this.dependsOnToolType;
            accommodation.functional = this.functional;
            accommodation.entryControl = this.entryControl;
            accommodation.studentControl = this.studentControl;
            accommodation.disableOnGuestSession = this.disableOnGuestSession;
            accommodation.toolMode = this.toolMode;
            accommodation.code = this.accommodationCode;
            accommodation.toolTypeSortOrder = this.toolTypeSortOrder;
            accommodation.visible = this.visible;
            accommodation.allowCombine = this.allowCombine;
            accommodation.type = this.accommodationType;
            accommodation.defaultAccommodation = this.defaultAccommodation;
            accommodation.typeMode = this.typeMode;
            accommodation.allowChange = this.allowChange;
            accommodation.segmentKey = this.segmentKey;
            accommodation.typeTotal = this.typeTotal;
            return accommodation;
        }
    }
}
