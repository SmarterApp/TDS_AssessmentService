package tds.assessment.repositories.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.Algorithm;
import tds.assessment.Assessment;
import tds.assessment.Segment;

import static org.assertj.core.api.Assertions.assertThat;

public class AssessmentMapperTest {
    private AssessmentMapper mapper;
    private Timestamp fieldTestStartDate;
    private Timestamp fieldTestEndDate;
    private Timestamp segFieldTestStartDate;
    private Timestamp segFieldTestEndDate;

    @Before
    public void setUp() throws Exception {
        mapper = new AssessmentMapper();
        fieldTestStartDate = new Timestamp(Instant.now().minusSeconds(100000).toEpochMilli());
        fieldTestEndDate = new Timestamp(Instant.now().toEpochMilli());
        segFieldTestStartDate = new Timestamp(Instant.now().minusSeconds(200000).toEpochMilli());
        segFieldTestEndDate = new Timestamp(Instant.now().minusSeconds(300000).toEpochMilli());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldReturnAssessmentWithoutSegments() {
        List<Map<String, Object>> records = createAssessmentRecord("assessmentKey1", 0);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();

        Assessment assessment = maybeAssessment.get();

        assertThat(assessment.getAssessmentId()).isEqualTo("assessmentId");
        assertThat(assessment.getKey()).isEqualTo("assessmentKey1");
        assertThat(assessment.getStartAbility()).isEqualTo(9.5f);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.getFieldTestStartDate().getMillis()).isEqualTo(fieldTestStartDate.getTime());
        assertThat(assessment.getFieldTestEndDate().getMillis()).isEqualTo(fieldTestEndDate.getTime());
        assertThat(assessment.getAbilityIntercept()).isEqualTo(50.5f);
        assertThat(assessment.getAbilitySlope()).isEqualTo(99.9f);
        assertThat(assessment.getMaxOpportunities()).isEqualTo(100);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");
        assertThat(assessment.isInitialAbilityBySubject()).isTrue();

        assertThat(assessment.getSegments()).hasSize(1);

        Segment segment = assessment.getSegment("assessmentKey1");
        assertThat(segment.getAssessmentKey()).isEqualTo("assessmentKey1");
        assertThat(segment.getSegmentId()).isEqualTo("assessmentId");
        assertThat(segment.getSelectionAlgorithm()).isEqualTo(Algorithm.VIRTUAL);
        assertThat(segment.getMinItems()).isEqualTo(1);
        assertThat(segment.getMaxItems()).isEqualTo(10);
        assertThat(segment.getFieldTestMinItems()).isEqualTo(5);
        assertThat(segment.getFieldTestMaxItems()).isEqualTo(15);
        assertThat(segment.getPosition()).isEqualTo(1);
        assertThat(segment.getStartAbility()).isEqualTo(9.5f);
        assertThat(segment.getSubject()).isEqualTo("ELA");
    }

    @Test
    public void shouldReturnSegmentedAssessment() {
        List<Map<String, Object>> records = createAssessmentRecord("assessmentKey1", 2);

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isPresent();

        Assessment assessment = maybeAssessment.get();

        assertThat(assessment.getAssessmentId()).isEqualTo("assessmentId");
        assertThat(assessment.getKey()).isEqualTo("assessmentKey1");
        assertThat(assessment.getStartAbility()).isEqualTo(9.5f);
        assertThat(assessment.getSubject()).isEqualTo("ELA");
        assertThat(assessment.getFieldTestStartDate().getMillis()).isEqualTo(fieldTestStartDate.getTime());
        assertThat(assessment.getFieldTestEndDate().getMillis()).isEqualTo(fieldTestEndDate.getTime());
        assertThat(assessment.getAbilityIntercept()).isEqualTo(50.5f);
        assertThat(assessment.getAbilitySlope()).isEqualTo(99.9f);
        assertThat(assessment.getMaxOpportunities()).isEqualTo(100);
        assertThat(assessment.getAccommodationFamily()).isEqualTo("family");

        assertThat(assessment.getSegments()).hasSize(2);

        Segment segment = assessment.getSegment("segmentKey1");
        assertThat(segment.getAssessmentKey()).isEqualTo("assessmentKey1");
        assertThat(segment.getSegmentId()).isEqualTo("segmentId");
        assertThat(segment.getSelectionAlgorithm()).isEqualTo(Algorithm.VIRTUAL);
        assertThat(segment.getMinItems()).isEqualTo(1);
        assertThat(segment.getMaxItems()).isEqualTo(10);
        assertThat(segment.getFieldTestMinItems()).isEqualTo(5);
        assertThat(segment.getFieldTestMaxItems()).isEqualTo(15);
        assertThat(segment.getPosition()).isEqualTo(1);
        assertThat(segment.getStartAbility()).isEqualTo(1.5f);
        assertThat(segment.getSubject()).isEqualTo("ELA");
        assertThat(segment.getFieldTestStartDate().getMillis()).isEqualTo(segFieldTestStartDate.getTime());
        assertThat(segment.getFieldTestEndDate().getMillis()).isEqualTo(segFieldTestEndDate.getTime());
        assertThat(assessment.getSegment("segmentKey2")).isNotNull();
    }

    @Test
    public void emptyResultWhenAssessmentIsNotPresent() {
        List<Map<String, Object>> records = new ArrayList<>();
        updateListWithSegmentData(records, "segmentKey", "assessmentKey");

        Optional<Assessment> maybeAssessment = mapper.mapResults(records);

        assertThat(maybeAssessment).isNotPresent();
    }

    private void updateListWithSegmentData(List<Map<String, Object>> records, String segmentKey, String assessmentKey) {
        Map<String, Object> segmentResult = new HashMap<>();

        segmentResult.put("assessmentKey", assessmentKey);
        segmentResult.put("assessmentSegmentKey", segmentKey);
        segmentResult.put("assessmentSegmentId", "segmentId");
        segmentResult.put("selectionalgorithm", Algorithm.VIRTUAL.getType());
        segmentResult.put("minItems", 1);
        segmentResult.put("maxItems", 10);
        segmentResult.put("fieldTestMinItems", 5);
        segmentResult.put("fieldTestMaxItems", 15);
        segmentResult.put("startAbility", 1.5f);
        segmentResult.put("segmentPosition", 1);
        segmentResult.put("subject", "ELA");
        segmentResult.put("segFieldTestStartDate", segFieldTestStartDate);
        segmentResult.put("segFieldTestEndDate", segFieldTestEndDate);
        records.add(segmentResult);
    }

    private List<Map<String, Object>> createAssessmentRecord(String assessmentKey, int numberOfSegments) {
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> assessmentResult = new HashMap<>();

        assessmentResult.put("assessmentSegmentKey", assessmentKey);
        assessmentResult.put("assessmentSegmentId", "assessmentId");
        assessmentResult.put("selectionalgorithm", Algorithm.VIRTUAL.getType());
        assessmentResult.put("startAbility", 9.5f);
        assessmentResult.put("subject", "ELA");

        assessmentResult.put("selectionalgorithm", Algorithm.VIRTUAL.getType());
        assessmentResult.put("minItems", 1);
        assessmentResult.put("maxItems", 10);
        assessmentResult.put("fieldTestMinItems", 5);
        assessmentResult.put("fieldTestMaxItems", 15);
        assessmentResult.put("segmentPosition", 1);
        assessmentResult.put("subject", "ELA");

        assessmentResult.put("ftstartdate", fieldTestStartDate);
        assessmentResult.put("ftenddate", fieldTestEndDate);
        assessmentResult.put("abilityslope", 99.9f);
        assessmentResult.put("abilityintercept", 50.5f);
        assessmentResult.put("accommodationfamily", "family");
        assessmentResult.put("maxopportunities", 100);
        assessmentResult.put("initialabilitybysubject", true);

        for (int i = 1; i <= numberOfSegments; i++) {
            updateListWithSegmentData(records, "segmentKey" + i, assessmentKey);
        }

        records.add(assessmentResult);

        return records;
    }
}