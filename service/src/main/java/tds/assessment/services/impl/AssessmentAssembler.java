/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.assessment.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tds.accommodation.AccommodationDependency;
import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.Strand;
import tds.common.Algorithm;

/**
 * A helper class for assembling the {@link tds.assessment.Assessment}
 */
class AssessmentAssembler {
    /**
     * Build a complete {@link tds.assessment.Assessment} out of its components.
     *
     * @param assessment                The {@link tds.assessment.Assessment} to assemble
     * @param strands                   The {@link tds.assessment.Strand}s associated with the assessment (and any of the Segments if they
     *                                  are configured to use the "adaptive2" selection algorithm
     * @param itemConstraints           A collection of {@link tds.assessment.ItemConstraint}s that should be applied to all Items
     *                                  in the Assessment
     * @param accommodationDependencies A collection of {@link tds.accommodation.AccommodationDependency} for accommodations of the assessment
     */
    static void assemble(final Assessment assessment,
                         final Set<Strand> strands,
                         final List<ItemConstraint> itemConstraints,
                         final List<ItemProperty> itemProperties,
                         final List<Item> items,
                         final List<Form> forms,
                         final List<AccommodationDependency> accommodationDependencies,
                         final List<String> grades) {
        // Update assessment metadata
        assessment.setItemConstraints(itemConstraints);
        assessment.setAccommodationDependencies(accommodationDependencies);
        assessment.setStrands(strands.stream()
            .filter(strand -> strand.getSegmentKey().equals(assessment.getKey()))
            .collect(Collectors.toSet()));
        assessment.setGrades(grades);

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
        // RULE:  The items in the form should be sorted by their formPosition in ascending order
        if (forms.size() > 0) {
            for (Form form : forms) {
                List<Item> itemsForThisForm = items.stream()
                    .filter(i -> i.getFormKey() != null
                        && i.getFormKey().equals(form.getKey())
                        && i.getSegmentKey().equals(form.getSegmentKey()))
                    .sorted(Comparator.comparingInt(Item::getFormPosition))
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