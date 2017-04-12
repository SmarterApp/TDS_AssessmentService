package tds.assessment.builders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tds.common.Algorithm;

/**
 * Build data structures that represent what Assessment data looks like in the database.  This class is intended to
 * support testing the {@code AssessmentMapper}.
 */
public class AssessmentRecordBuilder {
    /**
     * Build a simulated record set of a fixed form Assessment that has no associated Segments.
     *
     * @param assessmentKey The key that uniquely identifies the Assessment
     *                      Example:  '(SBAC_PT)SBAC-Perf-MATH-3-Spring-2013-2015'
     * @return A {@code List<Map<String, Object>>} simulating records returned from the database for a fixed form
     * Assessment with no associated Segments
     */
    public static List<Map<String, Object>> createNonSegmentedFixedFormAssessmentRecord(String assessmentKey,
                                                                                        Timestamp fieldTestStartDate,
                                                                                        Timestamp fieldTestEndDate,
                                                                                        Timestamp segmentFieldTestStartDate,
                                                                                        Timestamp segmentFieldTestEndDate,
                                                                                        Integer fieldTestStartPosition,
                                                                                        Integer fieldTestEndPosition) {
        return createAssessmentRecord(assessmentKey,
            Algorithm.FIXED_FORM,
            0,
            0,
            fieldTestStartDate,
            fieldTestEndDate,
            segmentFieldTestStartDate,
            segmentFieldTestEndDate,
            fieldTestStartPosition,
            fieldTestEndPosition);
    }

    /**
     * Build a simulated record set of a fixed form Assessment that has several associated Segments.
     *
     * @param assessmentKey    The key that uniquely identifies the Assessment
     *                         Example:  '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016'
     * @param numberOfSegments The number of Segments to create for this Assessment
     * @return A {@code List<Map<String, Object>>} simulating records returned from the database for a fixed form
     * Assessment with the number of associated Segments
     */
    public static List<Map<String, Object>> createSegmentedFixedFormAssessmentRecords(String assessmentKey,
                                                                                      int numberOfSegments,
                                                                                      Timestamp fieldTestStartDate,
                                                                                      Timestamp fieldTestEndDate,
                                                                                      Timestamp segmentFieldTestStartDate,
                                                                                      Timestamp segmentFieldTestEndDate,
                                                                                      Integer fieldTestStartPosition,
                                                                                      Integer fieldTestEndPosition) {
        return createAssessmentRecord(assessmentKey,
            Algorithm.VIRTUAL,
            numberOfSegments,
            0,
            fieldTestStartDate,
            fieldTestEndDate,
            segmentFieldTestStartDate,
            segmentFieldTestEndDate,
            fieldTestStartPosition,
            fieldTestEndPosition);
    }

    /**
     * Build a simulated record set of an adaptive Assessment that does not have any associated Segments.
     *
     * @param assessmentKey The key that uniquely identifies the Assessment
     *                      Example:  '(SBAC_PT)SBAC-IRP-CAT-ELA-11-Summer-2015-2016'
     * @return A {@code List<Map<String, Object>>} simulating records returned from the database for an adaptive
     * Assessment with no associated Segments
     */
    public static List<Map<String, Object>> createNonSegmentedAdaptiveAssessmentRecord(String assessmentKey,
                                                                                       Timestamp fieldTestStartDate,
                                                                                       Timestamp fieldTestEndDate,
                                                                                       Timestamp segmentFieldTestStartDate,
                                                                                       Timestamp segmentFieldTestEndDate,
                                                                                       Integer fieldTestStartPosition,
                                                                                       Integer fieldTestEndPosition) {
        return createAssessmentRecord(assessmentKey,
            Algorithm.ADAPTIVE_2,
            0,
            0,
            fieldTestStartDate,
            fieldTestEndDate,
            segmentFieldTestStartDate,
            segmentFieldTestEndDate,
            fieldTestStartPosition,
            fieldTestEndPosition);
    }

    /**
     * Build a simulated record set of an adaptive Assessment that has several associated Segments.
     *
     * @param assessmentKey    The key that uniquely identifies the Assessment
     *                         Example:  '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016'
     * @param numberOfSegments The number of Segments to create for this Assessment
     * @return A {@code List<Map<String, Object>>} simulating records returned from the database for an adaptive
     * Assessment with the number of associated Segments
     */
    public static List<Map<String, Object>> createSegmentedAdaptiveAssessmentRecords(String assessmentKey,
                                                                                     int numberOfSegments,
                                                                                     Timestamp fieldTestStartDate,
                                                                                     Timestamp fieldTestEndDate,
                                                                                     Timestamp segmentFieldTestStartDate,
                                                                                     Timestamp segmentFieldTestEndDate,
                                                                                     Integer fieldTestStartPosition,
                                                                                     Integer fieldTestEndPosition) {
        return createAssessmentRecord(assessmentKey,
            Algorithm.VIRTUAL,
            0,
            numberOfSegments,
            fieldTestStartDate,
            fieldTestEndDate,
            segmentFieldTestStartDate,
            segmentFieldTestEndDate,
            fieldTestStartPosition,
            fieldTestEndPosition);
    }

    /**
     * Build a simulated record set of an Assessment that has Segments with multiple selection algorithms.
     *
     * @param assessmentKey             The key that uniquely identifies the Assessment
     *                                  Example: '(SBAC_PT)MSB-Mathematics-3' (1 adaptive, 3 fixedform segments)
     * @param numberOfFixedFormSegments The number of "fixed form" Segments to create for this Assessment
     * @param numberOfAdaptiveSegments  The number of "adaptive" Segments to create for this Assessment
     * @return A {@code List<Map<String, Object>>} simulating records returned from the database for an Assessment with
     * the number of associated fixed-form and adaptive Segments
     */
    public static List<Map<String, Object>> createSegmentedAssessmentWithManySelectionAlgorithms(String assessmentKey,
                                                                                                 int numberOfFixedFormSegments,
                                                                                                 int numberOfAdaptiveSegments,
                                                                                                 Timestamp fieldTestStartDate,
                                                                                                 Timestamp fieldTestEndDate,
                                                                                                 Timestamp segmentFieldTestStartDate,
                                                                                                 Timestamp segmentFieldTestEndDate,
                                                                                                 Integer fieldTestStartPosition,
                                                                                                 Integer fieldTestEndPosition) {
        return createAssessmentRecord(assessmentKey,
            Algorithm.VIRTUAL,
            numberOfFixedFormSegments,
            numberOfAdaptiveSegments,
            fieldTestStartDate,
            fieldTestEndDate,
            segmentFieldTestStartDate,
            segmentFieldTestEndDate,
            fieldTestStartPosition,
            fieldTestEndPosition
            );
    }

    /**
     * Build a collection of assessment and segment data.  These records are designed to emulate the results returned
     * from the {@code AssessmentQueryRepositoryImpl.findAssessmentByKey()} query.
     *
     * @param assessmentKey             The key that uniquely identifies the Assessment in the {@code itembank.tblsetofadminsubjects}
     *                                  table.
     * @param selectionAlgorithm        The selection algorithm that should be assigned to the assessment
     * @param numberOfFixedFormSegments The number of Segments that should be associated to this Assessment
     * @param numberOfAdaptiveSegments  The number of Segments that should be associated to this Assessment
     * @return A {@code List<Map<String, Object>>} simulating records returned from the database for the Assessment and
     * its associated Segment(s)
     */
    private static List<Map<String, Object>> createAssessmentRecord(String assessmentKey,
                                                                    Algorithm selectionAlgorithm,
                                                                    int numberOfFixedFormSegments,
                                                                    int numberOfAdaptiveSegments,
                                                                    Timestamp fieldTestStartDate,
                                                                    Timestamp fieldTestEndDate,
                                                                    Timestamp segmentFieldTestStartDate,
                                                                    Timestamp segmentFieldTestEndDate,
                                                                    Integer fieldTestStartPosition,
                                                                    Integer fieldTestEndPosition) {
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> assessmentResult = new HashMap<>();

        assessmentResult.put("assessmentSegmentKey", assessmentKey);
        assessmentResult.put("assessmentSegmentId", "assessmentId");
        assessmentResult.put("selectionAlgorithm", selectionAlgorithm.getType());
        assessmentResult.put("startAbility", 9.5f);
        assessmentResult.put("segmentPosition", null); // The assessment record always has a null segmentPosition
        assessmentResult.put("minItems", 1);
        assessmentResult.put("maxItems", 10);
        assessmentResult.put("fieldTestMinItems", 5);
        assessmentResult.put("fieldTestMaxItems", 15);
        assessmentResult.put("fieldTestStartPosition", fieldTestStartPosition);
        assessmentResult.put("fieldTestEndPosition", fieldTestEndPosition);
        assessmentResult.put("subject", "ELA");
        assessmentResult.put("assessmentKey", null); // The assessment record always has a null virtualtest
        assessmentResult.put("ftstartdate", fieldTestStartDate); // this is always null in the db; set here for testing purposes only
        assessmentResult.put("ftenddate", fieldTestEndDate); // this is always null in the db; set here for testing purposes only
        assessmentResult.put("accommodationfamily", "family");
        assessmentResult.put("maxopportunities", 100);
        assessmentResult.put("abilityslope", 99.9f);
        assessmentResult.put("abilityintercept", 50.5f);
        assessmentResult.put("initialabilitybysubject", true);
        assessmentResult.put("segFieldTestStartDate", segmentFieldTestStartDate); // this is always null in the db; set here for testing purposes only
        assessmentResult.put("segFieldTestEndDate", segmentFieldTestEndDate); // this is always null in the db; set here for testing purposes only

        // Build the fixed-form segments first (if any were requested)
        for (int i = 1; i <= numberOfFixedFormSegments; i++) {
            Map<String, Object> segmentRecord = getSegmentRecord(i,
                assessmentKey,
                Algorithm.FIXED_FORM,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                fieldTestStartPosition,
                fieldTestEndPosition);
            records.add(segmentRecord);
        }

        // Build the adaptive segments second, setting them up to be positioned after the fixed-form segments (if any
        // fixed-form segments were requested).
        // NOTE: This behavior is for testing purposes only; there is no rule explicitly stating what order the
        // segments should go in.
        for (int j = numberOfFixedFormSegments + 1; j <= numberOfAdaptiveSegments + numberOfFixedFormSegments; j++) {
            Map<String, Object> segmentRecord = getSegmentRecord(j,
                assessmentKey,
                Algorithm.ADAPTIVE_2,
                segmentFieldTestStartDate,
                segmentFieldTestEndDate,
                fieldTestStartPosition,
                fieldTestEndPosition);
            records.add(segmentRecord);
        }

        records.add(assessmentResult);

        return records;
    }

    /**
     * Simulate a database record that represents a segment for an assessment.
     *
     * @param segmentIndex  The index of the segment being created
     * @param assessmentKey The key of the assessment that owns the segment
     * @param algorithm     The selection algorithm for the segment
     * @return A {@code Map} that represents a database record with Segment data
     */
    private static Map<String, Object> getSegmentRecord(int segmentIndex, String assessmentKey,
                                                        Algorithm algorithm,
                                                        Timestamp segmentFieldTestStartDate,
                                                        Timestamp segmentFieldTestEndDate,
                                                        Integer fieldTestStartPosition,
                                                        Integer fieldTestEndPosition) {
        String segmentKey = String.format("segmentKey%s", segmentIndex);
        String segmentId = String.format("segmentId-S%s", segmentIndex);
        Map<String, Object> segmentResult = new HashMap<>();

        segmentResult.put("assessmentSegmentKey", segmentKey);
        segmentResult.put("assessmentSegmentId", segmentId);
        segmentResult.put("selectionAlgorithm", algorithm.getType());
        segmentResult.put("minItems", 1);
        segmentResult.put("maxItems", 10);
        segmentResult.put("fieldTestStartPosition", fieldTestStartPosition);
        segmentResult.put("fieldTestEndPosition", fieldTestEndPosition);
        segmentResult.put("fieldTestMinItems", 5);
        segmentResult.put("fieldTestMaxItems", 15);
        segmentResult.put("subject", "ELA");
        segmentResult.put("assessmentKey", assessmentKey);
        segmentResult.put("startAbility", 1.5f);
        segmentResult.put("segmentPosition", segmentIndex);
        segmentResult.put("segFieldTestStartDate", segmentFieldTestStartDate);
        segmentResult.put("segFieldTestEndDate", segmentFieldTestEndDate);
        segmentResult.put("blueprintWeight", 99.9f);
        segmentResult.put("itemweight", 99.8f);
        segmentResult.put("abilityOffset", 99.7f);
        segmentResult.put("cset1size", 201);
        segmentResult.put("cset1order", "ability");
        segmentResult.put("cset2random", 199);
        segmentResult.put("cset2initialrandom", 198);
        segmentResult.put("slope", 99.3f);
        segmentResult.put("intercept", 99.2f);
        segmentResult.put("adaptiveVersion", "apVersion");
        segmentResult.put("abilityWeight", 197.1f);
        segmentResult.put("rcAbilityWeight", 99f);
        segmentResult.put("precisionTarget", 98.9f);
        segmentResult.put("precisionTargetMetWeight", 98.8f);
        segmentResult.put("precisionTargetNotMetWeight",98.7f);
        segmentResult.put("adaptiveCut", 98.6f);
        segmentResult.put("tooCloseSEs", 98.5f);
        segmentResult.put("terminationFlagsAnd", true);
        segmentResult.put("terminationMinCount", true);
        segmentResult.put("terminationOverallInfo", true);
        segmentResult.put("terminationRCInfo", true);
        segmentResult.put("terminationTooClose", true);

        return segmentResult;
    }
}
