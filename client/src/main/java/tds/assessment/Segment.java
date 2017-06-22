/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.accommodation.Accommodation;
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
    private int refreshMinutes;
    private float blueprintWeight;
    private float itemWeight;
    private float abilityOffset;
    private int candidateSet1Size;
    private String candidateSet1Order;
    private int randomizer;
    private int initialRandom;
    private float abilityWeight;
    private String adaptiveVersion;
    private float reportingCandidateAbilityWeight;
    private float precisionTarget;
    private float precisionTargetMetWeight;
    private float precisionTargetNotMetWeight;
    private float adaptiveCut;
    private float tooCloseStandardErrors;
    private boolean terminationOverallInformation;
    private boolean terminationReportingCategoryInfo;
    private boolean terminationMinCount;
    private boolean terminationTooClose;
    private boolean terminationFlagsAnd;
    private float slope;
    private float intercept;
    private float startInfo;
    private List<ItemGroup> itemGroups = new ArrayList<>();

    // Fixed-form specific fields
    private List<Form> forms = new ArrayList<>();
    // Adaptive-specific fields
    private Set<Strand> strands = new HashSet<>();
    private List<Item> items = new ArrayList<>();

    public Segment(@JsonProperty("key") String key,
                   @JsonProperty("algorithm") Algorithm algorithm) {
        this.key = key;
        this.selectionAlgorithm = algorithm;
    }

    public float getStartInfo() {
        return startInfo;
    }

    public void setStartInfo(final float startInfo) {
        this.startInfo = startInfo;
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
     * This getter/setter is only used by {@link tds.assessment.Segment}s that are configured for the fixed form
     * {@link tds.common.Algorithm}.  Segments configured for the "adaptive2" algorithm do not have forms, in which case an empty
     * {@code ArrayList} is returned
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
     * This method is only used by {@link tds.assessment.Segment}s that are configured for the fixed form
     * {@link tds.common.Algorithm}.  Segments configured for the adaptive algorithm do not have forms.
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
     * This method is only used by {@link tds.assessment.Segment}s that are configured for the fixed form
     * {@link tds.common.Algorithm}.  Segments configured for the adaptive algorithm do not have forms.
     * </p>
     *
     * @param languageCode The student's specified language
     * @param cohort       The form cohort to filter by
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
     * This method is only used by {@link tds.assessment.Segment}s that are configured for the adaptive
     * {@link tds.common.Algorithm}.  Segments configured for the fixed form algorithm do not have strands, in
     * which case this method will return an empty {@code HashSet}.
     * </p>
     */
    public Set<Strand> getStrands() {
        return strands;
    }

    public void setStrands(Set<Strand> strands) {
        this.strands = strands;
    }

    public List<Item> getItems() {
        return items;
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
        final List<Item> retItems = new ArrayList<>();

        if (getSelectionAlgorithm() == Algorithm.FIXED_FORM) {
            for (final Form form : getForms(languageCode)) {
                retItems.addAll(form.getItems());
            }
        } else {
            for (final Item item : items) {
                // An item can have many languages (e.g. English, English-Braille, Spanish).  If the item has a
                // language property for the specified language code, return it.
                for (final ItemProperty itemProperty : item.getItemProperties()) {
                    if (itemProperty.getName().equalsIgnoreCase(Accommodation.ACCOMMODATION_TYPE_LANGUAGE)
                        && itemProperty.getValue().equalsIgnoreCase(languageCode)) {
                        retItems.add(item);
                    }
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

    public int getRefreshMinutes() {
        return refreshMinutes;
    }

    public void setRefreshMinutes(final int refreshMinutes) {
        this.refreshMinutes = refreshMinutes;
    }

    public float getBlueprintWeight() {
        return blueprintWeight;
    }

    public void setBlueprintWeight(final float blueprintWeight) {
        this.blueprintWeight = blueprintWeight;
    }

    public float getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(final float itemWeight) {
        this.itemWeight = itemWeight;
    }

    public float getAbilityOffset() {
        return abilityOffset;
    }

    public void setAbilityOffset(final float abilityOffset) {
        this.abilityOffset = abilityOffset;
    }

    public int getCandidateSet1Size() {
        return candidateSet1Size;
    }

    public void setCandidateSet1Size(final int candidateSet1Size) {
        this.candidateSet1Size = candidateSet1Size;
    }

    public String getCandidateSet1Order() {
        return candidateSet1Order;
    }

    public void setCandidateSet1Order(final String candidateSet1Order) {
        this.candidateSet1Order = candidateSet1Order;
    }

    public int getRandomizer() {
        return randomizer;
    }

    public void setRandomizer(final int randomizer) {
        this.randomizer = randomizer;
    }

    public int getInitialRandom() {
        return initialRandom;
    }

    public void setInitialRandom(final int initialRandom) {
        this.initialRandom = initialRandom;
    }

    public float getAbilityWeight() {
        return abilityWeight;
    }

    public void setAbilityWeight(final float abilityWeight) {
        this.abilityWeight = abilityWeight;
    }

    public String getAdaptiveVersion() {
        return adaptiveVersion;
    }

    public void setAdaptiveVersion(final String adaptiveVersion) {
        this.adaptiveVersion = adaptiveVersion;
    }

    public float getReportingCandidateAbilityWeight() {
        return reportingCandidateAbilityWeight;
    }

    public void setReportingCandidateAbilityWeight(final float reportingCandidateAbilityWeight) {
        this.reportingCandidateAbilityWeight = reportingCandidateAbilityWeight;
    }

    public float getPrecisionTarget() {
        return precisionTarget;
    }

    public void setPrecisionTarget(final float precisionTarget) {
        this.precisionTarget = precisionTarget;
    }

    public float getPrecisionTargetMetWeight() {
        return precisionTargetMetWeight;
    }

    public void setPrecisionTargetMetWeight(final float precisionTargetMetWeight) {
        this.precisionTargetMetWeight = precisionTargetMetWeight;
    }

    public float getPrecisionTargetNotMetWeight() {
        return precisionTargetNotMetWeight;
    }

    public void setPrecisionTargetNotMetWeight(final float precisionTargetNotMetWeight) {
        this.precisionTargetNotMetWeight = precisionTargetNotMetWeight;
    }

    public float getAdaptiveCut() {
        return adaptiveCut;
    }

    public void setAdaptiveCut(final float adaptiveCut) {
        this.adaptiveCut = adaptiveCut;
    }

    public float getTooCloseStandardErrors() {
        return tooCloseStandardErrors;
    }

    public void setTooCloseStandardErrors(final float tooCloseStandardErrors) {
        this.tooCloseStandardErrors = tooCloseStandardErrors;
    }

    public boolean isTerminationOverallInformation() {
        return terminationOverallInformation;
    }

    public void setTerminationOverallInformation(final boolean terminationOverallInformation) {
        this.terminationOverallInformation = terminationOverallInformation;
    }

    public boolean isTerminationReportingCategoryInfo() {
        return terminationReportingCategoryInfo;
    }

    public void setTerminationReportingCategoryInfo(final boolean terminationReportingCategoryInfo) {
        this.terminationReportingCategoryInfo = terminationReportingCategoryInfo;
    }

    public boolean isTerminationMinCount() {
        return terminationMinCount;
    }

    public void setTerminationMinCount(final boolean terminationMinCount) {
        this.terminationMinCount = terminationMinCount;
    }

    public boolean isTerminationTooClose() {
        return terminationTooClose;
    }

    public void setTerminationTooClose(final boolean terminationTooClose) {
        this.terminationTooClose = terminationTooClose;
    }

    public boolean isTerminationFlagsAnd() {
        return terminationFlagsAnd;
    }

    public void setTerminationFlagsAnd(final boolean terminationFlagsAnd) {
        this.terminationFlagsAnd = terminationFlagsAnd;
    }

    public float getSlope() {
        return slope;
    }

    public void setSlope(final float slope) {
        this.slope = slope;
    }

    /**
     * @return leveraged when choosing items during an adaptive selection algorithm
     */
    public float getIntercept() {
        return intercept;
    }

    public void setIntercept(final float intercept) {
        this.intercept = intercept;
    }

    /**
     * @return The item groups that are associated with this segment.  This can be empty if there are not groups.  In
     * those instances the item will be given a hard coded group in the selection code.
     */
    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public void setItemGroups(final List<ItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
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
        if (refreshMinutes != segment.refreshMinutes) return false;
        if (Float.compare(segment.blueprintWeight, blueprintWeight) != 0) return false;
        if (Float.compare(segment.itemWeight, itemWeight) != 0) return false;
        if (Float.compare(segment.abilityOffset, abilityOffset) != 0) return false;
        if (candidateSet1Size != segment.candidateSet1Size) return false;
        if (randomizer != segment.randomizer) return false;
        if (initialRandom != segment.initialRandom) return false;
        if (Float.compare(segment.abilityWeight, abilityWeight) != 0) return false;
        if (Float.compare(segment.reportingCandidateAbilityWeight, reportingCandidateAbilityWeight) != 0) return false;
        if (Float.compare(segment.precisionTarget, precisionTarget) != 0) return false;
        if (Float.compare(segment.precisionTargetMetWeight, precisionTargetMetWeight) != 0) return false;
        if (Float.compare(segment.precisionTargetNotMetWeight, precisionTargetNotMetWeight) != 0) return false;
        if (Float.compare(segment.adaptiveCut, adaptiveCut) != 0) return false;
        if (Float.compare(segment.tooCloseStandardErrors, tooCloseStandardErrors) != 0) return false;
        if (terminationOverallInformation != segment.terminationOverallInformation) return false;
        if (terminationReportingCategoryInfo != segment.terminationReportingCategoryInfo) return false;
        if (terminationMinCount != segment.terminationMinCount) return false;
        if (terminationTooClose != segment.terminationTooClose) return false;
        if (terminationFlagsAnd != segment.terminationFlagsAnd) return false;
        if (Float.compare(segment.slope, slope) != 0) return false;
        if (Float.compare(segment.intercept, intercept) != 0) return false;
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
        if (candidateSet1Order != null ? !candidateSet1Order.equals(segment.candidateSet1Order) : segment.candidateSet1Order != null)
            return false;
        if (adaptiveVersion != null ? !adaptiveVersion.equals(segment.adaptiveVersion) : segment.adaptiveVersion != null)
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
        result = 31 * result + refreshMinutes;
        result = 31 * result + (blueprintWeight != +0.0f ? Float.floatToIntBits(blueprintWeight) : 0);
        result = 31 * result + (itemWeight != +0.0f ? Float.floatToIntBits(itemWeight) : 0);
        result = 31 * result + (abilityOffset != +0.0f ? Float.floatToIntBits(abilityOffset) : 0);
        result = 31 * result + candidateSet1Size;
        result = 31 * result + (candidateSet1Order != null ? candidateSet1Order.hashCode() : 0);
        result = 31 * result + randomizer;
        result = 31 * result + initialRandom;
        result = 31 * result + (abilityWeight != +0.0f ? Float.floatToIntBits(abilityWeight) : 0);
        result = 31 * result + (adaptiveVersion != null ? adaptiveVersion.hashCode() : 0);
        result = 31 * result + (reportingCandidateAbilityWeight != +0.0f ? Float.floatToIntBits(reportingCandidateAbilityWeight) : 0);
        result = 31 * result + (precisionTarget != +0.0f ? Float.floatToIntBits(precisionTarget) : 0);
        result = 31 * result + (precisionTargetMetWeight != +0.0f ? Float.floatToIntBits(precisionTargetMetWeight) : 0);
        result = 31 * result + (precisionTargetNotMetWeight != +0.0f ? Float.floatToIntBits(precisionTargetNotMetWeight) : 0);
        result = 31 * result + (adaptiveCut != +0.0f ? Float.floatToIntBits(adaptiveCut) : 0);
        result = 31 * result + (tooCloseStandardErrors != +0.0f ? Float.floatToIntBits(tooCloseStandardErrors) : 0);
        result = 31 * result + (terminationOverallInformation ? 1 : 0);
        result = 31 * result + (terminationReportingCategoryInfo ? 1 : 0);
        result = 31 * result + (terminationMinCount ? 1 : 0);
        result = 31 * result + (terminationTooClose ? 1 : 0);
        result = 31 * result + (terminationFlagsAnd ? 1 : 0);
        result = 31 * result + (slope != +0.0f ? Float.floatToIntBits(slope) : 0);
        result = 31 * result + (intercept != +0.0f ? Float.floatToIntBits(intercept) : 0);
        result = 31 * result + (forms != null ? forms.hashCode() : 0);
        result = 31 * result + (strands != null ? strands.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }
}
