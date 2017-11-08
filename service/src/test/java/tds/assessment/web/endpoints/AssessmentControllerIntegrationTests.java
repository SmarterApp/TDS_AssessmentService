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

package tds.assessment.web.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.services.AssessmentService;
import tds.common.Algorithm;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;
import tds.common.web.exceptions.NotFoundException;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AssessmentController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class})
public class AssessmentControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssessmentService assessmentService;

    @Test
    public void shouldReturnAssessmentByKey() throws Exception {
        EnhancedRandom rand = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .collectionSizeRange(2, 5)
            .stringLengthRange(1, 20)
            .build();
        Assessment assessment = rand.nextObject(Assessment.class);
        assessment.setKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assessment.setAssessmentId("IRP-Perf-ELA-11");
        assessment.setSelectionAlgorithm(Algorithm.VIRTUAL);
        assessment.setSubject("ELA");
        assessment.setStartAbility(50F);
        assessment.setDeleteUnansweredItems(true);

        when(assessmentService.findAssessment("SBAC_PT", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.of(assessment));

        URI uri = UriComponentsBuilder.fromUriString("/SBAC_PT/assessments/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016").build().toUri();

        MvcResult result = http.perform(get(uri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("key", is("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")))
            .andExpect(jsonPath("assessmentId", is("IRP-Perf-ELA-11")))
            .andExpect(jsonPath("subject", is("ELA")))
            .andExpect(jsonPath("selectionAlgorithm", is(Algorithm.VIRTUAL.name())))
            .andReturn();

        Assessment parsedAssessment = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Assessment.class);
        assertThat(parsedAssessment).isEqualTo(assessment);
    }

    @Test
    public void shouldReturnSegmentedFixedFormAssessmentByKey() throws Exception {
        List<Segment> segments = new ArrayList<>();
//        Segment seg1 = new Segment("(SBAC_PT)SBAC-SEG1-MATH-11-Summer-2015-2016");
        // TODO: fix test
        Segment seg1 = new Segment("(SBAC_PT)SBAC-SEG1-MATH-11-Summer-2015-2016", Algorithm.FIXED_FORM);
        seg1.setSegmentId("SBAC-SEG1-MATH-11");
        seg1.setStartAbility(0);
        seg1.setAssessmentKey("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016");
        seg1.setSubject("ELA");

//        Segment seg2 = new Segment("(SBAC_PT)SBAC-SEG2-MATH-11-Summer-2015-2016");
        // TODO: fix test
        Segment seg2 = new Segment("(SBAC_PT)SBAC-SEG2-MATH-11-Summer-2015-2016", Algorithm.FIXED_FORM);
        seg2.setSegmentId("SBAC-SEG2-MATH-11");
        seg2.setStartAbility(0);
        seg2.setAssessmentKey("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016");
        seg2.setSubject("ELA");

        segments.add(seg1);
        segments.add(seg2);

        Assessment assessment = new Assessment();
        assessment.setKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assessment.setAssessmentId("IRP-Perf-ELA-11");
        assessment.setSegments(segments);
        assessment.setSelectionAlgorithm(Algorithm.VIRTUAL);
        assessment.setSubject("ELA");
        assessment.setStartAbility(50F);

        when(assessmentService.findAssessment("SBAC_PT", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.of(assessment));

        http.perform(get(new URI("/SBAC_PT/assessments/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("key", is("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")))
            .andExpect(jsonPath("assessmentId", is("IRP-Perf-ELA-11")))
            .andExpect(jsonPath("subject", is("ELA")))
            .andExpect(jsonPath("selectionAlgorithm", is(Algorithm.VIRTUAL.name())))
            .andExpect(jsonPath("segments[0].key", is("(SBAC_PT)SBAC-SEG1-MATH-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[0].segmentId", is("SBAC-SEG1-MATH-11")))
            .andExpect(jsonPath("segments[0].selectionAlgorithm", is(Algorithm.FIXED_FORM.name())))
            .andExpect(jsonPath("segments[0].startAbility", is(0.0)))
            .andExpect(jsonPath("segments[0].assessmentKey", is("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[0].subject", is("ELA")))
            .andExpect(jsonPath("segments[1].key", is("(SBAC_PT)SBAC-SEG2-MATH-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[1].segmentId", is("SBAC-SEG2-MATH-11")))
            .andExpect(jsonPath("segments[1].selectionAlgorithm", is(Algorithm.FIXED_FORM.name())))
            .andExpect(jsonPath("segments[1].startAbility", is(0.0)))
            .andExpect(jsonPath("segments[1].assessmentKey", is("(SBAC_PT)SBAC-Mathematics-11-Summer-2015-2016")))
            .andExpect(jsonPath("segments[1].subject", is("ELA")));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        when(assessmentService.findAssessment("SBAC_PT", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016")).thenReturn(Optional.empty());
        http.perform(get(new URI("/SBAC_PT/assessments/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn204WhenRemoveSuccessful() throws Exception {
        http.perform(delete(new URI("/SBAC_PT/assessments/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        verify(assessmentService).removeAssessment("SBAC_PT", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
    }

    @Test
    public void shouldReturn404WhenAssessmentNotFound() throws Exception {
        doThrow(NotFoundException.class).when(assessmentService).removeAssessment("SBAC_PT", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        http.perform(delete(new URI("/SBAC_PT/assessments/(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
        verify(assessmentService).removeAssessment("SBAC_PT", "(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
    }
}
