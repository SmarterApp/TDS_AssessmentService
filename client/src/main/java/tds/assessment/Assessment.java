package tds.assessment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.accommodation.AccommodationDependency;
import tds.common.Algorithm;

/**
 * Represents admin information concerning an assessment and its individual segments (if any)
 */
@JsonIgnoreProperties(value="segmented", allowGetters=true)
public class Assessment {
    private String key;
    private String assessmentId;
    private String label;
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
    private List<AccommodationDependency> accommodationDependencies;
    private List<ItemConstraint> itemConstraints;
    private List<Segment> segments;
    private Set<Strand> strands;
    private Set<String> languageCodes;
    private boolean validateCompleteness;
    private boolean deleteUnansweredItems;
    private boolean multiStageBraille;
    private List<String> grades;
    private Integer handScoreProjectId;
    private String contract;
    private String type;
    private String academicYear;
    private Long loadVersion;
    private Long updateVersion;

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
    @JsonGetter("deleteUnansweredItems")
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
    
    /**
     * @return The human-readable label of the assessment
     */
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * @return The list of accommodation/tool dependencies
     */
    public List<AccommodationDependency> getAccommodationDependencies() {
        return accommodationDependencies;
    }
    
    public void setAccommodationDependencies(List<AccommodationDependency> accommodationDependencies) {
        this.accommodationDependencies = accommodationDependencies;
    }

    /**
     * @return a flag indicating whethere the assessment is a multi-stage braille assessment
     */
    public boolean isMultiStageBraille() {
        return multiStageBraille;
    }

    public void setMultiStageBraille(boolean multiStageBraille) {
        this.multiStageBraille = multiStageBraille;
    }

    /**
     * @return The string representing the grade or grades of the assessment
     */
    public List<String> getGrades() {
        return grades;
    }

    public void setGrades(List<String> grades) {
        this.grades = grades;
    }

    /**
     * @return the id of the handscore project (optional)
     */
    public Integer getHandScoreProjectId() {
        return handScoreProjectId;
    }

    public void setHandScored(Integer handScoreProjectId) {
        this.handScoreProjectId = handScoreProjectId;
    }

    /**
     * @return The contract name of the assessment
     */
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    /**
     * @return The type of assessment (e.g., summative, interim)
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return String representing the assessment's academic year
     */
    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    /**
     * @return The version of the assessment when first loaded
     */
    public Long getLoadVersion() {
        return loadVersion;
    }

    public void setLoadVersion(Long loadVersion) {
        this.loadVersion = loadVersion;
    }

    /**
     * @return The version of the assessment last updated
     */
    public Long getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(Long updateVersion) {
        this.updateVersion = updateVersion;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Assessment that = (Assessment) o;

        if (Float.compare(that.startAbility, startAbility) != 0) return false;
        if (prefetch != that.prefetch) return false;
        if (Float.compare(that.abilitySlope, abilitySlope) != 0) return false;
        if (Float.compare(that.abilityIntercept, abilityIntercept) != 0) return false;
        if (maxOpportunities != that.maxOpportunities) return false;
        if (initialAbilityBySubject != that.initialAbilityBySubject) return false;
        if (validateCompleteness != that.validateCompleteness) return false;
        if (deleteUnansweredItems != that.deleteUnansweredItems) return false;
        if (multiStageBraille != that.multiStageBraille) return false;
        if (!key.equals(that.key)) return false;
        if (assessmentId != null ? !assessmentId.equals(that.assessmentId) : that.assessmentId != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (selectionAlgorithm != that.selectionAlgorithm) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (accommodationFamily != null ? !accommodationFamily.equals(that.accommodationFamily) : that.accommodationFamily != null)
            return false;
        if (fieldTestStartDate != null ? !fieldTestStartDate.equals(that.fieldTestStartDate) : that.fieldTestStartDate != null)
            return false;
        if (fieldTestEndDate != null ? !fieldTestEndDate.equals(that.fieldTestEndDate) : that.fieldTestEndDate != null)
            return false;
        if (accommodationDependencies != null ? !accommodationDependencies.equals(that.accommodationDependencies) : that.accommodationDependencies != null)
            return false;
        if (itemConstraints != null ? !itemConstraints.equals(that.getItemConstraints()) : that.itemConstraints != null)
            return false;
        if (segments != null ? !segments.equals(that.getSegments()) : that.segments != null) return false;
        if (strands != null ? !strands.equals(that.strands) : that.strands != null) return false;
        if (languageCodes != null ? !languageCodes.equals(that.languageCodes) : that.languageCodes != null)
            return false;
        if (grades != null ? !grades.equals(that.grades) : that.grades != null) return false;
        if (handScoreProjectId != null ? !handScoreProjectId.equals(that.handScoreProjectId) : that.handScoreProjectId != null)
            return false;
        if (contract != null ? !contract.equals(that.contract) : that.contract != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (academicYear != null ? !academicYear.equals(that.academicYear) : that.academicYear != null) return false;
        if (loadVersion != null ? !loadVersion.equals(that.loadVersion) : that.loadVersion != null) return false;
        return updateVersion != null ? updateVersion.equals(that.updateVersion) : that.updateVersion == null;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + (assessmentId != null ? assessmentId.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (selectionAlgorithm != null ? selectionAlgorithm.hashCode() : 0);
        result = 31 * result + (startAbility != +0.0f ? Float.floatToIntBits(startAbility) : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + prefetch;
        result = 31 * result + (abilitySlope != +0.0f ? Float.floatToIntBits(abilitySlope) : 0);
        result = 31 * result + (abilityIntercept != +0.0f ? Float.floatToIntBits(abilityIntercept) : 0);
        result = 31 * result + (accommodationFamily != null ? accommodationFamily.hashCode() : 0);
        result = 31 * result + maxOpportunities;
        result = 31 * result + (fieldTestStartDate != null ? fieldTestStartDate.hashCode() : 0);
        result = 31 * result + (fieldTestEndDate != null ? fieldTestEndDate.hashCode() : 0);
        result = 31 * result + (initialAbilityBySubject ? 1 : 0);
        result = 31 * result + (accommodationDependencies != null ? accommodationDependencies.hashCode() : 0);
        result = 31 * result + (itemConstraints != null ? itemConstraints.hashCode() : 0);
        result = 31 * result + (segments != null ? segments.hashCode() : 0);
        result = 31 * result + (strands != null ? strands.hashCode() : 0);
        result = 31 * result + (languageCodes != null ? languageCodes.hashCode() : 0);
        result = 31 * result + (validateCompleteness ? 1 : 0);
        result = 31 * result + (deleteUnansweredItems ? 1 : 0);
        result = 31 * result + (multiStageBraille ? 1 : 0);
        result = 31 * result + (grades != null ? grades.hashCode() : 0);
        result = 31 * result + (handScoreProjectId != null ? handScoreProjectId.hashCode() : 0);
        result = 31 * result + (contract != null ? contract.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (academicYear != null ? academicYear.hashCode() : 0);
        result = 31 * result + (loadVersion != null ? loadVersion.hashCode() : 0);
        result = 31 * result + (updateVersion != null ? updateVersion.hashCode() : 0);
        return result;
    }
}
