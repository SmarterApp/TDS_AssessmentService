package tds.assessment.repositories.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import tds.assessment.Assessment;
import tds.assessment.Property;
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

                //If holder is null this is the first record for this segment
                Segment.Builder segment = new Segment.Builder((String) row.get("assessmentSegmentKey"))
                    .withSegmentId((String) row.get("assessmentSegmentId"))
                    .withAssessmentKey((String) row.get("assessmentKey"))
                    .withSelectionAlgorithm((String) row.get("selectionalgorithm"))
                    .withStartAbility((float) row.get("startAbility"))
                    .withSubject((String) row.get("subject"));

                holder.setSegment(segment);

                //Add the language
                if (row.containsKey("propname")) {
                    Property language = new Property(
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
            Segment.Builder segment = new Segment.Builder((String) assessmentRow.get("assessmentSegmentKey"))
                .withSegmentId((String) assessmentRow.get("assessmentSegmentId"))
                .withSelectionAlgorithm((String) assessmentRow.get("selectionalgorithm"))
                .withStartAbility((float) assessmentRow.get("startAbility"))
                .withAssessmentKey((String) assessmentRow.get("assessmentSegmentKey"))
                .withSubject((String) assessmentRow.get("subject"));

            if (assessmentRow.containsKey("propname")) {
                Property language = new Property(
                    (String) assessmentRow.get("propname"),
                    (String) assessmentRow.get("propvalue"),
                    (String) assessmentRow.get("propdescription")
                );

                segment.withLanguages(Collections.singletonList(language));
            }

            assessmentSegments.add(segment.build());
        } else {
            assessmentSegments.addAll(segments.values()
                .stream()
                .map(SegmentInformationHolder::build)
                .collect(Collectors.toList()));
        }

        Assessment assessment = new Assessment.Builder()
                    .withKey((String) assessmentRow.get("assessmentSegmentKey"))
                    .withAssessmentId((String) assessmentRow.get("assessmentSegmentId"))
                    .withSelectionAlgorithm((String) assessmentRow.get("selectionalgorithm"))
                    .withStartAbility((float) assessmentRow.get("startAbility"))
                    .withSubject((String) assessmentRow.get("subject"))
                    .withSegments(assessmentSegments)
                    .build();

        return Optional.of(assessment);
    }

    private class SegmentInformationHolder {
        private Segment.Builder segment;
        private List<Property> languages = new ArrayList<>();

        List<Property> getLanguages() {
            return languages;
        }

        void setSegment(Segment.Builder segment) {
            this.segment = segment;
        }

        Segment build() {
            segment.withLanguages(languages);
            return segment.build();
        }
    }

}
