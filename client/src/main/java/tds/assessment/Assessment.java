package tds.assessment;

import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents admin information concerning an assessment and its individual segments (if any)
 */
public class Assessment {
    private String key;
    private String assessmentId;
    private Algorithm selectionAlgorithm;
    private float startAbility;
    private String subject;
    private int prefetch;
    private float abilitySlope;
    private float abilityIntercept;
    private String accommodationFamily;
    private int maxOpportunities;
    private Instant fieldTestStartDate;
    private Instant fieldTestEndDate;
    private boolean initialAbilityBySubject;
    private List<ItemConstraint> itemConstraints;
    private List<Segment> segments;
    private Set<Strand> strands;
    private Set<String> languageCodes;
    private boolean validateCompleteness;
    private boolean deleteUnansweredItems;

    public List<ItemConstraint> getItemConstraints() {
        return itemConstraints != null ? itemConstraints : new ArrayList<ItemConstraint>();
    }

    public void setItemConstraints(List<ItemConstraint> itemConstraints) {
        this.itemConstraints = itemConstraints;
    }

    /**
     * @return  A collection of {@link Segment}s for this assessment
     */
    public List<Segment> getSegments() {
        return segments != null ? segments : new ArrayList<Segment>();
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    /**
     * @return key to the assessment
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the associated assessment key
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    /**
     * @return {@code true} if the assessment is segmented
     */
    public boolean isSegmented() {

        return segments != null && segments.size() > 1;
    }

    public Segment getSegment(final String segmentKey) {
        Segment matchingSegment = null;
        for (Segment segment : segments) {
            if (segmentKey.equals(segment.getKey())) {
                matchingSegment = segment;
                break;
            }
        }

        if (matchingSegment == null) {
            throw new IllegalArgumentException(String.format("No segment with key %s found in the assessment.", segmentKey));
        }

        return matchingSegment;
    }

    /**
     * @return the selection algorithm key for the assessment
     */
    public Algorithm getSelectionAlgorithm() {
        return selectionAlgorithm;
    }

    public void setSelectionAlgorithm(Algorithm selectionAlgorithm) {
        this.selectionAlgorithm = selectionAlgorithm;
    }

    /**
     * @return the start ability value for the assessment
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
     * @return The number of pages to prefetch
     */
    public int getPrefetch() {
        return prefetch;
    }

    public void setPrefetch(int prefetch) {
        this.prefetch = prefetch;
    }

    /**
     * @return the collection of {@link tds.assessment.Strand}s containing adaptive algorithm metadata for
     * the {@link tds.assessment.Assessment}
     */
    public Set<Strand> getStrands() {
        return strands != null ? strands : new HashSet<Strand>();
    }

    public void setStrands(Set<Strand> strands) {
        this.strands = strands;
    }

    /**
     * @return the ability slope
     */
    public float getAbilitySlope() {
        return abilitySlope;
    }

    public void setAbilitySlope(float abilitySlope) {
        this.abilitySlope = abilitySlope;
    }

    /**
     * @return the intercept for the ability
     */
    public float getAbilityIntercept() {
        return abilityIntercept;
    }

    public void setAbilityIntercept(float abilityIntercept) {
        this.abilityIntercept = abilityIntercept;
    }

    /**
     * @return the accommodation family
     */
    public String getAccommodationFamily() {
        return accommodationFamily;
    }

    public void setAccommodationFamily(String accommodationFamily) {
        this.accommodationFamily = accommodationFamily;
    }

    /**
     * @return the max opportunities an exam can be taken
     */
    public int getMaxOpportunities() {
        return maxOpportunities;
    }

    public void setMaxOpportunities(int maxOpportunities) {
        this.maxOpportunities = maxOpportunities;
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
     * @return {@code true} if the initial ability should be by subject
     */
    public boolean isInitialAbilityBySubject() {
        return initialAbilityBySubject;
    }

    public void setInitialAbilityBySubject(boolean initialAbilityBySubject) {
        this.initialAbilityBySubject = initialAbilityBySubject;
    }

    /**
     * @return A collection of language codes that are associated with this {@link tds.assessment.Assessment}
     * <p>
     *     The languageCodes that are available to an assessment are determined by collecting the unique "language"
     *     {@link tds.assessment.ItemProperty} of the {@link tds.assessment.Item}.
     * </p>
     */
    public Set<String> getLanguageCodes() {
        return languageCodes;
    }

    public void setLanguageCodes(Set<String> languageCodes) {
        this.languageCodes = languageCodes;
    }

    /**
     * @return A flag indicating whether unanswered items should be cleared automatically.
     */
    public boolean shouldDeleteUnansweredItems() {
        return deleteUnansweredItems;
    }

    public void setDeleteUnansweredItems(boolean deleteUnansweredItems) {
        this.deleteUnansweredItems = deleteUnansweredItems;
    }

    /**
     * @return A flag indicating whether all items need to ansered before test submission
     */
    public boolean isValidateCompleteness() {
        return validateCompleteness;
    }

    public void setValidateCompleteness(boolean validateCompleteness) {
        this.validateCompleteness = validateCompleteness;
    }
}
