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
import tds.assessment.ItemProperty;
import tds.assessment.Segment;

/**
 * Mapper used to map results from the AssessmentQuery for assessments by key
 */
class AssessmentMapper {
    /**
     * Maps the results to an {@link tds.assessment.Assessment}
     *
     * @param rows data result rows that represent a result row from the query result
     * @return optional with {@link tds.assessment.Assessment} otherwise empty
     */
    Optional<Assessment> mapResults(List<Map<String, Object>> rows) {
        Map<String, Segment> segments = new LinkedHashMap<>();
        ResultRow assessmentRow = null;
        for (Map<String, Object> result : rows) {
            ResultRow row = new ResultRow(result);
            if (row.getString("assessmentKey") == null) { // This is the assessment row
                assessmentRow = row;
            } else {  // This is a segment row
                String key = row.getString("assessmentSegmentKey");

                if (!segments.containsKey(key)) {
                    segments.put(key, new Segment(key));
                }

                updateSegmentWithRowData(segments.get(key), row, false);
            }
        }

        //If no assessment row then it means we did not get the parent record so return empty
        if (assessmentRow == null) {
            return Optional.empty();
        }

        List<Segment> assessmentSegments = new ArrayList<>();
        if (segments.isEmpty()) {
            Segment segment = new Segment(assessmentRow.getString("assessmentSegmentKey"));
            segment.setAssessmentKey(assessmentRow.getString("assessmentSegmentKey"));
            updateSegmentWithRowData(segment, assessmentRow, true);
            assessmentSegments.add(segment);
        } else {
            assessmentSegments.addAll(segments.values());
        }

        Assessment assessment = new Assessment();
        assessment.setKey(assessmentRow.getString("assessmentSegmentKey"));
        assessment.setAssessmentId(assessmentRow.getString("assessmentSegmentId"));
        assessment.setSelectionAlgorithm(assessmentRow.getAlgorithm("selectionalgorithm"));
        assessment.setStartAbility(assessmentRow.getFloat("startAbility"));
        assessment.setSubject(assessmentRow.getString("subject"));
        assessment.setAbilityIntercept(assessmentRow.getFloat("abilityintercept"));
        assessment.setAbilitySlope(assessmentRow.getFloat("abilityslope"));
        assessment.setFieldTestStartDate(assessmentRow.getJodaInstantFromTimestamp("ftstartdate"));
        assessment.setFieldTestEndDate(assessmentRow.getJodaInstantFromTimestamp("ftenddate"));
        assessment.setAbilitySlope(assessmentRow.getFloat("abilityslope"));
        assessment.setAbilityIntercept(assessmentRow.getFloat("abilityintercept"));
        assessment.setAccommodationFamily(assessmentRow.getString("accommodationfamily"));
        assessment.setMaxOpportunities(assessmentRow.getInt("maxopportunities"));

        assessment.setSegments(assessmentSegments);

        return Optional.of(assessment);
    }

    private void updateSegmentWithRowData(Segment segment, ResultRow row, boolean isAssessmentRow) {
        String assessmentKey = isAssessmentRow ? row.getString("assessmentSegmentKey") : row.getString("assessmentKey");
        int position = isAssessmentRow ? 1 : row.getInt("segmentPosition");

        segment.setSegmentId(row.getString("assessmentSegmentId"));
        segment.setAssessmentKey(assessmentKey);
        segment.setSelectionAlgorithm(row.getAlgorithm("selectionalgorithm"));
        segment.setMinItems(row.getInt("minItems"));
        segment.setMaxItems(row.getInt("maxItems"));
        segment.setFieldTestMinItems(row.getInt("fieldTestMinItems"));
        segment.setFieldTestMaxItems(row.getInt("fieldTestMaxItems"));
        segment.setStartAbility(row.getFloat("startAbility"));
        segment.setPosition(position);
        segment.setSubject(row.getString("subject"));

        //Add the language
        String propName = row.getString("propname");
        if (propName != null) {
            ItemProperty language = new ItemProperty(
                propName,
                row.getString("propvalue"),
                row.getString("propdescription")
            );
            segment.getLanguages().add(language);
        }
    }

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
    }
}
