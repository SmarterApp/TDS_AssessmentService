package tds.assessment.web.endpoints;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.services.AssessmentService;
import tds.common.web.advice.ExceptionAdvice;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AssessmentController.class)
@Import(ExceptionAdvice.class)
public class AssessmentControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private AssessmentService assessmentSegmentService;

    @Test
    public void shouldReturnAssessmentByKey() throws Exception {
        Assessment assessment = new Assessment.Builder()
            .withKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")
            .withAssessmentId("IRP-Perf-ELA-11")
            .withSelectionAlgorithm("virtual")
            .withSubject("ELA")
            .withStartAbility(50F)
            .build();

        when(assessmentSegmentService.findAssessmentByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.of(assessment));

        URI uri = UriComponentsBuilder.fromUriString("/assessments/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016").build().toUri();

        http.perform(get(uri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("key", is("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")))
            .andExpect(jsonPath("assessmentId", is("IRP-Perf-ELA-11")))
            .andExpect(jsonPath("subject", is("ELA")))
            .andExpect(jsonPath("selectionAlgorithm", is("virtual")));
    }

    @Test
    public void shouldReturnSegmentedAssessmentByKey() throws Exception {
        List<Segment> segments = new ArrayList<>();
        Segment seg1 = new Segment.Builder("(SBAC_PT)SBAC-SEG1-MATH-11-Summer-2015-2016")
            .withSegmentId("SBAC-SEG1-MATH-11")
            .withSelectionAlgorithm("fixedform")
            .withStartAbility(0)
            .withAssessmentKey("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016")
            .withSubject("ELA")
            .build();

        Segment seg2 = new Segment.Builder("(SBAC_PT)SBAC-SEG2-MATH-11-Summer-2015-2016")
            .withSegmentId("SBAC-SEG2-MATH-11")
            .withSelectionAlgorithm("fixedform")
            .withStartAbility(0)
            .withAssessmentKey("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016")
            .withSubject("ELA")
            .build();

        segments.add(seg1);
        segments.add(seg2);

//        Assessment assessment = new Assessment("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016", "IRP-Perf-ELA-11", segments, "virtual", 50F, "ELA");
        Assessment assessment = new Assessment.Builder()
            .withKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")
            .withAssessmentId("IRP-Perf-ELA-11")
            .withSegments(segments)
            .withSelectionAlgorithm("virtual")
            .withSubject("ELA")
            .withStartAbility(50F)
            .build();

        when(assessmentSegmentService.findAssessmentByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.of(assessment));

        http.perform(get(new URI("/assessments/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("key", is("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")))
            .andExpect(jsonPath("assessmentId", is("IRP-Perf-ELA-11")))
            .andExpect(jsonPath("subject", is("ELA")))
            .andExpect(jsonPath("selectionAlgorithm", is("virtual")))
            .andExpect(jsonPath("segments[0].key", is("(SBAC_PT)SBAC-SEG1-MATH-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[0].segmentId", is("SBAC-SEG1-MATH-11")))
            .andExpect(jsonPath("segments[0].selectionAlgorithm", is("fixedform")))
            .andExpect(jsonPath("segments[0].startAbility", is(0.0)))
            .andExpect(jsonPath("segments[0].assessmentKey", is("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[0].subject", is("ELA")))
            .andExpect(jsonPath("segments[1].key", is("(SBAC_PT)SBAC-SEG2-MATH-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[1].segmentId", is("SBAC-SEG2-MATH-11")))
            .andExpect(jsonPath("segments[1].selectionAlgorithm", is("fixedform")))
            .andExpect(jsonPath("segments[1].startAbility", is(0.0)))
            .andExpect(jsonPath("segments[1].assessmentKey", is("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[1].subject", is("ELA")));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        when(assessmentSegmentService.findAssessmentByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.empty());
        http.perform(get(new URI("/assessments/assessment/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
