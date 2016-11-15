package tds.assessment.repositories.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
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
    Optional<Assessment> mapResults(List<Map<String, Object>> rows, List<Form> forms, List<Item> items) {
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
                Segment.Builder segment = new Segment.Builder(segmentKey)
                    .withSegmentId((String) row.get("assessmentSegmentId"))
                    .withAssessmentKey((String) row.get("assessmentKey"))
                    .withSelectionAlgorithm((String) row.get("selectionalgorithm"))
                    .withMinItems((int) row.get("minItems"))
                    .withMaxItems((int) row.get("maxItems"))
                    .withStartAbility((float) row.get("startAbility"))
                    .withPosition((Integer) row.get("segmentPosition"))
                    .withSubject((String) row.get("subject"))
                    .withForms(forms.stream()
                        .filter(form -> form.getSegmentKey().equals(segmentKey)).collect(Collectors.toList()))
                    .withItems(items.stream()
                        .filter(item -> item.getSegmentKey().equals(segmentKey)).collect(Collectors.toList()));

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
            Segment.Builder segment = new Segment.Builder((String) assessmentRow.get("assessmentSegmentKey"))
                .withSegmentId((String) assessmentRow.get("assessmentSegmentId"))
                .withSelectionAlgorithm((String) assessmentRow.get("selectionalgorithm"))
                .withStartAbility((float) assessmentRow.get("startAbility"))
                .withAssessmentKey((String) assessmentRow.get("assessmentSegmentKey"))
                .withPosition(1)
                .withMinItems((int) assessmentRow.get("minItems"))
                .withMaxItems((int) assessmentRow.get("maxItems"))
                .withSubject((String) assessmentRow.get("subject"))
                .withForms(forms)
                .withItems(items);

            if (assessmentRow.containsKey("propname")) {
                ItemProperty language = new ItemProperty(
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
        private List<ItemProperty> languages = new ArrayList<>();

        List<ItemProperty> getLanguages() {
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
