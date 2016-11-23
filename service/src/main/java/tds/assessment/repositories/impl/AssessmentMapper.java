package tds.assessment.repositories.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @param rows
     * @return
     */
    Optional<Assessment> mapResults(List<Map<String, Object>> rows) {
        Map<String, SegmentInformationHolder> segments = new LinkedHashMap<>();
        Map<String, Object> assessmentRow = null;
        for (Map<String, Object> row : rows) {
            if (row.get("assessmentKey") == null) { // This is the assessment row
                assessmentRow = row;
            } else {  // This is a segment row
                String key = (String) row.get("assessmentSegmentKey");

                if (!segments.containsKey(key)) {
                    segments.put(key, new SegmentInformationHolder());
                }

                SegmentInformationHolder holder = segments.get(key);
                String segmentKey = (String) row.get("assessmentSegmentKey");

                //If holder is null this is the first record for this segment
                Segment segment = new Segment(segmentKey);
                segment.setSegmentId((String) row.get("assessmentSegmentId"));
                segment.setAssessmentKey((String) row.get("assessmentKey"));
                segment.setSelectionAlgorithm(Algorithm.fromType((String) row.get("selectionalgorithm")));
                segment.setMinItems((int) row.get("minItems"));
                segment.setMaxItems((int) row.get("maxItems"));
                segment.setFieldTestMinItems((int) row.get("fieldTestMinItems"));
                segment.setFieldTestMaxItems((int) row.get("fieldTestMaxItems"));
                segment.setStartAbility((float) row.get("startAbility"));
                segment.setPosition((Integer) row.get("segmentPosition"));
                segment.setSubject((String) row.get("subject"));

                holder.setSegment(segment);

                //Add the language
                if (row.containsKey("propname")) {
                    ItemProperty language = new ItemProperty(
                        (String) row.get("propname"),
                        (String) row.get("propvalue"),
                        (String) row.get("propdescription")
                    );
                    holder.getLanguages().add(language);
                }

                segments.put(key, holder);
            }
        }

        //If no assessment row then it means we did not get the parent record so return empty
        if (assessmentRow == null) {
            return Optional.empty();
        }

        List<Segment> assessmentSegments = new ArrayList<>();
        if (segments.isEmpty()) {
            Segment segment = new Segment((String) assessmentRow.get("assessmentSegmentKey"));
            segment.setSegmentId((String) assessmentRow.get("assessmentSegmentId"));
            segment.setSelectionAlgorithm(Algorithm.fromType((String) assessmentRow.get("selectionalgorithm")));
            segment.setStartAbility((float) assessmentRow.get("startAbility"));
            segment.setAssessmentKey((String) assessmentRow.get("assessmentSegmentKey"));
            segment.setPosition(1);
            segment.setMinItems((int) assessmentRow.get("minItems"));
            segment.setMaxItems((int) assessmentRow.get("maxItems"));
            segment.setFieldTestMinItems((int) assessmentRow.get("fieldTestMinItems"));
            segment.setFieldTestMaxItems((int) assessmentRow.get("fieldTestMaxItems"));
            segment.setSubject((String) assessmentRow.get("subject"));

            if (assessmentRow.containsKey("propname")) {
                ItemProperty language = new ItemProperty(
                    (String) assessmentRow.get("propname"),
                    (String) assessmentRow.get("propvalue"),
                    (String) assessmentRow.get("propdescription")
                );

                segment.setLanguages(Collections.singletonList(language));
            }

            assessmentSegments.add(segment);
        } else {
            assessmentSegments.addAll(segments.values()
                .stream()
                .map(SegmentInformationHolder::build)
                .collect(Collectors.toList()));
        }

        Assessment assessment = new Assessment();
        assessment.setKey((String) assessmentRow.get("assessmentSegmentKey"));
        assessment.setAssessmentId((String) assessmentRow.get("assessmentSegmentId"));
        assessment.setSelectionAlgorithm(Algorithm.fromType((String) assessmentRow.get("selectionalgorithm")));
        assessment.setStartAbility((float) assessmentRow.get("startAbility"));
        assessment.setSubject((String) assessmentRow.get("subject"));
        assessment.setSegments(assessmentSegments);

        return Optional.of(assessment);
    }

    private class SegmentInformationHolder {
        private Segment segment;
        private List<ItemProperty> languages = new ArrayList<>();

        List<ItemProperty> getLanguages() {
            return languages;
        }

        void setSegment(Segment segment) {
            this.segment = segment;
        }

        Segment build() {
            segment.setLanguages(languages);
            return segment;
        }
    }

}
