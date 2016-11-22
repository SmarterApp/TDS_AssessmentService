package tds.assessment.services.impl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.Strand;

import static org.assertj.core.api.Assertions.assertThat;

public class AssessmentAssemblerTest {

    @Test
    public void shouldAssembleAssessment() {
        // Assessment Init
        final String assessmentId = "foo";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        assessment.setAssessmentId(assessmentId);

        // Segments init
        List<Segment> segs = new ArrayList<>();
        Segment seg1 = new Segment("seg1");
        Segment seg2 = new Segment("seg2");
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
                .build();
        Form seg1form2 = new Form.Builder("form2")
                .withSegmentKey("seg1")
                .build();
        Form seg2form1 = new Form.Builder("form3")
                .withSegmentKey("seg2")
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
        ItemProperty prop2 = new ItemProperty("name2", "val2", "desc2", "item1");
        ItemProperty prop3 = new ItemProperty("name3", "val3", "desc3", "item3");
        itemProperties.add(prop1);
        itemProperties.add(prop2);
        itemProperties.add(prop3);

        // Call assemble
        AssessmentAssembler.assemble(assessment, items, itemProperties, forms, itemConstraints, allStrands);

        List<Segment> retSegments = assessment.getSegments();
        assertThat(retSegments).hasSize(2);

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
        List<Item> retSeg1Items = retSeg1.getItems();
        assertThat(retSeg1Items).hasSize(2);

        // Seg 1 Items + item props
        Item retSeg1Item1 = retSeg1Items.get(0);
        assertThat(retSeg1Item1.getId()).isEqualTo(seg1Item1.getId());
        assertThat(retSeg1Item1.getSegmentKey()).isEqualTo(seg1Item1.getSegmentKey());
        assertThat(retSeg1Item1.getGroupId()).isEqualTo(seg1Item1.getGroupId());
        assertThat(retSeg1Item1.getPosition()).isEqualTo(seg1Item1.getPosition());

        List<ItemProperty> retSeg1Item1Prop = retSeg1Item1.getItemProperties();
        assertThat(retSeg1Item1Prop).hasSize(2);
        assertThat(retSeg1Item1Prop.get(0).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Item1Prop.get(0).getName()).isEqualTo("name1");
        assertThat(retSeg1Item1Prop.get(0).getValue()).isEqualTo("val1");
        assertThat(retSeg1Item1Prop.get(0).getDescription()).isEqualTo("desc1");
        assertThat(retSeg1Item1Prop.get(1).getItemId()).isEqualTo("item1");
        assertThat(retSeg1Item1Prop.get(1).getName()).isEqualTo("name2");
        assertThat(retSeg1Item1Prop.get(1).getValue()).isEqualTo("val2");
        assertThat(retSeg1Item1Prop.get(1).getDescription()).isEqualTo("desc2");

        Item retSeg1Item2 = retSeg1Items.get(1);
        assertThat(retSeg1Item2.getId()).isEqualTo(seg1Item2.getId());
        assertThat(retSeg1Item2.getSegmentKey()).isEqualTo(seg1Item2.getSegmentKey());
        assertThat(retSeg1Item2.getGroupId()).isEqualTo(seg1Item2.getGroupId());
        assertThat(retSeg1Item2.getPosition()).isEqualTo(seg1Item2.getPosition());

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

        // Seg 1 forms
        List<Form> retSeg1Forms = retSeg1.getForms();
        assertThat(retSeg1Forms).hasSize(2);

        Form retSeg1Form1 = retSeg1Forms.get(0);
        assertThat(retSeg1Form1.getKey()).isEqualTo("form1");
        assertThat(retSeg1Form1.getSegmentKey()).isEqualTo("seg1");
        Form retSeg1Form2 = retSeg1Forms.get(1);
        assertThat(retSeg1Form2.getKey()).isEqualTo("form2");
        assertThat(retSeg1Form2.getSegmentKey()).isEqualTo("seg1");

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

        // Seg 2 forms
        List<Form> retSeg2Forms = retSeg2.getForms();
        assertThat(retSeg2Forms).hasSize(1);
        Form retSeg2Form1 = retSeg2Forms.get(0);
        assertThat(retSeg2Form1.getKey()).isEqualTo("form3");
        assertThat(retSeg2Form1.getSegmentKey()).isEqualTo("seg2");

        // Seg 2 items + item props
        List<Item> retSeg2Items = retSeg2.getItems();
        assertThat(retSeg2Items).hasSize(1);

        Item retSeg2Item1 = retSeg2Items.get(0);
        assertThat(retSeg2Item1.getId()).isEqualTo(seg2Item1.getId());
        assertThat(retSeg2Item1.getSegmentKey()).isEqualTo(seg2Item1.getSegmentKey());
        assertThat(retSeg2Item1.getGroupId()).isEqualTo(seg2Item1.getGroupId());
        assertThat(retSeg2Item1.getPosition()).isEqualTo(seg2Item1.getPosition());

        List<ItemProperty> retSeg2Item1Prop = retSeg2Item1.getItemProperties();
        assertThat(retSeg2Item1Prop).hasSize(1);
        assertThat(retSeg2Item1Prop.get(0).getItemId()).isEqualTo("item3");
        assertThat(retSeg2Item1Prop.get(0).getName()).isEqualTo("name3");
        assertThat(retSeg2Item1Prop.get(0).getValue()).isEqualTo("val3");
        assertThat(retSeg2Item1Prop.get(0).getDescription()).isEqualTo("desc3");
    }
}
