package tds.assessment;

import java.util.List;

/**
 * Represents admin information concerning a segment
 */
public class Segment {
    private String key;
    private String segmentId;
    private String selectionAlgorithm;
    private float startAbility;
    private String subject;
    private String assessmentKey;
    private int position;private int minItems;
    private int maxItems;
    private int fieldTestMinItems;
    private int fieldTestMaxItems;
    private List<ItemProperty> languages;
    private List<Form> forms;
    private List<Item> items;

    /**
     * Empty constructor for frameworks
     */
    private Segment() {}

    public Segment(String key) {
        this.key = key;
    }

    /**
     * @return key to the segment
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the key to the assessment this segment is a part of
     */
    public String getAssessmentKey() {
        return assessmentKey;
    }

    /**
     * @return the associated segment id
     */
    public String getSegmentId() {
        return segmentId;
    }

    /**
     * @return the selection algorithm key for the segment
     */
    public String getSelectionAlgorithm() {
        return selectionAlgorithm;
    }

    /**
     * @return the start ability value for the segment
     */
    public float getStartAbility() {
        return startAbility;
    }

    /**
     * @return the subject name - this can be null
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return languages associated with the segment
     */
    public List<ItemProperty> getLanguages() {
        return languages;
    }

    /**
     * @return the position of the segment in the {@link Assessment}
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the minimum number of items in the {@link Segment}
     */
    public int getMinItems() {
        return minItems;
    }

    /**
     * @return the minimum number of items in the {@link Segment}
     */
    public int getMaxItems() {
        return maxItems;
    }

    /**
     * @return the minimum number of field test items in the {@link tds.assessment.Segment}
     */
    public int getFieldTestMinItems() {
        return fieldTestMinItems;
    }

    /**
     * @return the maximum number of field test items in the {@link tds.assessment.Segment}
     */
    public int getFieldTestMaxItems() {
        return fieldTestMaxItems;
    }

    /**
     * @return return the forms that are a part of this assessment's {@link tds.assessment.Segment}
     */
    public List<Form> getForms() {
        return forms;
    }

    /**
     * @return return the items that a part of this assessment's {@link tds.assessment.Segment}
     */
    public List<Item> getItems() {
        return items;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public void setSelectionAlgorithm(String selectionAlgorithm) {
        this.selectionAlgorithm = selectionAlgorithm;
    }

    public void setStartAbility(float startAbility) {
        this.startAbility = startAbility;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setAssessmentKey(String assessmentKey) {
        this.assessmentKey = assessmentKey;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setMinItems(int minItems) {
        this.minItems = minItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public void setFieldTestMinItems(int fieldTestMinItems) {
        this.fieldTestMinItems = fieldTestMinItems;
    }

    public void setFieldTestMaxItems(int fieldTestMaxItems) {
        this.fieldTestMaxItems = fieldTestMaxItems;
    }

    public void setLanguages(List<ItemProperty> languages) {
        this.languages = languages;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
