package tds.assessment.web.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.List;

import tds.assessment.AssessmentInfo;
import tds.assessment.services.AssessmentInfoService;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AssessmentInfoController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class})
public class AssessmentInfoControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssessmentInfoService assessmentInfoService;

    @Test
    public void shouldReturnAssessmentInfo() throws Exception {
        final String clientName = "SBAC_PT";
        AssessmentInfo info1 = random(AssessmentInfo.class);
        AssessmentInfo info2 = random(AssessmentInfo.class);
        when(assessmentInfoService.findAssessmentInfo(clientName, info1.getKey(), info2.getKey())).thenReturn(Arrays.asList(info1, info2));

        MvcResult result = http.perform(get(UriComponentsBuilder.fromUriString("/SBAC_PT/assessments/").build().toUri())
            .param("assessmentKeys", info1.getKey())
            .param("assessmentKeys", info2.getKey())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].key", is(info1.getKey())))
            .andExpect(jsonPath("[0].id", is(info1.getId())))
            .andExpect(jsonPath("[0].subject", is(info1.getSubject())))
            .andExpect(jsonPath("[0].grades", is(info1.getGrades())))
            .andExpect(jsonPath("[0].label", is(info1.getLabel())))
            .andExpect(jsonPath("[0].languages", is(info1.getLanguages())))
            .andExpect(jsonPath("[1].key", is(info2.getKey())))
            .andExpect(jsonPath("[1].id", is(info2.getId())))
            .andExpect(jsonPath("[1].subject", is(info2.getSubject())))
            .andExpect(jsonPath("[1].grades", is(info2.getGrades())))
            .andExpect(jsonPath("[1].label", is(info2.getLabel())))
            .andExpect(jsonPath("[1].languages", is(info2.getLanguages())))
            .andReturn();

        List<AssessmentInfo> parsedAssessments = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<AssessmentInfo>>() {});
        assertThat(parsedAssessments.get(0)).isEqualTo(info1);
        assertThat(parsedAssessments.get(1)).isEqualTo(info2);
    }
}
