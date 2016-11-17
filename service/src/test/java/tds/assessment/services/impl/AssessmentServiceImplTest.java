package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.services.AssessmentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssessmentServiceImplTest {
    private AssessmentQueryRepository assessmentQueryRepository;
    private ItemQueryRepository itemQueryRepository;
    private FormQueryRepository formQueryRepository;
    private AssessmentService service;

    @Before
    public void setUp() {
        assessmentQueryRepository = mock(AssessmentQueryRepository.class);
        itemQueryRepository = mock(ItemQueryRepository.class);
        formQueryRepository = mock(FormQueryRepository.class);
        service = new AssessmentServiceImpl(assessmentQueryRepository, itemQueryRepository, formQueryRepository);
    }

    @Test
    public void shouldReturnAssessment() {
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");

        when(assessmentQueryRepository.findAssessmentByKey("theKey")).thenReturn(Optional.of(assessment));
        when(formQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        Optional<Assessment> maybeAssessment = service.findAssessmentByKey("theKey");
        verify(assessmentQueryRepository).findAssessmentByKey("theKey");
        verify(formQueryRepository).findFormsForAssessment("theKey");
        verify(itemQueryRepository).findActiveItemsProperties("theKey");
        verify(itemQueryRepository).findItemsForAssessment("theKey");
        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }

    @Test
    public void shouldReturnAssessmentWithForms() {
        final String assessmentId = "foo";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        assessment.setAssessmentId(assessmentId);

        List<Segment> segs = new ArrayList<>();
        Segment seg1 = new Segment("seg1");
        Segment seg2 = new Segment("seg2");
        segs.add(seg1);
        segs.add(seg2);
        assessment.setSegments(segs);

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

        when(assessmentQueryRepository.findAssessmentByKey("theKey")).thenReturn(Optional.of(assessment));
        when(formQueryRepository.findFormsForAssessment("theKey")).thenReturn(forms);
        when(itemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemConstraintsForAssessment(assessmentId)).thenReturn(new ArrayList<>());
        Optional<Assessment> maybeAssessment = service.findAssessmentByKey("theKey");
        verify(assessmentQueryRepository).findAssessmentByKey("theKey");
        verify(formQueryRepository).findFormsForAssessment("theKey");
        verify(itemQueryRepository).findActiveItemsProperties("theKey");
        verify(itemQueryRepository).findItemsForAssessment("theKey");
        verify(itemQueryRepository).findItemConstraintsForAssessment(assessmentId);
        assertThat(maybeAssessment).isPresent();

        List<Segment> retSegments = maybeAssessment.get().getSegments();
        assertThat(retSegments).hasSize(2);
        Segment retSeg1 = retSegments.get(0);
        assertThat(retSeg1.getKey()).isEqualTo("seg1");
        List<Form> retSeg1Forms = retSeg1.getForms();
        assertThat(retSeg1Forms).hasSize(2);

        Form retSeg1Form1 = retSeg1Forms.get(0);
        assertThat(retSeg1Form1.getKey()).isEqualTo("form1");
        assertThat(retSeg1Form1.getSegmentKey()).isEqualTo("seg1");
        Form retSeg1Form2 = retSeg1Forms.get(1);
        assertThat(retSeg1Form2.getKey()).isEqualTo("form2");
        assertThat(retSeg1Form2.getSegmentKey()).isEqualTo("seg1");
        Segment retSeg2 = retSegments.get(1);
        assertThat(retSeg2.getKey()).isEqualTo("seg2");
        List<Form> retSeg2Forms = retSeg2.getForms();
        assertThat(retSeg2Forms).hasSize(1);
        Form retSeg2Form1 = retSeg2Forms.get(0);
        assertThat(retSeg2Form1.getKey()).isEqualTo("form3");
        assertThat(retSeg2Form1.getSegmentKey()).isEqualTo("seg2");
    }

    @Test
    public void shouldReturnMultiSegAssessmentWithItemsAndItemProps() {
        final String assessmentId = "foo";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        assessment.setAssessmentId(assessmentId);

        // Segments
        List<Segment> segs = new ArrayList<>();
        Segment seg1 = new Segment("seg1");
        Segment seg2 = new Segment("seg2");
        segs.add(seg1);
        segs.add(seg2);
        assessment.setSegments(segs);

        // Items
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

        when(assessmentQueryRepository.findAssessmentByKey("theKey")).thenReturn(Optional.of(assessment));
        when(formQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(itemProperties);
        when(itemQueryRepository.findItemsForAssessment("theKey")).thenReturn(items);
        when(itemQueryRepository.findItemConstraintsForAssessment(assessmentId)).thenReturn(new ArrayList<>());
        Optional<Assessment> maybeAssessment = service.findAssessmentByKey("theKey");
        verify(assessmentQueryRepository).findAssessmentByKey("theKey");
        verify(formQueryRepository).findFormsForAssessment("theKey");
        verify(itemQueryRepository).findActiveItemsProperties("theKey");
        verify(itemQueryRepository).findItemsForAssessment("theKey");
        verify(itemQueryRepository).findItemConstraintsForAssessment(assessmentId);
        assertThat(maybeAssessment).isPresent();

        List<Segment> retSegments = maybeAssessment.get().getSegments();
        assertThat(retSegments).hasSize(2);
        Segment retSeg1 = retSegments.get(0);
        assertThat(retSeg1.getKey()).isEqualTo("seg1");
        List<Item> retSeg1Items = retSeg1.getItems();
        assertThat(retSeg1Items).hasSize(2);

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

        Segment retSeg2 = retSegments.get(1);
        assertThat(retSeg2.getKey()).isEqualTo("seg2");
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

    @Test
    public void shouldReturnAssessmentWithItemProps() {
        final String assessmentId = "foo";
        Assessment assessment = new Assessment();
        assessment.setKey("theKey");
        assessment.setAssessmentId(assessmentId);

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

        when(assessmentQueryRepository.findAssessmentByKey("theKey")).thenReturn(Optional.of(assessment));
        when(formQueryRepository.findFormsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findActiveItemsProperties("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemsForAssessment("theKey")).thenReturn(new ArrayList<>());
        when(itemQueryRepository.findItemConstraintsForAssessment(assessmentId)).thenReturn(itemConstraints);
        Optional<Assessment> maybeAssessment = service.findAssessmentByKey("theKey");
        verify(assessmentQueryRepository).findAssessmentByKey("theKey");
        verify(formQueryRepository).findFormsForAssessment("theKey");
        verify(itemQueryRepository).findActiveItemsProperties("theKey");
        verify(itemQueryRepository).findItemsForAssessment("theKey");
        verify(itemQueryRepository).findItemConstraintsForAssessment("foo");
        assertThat(maybeAssessment).isPresent();
        List<ItemConstraint> retConstraints = maybeAssessment.get().getItemConstraints();
        assertThat(retConstraints).hasSize(2);
        ItemConstraint retCon1 = retConstraints.get(0);
        ItemConstraint retCon2 = retConstraints.get(1);
        assertThat(retCon1.isInclusive()).isTrue();
        assertThat(retCon1.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon1.getToolType()).isEqualTo(constraint1.getToolType());
        assertThat(retCon1.getToolValue()).isEqualTo(constraint1.getToolValue());
        assertThat(retCon1.getPropertyName()).isEqualTo(constraint1.getPropertyName());
        assertThat(retCon1.getPropertyValue()).isEqualTo(constraint1.getPropertyValue());
        assertThat(retCon2.isInclusive()).isFalse();
        assertThat(retCon2.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(retCon2.getToolType()).isEqualTo(constraint2.getToolType());
        assertThat(retCon2.getToolValue()).isEqualTo(constraint2.getToolValue());
        assertThat(retCon2.getPropertyName()).isEqualTo(constraint2.getPropertyName());
        assertThat(retCon2.getPropertyValue()).isEqualTo(constraint2.getPropertyValue());
    }
}
