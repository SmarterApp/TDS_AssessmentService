package tds.assessment.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tds.assessment.Algorithm;
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
     * Build a complete {@link tds.assessment.Assessment} out of its components.
     *
     * @param assessment The {@link tds.assessment.Assessment} to assemble
     * @param strands The {@link tds.assessment.Strand}s associated with the assessment (and any of the Segments if they
     *                are configured to use the "adaptive2" selection algorithm
     * @param itemConstraints A collection of {@link tds.assessment.ItemConstraint}s that should be applied to all Items
     *                        in the Assessment
     */
    static void assemble(Assessment assessment,
                         Set<Strand> strands,
                         List<ItemConstraint> itemConstraints,
                         List<ItemProperty> itemProperties,
                         List<Item> items,
                         List<Form> forms) {
        // Update assessment metadata
        assessment.setItemConstraints(itemConstraints);
        assessment.setStrands(strands.stream()
            .filter(strand -> strand.getSegmentKey().equals(assessment.getKey()))
            .collect(Collectors.toSet()));

        // To determine which language(s) are available to an assessment, we need to look at the item properties for
        // each item.  Each item should have a property with a name equal to "Language" (case-insensitive).  While
        // iterating through the item properties, we look for the language item property and collect its value.
        Set<String> languages = itemProperties.stream()
            .filter(ip -> ip.getName().equalsIgnoreCase("language"))
            .map(ItemProperty::getValue)
            .collect(Collectors.toSet());
        assessment.setLanguageCodes(languages);

        // Update items with their appropriate item properties
        for (Item item : items) {
            List<ItemProperty> properties = itemProperties.stream()
                .filter(ip -> ip.getItemId().equals(item.getId()))
                .collect(Collectors.toList());

            item.setItemProperties(properties);
        }

        // FIXED FORM SEGMENTS - If forms were supplied, wire up the items to their appropriate form
        if (forms.size() > 0) {
            for (Form form : forms) {
                List<Item> itemsForThisForm = items.stream()
                    .filter(i -> i.getFormKeys() != null
                        && i.getFormKeys().contains(form.getKey())
                        && i.getSegmentKey().equals(form.getSegmentKey()))
                    .collect(Collectors.toList());
                form.setItems(itemsForThisForm);

                // Add the form to its parent fixed-form segment
                assessment.getSegments().stream()
                    .filter(s -> s.getKey().equals(form.getSegmentKey())
                        && s.getSelectionAlgorithm().equals(Algorithm.FIXED_FORM))
                    .findFirst()
                    .ifPresent(s -> s.getForms().add(form));
            }
        }

        // ADAPTIVE SEGMENTS - Update the segment with data based on its selection algorithm
        for (Segment segment : assessment.getSegments()) {
            segment.setStrands(strands.stream()
                .filter(strand -> strand.getSegmentKey().equals(segment.getKey()))
                .collect(Collectors.toSet()));
            segment.setItems(items.stream()
                .filter(item -> item.getSegmentKey().equals(segment.getKey()))
                .collect(Collectors.toList()));
        }
    }
}