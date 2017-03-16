package tds.assessment.services.impl;

import com.google.common.base.Optional;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
        // Assessment Init
        final String assessmentId = "foo";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        assessment.setAssessmentId(assessmentId);
        
        // Accommodation Dependencies init
        List<AccommodationDependency> accommodationDependencies = Arrays.asList(
            new AccommodationDependency.Builder(assessmentId)
                .withIfType("Language")
                .withIfValue("ESN")
                .withThenType("Word List")
                .withThenValue("TDS_WL_Glossary")
                .withIsDefault(false)
                .build()
        );
        assessment.setAccommodationDependencies(accommodationDependencies);

        // Segments init
        List<Segment> segs = new ArrayList<>();
        Segment seg1 = new Segment("seg1", Algorithm.FIXED_FORM);
        seg1.setAssessmentKey(assessment.getKey());
        Segment seg2 = new Segment("seg2", Algorithm.FIXED_FORM);
        seg2.setAssessmentKey(assessment.getKey());
        segs.add(seg1);
        segs.add(seg2);
        assessment.setSegments(segs);

        // Item constraints init
        List<ItemConstraint> itemConstraints = new ArrayList<>();
        ItemConstraint constraint1 = new ItemConstraint.Builder()
            .withAssessmentId(assessmentId)
            .withInclusive(true)
            .withPropertyName("Language")
            .withPropertyValue("ENU")
            .withToolType("Language")
            .withToolValue("ENU")
            .build();
        ItemConstraint constraint2 = new ItemConstraint.Builder()
            .withAssessmentId(assessmentId)
            .withInclusive(false)
            .withPropertyName("--ITEMTYPE--")
            .withPropertyValue("ER")
            .withToolType("Item Types Exclusion")
            .withToolValue("TDS_ItemTypeExcl_ER")
            .build();
        itemConstraints.add(constraint1);
        itemConstraints.add(constraint2);

        // Forms init
        List<Form> forms = new ArrayList<>();
        Form seg1form1 = new Form.Builder("form1")
            .withSegmentKey("seg1")
            .withLanguage("lang1")
            .withCohort("Chargers")
            .build();
        Form seg1form2 = new Form.Builder("form2")
            .withSegmentKey("seg1")
            .withLanguage("lang2")
            .withCohort("broncos")
            .build();
        Form seg2form1 = new Form.Builder("form3")
            .withSegmentKey("seg2")
            .withCohort("chiefs")
            .withLanguage("lang3")
            .build();
        forms.add(seg1form1);
        forms.add(seg1form2);
        forms.add(seg2form1);

        // Strands init
        Set<Strand> assessmentStrands = new HashSet<>();
        Strand assessmentStrand = new Strand.Builder()
            .withKey("Strand1-key")
            .withName("Strand1")
            .withMinItems(0)
            .withMaxItems(4)
            .withSegmentKey("theKey")
            .withAdaptiveCut(26.32F)
            .build();
        assessmentStrands.add(assessmentStrand);
        Set<Strand> seg1Strands = new HashSet<>();
        Strand seg1Strand1 = new Strand.Builder()
            .withKey("Strand2-key")
            .withName("Strand2")
            .withMinItems(2)
            .withMaxItems(3)
            .withSegmentKey("seg1")
            .withAdaptiveCut(29.35F)
            .build();
        Strand seg1Strand2 = new Strand.Builder()
            .withKey("Strand3-key")
            .withName("Strand3")
            .withMinItems(0)
            .withMaxItems(12)
            .withSegmentKey("seg1")
            .withAdaptiveCut(-26.32F)
            .build();
        seg1Strands.add(seg1Strand1);
        seg1Strands.add(seg1Strand2);

        Set<Strand> seg2Strands = new HashSet<>();
        Strand seg2Strand1 = new Strand.Builder()
            .withKey("Strand4-key")
            .withName("Strand4")
            .withMinItems(3)
            .withMaxItems(14)
            .withSegmentKey("seg2")
            .withAdaptiveCut(-32.522F)
            .build();
        seg2Strands.add(seg2Strand1);

        Set<Strand> allStrands = new HashSet<>();
        allStrands.addAll(assessmentStrands);
        allStrands.addAll(seg1Strands);
        allStrands.addAll(seg2Strands);

        // ITEMS INIT
        // Items to Forms map (this is how form items are returned from the database)

        // Build items for Segment 1 segForm1
        List<Item> items = new ArrayList<>();
        Item seg1Form1Item1 = new Item("item1");
        Set seg1Form1Item1FormKeys = new HashSet<>();
        seg1Form1Item1.setGroupId("G-1");
        seg1Form1Item1.setPosition(1);
        seg1Form1Item1.setSegmentKey("seg1");
        seg1Form1Item1FormKeys.add("form1");
        seg1Form1Item1.setFormKeys(seg1Form1Item1FormKeys);

        // NOTE:  This item is shared across multiple forms: form1 and form2
        Item seg1Form1Item2andForm2Item1 = new Item("item2");
        Set seg1Form1Item2andForm2Item1FormKeys = new HashSet<>();
        seg1Form1Item2andForm2Item1.setGroupId("G-2");
        seg1Form1Item2andForm2Item1.setPosition(2);
        seg1Form1Item2andForm2Item1.setSegmentKey("seg1");
        seg1Form1Item2andForm2Item1FormKeys.add("form1");
        seg1Form1Item2andForm2Item1FormKeys.add("form2");
        seg1Form1Item2andForm2Item1.setFormKeys(seg1Form1Item2andForm2Item1FormKeys);

        // Build items for Segment 1 segForm2
        Item seg1Form2Item1 = new Item("item3");
        Set seg1Form2Item1Formkeys = new HashSet<>();
        seg1Form2Item1.setGroupId("G-1");
        seg1Form2Item1.setPosition(1);
        seg1Form2Item1.setSegmentKey("seg1");
        seg1Form2Item1Formkeys.add("form2");
        seg1Form2Item1.setFormKeys(seg1Form2Item1Formkeys);

        // Build items for Segment 2 segForm1
        Item seg2Form1Item1 = new Item("item4");
        Set seg2Form1ItemsFormKeys = new HashSet<>();
        seg2Form1Item1.setGroupId("G-3");
        seg2Form1Item1.setPosition(1);
        seg2Form1Item1.setSegmentKey("seg2");
        seg2Form1ItemsFormKeys.add("form3");
        seg2Form1Item1.setFormKeys(seg2Form1ItemsFormKeys);

        // Add items to collection
        items.add(seg1Form1Item1);
        items.add(seg1Form1Item2andForm2Item1);
        items.add(seg1Form2Item1);
        items.add(seg2Form1Item1);

        // Item props
        List<ItemProperty> itemProperties = new ArrayList<>();

        // Properties for Item1
        ItemProperty prop1 = new ItemProperty("name1", "val1", "desc1", "item1");
        ItemProperty prop2 = new ItemProperty("name2", "val2", "desc2", "item1");
        ItemProperty langProp1 = new ItemProperty("language", "lang1", "desc5", "item1");

        // Properties for Item2
        ItemProperty langProp2 = new ItemProperty("language", "lang1", "desc6", "item2");  // verify the same language doesn't show up more than once on the assessment

        // Properties for Item3
        ItemProperty prop3 = new ItemProperty("name3", "val3", "desc3", "item3");
        ItemProperty langProp3 = new ItemProperty("language", "lang2", "desc7", "item3");

        // Properties for Item4
        ItemProperty prop4 = new ItemProperty("name4", "val4", "desc4", "item4");
        ItemProperty langProp4 = new ItemProperty("language", "lang3", "desc8", "item4");

        itemProperties.add(prop1);
        itemProperties.add(prop2);
        itemProperties.add(prop3);
        itemProperties.add(prop4);
        itemProperties.add(langProp1);
        itemProperties.add(langProp2);
        itemProperties.add(langProp3);
        itemProperties.add(langProp4);

        // CALL ASSEMBLE - for a fixed-form Assessment
        AssessmentAssembler.assemble(assessment, allStrands, itemConstraints, itemProperties, items, forms, accommodationDependencies);

        List<Segment> retSegments = assessment.getSegments();
        assertThat(retSegments).hasSize(2);

        // Assessment Languages
        assertThat(assessment.getLanguageCodes()).containsExactlyInAnyOrder("lang1", "lang2", "lang3");

        // Item constraints
        List<ItemConstraint> retConstraints = assessment.getItemConstraints();
        assertThat(retConstraints).hasSize(2);
        ItemConstraint retCon1 = retConstraints.get(0);
        assertThat(retCon1.isInclusive()).isTrue();
        assertThat(retCon1.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon1.getToolType()).isEqualTo(constraint1.getToolType());
        assertThat(retCon1.getToolValue()).isEqualTo(constraint1.getToolValue());
        assertThat(retCon1.getPropertyName()).isEqualTo(constraint1.getPropertyName());
        assertThat(retCon1.getPropertyValue()).isEqualTo(constraint1.getPropertyValue());

        ItemConstraint retCon2 = retConstraints.get(1);
        assertThat(retCon2.isInclusive()).isFalse();
        assertThat(retCon2.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon2.getToolType()).isEqualTo(constraint2.getToolType());
        assertThat(retCon2.getToolValue()).isEqualTo(constraint2.getToolValue());
        assertThat(retCon2.getPropertyName()).isEqualTo(constraint2.getPropertyName());
        assertThat(retCon2.getPropertyValue()).isEqualTo(constraint2.getPropertyValue());
        
        // Assessment Dependencies
        assertThat(assessment.getAccommodationDependencies()).containsExactly(accommodationDependencies.get(0));

        // Assessment strand
        Set<Strand> retAssessmentStrands = assessment.getStrands();
        assertThat(retAssessmentStrands).hasSize(1);
        Strand retAssessmentStrand = retAssessmentStrands.iterator().next(); // we know its one item, just get the first one
        assertThat(retAssessmentStrand.getKey()).isEqualTo(assessmentStrand.getKey());
        assertThat(retAssessmentStrand.getSegmentKey()).isEqualTo(assessment.getKey());
        assertThat(retAssessmentStrand.getMinItems()).isEqualTo(assessmentStrand.getMinItems());
        assertThat(retAssessmentStrand.getMaxItems()).isEqualTo(assessmentStrand.getMaxItems());
        assertThat(retAssessmentStrand.getAdaptiveCut()).isEqualTo(assessmentStrand.getAdaptiveCut());

        // Segment 1
        Segment retSeg1 = retSegments.get(0);
        assertThat(retSeg1.getKey()).isEqualTo("seg1");
        List<Item> retSeg1Form1Items = retSeg1.getForms().get(0).getItems();
        assertThat(retSeg1Form1Items).hasSize(2);

        // Segment 1 Forms
        assertThat(retSeg1.getForms()).containsExactlyInAnyOrder(seg1form1, seg1form2);

        Form retSeg1Form1 = retSeg1.getForms().get(0);
        assertThat(retSeg1Form1.getKey()).isEqualTo("form1");
        assertThat(retSeg1Form1.getSegmentKey()).isEqualTo("seg1");
        assertThat(retSeg1Form1.getItems()).hasSize(2);
        assertThat(retSeg1Form1.getItems()).containsExactlyInAnyOrder(seg1Form1Item1, seg1Form1Item2andForm2Item1);

        Form retSeg1Form2 = retSeg1.getForms().get(1);
        assertThat(retSeg1Form2.getKey()).isEqualTo("form2");
        assertThat(retSeg1Form2.getSegmentKey()).isEqualTo("seg1");
        assertThat(retSeg1Form2.getItems()).hasSize(2);
        assertThat(retSeg1Form2.getItems()).containsExactlyInAnyOrder(seg1Form1Item2andForm2Item1, seg1Form2Item1);

        // SEGMENT 1 ITEMS + ITEM PROPERTIES
        // Segment 1, Form 1, Item 1
        Item retSeg1Form1Item1 = retSeg1Form1Items.get(0);
        assertThat(retSeg1Form1Item1.getId()).isEqualTo(seg1Form1Item1.getId());
        assertThat(retSeg1Form1Item1.getSegmentKey()).isEqualTo(seg1Form1Item1.getSegmentKey());
        assertThat(retSeg1Form1Item1.getGroupId()).isEqualTo(seg1Form1Item1.getGroupId());
        assertThat(retSeg1Form1Item1.getPosition()).isEqualTo(seg1Form1Item1.getPosition());

        List<ItemProperty> retSeg1Form1Item1Prop = retSeg1Form1Item1.getItemProperties();
        assertThat(retSeg1Form1Item1Prop).hasSize(3);
        assertThat(retSeg1Form1Item1Prop.get(0).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Form1Item1Prop.get(0).getName()).isEqualTo("name1");
        assertThat(retSeg1Form1Item1Prop.get(0).getValue()).isEqualTo("val1");
        assertThat(retSeg1Form1Item1Prop.get(0).getDescription()).isEqualTo("desc1");
        assertThat(retSeg1Form1Item1Prop.get(1).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Form1Item1Prop.get(1).getName()).isEqualTo("name2");
        assertThat(retSeg1Form1Item1Prop.get(1).getValue()).isEqualTo("val2");
        assertThat(retSeg1Form1Item1Prop.get(1).getDescription()).isEqualTo("desc2");
        assertThat(retSeg1Form1Item1Prop.get(2).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Form1Item1Prop.get(2).getName()).isEqualTo("language");
        assertThat(retSeg1Form1Item1Prop.get(2).getValue()).isEqualTo("lang1");
        assertThat(retSeg1Form1Item1Prop.get(2).getDescription()).isEqualTo("desc5");

        // Segment 1, Form 1, Item 2
        Item retSeg1Form1Item2 = retSeg1Form1Items.get(1);
        assertThat(retSeg1Form1Item2.getId()).isEqualTo(seg1Form1Item2andForm2Item1.getId());
        assertThat(retSeg1Form1Item2.getSegmentKey()).isEqualTo(seg1Form1Item2andForm2Item1.getSegmentKey());
        assertThat(retSeg1Form1Item2.getGroupId()).isEqualTo(seg1Form1Item2andForm2Item1.getGroupId());
        assertThat(retSeg1Form1Item2.getPosition()).isEqualTo(seg1Form1Item2andForm2Item1.getPosition());

        List<ItemProperty> retSeg1FormItem2Prop = retSeg1Form1Item2.getItemProperties();
        assertThat(retSeg1FormItem2Prop).hasSize(1);
        assertThat(retSeg1FormItem2Prop.get(0).getItemId()).isEqualTo("item2");
        assertThat(retSeg1FormItem2Prop.get(0).getName()).isEqualTo("language");
        assertThat(retSeg1FormItem2Prop.get(0).getValue()).isEqualTo("lang1");
        assertThat(retSeg1FormItem2Prop.get(0).getDescription()).isEqualTo("desc6");

        // Segment 1, Form 2, Item 1
        Item retSeg1Form2Item1 = retSeg1Form2.getItems().get(0);
        assertThat(retSeg1Form2Item1.getId()).isEqualTo(seg1Form1Item2andForm2Item1.getId());
        assertThat(retSeg1Form2Item1.getSegmentKey()).isEqualTo(seg1Form1Item2andForm2Item1.getSegmentKey());
        assertThat(retSeg1Form2Item1.getGroupId()).isEqualTo(seg1Form1Item2andForm2Item1.getGroupId());
        assertThat(retSeg1Form2Item1.getPosition()).isEqualTo(seg1Form1Item2andForm2Item1.getPosition());

        List<ItemProperty> retSeg1Form2Item1Prop = retSeg1Form2Item1.getItemProperties();
        assertThat(retSeg1Form2Item1Prop).hasSize(1);
        assertThat(retSeg1Form2Item1Prop.get(0).getItemId()).isEqualTo("item2");
        assertThat(retSeg1Form2Item1Prop.get(0).getName()).isEqualTo("language");
        assertThat(retSeg1Form2Item1Prop.get(0).getValue()).isEqualTo("lang1");
        assertThat(retSeg1Form2Item1Prop.get(0).getDescription()).isEqualTo("desc6");

        // Segment 1, Form 2, Item 2
        Item retSeg1Form2Item2 = retSeg1Form2.getItems().get(1);
        assertThat(retSeg1Form2Item2.getId()).isEqualTo(seg1Form2Item1.getId());
        assertThat(retSeg1Form2Item2.getSegmentKey()).isEqualTo(seg1Form2Item1.getSegmentKey());
        assertThat(retSeg1Form2Item2.getGroupId()).isEqualTo(seg1Form2Item1.getGroupId());
        assertThat(retSeg1Form2Item2.getPosition()).isEqualTo(seg1Form2Item1.getPosition());

        List<ItemProperty> retSeg1Form2Item2Prop = retSeg1Form2Item2.getItemProperties();
        assertThat(retSeg1Form2Item2Prop).hasSize(2);
        assertThat(retSeg1Form2Item2Prop.get(0).getItemId()).isEqualTo("item3");
        assertThat(retSeg1Form2Item2Prop.get(0).getName()).isEqualTo("name3");
        assertThat(retSeg1Form2Item2Prop.get(0).getValue()).isEqualTo("val3");
        assertThat(retSeg1Form2Item2Prop.get(0).getDescription()).isEqualTo("desc3");
        assertThat(retSeg1Form2Item2Prop.get(1).getItemId()).isEqualTo("item3");
        assertThat(retSeg1Form2Item2Prop.get(1).getName()).isEqualTo("language");
        assertThat(retSeg1Form2Item2Prop.get(1).getValue()).isEqualTo("lang2");
        assertThat(retSeg1Form2Item2Prop.get(1).getDescription()).isEqualTo("desc7");

        // Segment 2
        Segment retSeg2 = retSegments.get(1);
        assertThat(retSeg2.getKey()).isEqualTo("seg2");

        // Seg 2 forms
        List<Form> retSeg2Forms = retSeg2.getForms();
        assertThat(retSeg2Forms).hasSize(1);
        Form retSeg2Form1 = retSeg2Forms.get(0);
        assertThat(retSeg2Form1.getKey()).isEqualTo("form3");
        assertThat(retSeg2Form1.getSegmentKey()).isEqualTo("seg2");
        assertThat(retSeg2Form1.getItems()).hasSize(1);
        assertThat(retSeg2Form1.getItems()).contains(seg2Form1Item1);

        // SEGMENT 2 ITEMS + ITEM PROPERTIES
        // Seg 2 items + item props
        List<Item> retSeg2Items = retSeg2Form1.getItems();
        assertThat(retSeg2Items).hasSize(1);

        // Segment 2, Form 1, Item 1
        Item retSeg2Form1Item1 = retSeg2Items.get(0);
        assertThat(retSeg2Form1Item1.getId()).isEqualTo(seg2Form1Item1.getId());
        assertThat(retSeg2Form1Item1.getSegmentKey()).isEqualTo(seg2Form1Item1.getSegmentKey());
        assertThat(retSeg2Form1Item1.getGroupId()).isEqualTo(seg2Form1Item1.getGroupId());
        assertThat(retSeg2Form1Item1.getPosition()).isEqualTo(seg2Form1Item1.getPosition());

        List<ItemProperty> retSeg2Form1Item1Prop = retSeg2Form1Item1.getItemProperties();
        assertThat(retSeg2Form1Item1Prop).hasSize(2);
        assertThat(retSeg2Form1Item1Prop.get(0).getItemId()).isEqualTo("item4");
        assertThat(retSeg2Form1Item1Prop.get(0).getName()).isEqualTo("name4");
        assertThat(retSeg2Form1Item1Prop.get(0).getValue()).isEqualTo("val4");
        assertThat(retSeg2Form1Item1Prop.get(0).getDescription()).isEqualTo("desc4");
        assertThat(retSeg2Form1Item1Prop.get(1).getItemId()).isEqualTo("item4");
        assertThat(retSeg2Form1Item1Prop.get(1).getName()).isEqualTo("language");
        assertThat(retSeg2Form1Item1Prop.get(1).getValue()).isEqualTo("lang3");
        assertThat(retSeg2Form1Item1Prop.get(1).getDescription()).isEqualTo("desc8");

        // SEGMENTS - GET ITEMS BY LANGUAGE
        List<Item> retSeg1Lang1Items = retSeg1.getItems("lang1");
        assertThat(retSeg1Lang1Items).hasSize(2);

        List<Item> retSeg1Lang2Items = retSeg1.getItems("lang2");
        assertThat(retSeg1Lang2Items).hasSize(2);

        List<Item> retSeg2Lang3Items = retSeg2.getItems("lang3");
        assertThat(retSeg2Lang3Items).hasSize(1);

        // SEGMENTS - GET FORM BY LANGUAGE
        List<Form> retLang1Forms = retSeg1.getForms("lang1");
        assertThat(retLang1Forms).hasSize(1);
        Form retLang1Form = retLang1Forms.get(0);
        assertThat(retLang1Form.getLanguageCode()).isEqualTo("lang1");
        assertThat(retLang1Form.getKey()).isEqualTo("form1");
        assertThat(retLang1Form.getItems()).hasSize(2);
        assertThat(retLang1Form.getSegmentKey()).isEqualTo("seg1");

        // SEGMENTS - GET FORM BY LANGUAGE AND COHORT
        Optional<Form> maybeRetLang2Form = retSeg1.getForm("lang2", "broncos");
        Form retLang2Form = maybeRetLang2Form.get();
        assertThat(retLang2Form.getLanguageCode()).isEqualTo("lang2");
        assertThat(retLang2Form.getKey()).isEqualTo("form2");
        assertThat(retLang2Form.getItems()).hasSize(2);
        assertThat(retLang2Form.getSegmentKey()).isEqualTo("seg1");

        Optional<Form> maybeRetLang3Form = retSeg2.getForm("lang3", "chiefs");
        Form retLang3Form = maybeRetLang3Form.get();
        assertThat(retLang3Form.getLanguageCode()).isEqualTo("lang3");
        assertThat(retLang3Form.getKey()).isEqualTo("form3");
        assertThat(retLang3Form.getItems()).hasSize(1);
        assertThat(retLang3Form.getSegmentKey()).isEqualTo("seg2");
    }

    @Test
    public void shouldAssembleAdaptiveAssessment() {
        // Assessment Init
        final String assessmentId = "foo";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        assessment.setAssessmentId(assessmentId);

        // Segments init
        List<Segment> segs = new ArrayList<>();
        Segment seg1 = new Segment("seg1", Algorithm.ADAPTIVE_2);
        seg1.setAssessmentKey(assessment.getKey());
        Segment seg2 = new Segment("seg2", Algorithm.ADAPTIVE_2);
        seg2.setAssessmentKey(assessment.getKey());
        segs.add(seg1);
        segs.add(seg2);
        assessment.setSegments(segs);

        // Item constraints init
        List<ItemConstraint> itemConstraints = new ArrayList<>();
        ItemConstraint constraint1 = new ItemConstraint.Builder()
            .withAssessmentId(assessmentId)
            .withInclusive(true)
            .withPropertyName("Language")
            .withPropertyValue("ENU")
            .withToolType("Language")
            .withToolValue("ENU")
            .build();
        ItemConstraint constraint2 = new ItemConstraint.Builder()
            .withAssessmentId(assessmentId)
            .withInclusive(false)
            .withPropertyName("--ITEMTYPE--")
            .withPropertyValue("ER")
            .withToolType("Item Types Exclusion")
            .withToolValue("TDS_ItemTypeExcl_ER")
            .build();
        itemConstraints.add(constraint1);
        itemConstraints.add(constraint2);

        // Strands init
        Set<Strand> assessmentStrands = new HashSet<>();
        Strand assessmentStrand = new Strand.Builder()
            .withKey("Strand1-key")
            .withName("Strand1")
            .withMinItems(0)
            .withMaxItems(4)
            .withSegmentKey("theKey")
            .withAdaptiveCut(26.32F)
            .build();
        assessmentStrands.add(assessmentStrand);
        Set<Strand> seg1Strands = new HashSet<>();
        Strand seg1Strand1 = new Strand.Builder()
            .withKey("Strand2-key")
            .withName("Strand2")
            .withMinItems(2)
            .withMaxItems(3)
            .withSegmentKey("seg1")
            .withAdaptiveCut(29.35F)
            .build();
        Strand seg1Strand2 = new Strand.Builder()
            .withKey("Strand3-key")
            .withName("Strand3")
            .withMinItems(0)
            .withMaxItems(12)
            .withSegmentKey("seg1")
            .withAdaptiveCut(-26.32F)
            .build();
        seg1Strands.add(seg1Strand1);
        seg1Strands.add(seg1Strand2);

        Set<Strand> seg2Strands = new HashSet<>();
        Strand seg2Strand1 = new Strand.Builder()
            .withKey("Strand4-key")
            .withName("Strand4")
            .withMinItems(3)
            .withMaxItems(14)
            .withSegmentKey("seg2")
            .withAdaptiveCut(-32.522F)
            .build();
        seg2Strands.add(seg2Strand1);

        Set<Strand> allStrands = new HashSet<>();
        allStrands.addAll(assessmentStrands);
        allStrands.addAll(seg1Strands);
        allStrands.addAll(seg2Strands);

        // Items init
        List<Item> items = new ArrayList<>();
        Item seg1Item1 = new Item("item1");
        seg1Item1.setGroupId("G-1");
        seg1Item1.setPosition(1);
        seg1Item1.setSegmentKey("seg1");
        Item seg1Item2 = new Item("item2");
        seg1Item2.setGroupId("G-2");
        seg1Item2.setPosition(2);
        seg1Item2.setSegmentKey("seg1");
        Item seg2Item1 = new Item("item3");
        seg2Item1.setGroupId("G-3");
        seg2Item1.setPosition(1);
        seg2Item1.setSegmentKey("seg2");
        items.add(seg1Item1);
        items.add(seg1Item2);
        items.add(seg2Item1);

        // Item props
        List<ItemProperty> itemProperties = new ArrayList<>();
        ItemProperty prop1 = new ItemProperty("name1", "val1", "desc1", "item1");
        ItemProperty languageProp1 = new ItemProperty("language", "lang1", "desc4", "item1");
        ItemProperty prop2 = new ItemProperty("name2", "val2", "desc2", "item1");
        ItemProperty languageProp2 = new ItemProperty("language", "lang2", "desc5", "item2");
        ItemProperty prop3 = new ItemProperty("name3", "val3", "desc3", "item3");
        ItemProperty languageProp3 = new ItemProperty("language", "lang3", "desc6", "item3");
        itemProperties.add(prop1);
        itemProperties.add(prop2);
        itemProperties.add(prop3);
        itemProperties.add(languageProp1);
        itemProperties.add(languageProp2);
        itemProperties.add(languageProp3);

        // CALL ASSEMBLE - for a fixed-form Assessment
        AssessmentAssembler.assemble(assessment, allStrands, itemConstraints, itemProperties, items, new ArrayList<>(), new ArrayList<>());

        List<Segment> retSegments = assessment.getSegments();
        assertThat(retSegments).hasSize(2);

        // Assessment Languages
        assertThat(assessment.getLanguageCodes()).containsExactlyInAnyOrder("lang1", "lang2", "lang3");

        // Item constraints
        List<ItemConstraint> retConstraints = assessment.getItemConstraints();
        assertThat(retConstraints).hasSize(2);
        ItemConstraint retCon1 = retConstraints.get(0);
        assertThat(retCon1.isInclusive()).isTrue();
        assertThat(retCon1.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon1.getToolType()).isEqualTo(constraint1.getToolType());
        assertThat(retCon1.getToolValue()).isEqualTo(constraint1.getToolValue());
        assertThat(retCon1.getPropertyName()).isEqualTo(constraint1.getPropertyName());
        assertThat(retCon1.getPropertyValue()).isEqualTo(constraint1.getPropertyValue());

        ItemConstraint retCon2 = retConstraints.get(1);
        assertThat(retCon2.isInclusive()).isFalse();
        assertThat(retCon2.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon2.getToolType()).isEqualTo(constraint2.getToolType());
        assertThat(retCon2.getToolValue()).isEqualTo(constraint2.getToolValue());
        assertThat(retCon2.getPropertyName()).isEqualTo(constraint2.getPropertyName());
        assertThat(retCon2.getPropertyValue()).isEqualTo(constraint2.getPropertyValue());

        // Assessment strand
        Set<Strand> retAssessmentStrands = assessment.getStrands();
        assertThat(retAssessmentStrands).hasSize(1);
        Strand retAssessmentStrand = retAssessmentStrands.iterator().next(); // we know its one item, just get the first one
        assertThat(retAssessmentStrand.getKey()).isEqualTo(assessmentStrand.getKey());
        assertThat(retAssessmentStrand.getSegmentKey()).isEqualTo(assessment.getKey());
        assertThat(retAssessmentStrand.getMinItems()).isEqualTo(assessmentStrand.getMinItems());
        assertThat(retAssessmentStrand.getMaxItems()).isEqualTo(assessmentStrand.getMaxItems());
        assertThat(retAssessmentStrand.getAdaptiveCut()).isEqualTo(assessmentStrand.getAdaptiveCut());

        // Segment 1
        Segment retSeg1 = retSegments.get(0);
        assertThat(retSeg1.getKey()).isEqualTo("seg1");
        List<Item> retSeg1Items = retSeg1.getItems("lang1");
        assertThat(retSeg1Items).hasSize(1);

        // Seg 1 Items + item props
        Item retSeg1Item1 = retSeg1Items.get(0);
        assertThat(retSeg1Item1.getId()).isEqualTo(seg1Item1.getId());
        assertThat(retSeg1Item1.getSegmentKey()).isEqualTo(seg1Item1.getSegmentKey());
        assertThat(retSeg1Item1.getGroupId()).isEqualTo(seg1Item1.getGroupId());
        assertThat(retSeg1Item1.getPosition()).isEqualTo(seg1Item1.getPosition());

        List<ItemProperty> retSeg1Item1Prop = retSeg1Item1.getItemProperties();
        assertThat(retSeg1Item1Prop).hasSize(3);
        assertThat(retSeg1Item1Prop.get(0).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Item1Prop.get(0).getName()).isEqualTo("name1");
        assertThat(retSeg1Item1Prop.get(0).getValue()).isEqualTo("val1");
        assertThat(retSeg1Item1Prop.get(0).getDescription()).isEqualTo("desc1");
        assertThat(retSeg1Item1Prop.get(1).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Item1Prop.get(1).getName()).isEqualTo("name2");
        assertThat(retSeg1Item1Prop.get(1).getValue()).isEqualTo("val2");
        assertThat(retSeg1Item1Prop.get(1).getDescription()).isEqualTo("desc2");
        assertThat(retSeg1Item1Prop.get(2).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Item1Prop.get(2).getName()).isEqualTo("language");
        assertThat(retSeg1Item1Prop.get(2).getValue()).isEqualTo("lang1");
        assertThat(retSeg1Item1Prop.get(2).getDescription()).isEqualTo("desc4");

        retSeg1Items = retSeg1.getItems("lang2");
        assertThat(retSeg1Items).hasSize(1);
        Item retSeg1Item2 = retSeg1Items.get(0);
        List<ItemProperty> retSeg1Item2Prop = retSeg1Item2.getItemProperties();
        assertThat(retSeg1Item2Prop).hasSize(1);
        assertThat(retSeg1Item2Prop.get(0).getItemId()).isEqualTo("item2");
        assertThat(retSeg1Item2Prop.get(0).getName()).isEqualTo("language");
        assertThat(retSeg1Item2Prop.get(0).getValue()).isEqualTo("lang2");
        assertThat(retSeg1Item2Prop.get(0).getDescription()).isEqualTo("desc5");

        // Segment 1 Strands
        Set<Strand> retSeg1Strands = retSeg1.getStrands();
        assertThat(retSeg1Strands).hasSize(2);

        Strand retSeg1Strand1 = null;
        Strand retSeg1Strand2 = null;

        for (Strand retStrand : retSeg1Strands) {
            if (retStrand.getName().equals(seg1Strand1.getName())) {
                retSeg1Strand1 = retStrand;
            } else if (retStrand.getName().equals(seg1Strand2.getName())) {
                retSeg1Strand2 = retStrand;
            }
        }

        assertThat(retSeg1Strand1.getKey()).isEqualTo(seg1Strand1.getKey());
        assertThat(retSeg1Strand1.getSegmentKey()).isEqualTo(seg1.getKey());
        assertThat(retSeg1Strand1.getMinItems()).isEqualTo(seg1Strand1.getMinItems());
        assertThat(retSeg1Strand1.getMaxItems()).isEqualTo(seg1Strand1.getMaxItems());
        assertThat(retSeg1Strand1.getAdaptiveCut()).isEqualTo(seg1Strand1.getAdaptiveCut());
        assertThat(retSeg1Strand2.getKey()).isEqualTo(seg1Strand2.getKey());
        assertThat(retSeg1Strand2.getSegmentKey()).isEqualTo(seg1.getKey());
        assertThat(retSeg1Strand2.getMinItems()).isEqualTo(seg1Strand2.getMinItems());
        assertThat(retSeg1Strand2.getMaxItems()).isEqualTo(seg1Strand2.getMaxItems());
        assertThat(retSeg1Strand2.getAdaptiveCut()).isEqualTo(seg1Strand2.getAdaptiveCut());

        // Segment 2
        Segment retSeg2 = retSegments.get(1);
        assertThat(retSeg2.getKey()).isEqualTo("seg2");

        // Segment 2 Strands
        Set<Strand> retSeg2Strands = retSeg2.getStrands();
        assertThat(retSeg2Strands).hasSize(1);
        Strand retSeg2Strand1 = retSeg2Strands.iterator().next(); // we know its one item, just get the first one
        assertThat(retSeg2Strand1.getKey()).isEqualTo(seg2Strand1.getKey());
        assertThat(retSeg2Strand1.getSegmentKey()).isEqualTo(seg2.getKey());
        assertThat(retSeg2Strand1.getMinItems()).isEqualTo(seg2Strand1.getMinItems());
        assertThat(retSeg2Strand1.getMaxItems()).isEqualTo(seg2Strand1.getMaxItems());
        assertThat(retSeg2Strand1.getAdaptiveCut()).isEqualTo(seg2Strand1.getAdaptiveCut());

        // Seg 2 items + item props
        List<Item> retSeg2Items = retSeg2.getItems("lang3");
        assertThat(retSeg2Items).hasSize(1);

        Item retSeg2Item1 = retSeg2Items.get(0);
        assertThat(retSeg2Item1.getId()).isEqualTo(seg2Item1.getId());
        assertThat(retSeg2Item1.getSegmentKey()).isEqualTo(seg2Item1.getSegmentKey());
        assertThat(retSeg2Item1.getGroupId()).isEqualTo(seg2Item1.getGroupId());
        assertThat(retSeg2Item1.getPosition()).isEqualTo(seg2Item1.getPosition());

        List<ItemProperty> retSeg2Item1Prop = retSeg2Item1.getItemProperties();
        assertThat(retSeg2Item1Prop).hasSize(2);
        assertThat(retSeg2Item1Prop.get(0).getItemId()).isEqualTo("item3");
        assertThat(retSeg2Item1Prop.get(0).getName()).isEqualTo("name3");
        assertThat(retSeg2Item1Prop.get(0).getValue()).isEqualTo("val3");
        assertThat(retSeg2Item1Prop.get(0).getDescription()).isEqualTo("desc3");
        assertThat(retSeg2Item1Prop.get(1).getItemId()).isEqualTo("item3");
        assertThat(retSeg2Item1Prop.get(1).getName()).isEqualTo("language");
        assertThat(retSeg2Item1Prop.get(1).getValue()).isEqualTo("lang3");
        assertThat(retSeg2Item1Prop.get(1).getDescription()).isEqualTo("desc6");
    }

    @Test
    public void shouldAssembleAnAssessmentWithManySelectionAlgorithms() {
        // Assessment Init
        final String assessmentId = "foo";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        assessment.setAssessmentId(assessmentId);

        // Segments init
        List<Segment> segs = new ArrayList<>();
        Segment seg1 = new Segment("seg1", Algorithm.FIXED_FORM);
        seg1.setAssessmentKey(assessment.getKey());
        Segment seg2 = new Segment("seg2", Algorithm.ADAPTIVE_2);
        seg2.setAssessmentKey(assessment.getKey());
        segs.add(seg1);
        segs.add(seg2);
        assessment.setSegments(segs);

        // Item constraints init
        List<ItemConstraint> itemConstraints = new ArrayList<>();
        ItemConstraint constraint1 = new ItemConstraint.Builder()
            .withAssessmentId(assessmentId)
            .withInclusive(true)
            .withPropertyName("Language")
            .withPropertyValue("ENU")
            .withToolType("Language")
            .withToolValue("ENU")
            .build();
        ItemConstraint constraint2 = new ItemConstraint.Builder()
            .withAssessmentId(assessmentId)
            .withInclusive(false)
            .withPropertyName("--ITEMTYPE--")
            .withPropertyValue("ER")
            .withToolType("Item Types Exclusion")
            .withToolValue("TDS_ItemTypeExcl_ER")
            .build();
        itemConstraints.add(constraint1);
        itemConstraints.add(constraint2);

        // Forms init
        List<Form> forms = new ArrayList<>();
        Form seg1form1 = new Form.Builder("form1")
            .withSegmentKey("seg1")
            .withLanguage("lang1")
            .build();
        Form seg1form2 = new Form.Builder("form2")
            .withSegmentKey("seg1")
            .withLanguage("lang2")
            .build();
        forms.add(seg1form1);
        forms.add(seg1form2);

        // Strands init
        Set<Strand> assessmentStrands = new HashSet<>();
        Strand assessmentStrand = new Strand.Builder()
            .withKey("Strand1-key")
            .withName("Strand1")
            .withMinItems(0)
            .withMaxItems(4)
            .withSegmentKey("theKey")
            .withAdaptiveCut(26.32F)
            .build();
        assessmentStrands.add(assessmentStrand);
        Set<Strand> seg1Strands = new HashSet<>();
        Strand seg1Strand1 = new Strand.Builder()
            .withKey("Strand2-key")
            .withName("Strand2")
            .withMinItems(2)
            .withMaxItems(3)
            .withSegmentKey("seg1")
            .withAdaptiveCut(29.35F)
            .build();
        Strand seg1Strand2 = new Strand.Builder()
            .withKey("Strand3-key")
            .withName("Strand3")
            .withMinItems(0)
            .withMaxItems(12)
            .withSegmentKey("seg1")
            .withAdaptiveCut(-26.32F)
            .build();
        seg1Strands.add(seg1Strand1);
        seg1Strands.add(seg1Strand2);

        Set<Strand> seg2Strands = new HashSet<>();
        Strand seg2Strand1 = new Strand.Builder()
            .withKey("Strand4-key")
            .withName("Strand4")
            .withMinItems(3)
            .withMaxItems(14)
            .withSegmentKey("seg2")
            .withAdaptiveCut(-32.522F)
            .build();
        seg2Strands.add(seg2Strand1);

        Set<Strand> allStrands = new HashSet<>();
        allStrands.addAll(assessmentStrands);
        allStrands.addAll(seg1Strands);
        allStrands.addAll(seg2Strands);

        // ITEMS INIT
        // Build items for segForm1
        List<Item> items = new ArrayList<>();

        Item seg1Form1Item1 = new Item("item1");
        Set<String> seg1Form1Item1FormKeys = new HashSet<>();
        seg1Form1Item1.setGroupId("G-1");
        seg1Form1Item1.setPosition(1);
        seg1Form1Item1.setSegmentKey("seg1");
        seg1Form1Item1FormKeys.add("form1");
        seg1Form1Item1.setFormKeys(seg1Form1Item1FormKeys);

        Item seg1Form1Item2andForm2Item1 = new Item("item2");
        Set<String> seg1Form1Item2andForm2Item1FormKeys = new HashSet<>();
        seg1Form1Item2andForm2Item1.setGroupId("G-2");
        seg1Form1Item2andForm2Item1.setPosition(2);
        seg1Form1Item2andForm2Item1.setSegmentKey("seg1");
        seg1Form1Item2andForm2Item1FormKeys.add("form1");
        seg1Form1Item2andForm2Item1FormKeys.add("form2");
        seg1Form1Item2andForm2Item1.setFormKeys(seg1Form1Item2andForm2Item1FormKeys);

        // Build items for segForm2
        Item seg1Form2Item1 = new Item("item3");
        Set<String> seg1Form2Item1FormKeys = new HashSet<>();
        seg1Form2Item1.setGroupId("G-1");
        seg1Form2Item1.setPosition(1);
        seg1Form2Item1.setSegmentKey("seg1");
        seg1Form2Item1FormKeys.add("form2");
        seg1Form2Item1.setFormKeys(seg1Form2Item1FormKeys);

        // Build items for seg2
        Item seg2Item1 = new Item("item4");
        seg2Item1.setGroupId("G-3");
        seg2Item1.setPosition(1);
        seg2Item1.setSegmentKey("seg2");

        items.add(seg1Form1Item1);
        items.add(seg1Form1Item2andForm2Item1);
        items.add(seg1Form2Item1);
        items.add(seg2Item1);

        // Item props
        List<ItemProperty> itemProperties = new ArrayList<>();

        // Properties for Item1
        ItemProperty prop1 = new ItemProperty("name1", "val1", "desc1", "item1");
        ItemProperty prop2 = new ItemProperty("name2", "val2", "desc2", "item1");
        ItemProperty langProp1 = new ItemProperty("language", "lang1", "desc5", "item1");

        // Properties for Item2
        ItemProperty langProp2 = new ItemProperty("language", "lang1", "desc6", "item2");  // verify the same language doesn't show up more than once on the assessment

        // Properties for Item3
        ItemProperty prop3 = new ItemProperty("name3", "val3", "desc3", "item3");
        ItemProperty langProp3 = new ItemProperty("language", "lang2", "desc7", "item3");

        // Properties for Item4
        ItemProperty prop4 = new ItemProperty("name4", "val4", "desc4", "item4");
        ItemProperty langProp4 = new ItemProperty("language", "lang3", "desc8", "item4");

        itemProperties.add(prop1);
        itemProperties.add(prop2);
        itemProperties.add(prop3);
        itemProperties.add(prop4);
        itemProperties.add(langProp1);
        itemProperties.add(langProp2);
        itemProperties.add(langProp3);
        itemProperties.add(langProp4);

        // CALL ASSEMBLE - for a fixed-form Assessment
        AssessmentAssembler.assemble(assessment, allStrands, itemConstraints, itemProperties, items, forms, new ArrayList<>());

        List<Segment> retSegments = assessment.getSegments();
        assertThat(retSegments).hasSize(2);

        // Assessment Languages
        assertThat(assessment.getLanguageCodes()).containsExactlyInAnyOrder("lang1", "lang2", "lang3");

        // Item constraints
        List<ItemConstraint> retConstraints = assessment.getItemConstraints();
        assertThat(retConstraints).hasSize(2);
        ItemConstraint retCon1 = retConstraints.get(0);
        assertThat(retCon1.isInclusive()).isTrue();
        assertThat(retCon1.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon1.getToolType()).isEqualTo(constraint1.getToolType());
        assertThat(retCon1.getToolValue()).isEqualTo(constraint1.getToolValue());
        assertThat(retCon1.getPropertyName()).isEqualTo(constraint1.getPropertyName());
        assertThat(retCon1.getPropertyValue()).isEqualTo(constraint1.getPropertyValue());

        ItemConstraint retCon2 = retConstraints.get(1);
        assertThat(retCon2.isInclusive()).isFalse();
        assertThat(retCon2.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon2.getToolType()).isEqualTo(constraint2.getToolType());
        assertThat(retCon2.getToolValue()).isEqualTo(constraint2.getToolValue());
        assertThat(retCon2.getPropertyName()).isEqualTo(constraint2.getPropertyName());
        assertThat(retCon2.getPropertyValue()).isEqualTo(constraint2.getPropertyValue());

        // Assessment strand
        Set<Strand> retAssessmentStrands = assessment.getStrands();
        assertThat(retAssessmentStrands).hasSize(1);
        Strand retAssessmentStrand = retAssessmentStrands.iterator().next(); // we know its one item, just get the first one
        assertThat(retAssessmentStrand.getKey()).isEqualTo(assessmentStrand.getKey());
        assertThat(retAssessmentStrand.getSegmentKey()).isEqualTo(assessment.getKey());
        assertThat(retAssessmentStrand.getMinItems()).isEqualTo(assessmentStrand.getMinItems());
        assertThat(retAssessmentStrand.getMaxItems()).isEqualTo(assessmentStrand.getMaxItems());
        assertThat(retAssessmentStrand.getAdaptiveCut()).isEqualTo(assessmentStrand.getAdaptiveCut());

        // Segment 1
        Segment retSeg1 = retSegments.get(0);
        assertThat(retSeg1.getKey()).isEqualTo("seg1");
        assertThat(retSeg1.getSelectionAlgorithm()).isEqualTo(Algorithm.FIXED_FORM);

        // Segment 1 Forms
        assertThat(retSeg1.getForms()).containsExactlyInAnyOrder(seg1form1, seg1form2);

        Form retSeg1Form1 = retSeg1.getForms().get(0);
        assertThat(retSeg1Form1.getKey()).isEqualTo("form1");
        assertThat(retSeg1Form1.getSegmentKey()).isEqualTo("seg1");
        assertThat(retSeg1Form1.getItems()).hasSize(2);
        assertThat(retSeg1Form1.getItems()).containsExactlyInAnyOrder(seg1Form1Item1, seg1Form1Item2andForm2Item1);

        Form retSeg1Form2 = retSeg1.getForms().get(1);
        assertThat(retSeg1Form2.getKey()).isEqualTo("form2");
        assertThat(retSeg1Form2.getSegmentKey()).isEqualTo("seg1");
        assertThat(retSeg1Form2.getItems()).hasSize(2);
        assertThat(retSeg1Form2.getItems()).containsExactlyInAnyOrder(seg1Form1Item2andForm2Item1, seg1Form2Item1);

        // SEGMENT 1 ITEMS + ITEM PROPERTIES
        // Segment 1, Form 1, Item 1
        List<Item> seg1Form1Items = retSeg1Form1.getItems();
        Item retSeg1Form1Item1 = seg1Form1Items.get(0);
        assertThat(retSeg1Form1Item1.getId()).isEqualTo(seg1Form1Item1.getId());
        assertThat(retSeg1Form1Item1.getSegmentKey()).isEqualTo(seg1Form1Item1.getSegmentKey());
        assertThat(retSeg1Form1Item1.getGroupId()).isEqualTo(seg1Form1Item1.getGroupId());
        assertThat(retSeg1Form1Item1.getPosition()).isEqualTo(seg1Form1Item1.getPosition());

        List<ItemProperty> retSeg1Form1Item1Prop = retSeg1Form1Item1.getItemProperties();
        assertThat(retSeg1Form1Item1Prop).hasSize(3);
        assertThat(retSeg1Form1Item1Prop.get(0).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Form1Item1Prop.get(0).getName()).isEqualTo("name1");
        assertThat(retSeg1Form1Item1Prop.get(0).getValue()).isEqualTo("val1");
        assertThat(retSeg1Form1Item1Prop.get(0).getDescription()).isEqualTo("desc1");
        assertThat(retSeg1Form1Item1Prop.get(1).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Form1Item1Prop.get(1).getName()).isEqualTo("name2");
        assertThat(retSeg1Form1Item1Prop.get(1).getValue()).isEqualTo("val2");
        assertThat(retSeg1Form1Item1Prop.get(1).getDescription()).isEqualTo("desc2");
        assertThat(retSeg1Form1Item1Prop.get(2).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Form1Item1Prop.get(2).getName()).isEqualTo("language");
        assertThat(retSeg1Form1Item1Prop.get(2).getValue()).isEqualTo("lang1");
        assertThat(retSeg1Form1Item1Prop.get(2).getDescription()).isEqualTo("desc5");

        // Segment 1, Form 1, Item 2
        Item retSeg1Form1Item2 = seg1Form1Items.get(1);
        assertThat(retSeg1Form1Item2.getId()).isEqualTo(seg1Form1Item2andForm2Item1.getId());
        assertThat(retSeg1Form1Item2.getSegmentKey()).isEqualTo(seg1Form1Item2andForm2Item1.getSegmentKey());
        assertThat(retSeg1Form1Item2.getGroupId()).isEqualTo(seg1Form1Item2andForm2Item1.getGroupId());
        assertThat(retSeg1Form1Item2.getPosition()).isEqualTo(seg1Form1Item2andForm2Item1.getPosition());

        List<ItemProperty> retSeg1FormItem2Prop = retSeg1Form1Item2.getItemProperties();
        assertThat(retSeg1FormItem2Prop).hasSize(1);
        assertThat(retSeg1FormItem2Prop.get(0).getItemId()).isEqualTo("item2");
        assertThat(retSeg1FormItem2Prop.get(0).getName()).isEqualTo("language");
        assertThat(retSeg1FormItem2Prop.get(0).getValue()).isEqualTo("lang1");
        assertThat(retSeg1FormItem2Prop.get(0).getDescription()).isEqualTo("desc6");

        // Segment 1, Form 2, Item 1
        List<Item> retSeg1Form2Items = retSeg1Form2.getItems();
        Item retSeg1Form2Item1 = retSeg1Form2Items.get(0);
        assertThat(retSeg1Form2Item1.getId()).isEqualTo(seg1Form1Item2andForm2Item1.getId());
        assertThat(retSeg1Form2Item1.getSegmentKey()).isEqualTo(seg1Form1Item2andForm2Item1.getSegmentKey());
        assertThat(retSeg1Form2Item1.getGroupId()).isEqualTo(seg1Form1Item2andForm2Item1.getGroupId());
        assertThat(retSeg1Form2Item1.getPosition()).isEqualTo(seg1Form1Item2andForm2Item1.getPosition());

        List<ItemProperty> retSeg1Form2Item1Prop = retSeg1Form1Item2.getItemProperties();
        assertThat(retSeg1Form2Item1Prop).hasSize(1);
        assertThat(retSeg1Form2Item1Prop.get(0).getItemId()).isEqualTo("item2");
        assertThat(retSeg1Form2Item1Prop.get(0).getName()).isEqualTo("language");
        assertThat(retSeg1Form2Item1Prop.get(0).getValue()).isEqualTo("lang1");
        assertThat(retSeg1Form2Item1Prop.get(0).getDescription()).isEqualTo("desc6");

        // Segment 1, Form 2, Item 2
        Item retSeg1Form2Item2 = retSeg1Form2.getItems().get(1);
        assertThat(retSeg1Form2Item2.getId()).isEqualTo(seg1Form2Item1.getId());
        assertThat(retSeg1Form2Item2.getSegmentKey()).isEqualTo(seg1Form2Item1.getSegmentKey());
        assertThat(retSeg1Form2Item2.getGroupId()).isEqualTo(seg1Form2Item1.getGroupId());
        assertThat(retSeg1Form2Item2.getPosition()).isEqualTo(seg1Form2Item1.getPosition());

        List<ItemProperty> retSeg1Form2Item2Prop = retSeg1Form2Item2.getItemProperties();
        assertThat(retSeg1Form2Item2Prop).hasSize(2);
        assertThat(retSeg1Form2Item2Prop.get(0).getItemId()).isEqualTo("item3");
        assertThat(retSeg1Form2Item2Prop.get(0).getName()).isEqualTo("name3");
        assertThat(retSeg1Form2Item2Prop.get(0).getValue()).isEqualTo("val3");
        assertThat(retSeg1Form2Item2Prop.get(0).getDescription()).isEqualTo("desc3");
        assertThat(retSeg1Form2Item2Prop.get(1).getItemId()).isEqualTo("item3");
        assertThat(retSeg1Form2Item2Prop.get(1).getName()).isEqualTo("language");
        assertThat(retSeg1Form2Item2Prop.get(1).getValue()).isEqualTo("lang2");
        assertThat(retSeg1Form2Item2Prop.get(1).getDescription()).isEqualTo("desc7");

        // Segment 2
        Segment retSeg2 = retSegments.get(1);
        assertThat(retSeg2.getKey()).isEqualTo("seg2");

        // SEGMENT 2 ITEMS + ITEM PROPERTIES
        // Seg 2 items + item props
        List<Item> retSeg2Items = retSeg2.getItems("lang3");
        assertThat(retSeg2Items).hasSize(1);

        // Segment 2, Item 1
        Item retSeg2Item1 = retSeg2Items.get(0);
        assertThat(retSeg2Item1.getId()).isEqualTo(seg2Item1.getId());
        assertThat(retSeg2Item1.getSegmentKey()).isEqualTo(seg2Item1.getSegmentKey());
        assertThat(retSeg2Item1.getGroupId()).isEqualTo(seg2Item1.getGroupId());
        assertThat(retSeg2Item1.getPosition()).isEqualTo(seg2Item1.getPosition());

        List<ItemProperty> retSeg2Item1Prop = retSeg2Item1.getItemProperties();
        assertThat(retSeg2Item1Prop).hasSize(2);
        assertThat(retSeg2Item1Prop.get(0).getItemId()).isEqualTo("item4");
        assertThat(retSeg2Item1Prop.get(0).getName()).isEqualTo("name4");
        assertThat(retSeg2Item1Prop.get(0).getValue()).isEqualTo("val4");
        assertThat(retSeg2Item1Prop.get(0).getDescription()).isEqualTo("desc4");
        assertThat(retSeg2Item1Prop.get(1).getItemId()).isEqualTo("item4");
        assertThat(retSeg2Item1Prop.get(1).getName()).isEqualTo("language");
        assertThat(retSeg2Item1Prop.get(1).getValue()).isEqualTo("lang3");
        assertThat(retSeg2Item1Prop.get(1).getDescription()).isEqualTo("desc8");

        // SEGMENTS - GET ITEMS BY LANGUAGE
        List<Item> retSeg1Lang1Items = retSeg1.getItems("lang1");
        assertThat(retSeg1Lang1Items).hasSize(2);

        List<Item> retSeg1Lang2Items = retSeg1.getItems("lang2");
        assertThat(retSeg1Lang2Items).hasSize(2);

        // SEGMENTS - GET FORM BY LANGUAGE
        List<Form> retLang1Forms = retSeg1.getForms("lang1");
        Form retLang1Form = retLang1Forms.get(0);
        assertThat(retLang1Form.getLanguageCode()).isEqualTo("lang1");
        assertThat(retLang1Form.getKey()).isEqualTo("form1");
        assertThat(retLang1Form.getItems()).hasSize(2);
        assertThat(retLang1Form.getSegmentKey()).isEqualTo("seg1");

        List<Form> retLang2Forms = retSeg1.getForms("lang2");
        Form retLang2Form = retLang2Forms.get(0);
        assertThat(retLang2Form.getLanguageCode()).isEqualTo("lang2");
        assertThat(retLang2Form.getKey()).isEqualTo("form2");
        assertThat(retLang2Form.getItems()).hasSize(2);
        assertThat(retLang2Form.getSegmentKey()).isEqualTo("seg1");
    }
}