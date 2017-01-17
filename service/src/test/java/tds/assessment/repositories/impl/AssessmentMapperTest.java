package tds.assessment.repositories.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.Algorithm;
import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.builders.AssessmentRecordBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class AssessmentMapperTest {
    private AssessmentMapper mapper;
    private Timestamp fieldTestStartDate;
    private Timestamp fieldTestEndDate;
    private Timestamp segmentFieldTestStartDate;
    private Timestamp segmentFieldTestEndDate;

    @Before
    public void setUp() throws Exception {
        mapper = new AssessmentMapper();
        fieldTestStartDate = new Timestamp(Instant.now().minusSeconds(100000).toEpochMilli());
        fieldTestEndDate = new Timestamp(Instant.now().toEpochMilli());
        segmentFieldTestStartDate = new Timestamp(Instant.now().minusSeconds(200000).toEpochMilli());
        segmentFieldTestEndDate = new Timestamp(Instant.now().minusSeconds(300000).toEpochMilli());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldReturnANonSegmentedFixedFormAssessment() {
        String assessmentKey = "nonSegmentedFixedFormKey";
        List<Map<String, Object>> records =
            AssessmentRecordBuilder.createNonSegmentedFixedFormAssessmentRecord(assessmentKey,
                fieldTestStartDate,
                fieldTestEndDate,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                5,
                20);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();

        Assessment assessment = maybeAssessment.get();
        assertThat(assessment.getAssessmentId()).isEqualTo("assessmentId");
        assertThat(assessment.getKey()).isEqualTo(assessmentKey);
        assertThat(assessment.getStartAbility()).isEqualTo(9.5f);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.getFieldTestStartDate().getMillis()).isEqualTo(fieldTestStartDate.getTime());
        assertThat(assessment.getFieldTestEndDate().getMillis()).isEqualTo(fieldTestEndDate.getTime());
        assertThat(assessment.getAbilityIntercept()).isEqualTo(50.5f);
        assertThat(assessment.getAbilitySlope()).isEqualTo(99.9f);
        assertThat(assessment.getMaxOpportunities()).isEqualTo(100);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");
        assertThat(assessment.isInitialAbilityBySubject()).isTrue();
        assertThat(assessment.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);

        assertThat(assessment.getSegments()).hasSize(1);

        // Since this Assessment does not have any Segment records, the Segment's key will be the same as the
        // Assessment's key.  This is because we are examining the "default" Segment created by the AssessmentMapper to
        // enforce the rule that all Assessments must have at least one Segment.
        Segment defaultSegment = assessment.getSegment(assessmentKey);
        assertThat(defaultSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(defaultSegment.getSegmentId()).isEqualTo("assessmentId");
        assertThat(defaultSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);
        assertThat(defaultSegment.getMinItems()).isEqualTo(1);
        assertThat(defaultSegment.getMaxItems()).isEqualTo(10);
        assertThat(defaultSegment.getFieldTestMinItems()).isEqualTo(5);
        assertThat(defaultSegment.getFieldTestMaxItems()).isEqualTo(15);
        assertThat(defaultSegment.getFieldTestStartPosition()).isEqualTo(5);
        assertThat(defaultSegment.getFieldTestEndPosition()).isEqualTo(20);
        assertThat(defaultSegment.getPosition()).isEqualTo(1);
        assertThat(defaultSegment.getStartAbility()).isEqualTo(9.5f);
        assertThat(defaultSegment.getSubject()).isEqualTo("ELA");
    }

    @Test
    public void shouldReturnSegmentedFixedFormAssessment() {
        String assessmentKey = "segmentedFixedFormKey";
        List<Map<String, Object>> records =
            AssessmentRecordBuilder.createSegmentedFixedFormAssessmentRecords(assessmentKey,
                2,
                fieldTestStartDate,
                fieldTestEndDate,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                5,
                20);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();

        Assessment assessment = maybeAssessment.get();
        assertThat(assessment.getAssessmentId()).isEqualTo("assessmentId");
        assertThat(assessment.getKey()).isEqualTo(assessmentKey);
        assertThat(assessment.getStartAbility()).isEqualTo(9.5f);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.getFieldTestStartDate().getMillis()).isEqualTo(fieldTestStartDate.getTime());
        assertThat(assessment.getFieldTestEndDate().getMillis()).isEqualTo(fieldTestEndDate.getTime());
        assertThat(assessment.getAbilityIntercept()).isEqualTo(50.5f);
        assertThat(assessment.getAbilitySlope()).isEqualTo(99.9f);
        assertThat(assessment.getMaxOpportunities()).isEqualTo(100);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");
        assertThat(assessment.getSelectionAlgorithm()).isEqualTo(Algorithm.VIRTUAL);

        assertThat(assessment.getSegments()).hasSize(2);

        // Since this Assessment has separate Segment records, each Segment will have its own unique key
        Segment firstSegment = assessment.getSegment("segmentKey1");
        assertThat(firstSegment.getKey()).isEqualTo("segmentKey1");
        assertThat(firstSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(firstSegment.getSegmentId()).isEqualTo("segmentId-S1");
        assertThat(firstSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);
        assertThat(firstSegment.getMinItems()).isEqualTo(1);
        assertThat(firstSegment.getMaxItems()).isEqualTo(10);
        assertThat(firstSegment.getFieldTestMinItems()).isEqualTo(5);
        assertThat(firstSegment.getFieldTestMaxItems()).isEqualTo(15);
        assertThat(firstSegment.getPosition()).isEqualTo(1);
        assertThat(firstSegment.getStartAbility()).isEqualTo(1.5f);
        assertThat(firstSegment.getSubject()).isEqualTo("ELA");
        assertThat(firstSegment.getFieldTestStartDate().getMillis()).isEqualTo(segmentFieldTestStartDate.getTime());
        assertThat(firstSegment.getFieldTestEndDate().getMillis()).isEqualTo(segmentFieldTestEndDate.getTime());

        Segment secondSegment = assessment.getSegment("segmentKey2");
        assertThat(secondSegment.getKey()).isEqualTo("segmentKey2");
        assertThat(secondSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(secondSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);
        assertThat(secondSegment.getSegmentId()).isEqualTo("segmentId-S2");
        assertThat(secondSegment.getPosition()).isEqualTo(2);
        // all other fields are identical to firstSegment
    }

    @Test
    public void shouldReturnANonSegmentedAdaptiveAssessment() {
        String assessmentKey = "nonSegmentedAdaptiveKey";
        List<Map<String, Object>> records =
            AssessmentRecordBuilder.createNonSegmentedAdaptiveAssessmentRecord(assessmentKey,
                fieldTestStartDate,
                fieldTestEndDate,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                5,
                20);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();

        Assessment assessment = maybeAssessment.get();
        assertThat(assessment.getAssessmentId()).isEqualTo("assessmentId");
        assertThat(assessment.getKey()).isEqualTo(assessmentKey);
        assertThat(assessment.getStartAbility()).isEqualTo(9.5f);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.getFieldTestStartDate().getMillis()).isEqualTo(fieldTestStartDate.getTime());
        assertThat(assessment.getFieldTestEndDate().getMillis()).isEqualTo(fieldTestEndDate.getTime());
        assertThat(assessment.getAbilityIntercept()).isEqualTo(50.5f);
        assertThat(assessment.getAbilitySlope()).isEqualTo(99.9f);
        assertThat(assessment.getMaxOpportunities()).isEqualTo(100);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");
        assertThat(assessment.isInitialAbilityBySubject()).isTrue();
        assertThat(assessment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);

        assertThat(assessment.getSegments()).hasSize(1);

        // Since this Assessment does not have any Segment records, the Segment's key will be the same as the
        // Assessment's key.  This is because we are examining the "default" Segment created by the AssessmentMapper to
        // enforce the rule that all Assessments must have at least one Segment.
        Segment defaultSegment = assessment.getSegment(assessmentKey);
        assertThat(defaultSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(defaultSegment.getSegmentId()).isEqualTo("assessmentId");
        assertThat(defaultSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);
        assertThat(defaultSegment.getMinItems()).isEqualTo(1);
        assertThat(defaultSegment.getMaxItems()).isEqualTo(10);
        assertThat(defaultSegment.getFieldTestMinItems()).isEqualTo(5);
        assertThat(defaultSegment.getFieldTestMaxItems()).isEqualTo(15);
        assertThat(defaultSegment.getFieldTestStartPosition()).isEqualTo(5);
        assertThat(defaultSegment.getFieldTestEndPosition()).isEqualTo(20);
        assertThat(defaultSegment.getPosition()).isEqualTo(1);
        assertThat(defaultSegment.getStartAbility()).isEqualTo(9.5f);
        assertThat(defaultSegment.getSubject()).isEqualTo("ELA");
    }

    @Test
    public void shouldReturnASegmentedAdaptiveAssessment() {
        String assessmentKey = "segmentedAdaptiveKey";
        List<Map<String, Object>> records =
            AssessmentRecordBuilder.createSegmentedAdaptiveAssessmentRecords(assessmentKey,
                3,
                fieldTestStartDate,
                fieldTestEndDate,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                5,
                20);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();

        Assessment assessment = maybeAssessment.get();
        assertThat(assessment.getAssessmentId()).isEqualTo("assessmentId");
        assertThat(assessment.getKey()).isEqualTo(assessmentKey);
        assertThat(assessment.getStartAbility()).isEqualTo(9.5f);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.getFieldTestStartDate().getMillis()).isEqualTo(fieldTestStartDate.getTime());
        assertThat(assessment.getFieldTestEndDate().getMillis()).isEqualTo(fieldTestEndDate.getTime());
        assertThat(assessment.getAbilityIntercept()).isEqualTo(50.5f);
        assertThat(assessment.getAbilitySlope()).isEqualTo(99.9f);
        assertThat(assessment.getMaxOpportunities()).isEqualTo(100);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");
        assertThat(assessment.getSelectionAlgorithm()).isEqualTo(Algorithm.VIRTUAL);

        assertThat(assessment.getSegments()).hasSize(3);

        // Since this Assessment has separate Segment records, each Segment will have its own unique key
        Segment firstSegment = assessment.getSegment("segmentKey1");
        assertThat(firstSegment.getKey()).isEqualTo("segmentKey1");
        assertThat(firstSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(firstSegment.getSegmentId()).isEqualTo("segmentId-S1");
        assertThat(firstSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);
        assertThat(firstSegment.getMinItems()).isEqualTo(1);
        assertThat(firstSegment.getMaxItems()).isEqualTo(10);
        assertThat(firstSegment.getFieldTestMinItems()).isEqualTo(5);
        assertThat(firstSegment.getFieldTestMaxItems()).isEqualTo(15);
        assertThat(firstSegment.getFieldTestStartPosition()).isEqualTo(5);
        assertThat(firstSegment.getFieldTestEndPosition()).isEqualTo(20);
        assertThat(firstSegment.getPosition()).isEqualTo(1);
        assertThat(firstSegment.getStartAbility()).isEqualTo(1.5f);
        assertThat(firstSegment.getSubject()).isEqualTo("ELA");
        assertThat(firstSegment.getFieldTestStartDate().getMillis()).isEqualTo(segmentFieldTestStartDate.getTime());
        assertThat(firstSegment.getFieldTestEndDate().getMillis()).isEqualTo(segmentFieldTestEndDate.getTime());

        Segment secondSegment = assessment.getSegment("segmentKey2");
        assertThat(secondSegment.getKey()).isEqualTo("segmentKey2");
        assertThat(secondSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(secondSegment.getSegmentId()).isEqualTo("segmentId-S2");
        assertThat(secondSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);
        assertThat(secondSegment.getPosition()).isEqualTo(2);
        // all other fields are identical to firstSegment

        Segment thirdSegment = assessment.getSegment("segmentKey3");
        assertThat(thirdSegment.getKey()).isEqualTo("segmentKey3");
        assertThat(thirdSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(thirdSegment.getSegmentId()).isEqualTo("segmentId-S3");
        assertThat(thirdSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);
        assertThat(thirdSegment.getPosition()).isEqualTo(3);
        // all other fields are identical to firstSegment
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionForNullFtStartPositions() {
        String assessmentKey = "nullFTStartPosAssessment";
        List<Map<String, Object>> records =
            AssessmentRecordBuilder.createSegmentedAssessmentWithManySelectionAlgorithms(assessmentKey,
                1,
                3,
                fieldTestStartDate,
                fieldTestEndDate,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                null,
                null);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionForZeroFtStartPositions() {
        String assessmentKey = "nullFTStartPosAssessment";
        List<Map<String, Object>> records =
            AssessmentRecordBuilder.createSegmentedAssessmentWithManySelectionAlgorithms(assessmentKey,
                1,
                3,
                fieldTestStartDate,
                fieldTestEndDate,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                0,
                0);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();
    }

    @Test
    public void shouldReturnASegmentedAssessmentWithManySelectionAlgorithms() {
        String assessmentKey = "segmentedMultiAlgorithmKey";
        List<Map<String, Object>> records =
            AssessmentRecordBuilder.createSegmentedAssessmentWithManySelectionAlgorithms(assessmentKey,
                1,
                3,
                fieldTestStartDate,
                fieldTestEndDate,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                5,
                20);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();

        Assessment assessment = maybeAssessment.get();
        assertThat(assessment.getAssessmentId()).isEqualTo("assessmentId");
        assertThat(assessment.getKey()).isEqualTo(assessmentKey);
        assertThat(assessment.getStartAbility()).isEqualTo(9.5f);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.getFieldTestStartDate().getMillis()).isEqualTo(fieldTestStartDate.getTime());
        assertThat(assessment.getFieldTestEndDate().getMillis()).isEqualTo(fieldTestEndDate.getTime());
        assertThat(assessment.getAbilityIntercept()).isEqualTo(50.5f);
        assertThat(assessment.getAbilitySlope()).isEqualTo(99.9f);
        assertThat(assessment.getMaxOpportunities()).isEqualTo(100);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");
        assertThat(assessment.getSelectionAlgorithm()).isEqualTo(Algorithm.VIRTUAL);

        assertThat(assessment.getSegments()).hasSize(4);

        // Since this Assessment has separate Segment records, each Segment will have its own unique key
        Segment firstSegment = assessment.getSegment("segmentKey1");
        assertThat(firstSegment.getKey()).isEqualTo("segmentKey1");
        assertThat(firstSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(firstSegment.getSegmentId()).isEqualTo("segmentId-S1");
        assertThat(firstSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);
        assertThat(firstSegment.getMinItems()).isEqualTo(1);
        assertThat(firstSegment.getMaxItems()).isEqualTo(10);
        assertThat(firstSegment.getFieldTestMinItems()).isEqualTo(5);
        assertThat(firstSegment.getFieldTestMaxItems()).isEqualTo(15);
        assertThat(firstSegment.getFieldTestStartPosition()).isEqualTo(5);
        assertThat(firstSegment.getFieldTestEndPosition()).isEqualTo(20);
        assertThat(firstSegment.getPosition()).isEqualTo(1);
        assertThat(firstSegment.getStartAbility()).isEqualTo(1.5f);
        assertThat(firstSegment.getSubject()).isEqualTo("ELA");
        assertThat(firstSegment.getFieldTestStartDate().getMillis()).isEqualTo(segmentFieldTestStartDate.getTime());
        assertThat(firstSegment.getFieldTestEndDate().getMillis()).isEqualTo(segmentFieldTestEndDate.getTime());

        Segment secondSegment = assessment.getSegment("segmentKey2");
        assertThat(secondSegment.getKey()).isEqualTo("segmentKey2");
        assertThat(secondSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(secondSegment.getSegmentId()).isEqualTo("segmentId-S2");
        assertThat(secondSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);
        assertThat(secondSegment.getPosition()).isEqualTo(2);
        // all other fields are identical to firstSegment

        Segment thirdSegment = assessment.getSegment("segmentKey3");
        assertThat(thirdSegment.getKey()).isEqualTo("segmentKey3");
        assertThat(thirdSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(thirdSegment.getSegmentId()).isEqualTo("segmentId-S3");
        assertThat(thirdSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);
        assertThat(thirdSegment.getPosition()).isEqualTo(3);
        // all other fields are identical to firstSegment

        Segment fourthSegment = assessment.getSegment("segmentKey4");
        assertThat(fourthSegment.getKey()).isEqualTo("segmentKey4");
        assertThat(fourthSegment.getAssessmentKey()).isEqualTo(assessmentKey);
        assertThat(fourthSegment.getSegmentId()).isEqualTo("segmentId-S4");
        assertThat(fourthSegment.getSelectionAlgorithm()).isEqualTo(Algorithm.ADAPTIVE_2);
        assertThat(fourthSegment.getPosition()).isEqualTo(4);
        // all other fields are identical to firstSegment
    }
}