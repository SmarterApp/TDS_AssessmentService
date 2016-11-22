package tds.assessment.services.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.Strand;

/**
 * A helper class for assembling the {@link tds.assessment.Assessment}
 */
class AssessmentAssembler {

    /**
     * Maps each {@link tds.assessment.ItemProperty} to its respective {@link tds.assessment.Item}
     *
     * @param items
     * @param itemProperties
     */
    static void assignPropertiesToItems(List<Item> items, List<ItemProperty> itemProperties) {
        Multimap<String, ItemProperty> itemIdToPropertyMap = ArrayListMultimap.create();
        itemProperties.stream()
                .filter(property -> property.getItemId() != null)
                .forEach(property -> itemIdToPropertyMap.put(property.getItemId(), property));

        for (Item item : items) {
            item.setItemProperties(new ArrayList<>(itemIdToPropertyMap.get(item.getId())));
        }
    }

    /**
     * Maps data to the {@link tds.assessment.Assessment} and {@link tds.assessment.Segment}s
     *
     * @param assessment the assessment object to map
     * @param forms     the forms to map to the segments in the assessment
     * @param items     the items to map to the segments in the assessment
     * @param constraints the constraints to map for the assessment
     * @param strands   the strands to map to each segment and assessment
     */
    static void assignDataToAssessmentSegments(Assessment assessment,
                                                      List<Form> forms,
                                                      List<Item> items,
                                                      List<ItemConstraint> constraints,
                                                      Set<Strand> strands) {
        assessment.setItemConstraints(constraints);
        assessment.setStrands(strands.stream()
                .filter(strand -> strand.getSegmentKey().equals(assessment.getKey())).collect(Collectors.toSet()));

        for (Segment segment : assessment.getSegments()) {
            segment.setForms(forms.stream()
                    .filter(form -> form.getSegmentKey().equals(segment.getKey())).collect(Collectors.toList()));
            segment.setItems(items.stream()
                    .filter(item -> item.getSegmentKey().equals(segment.getKey())).collect(Collectors.toList()));
            segment.setStrands(strands.stream()
                    .filter(strand -> strand.getSegmentKey().equals(segment.getKey())).collect(Collectors.toSet()));
        }
    }

}
