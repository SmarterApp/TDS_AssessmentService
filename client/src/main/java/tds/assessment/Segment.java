package tds.assessment;

import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents admin information common to all segments, regardless of the selection algorithm.
 */
public class Segment {
    private final String key;
    private final Algorithm selectionAlgorithm;
    private String segmentId;
    private float startAbility;
    private String subject;
    private String assessmentKey;
    private int position;
    private int minItems;
    private int maxItems;
    private int fieldTestMinItems;
    private int fieldTestMaxItems;
    private Instant fieldTestStartDate;
    private Instant fieldTestEndDate;

    // Fixed-form specific fields
    private List<Form> forms;

    // Adaptive-specific fields
    private Set<Strand> strands;
    private List<Item> items;

    public Segment(String key, Algorithm algorithm) {
        this.key = key;
        this.selectionAlgorithm = algorithm;
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

    public void setAssessmentKey(String assessmentKey) {
        this.assessmentKey = assessmentKey;
    }

    /**
     * @return the associated segment id
     */
    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    /**
     * @return the selection algorithm key for the segment
     */
    public Algorithm getSelectionAlgorithm() {
        return selectionAlgorithm;
    }

    /**
     * @return the start ability value for the segment
     */
    public float getStartAbility() {
        return startAbility;
    }

    public void setStartAbility(float startAbility) {
        this.startAbility = startAbility;
    }

    /**
     * @return the subject name - this can be null
     */
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the position of the segment in the {@link Assessment}
     */
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return The minimum number of items for this {@link tds.assessment.Segment}
     */
    public int getMinItems() {
        return minItems;
    }

    public void setMinItems(int minItems) {
        this.minItems = minItems;
    }

    /**
     * @return The maximum number of items for this {@link tds.assessment.Segment}
     */
    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    /**
     * @return the minimum number of field test items in the {@link tds.assessment.Segment}
     */
    public int getFieldTestMinItems() {
        return fieldTestMinItems;
    }

    public void setFieldTestMinItems(int fieldTestMinItems) {
        this.fieldTestMinItems = fieldTestMinItems;
    }

    /**
     * @return the maximum number of field test items in the {@link tds.assessment.Segment}
     */
    public int getFieldTestMaxItems() {
        return fieldTestMaxItems;
    }

    public void setFieldTestMaxItems(int fieldTestMaxItems) {
        this.fieldTestMaxItems = fieldTestMaxItems;
    }

    /**
     * @return the field test start date
     */
    public Instant getFieldTestStartDate() {
        return fieldTestStartDate;
    }

    public void setFieldTestStartDate(Instant fieldTestStartDate) {
        this.fieldTestStartDate = fieldTestStartDate;
    }

    /**
     * @return the field test end date
     */
    public Instant getFieldTestEndDate() {
        return fieldTestEndDate;
    }

    public void setFieldTestEndDate(Instant fieldTestEndDate) {
        this.fieldTestEndDate = fieldTestEndDate;
    }

    /**
     * @return All the {@link tds.assessment.Form}s associated with this segment.
     * <p>
     *     This getter/setter is only used by {@link tds.assessment.Segment}s that are configured for the fixed form
     *     {@link tds.assessment.Algorithm}.  Segments configured for the "adaptive2" algorithm do not have forms, in which case an empty
     *     {@code ArrayList} is returned
     * </p>
     */
    public List<Form> getForms() {

        if (forms == null) {
            forms = new ArrayList<>();
        }

        return forms;

    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    /**
     * Choose the {@link tds.assessment.Form} for the specified language.
     * <p>
     *     This method is only used by {@link tds.assessment.Segment}s that are configured for the fixed form
     *     {@link tds.assessment.Algorithm}.  Segments configured for the adaptive algorithm do not have forms, in which
     *     case this method will throw an java.lang.IllegalArgumentException.
     * </p>
     *
     * @param languageCode The student's specified language
     * @return The {@link tds.assessment.Form} for the specified language
     * @throws java.lang.IllegalArgumentException if the languageCode does not match any of the forms contained within
     * this Segment
     */
    public Form getForm(String languageCode) {
        if (forms == null) {
            forms = new ArrayList<>();
        }

        for (Form f : forms) {
            if (f.getLanguage().equalsIgnoreCase(languageCode)) {
                return f;
            }
        }

        throw new IllegalArgumentException(String.format("Could not find a Form for language code '%s'", languageCode));
    }

    /**
     * @return the collection of {@link tds.assessment.Strand}s containing adaptive algorithm metadata for the
     * {@link tds.assessment.Segment}
     * <p>
     *     This method is only used by {@link tds.assessment.Segment}s that are configured for the adaptive
     *     {@link tds.assessment.Algorithm}.  Segments configured for the fixed form algorithm do not have strands, in
     *     which case this method will return an empty {@code HashSet}.
     * </p>
     */
    public Set<Strand> getStrands() {
        if (strands == null) {
            strands = new HashSet<>();
        }

        return strands;
    }

    public void setStrands(Set<Strand> strands) {
        this.strands = strands;
    }

    /**
     * Get a collection of {@link tds.assessment.Item}s based on this Segment's selection algorithm.
     *
     * @param languageCode The language code
     * @return A @{code List<Item>} collection for the student's language
     */
    public List<Item> getItems(String languageCode) {
        // RULE: When the Segment's algorithm is set to "fixedform", the items should come from the Form that represents
        // the student's selected language.  Otherwise, the items are returned directly (because they will have been
        // collected by other means).
        return getSelectionAlgorithm().equals(Algorithm.FIXED_FORM)
            ? getForm(languageCode).getItems()
            : items;
    }

    /**
     * This setter is only used by {@link tds.assessment.Segment}s that are configured for the "adaptive2" algorithm.
     * Segments configured for the "fixedform" algorithm rely on a {@link tds.assessment.Form} to get the correct
     * collection of {@link tds.assessment.Item}s.
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }
}
