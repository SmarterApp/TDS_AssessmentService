package tds.assessment.repositories.impl;

import org.joda.time.Instant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.assessment.Algorithm;
import tds.assessment.Assessment;
import tds.assessment.Segment;

/**
 * Mapper used to map results from the AssessmentQuery for assessments by key
 */
class AssessmentMapper {
    /**
     * Maps the results to an {@link tds.assessment.Assessment}.
     *
     * @param rows data result rows that represent a result row from the query result
     * @return optional with {@link tds.assessment.Assessment} otherwise empty
     */
    Optional<Assessment> mapResults(List<Map<String, Object>> rows) {
        Optional<Assessment> maybeAssessment = Optional.empty();
        Map<String, Segment> segments = new LinkedHashMap<>();

        for (Map<String, Object> result : rows) {
            ResultRow row = new ResultRow(result);
            if (row.getString("assessmentKey") == null) { // This is the ASSESSMENT row
                maybeAssessment = getAssessmentWithDefaultSegment(row);
            } else {  // This is a SEGMENT row
                String segmentKey = row.getString("assessmentSegmentKey");
                if (!segments.containsKey(segmentKey)) {
                    segments.put(segmentKey, getSegment(row));
                }
            }
        }

        // If the assessment data returned from the query has one or more segments, remove the default segment that was
        // created and replace it with the segments that were constructed from the data returned by the query.
        if (maybeAssessment.isPresent() && !segments.isEmpty()) {
            Assessment assessment = maybeAssessment.get();
            assessment.getSegments().clear();
            assessment.setSegments(new ArrayList<>(segments.values()));
        }

        return maybeAssessment;
    }

    /**
     * Create an {@link tds.assessment.Assessment} from data returned by the query.
     * <p>
     *     An assessment must always have one segment.  Since each record returned from the {@code findAssessmentByKey}
     *     method has enough data to build an assessment and/or a segment (because assessments and segments are stored
     *     in the same table), this method builds a "default" segment from the same record that is used to build the
     *     assessment.
     * </p>
     *
     * @param row The {@link tds.assessment.repositories.impl.AssessmentMapper.ResultRow} representing the assessment
     *            record returned from the query.
     * @return An {@link tds.assessment.Assessment} with a default {@link tds.assessment.Segment}
     */
    private Optional<Assessment> getAssessmentWithDefaultSegment(ResultRow row) {
        // When building the Assessment instance, the "assessmentSegmentKey" field represents the key (unique
        // identifier) of the Assessment
        Assessment assessment = new Assessment();
        assessment.setKey(row.getString("assessmentSegmentKey"));
        assessment.setAssessmentId(row.getString("assessmentSegmentId"));
        assessment.setSelectionAlgorithm(row.getAlgorithm("selectionAlgorithm"));
        assessment.setStartAbility(row.getFloat("startAbility"));
        assessment.setSubject(row.getString("subject"));
        assessment.setFieldTestStartDate(row.getJodaInstantFromTimestamp("ftstartdate"));
        assessment.setFieldTestEndDate(row.getJodaInstantFromTimestamp("ftenddate"));
        assessment.setAccommodationFamily(row.getString("accommodationfamily"));
        assessment.setMaxOpportunities(row.getInt("maxopportunities"));
        assessment.setAbilitySlope(row.getFloat("abilityslope"));
        assessment.setAbilityIntercept(row.getFloat("abilityintercept"));
        assessment.setInitialAbilityBySubject(row.getBoolean("initialabilitybysubject"));
        assessment.setValidateCompleteness(row.getBoolean("validateCompleteness"));
        assessment.setPrefetch(row.getInt("prefetch"));
        assessment.setDeleteUnansweredItems(row.getBoolean("deleteUnansweredItems"));
        // RULE:  An Assessment always has at least one Segment.  Create a "default" Segment from the data contained in
        // the row that represents the Assessment.  This will account for assessments that do not have any segments.  In
        // this case, the default Segment's assessmentKey will be set to the same value as the Assessment's key (to
        // indicate the Segment is owned by the Assessment).
        List<Segment> segments = new ArrayList<>();
        segments.add(getSegment(row));
        assessment.setSegments(segments);

        return Optional.of(assessment);
    }

    /**
     * Build a {@link tds.assessment.Segment} that represents the {@link tds.assessment.Assessment}'s selection
     * algorithm.
     *
     * @param row The database record containing data for the {@link tds.assessment.Segment}
     * @return A new {@link tds.assessment.Segment}
     */
    private Segment getSegment(ResultRow row) {
        // RULE:  The Segment's assessmentKey field should never be null.  If we're creating the "default" Segment (a
        // segment for an Assessment that doesn't have any Segment records in the database), the "assessmentKey" field
        // will be null, so the Segment's assessmentKey should be set to the whatever the Assessment's key is.  This
        // means for the "default" Segment, its key and assessmentKey fields will be the same value.
        String assessmentKey = row.getString("assessmentKey") == null
            ? row.getString("assessmentSegmentKey")
            : row.getString("assessmentKey");

        // RULE:  A Segment's position can never be less than 1 (because segment position is 1-based).  If the segment
        // position is less than 1 (which would be the case when segmentPosition is null in the result from the query -
        // getInt() will return 0), set it to one.
        int position = row.getInt("segmentPosition") < 1
            ? 1
            : row.getInt("segmentPosition");

        // Populate the segment instance with data.  Because we are building a Segment, the "assessmentSegmentKey"
        // represents the key (unique identifier) for this Segment (and not the Assessment).
        Segment segment = new Segment(row.getString("assessmentSegmentKey"),
            Algorithm.fromType(row.getString("selectionAlgorithm")));
        segment.setAssessmentKey(assessmentKey);
        segment.setSegmentId(row.getString("assessmentSegmentId"));
        segment.setStartAbility(row.getFloat("startAbility"));
        segment.setPosition(position);
        segment.setMinItems(row.getInt("minItems"));
        segment.setMaxItems(row.getInt("maxItems"));
        segment.setFieldTestMinItems(row.getInt("fieldTestMinItems"));
        segment.setFieldTestMaxItems(row.getInt("fieldTestMaxItems"));
        segment.setSubject(row.getString("subject"));
        segment.setFieldTestStartDate(row.getJodaInstantFromTimestamp("segFieldTestStartDate"));
        segment.setFieldTestEndDate(row.getJodaInstantFromTimestamp("segFieldTestEndDate"));

        return segment;
    }

    /**
     * A class representing a single record returned by the {@code findAssessmentByKey()} method.
     */
    private class ResultRow {
        private final Map<String, Object> rowData;

        ResultRow(Map<String, Object> rowData) {
            this.rowData = rowData;
        }

        int getInt(String key) {
            return rowData.get(key) != null ? (int) rowData.get(key) : 0;
        }

        float getFloat(String key) {
            return rowData.get(key) != null ? (float) rowData.get(key) : 0;
        }

        String getString(String key) {
            return rowData.get(key) != null ? (String) rowData.get(key) : null;
        }

        Instant getJodaInstantFromTimestamp(String key) {
            Object data = rowData.get(key);
            if (data == null) {
                return null;
            }

            Timestamp time = (Timestamp) data;
            return new Instant(time.getTime());
        }

        Algorithm getAlgorithm(String key) {
            return rowData.get(key) != null ? Algorithm.fromType(getString(key)) : null;
        }

        boolean getBoolean(String key) {
            return rowData.get(key) != null && (boolean) rowData.get(key);
        }
    }
}
