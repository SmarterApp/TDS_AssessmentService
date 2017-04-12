package tds.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.common.Algorithm;

/**
 * Represents admin information common to all segments, regardless of the selection algorithm.
 */
public class Segment {
    private final String key;
    private String label;
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
    private int fieldTestStartPosition;
    private int fieldTestEndPosition;

    // Fixed-form specific fields
    private List<Form> forms;

    // Adaptive-specific fields
    private Set<Strand> strands;
    private List<Item> items;

    public Segment(@JsonProperty("key") String key,
                   @JsonProperty("algorithm") Algorithm algorithm) {
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
     * @return The human-readable label of the segment
     */
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
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
     * @return The lowest possible start position in the exam where this segment may contain field test items
     */
    public int getFieldTestStartPosition() {
        return fieldTestStartPosition;
    }

    public void setFieldTestStartPosition(int fieldTestStartPosition) {
        this.fieldTestStartPosition = fieldTestStartPosition;
    }

    /**
     * @return The highest possible position in the exam where this segment may contain field test items
     */
    public int getFieldTestEndPosition() {
        return fieldTestEndPosition;
    }

    public void setFieldTestEndPosition(int fieldTestEndPosition) {
        this.fieldTestEndPosition = fieldTestEndPosition;
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
     *     {@link tds.common.Algorithm}.  Segments configured for the "adaptive2" algorithm do not have forms, in which case an empty
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
     * Choose the {@link tds.assessment.Form}s for the specified language.
     * <p>
     *     This method is only used by {@link tds.assessment.Segment}s that are configured for the fixed form
     *     {@link tds.common.Algorithm}.  Segments configured for the adaptive algorithm do not have forms.
     * </p>
     *
     * @param languageCode The student's specified language
     * @return The {@link tds.assessment.Form} for the specified language
     */
    public List<Form> getForms(final String languageCode) {
        List<Form> formsForLanguage = new ArrayList<>();

        if (forms == null) {
            return new ArrayList<>();
        }

        for (Form form : this.forms) {
            if (form.getLanguageCode().equalsIgnoreCase(languageCode)) {
                formsForLanguage.add(form);
            }
        }

        return formsForLanguage;
    }

    /**
     * Choose the {@link tds.assessment.Form} for the specified language and form cohort.
     * <p>
     *     This method is only used by {@link tds.assessment.Segment}s that are configured for the fixed form
     *     {@link tds.common.Algorithm}.  Segments configured for the adaptive algorithm do not have forms.
     * </p>
     *
     * @param languageCode The student's specified language
     * @param cohort The form cohort to filter by
     * @return The {@link tds.assessment.Form} for the specified language
     */
    public Optional<Form> getForm(final String languageCode, final String cohort) {
        Optional<Form> maybeForm = Optional.absent();

        for (Form form : this.forms) {
            if (form.getLanguageCode().equalsIgnoreCase(languageCode) && form.getCohort().equalsIgnoreCase(cohort)) {
                maybeForm = Optional.of(form);
                break;
            }
        }

        return maybeForm;
    }

    /**
     * @return the collection of {@link tds.assessment.Strand}s containing adaptive algorithm metadata for the
     * {@link tds.assessment.Segment}
     * <p>
     *     This method is only used by {@link tds.assessment.Segment}s that are configured for the adaptive
     *     {@link tds.common.Algorithm}.  Segments configured for the fixed form algorithm do not have strands, in
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
    
    public List<Item> getItems() {
        return items == null ? new ArrayList<Item>() : items;
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
        List<Item> retItems = new ArrayList<>();

        if (getSelectionAlgorithm() == Algorithm.FIXED_FORM) {
            for (Form form : getForms(languageCode)) {
                retItems.addAll(form.getItems());
            }
        } else {
            for (Item item : items) {
                if (languageCode.equalsIgnoreCase(item.getLanguageCode())) {
                    retItems.add(item);
                }
            }
        }

        return retItems;
    }

    /**
     * This setter is only used by {@link tds.assessment.Segment}s that are configured for the "adaptive2" algorithm.
     * Segments configured for the "fixedform" algorithm rely on a {@link tds.assessment.Form} to get the correct
     * collection of {@link tds.assessment.Item}s.
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Segment segment = (Segment) o;

        if (Float.compare(segment.startAbility, startAbility) != 0) return false;
        if (position != segment.position) return false;
        if (minItems != segment.minItems) return false;
        if (maxItems != segment.maxItems) return false;
        if (fieldTestMinItems != segment.fieldTestMinItems) return false;
        if (fieldTestMaxItems != segment.fieldTestMaxItems) return false;
        if (fieldTestStartPosition != segment.fieldTestStartPosition) return false;
        if (fieldTestEndPosition != segment.fieldTestEndPosition) return false;
        if (key != null ? !key.equals(segment.key) : segment.key != null) return false;
        if (label != null ? !label.equals(segment.label) : segment.label != null) return false;
        if (selectionAlgorithm != segment.selectionAlgorithm) return false;
        if (segmentId != null ? !segmentId.equals(segment.segmentId) : segment.segmentId != null) return false;
        if (subject != null ? !subject.equals(segment.subject) : segment.subject != null) return false;
        if (assessmentKey != null ? !assessmentKey.equals(segment.assessmentKey) : segment.assessmentKey != null)
            return false;
        if (fieldTestStartDate != null ? !fieldTestStartDate.equals(segment.fieldTestStartDate) : segment.fieldTestStartDate != null)
            return false;
        if (fieldTestEndDate != null ? !fieldTestEndDate.equals(segment.fieldTestEndDate) : segment.fieldTestEndDate != null)
            return false;
        if (forms != null ? !forms.equals(segment.forms) : segment.forms != null) return false;
        if (strands != null ? !strands.equals(segment.strands) : segment.strands != null) return false;
        return items != null ? items.equals(segment.items) : segment.items == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (selectionAlgorithm != null ? selectionAlgorithm.hashCode() : 0);
        result = 31 * result + (segmentId != null ? segmentId.hashCode() : 0);
        result = 31 * result + (startAbility != +0.0f ? Float.floatToIntBits(startAbility) : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (assessmentKey != null ? assessmentKey.hashCode() : 0);
        result = 31 * result + position;
        result = 31 * result + minItems;
        result = 31 * result + maxItems;
        result = 31 * result + fieldTestMinItems;
        result = 31 * result + fieldTestMaxItems;
        result = 31 * result + (fieldTestStartDate != null ? fieldTestStartDate.hashCode() : 0);
        result = 31 * result + (fieldTestEndDate != null ? fieldTestEndDate.hashCode() : 0);
        result = 31 * result + fieldTestStartPosition;
        result = 31 * result + fieldTestEndPosition;
        result = 31 * result + (forms != null ? forms.hashCode() : 0);
        result = 31 * result + (strands != null ? strands.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
