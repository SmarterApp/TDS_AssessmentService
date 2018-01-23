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

package tds.assessment.model.itembank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblsetofadminsubjects", schema = "itembank")
public class TblAdminSubject {
    private String key;
    private String clientName;
    private String subjectKey;
    private String id;
    private float startAbility;
    private float startInfo;
    private int minItems;
    private int maxItems;
    private float slope;
    private float intercept;
    private Integer fieldTestStartPos;
    private Integer fieldTestEndPos;
    private int fieldTestMinItems;
    private int fieldTestMaxItems;
    private Float sampleTarget;
    private String selectionAlgorithm;
    private String formSelection;
    private float blueprintWeight;
    private float abilityWeight;
    private int cSet1Size;
    private int cSet2Random;
    private int cSet2InitialRandom;
    private String virtualTest;
    private Integer testPosition;
    private boolean segmented;
    private boolean computeAbilityEstimates;
    private String version;
    private String updateVersion;
    private float itemWeight;
    private float abilityOffset;
    private String cSet1Order;
    private float rcAbilityWeight;
    private Float precisionTarget;
    private Float precisionTargetMetWeight;
    private Float precisionTargetNotMetWeight;
    private Float adaptiveCut;
    private Float tooCloseses;
    private boolean terminationOverallInfo;
    private boolean terminationRCInfo;
    private boolean terminationMinCount;
    private boolean terminationTooClose;
    private boolean terminationFlagsAnd;
    private String bpMetricFunction;
    private String testType;
    private String contract;

    public static final class Builder {
        private String key;
        private String clientName;
        private String subjectKey;
        private String id;
        private float startAbility;
        private float startInfo;
        private int minItems;
        private int maxItems;
        private float slope;
        private float intercept;
        private Integer fieldTestStartPos;
        private Integer fieldTestEndPos;
        private int fieldTestMinItems;
        private int fieldTestMaxItems;
        private Float sampleTarget;
        private String selectionAlgorithm;
        private String formSelection;
        private float blueprintWeight;
        private float abilityWeight;
        private int cSet1Size;
        private int cSet2Random;
        private int cSet2InitialRandom;
        private String virtualTest;
        private Integer testPosition;
        private boolean segmented;
        private boolean computeAbilityEstimates;
        private String version;
        private String updateVersion;
        private float itemWeight;
        private float abilityOffset;
        private String cSet1Order;
        private float rcAbilityWeight;
        private Float precisionTarget;
        private Float precisionTargetMetWeight;
        private Float precisionTargetNotMetWeight;
        private Float adaptiveCut;
        private Float tooCloseses;
        private boolean terminationOverallInfo;
        private boolean terminationRCInfo;
        private boolean terminationMinCount;
        private boolean terminationTooClose;
        private boolean terminationFlagsAnd;
        private String bpMetricFunction;
        private String testType;

        public Builder() {
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withSubjectKey(String subjectKey) {
            this.subjectKey = subjectKey;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withStartAbility(float startAbility) {
            this.startAbility = startAbility;
            return this;
        }

        public Builder withStartInfo(float startInfo) {
            this.startInfo = startInfo;
            return this;
        }

        public Builder withMinItems(int minItems) {
            this.minItems = minItems;
            return this;
        }

        public Builder withMaxItems(int maxItems) {
            this.maxItems = maxItems;
            return this;
        }

        public Builder withSlope(float slope) {
            this.slope = slope;
            return this;
        }

        public Builder withIntercept(float intercept) {
            this.intercept = intercept;
            return this;
        }

        public Builder withFieldTestStartPos(Integer fieldTestStartPos) {
            this.fieldTestStartPos = fieldTestStartPos;
            return this;
        }

        public Builder withFieldTestEndPos(Integer fieldTestEndPos) {
            this.fieldTestEndPos = fieldTestEndPos;
            return this;
        }

        public Builder withFieldTestMinItems(int fieldTestMinItems) {
            this.fieldTestMinItems = fieldTestMinItems;
            return this;
        }

        public Builder withFieldTestMaxItems(int fieldTestMaxItems) {
            this.fieldTestMaxItems = fieldTestMaxItems;
            return this;
        }

        public Builder withSampleTarget(Float sampleTarget) {
            this.sampleTarget = sampleTarget;
            return this;
        }

        public Builder withSelectionAlgorithm(String selectionAlgorithm) {
            this.selectionAlgorithm = selectionAlgorithm;
            return this;
        }

        public Builder withFormSelection(String formSelection) {
            this.formSelection = formSelection;
            return this;
        }

        public Builder withBlueprintWeight(float blueprintWeight) {
            this.blueprintWeight = blueprintWeight;
            return this;
        }

        public Builder withAbilityWeight(float abilityWeight) {
            this.abilityWeight = abilityWeight;
            return this;
        }

        public Builder withCSet1Size(int cSet1Size) {
            this.cSet1Size = cSet1Size;
            return this;
        }

        public Builder withCSet2Random(int cSet2Random) {
            this.cSet2Random = cSet2Random;
            return this;
        }

        public Builder withCSet2InitialRandom(int cSet2InitialRandom) {
            this.cSet2InitialRandom = cSet2InitialRandom;
            return this;
        }

        public Builder withVirtualTest(String virtualTest) {
            this.virtualTest = virtualTest;
            return this;
        }

        public Builder withTestPosition(Integer testPosition) {
            this.testPosition = testPosition;
            return this;
        }

        public Builder withSegmented(boolean segmented) {
            this.segmented = segmented;
            return this;
        }

        public Builder withComputeAbilityEstimates(boolean computeAbilityEstimates) {
            this.computeAbilityEstimates = computeAbilityEstimates;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withUpdateVersion(String updateVersion) {
            this.updateVersion = updateVersion;
            return this;
        }

        public Builder withItemWeight(float itemWeight) {
            this.itemWeight = itemWeight;
            return this;
        }

        public Builder withAbilityOffset(float abilityOffset) {
            this.abilityOffset = abilityOffset;
            return this;
        }

        public Builder withCSet1Order(String cSet1Order) {
            this.cSet1Order = cSet1Order;
            return this;
        }

        public Builder withRcAbilityWeight(float rcAbilityWeight) {
            this.rcAbilityWeight = rcAbilityWeight;
            return this;
        }

        public Builder withPrecisionTarget(Float precisionTarget) {
            this.precisionTarget = precisionTarget;
            return this;
        }

        public Builder withPrecisionTargetMetWeight(Float precisionTargetMetWeight) {
            this.precisionTargetMetWeight = precisionTargetMetWeight;
            return this;
        }

        public Builder withPrecisionTargetNotMetWeight(Float precisionTargetNotMetWeight) {
            this.precisionTargetNotMetWeight = precisionTargetNotMetWeight;
            return this;
        }

        public Builder withAdaptiveCut(Float adaptiveCut) {
            this.adaptiveCut = adaptiveCut;
            return this;
        }

        public Builder withTooCloseses(Float tooCloseses) {
            this.tooCloseses = tooCloseses;
            return this;
        }

        public Builder withTerminationOverallInfo(boolean terminationOverallInfo) {
            this.terminationOverallInfo = terminationOverallInfo;
            return this;
        }

        public Builder withTerminationRCInfo(boolean terminationRCInfo) {
            this.terminationRCInfo = terminationRCInfo;
            return this;
        }

        public Builder withTerminationMinCount(boolean terminationMinCount) {
            this.terminationMinCount = terminationMinCount;
            return this;
        }

        public Builder withTerminationTooClose(boolean terminationTooClose) {
            this.terminationTooClose = terminationTooClose;
            return this;
        }

        public Builder withTerminationFlagsAnd(boolean terminationFlagsAnd) {
            this.terminationFlagsAnd = terminationFlagsAnd;
            return this;
        }

        public Builder withBpMetricFunction(String bpMetricFunction) {
            this.bpMetricFunction = bpMetricFunction;
            return this;
        }

        public Builder withTestType(String testType) {
            this.testType = testType;
            return this;
        }

        public TblAdminSubject build() {
            TblAdminSubject tblAdminSubjects = new TblAdminSubject();
            tblAdminSubjects.terminationTooClose = this.terminationTooClose;
            tblAdminSubjects.clientName = this.clientName;
            tblAdminSubjects.fieldTestMinItems = this.fieldTestMinItems;
            tblAdminSubjects.cSet2Random = this.cSet2Random;
            tblAdminSubjects.cSet2InitialRandom = this.cSet2InitialRandom;
            tblAdminSubjects.intercept = this.intercept;
            tblAdminSubjects.version = this.version;
            tblAdminSubjects.segmented = this.segmented;
            tblAdminSubjects.minItems = this.minItems;
            tblAdminSubjects.maxItems = this.maxItems;
            tblAdminSubjects.rcAbilityWeight = this.rcAbilityWeight;
            tblAdminSubjects.precisionTargetMetWeight = this.precisionTargetMetWeight;
            tblAdminSubjects.fieldTestMaxItems = this.fieldTestMaxItems;
            tblAdminSubjects.computeAbilityEstimates = this.computeAbilityEstimates;
            tblAdminSubjects.updateVersion = this.updateVersion;
            tblAdminSubjects.tooCloseses = this.tooCloseses;
            tblAdminSubjects.terminationMinCount = this.terminationMinCount;
            tblAdminSubjects.testType = this.testType;
            tblAdminSubjects.key = this.key;
            tblAdminSubjects.virtualTest = this.virtualTest;
            tblAdminSubjects.cSet1Order = this.cSet1Order;
            tblAdminSubjects.id = this.id;
            tblAdminSubjects.abilityOffset = this.abilityOffset;
            tblAdminSubjects.terminationOverallInfo = this.terminationOverallInfo;
            tblAdminSubjects.precisionTarget = this.precisionTarget;
            tblAdminSubjects.fieldTestStartPos = this.fieldTestStartPos;
            tblAdminSubjects.selectionAlgorithm = this.selectionAlgorithm;
            tblAdminSubjects.startInfo = this.startInfo;
            tblAdminSubjects.itemWeight = this.itemWeight;
            tblAdminSubjects.precisionTargetNotMetWeight = this.precisionTargetNotMetWeight;
            tblAdminSubjects.bpMetricFunction = this.bpMetricFunction;
            tblAdminSubjects.fieldTestEndPos = this.fieldTestEndPos;
            tblAdminSubjects.abilityWeight = this.abilityWeight;
            tblAdminSubjects.slope = this.slope;
            tblAdminSubjects.formSelection = this.formSelection;
            tblAdminSubjects.cSet1Size = this.cSet1Size;
            tblAdminSubjects.terminationRCInfo = this.terminationRCInfo;
            tblAdminSubjects.subjectKey = this.subjectKey;
            tblAdminSubjects.startAbility = this.startAbility;
            tblAdminSubjects.testPosition = this.testPosition;
            tblAdminSubjects.sampleTarget = this.sampleTarget;
            tblAdminSubjects.blueprintWeight = this.blueprintWeight;
            tblAdminSubjects.adaptiveCut = this.adaptiveCut;
            tblAdminSubjects.terminationFlagsAnd = this.terminationFlagsAnd;
            tblAdminSubjects.contract = this.clientName;
            return tblAdminSubjects;
        }
    }

    @Id
    @Column(name = "_key")
    public String getKey() {
        return key;
    }

    @Column(name = "_fk_testadmin")
    public String getClientName() {
        return clientName;
    }

    @Column(name = "contract")
    public String getContract() {
        return clientName;
    }

    @Column(name = "_fk_subject")
    public String getSubjectKey() {
        return subjectKey;
    }

    @Column(name = "testid")
    public String getId() {
        return id;
    }

    public float getStartAbility() {
        return startAbility;
    }

    public float getStartInfo() {
        return startInfo;
    }

    public int getMinItems() {
        return minItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public float getSlope() {
        return slope;
    }

    public float getIntercept() {
        return intercept;
    }

    @Column(name = "ftstartpos")
    public Integer getFieldTestStartPos() {
        return fieldTestStartPos;
    }

    @Column(name = "ftendpos")
    public Integer getFieldTestEndPos() {
        return fieldTestEndPos;
    }

    @Column(name = "ftminitems")
    public int getFieldTestMinItems() {
        return fieldTestMinItems;
    }

    @Column(name = "ftmaxitems")
    public int getFieldTestMaxItems() {
        return fieldTestMaxItems;
    }

    public Float getSampleTarget() {
        return sampleTarget;
    }

    public String getSelectionAlgorithm() {
        return selectionAlgorithm;
    }

    public String getFormSelection() {
        return formSelection;
    }

    public float getBlueprintWeight() {
        return blueprintWeight;
    }

    public float getAbilityWeight() {
        return abilityWeight;
    }

    public int getcSet1Size() {
        return cSet1Size;
    }

    public int getcSet2Random() {
        return cSet2Random;
    }

    public int getcSet2InitialRandom() {
        return cSet2InitialRandom;
    }

    public String getVirtualTest() {
        return virtualTest;
    }

    public Integer getTestPosition() {
        return testPosition;
    }

    @Column(name = "issegmented")
    public boolean isSegmented() {
        return segmented;
    }

    public boolean isComputeAbilityEstimates() {
        return computeAbilityEstimates;
    }

    @Column(name = "loadconfig")
    public String getVersion() {
        return version;
    }

    @Column(name = "updateconfig")
    public String getUpdateVersion() {
        return updateVersion;
    }

    public float getItemWeight() {
        return itemWeight;
    }

    public float getAbilityOffset() {
        return abilityOffset;
    }

    public String getcSet1Order() {
        return cSet1Order;
    }

    public float getRcAbilityWeight() {
        return rcAbilityWeight;
    }

    public Float getPrecisionTarget() {
        return precisionTarget;
    }

    public Float getPrecisionTargetMetWeight() {
        return precisionTargetMetWeight;
    }

    public Float getPrecisionTargetNotMetWeight() {
        return precisionTargetNotMetWeight;
    }

    public Float getAdaptiveCut() {
        return adaptiveCut;
    }

    public Float getTooCloseses() {
        return tooCloseses;
    }

    public boolean isTerminationOverallInfo() {
        return terminationOverallInfo;
    }

    public boolean isTerminationRCInfo() {
        return terminationRCInfo;
    }

    public boolean isTerminationMinCount() {
        return terminationMinCount;
    }

    public boolean isTerminationTooClose() {
        return terminationTooClose;
    }

    public boolean isTerminationFlagsAnd() {
        return terminationFlagsAnd;
    }

    public String getBpMetricFunction() {
        return bpMetricFunction;
    }

    public String getTestType() {
        return testType;
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setKey(final String key) {
        this.key = key;
    }

    private void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    private void setSubjectKey(final String subjectKey) {
        this.subjectKey = subjectKey;
    }

    private void setId(final String id) {
        this.id = id;
    }

    private void setStartAbility(final float startAbility) {
        this.startAbility = startAbility;
    }

    private void setStartInfo(final float startInfo) {
        this.startInfo = startInfo;
    }

    private void setMinItems(final int minItems) {
        this.minItems = minItems;
    }

    private void setMaxItems(final int maxItems) {
        this.maxItems = maxItems;
    }

    private void setSlope(final float slope) {
        this.slope = slope;
    }

    private void setIntercept(final float intercept) {
        this.intercept = intercept;
    }

    private void setFieldTestStartPos(final Integer fieldTestStartPos) {
        this.fieldTestStartPos = fieldTestStartPos;
    }

    private void setFieldTestEndPos(final Integer fieldTestEndPos) {
        this.fieldTestEndPos = fieldTestEndPos;
    }

    private void setFieldTestMinItems(final int fieldTestMinItems) {
        this.fieldTestMinItems = fieldTestMinItems;
    }

    private void setFieldTestMaxItems(final int fieldTestMaxItems) {
        this.fieldTestMaxItems = fieldTestMaxItems;
    }

    private void setSampleTarget(final Float sampleTarget) {
        this.sampleTarget = sampleTarget;
    }

    private void setSelectionAlgorithm(final String selectionAlgorithm) {
        this.selectionAlgorithm = selectionAlgorithm;
    }

    private void setFormSelection(final String formSelection) {
        this.formSelection = formSelection;
    }

    private void setBlueprintWeight(final float blueprintWeight) {
        this.blueprintWeight = blueprintWeight;
    }

    private void setAbilityWeight(final float abilityWeight) {
        this.abilityWeight = abilityWeight;
    }

    private void setcSet1Size(final int cSet1Size) {
        this.cSet1Size = cSet1Size;
    }

    private void setcSet2Random(final int cSet2Random) {
        this.cSet2Random = cSet2Random;
    }

    private void setcSet2InitialRandom(final int cSet2InitialRandom) {
        this.cSet2InitialRandom = cSet2InitialRandom;
    }

    private void setVirtualTest(final String virtualTest) {
        this.virtualTest = virtualTest;
    }

    private void setTestPosition(final Integer testPosition) {
        this.testPosition = testPosition;
    }

    private void setSegmented(final boolean segmented) {
        this.segmented = segmented;
    }

    private void setComputeAbilityEstimates(final boolean computeAbilityEstimates) {
        this.computeAbilityEstimates = computeAbilityEstimates;
    }

    private void setVersion(final String version) {
        this.version = version;
    }

    private void setUpdateVersion(final String updateVersion) {
        this.updateVersion = updateVersion;
    }

    private void setItemWeight(final float itemWeight) {
        this.itemWeight = itemWeight;
    }

    private void setAbilityOffset(final float abilityOffset) {
        this.abilityOffset = abilityOffset;
    }

    private void setcSet1Order(final String cSet1Order) {
        this.cSet1Order = cSet1Order;
    }

    private void setRcAbilityWeight(final float rcAbilityWeight) {
        this.rcAbilityWeight = rcAbilityWeight;
    }

    private void setPrecisionTarget(final Float precisionTarget) {
        this.precisionTarget = precisionTarget;
    }

    private void setPrecisionTargetMetWeight(final Float precisionTargetMetWeight) {
        this.precisionTargetMetWeight = precisionTargetMetWeight;
    }

    private void setPrecisionTargetNotMetWeight(final Float precisionTargetNotMetWeight) {
        this.precisionTargetNotMetWeight = precisionTargetNotMetWeight;
    }

    private void setAdaptiveCut(final Float adaptiveCut) {
        this.adaptiveCut = adaptiveCut;
    }

    private void setTooCloseses(final Float tooCloseses) {
        this.tooCloseses = tooCloseses;
    }

    private void setTerminationOverallInfo(final boolean terminationOverallInfo) {
        this.terminationOverallInfo = terminationOverallInfo;
    }

    private void setTerminationRCInfo(final boolean terminationRCInfo) {
        this.terminationRCInfo = terminationRCInfo;
    }

    private void setTerminationMinCount(final boolean terminationMinCount) {
        this.terminationMinCount = terminationMinCount;
    }

    private void setTerminationTooClose(final boolean terminationTooClose) {
        this.terminationTooClose = terminationTooClose;
    }

    private void setTerminationFlagsAnd(final boolean terminationFlagsAnd) {
        this.terminationFlagsAnd = terminationFlagsAnd;
    }

    private void setBpMetricFunction(final String bpMetricFunction) {
        this.bpMetricFunction = bpMetricFunction;
    }

    private void setTestType(final String testType) {
        this.testType = testType;
    }

    private void setContract(final String contract) {
        this.contract = contract;
    }
}
