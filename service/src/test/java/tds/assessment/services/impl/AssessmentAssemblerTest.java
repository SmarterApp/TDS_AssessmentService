package tds.assessment.services.impl;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import tds.accommodation.AccommodationDependency;
import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.Strand;
import tds.common.Algorithm;

import static org.assertj.core.api.Assertions.assertThat;

public class AssessmentAssemblerTest {
    @Test
    public void shouldAssembleASegmentedFixedFormAssessment() {
        Assessment assessment = new Assessment();
        assessment.setKey("assessmentKey");
        assessment.setSelectionAlgorithm(Algorithm.FIXED_FORM);

        Segment segment = new Segment("assessmentKey", Algorithm.FIXED_FORM);
        assessment.setSegments(Collections.singletonList(segment));

        Strand strand = new Strand.Builder()
            .withSegmentKey(assessment.getKey())
            .build();
        Set<Strand> strands = new HashSet<>();
        strands.add(strand);
        ItemConstraint itemConstraint = new ItemConstraint.Builder().build();
        ItemProperty itemProperty = new ItemProperty("language", "ENU", "language", "187-564");
        ItemProperty itemProperty2 = new ItemProperty("something", "new", "desc", "187-564");

        Item item = new Item("187-564");
        item.setFormKey("form1");
        item.setFormPosition(2);
        item.setSegmentKey("assessmentKey");

        Item item2 = new Item("id2");
        item2.setFormKey("form1");
        item2.setFormPosition(1);
        item2.setSegmentKey("assessmentKey");
        Form form = new Form.Builder("form1")
            .withSegmentKey("assessmentKey")
            .build();

        AccommodationDependency dependency = new AccommodationDependency.Builder("context").build();

        AssessmentAssembler.assemble(assessment,
            strands,
            Collections.singletonList(itemConstraint),
            Arrays.asList(itemProperty, itemProperty2),
            Arrays.asList(item, item2),
            Collections.singletonList(form),
            Collections.singletonList(dependency),
            Collections.singletonList("Third"));

        assertThat(assessment.getStrands()).containsExactly(strand);
        assertThat(assessment.getLanguageCodes()).containsExactly("ENU");
        assertThat(assessment.getGrades()).containsExactly("Third");
        assertThat(segment.getItems()).containsExactly(item, item2);
        assertThat(form.getItems()).containsExactly(item2, item);
    }

    @Test
    public void shouldAssembleASegmentedAdaptiveAssessment() {
        Assessment assessment = new Assessment();
        assessment.setKey("assessmentKey");
        assessment.setSelectionAlgorithm(Algorithm.ADAPTIVE_2);

        Segment segment = new Segment("assessmentKey", Algorithm.ADAPTIVE_2);
        assessment.setSegments(Collections.singletonList(segment));

        Strand strand = new Strand.Builder()
            .withSegmentKey(assessment.getKey())
            .build();
        Set<Strand> strands = new HashSet<>();
        strands.add(strand);
        ItemConstraint itemConstraint = new ItemConstraint.Builder().build();
        ItemProperty itemProperty = new ItemProperty("language", "ENU", "language", "187-564");
        ItemProperty itemProperty2 = new ItemProperty("something", "new", "desc", "187-564");

        Item item = new Item("187-564");
        item.setSegmentKey("assessmentKey");

        Item item2 = new Item("id2");
        item2.setSegmentKey("assessmentKey");
        Form form = new Form.Builder("form1")
            .withSegmentKey("assessmentKey")
            .build();

        AccommodationDependency dependency = new AccommodationDependency.Builder("context").build();

        AssessmentAssembler.assemble(assessment,
            strands,
            Collections.singletonList(itemConstraint),
            Arrays.asList(itemProperty, itemProperty2),
            Arrays.asList(item, item2),
            Collections.singletonList(form),
            Collections.singletonList(dependency),
            Collections.singletonList("Third"));

        assertThat(assessment.getStrands()).containsExactly(strand);
        assertThat(assessment.getLanguageCodes()).containsExactly("ENU");
        assertThat(assessment.getGrades()).containsExactly("Third");
        assertThat(segment.getItems()).containsExactly(item, item2);
    }
}