package tds.assessment.web.endpoints;

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

import java.util.Collections;
import java.util.Optional;

import tds.assessment.ContentLevelSpecification;
import tds.assessment.Item;
import tds.assessment.ItemControlParameter;
import tds.assessment.ItemGroup;
import tds.assessment.ItemMeasurement;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.SegmentItemInformation;
import tds.assessment.services.SegmentService;
import tds.common.Algorithm;
import tds.common.configuration.JacksonObjectMapperConfiguration;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SegmentController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class, JacksonObjectMapperConfiguration.class})
public class SegmentControllerIntegrationTests {
    private static String SEGMENT_KEY = "(SBAC_PT)SBAC-IRP-Perf-S2-ELA-3-Summer-2015-2016";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc http;

    @MockBean
    private SegmentService segmentService;

    @Test
    public void shouldReturnSegmentInformation() throws Exception {
        Segment segment = new Segment("segmentKey", Algorithm.ADAPTIVE_2);

        ContentLevelSpecification spec = new ContentLevelSpecification.Builder()
            .withContentLevel("contentLevel")
            .withElementType(1)
            .withReportingCategory(true)
            .withAbilityWeight(1.2f)
            .withAdaptiveCut(1.3f)
            .withBpWeight(1.4f)
            .withMaxItems(1)
            .withMinItems(2)
            .withPrecisionTarget(2.1f)
            .withPrecisionTargetMetWeight(2.2f)
            .withPrecisionTargetNotMetWeight(2.3f)
            .withScalar(3.1f)
            .withStartAbility(3.2f)
            .withStartInfo(3.3f)
            .withStrictMax(false)
            .build();
        ItemControlParameter itemControlParameter = new ItemControlParameter("bpId", "name", "value");
        ItemGroup itemGroup = new ItemGroup("group", 1, 10, 1f);
        ItemMeasurement itemMeasurement = new ItemMeasurement.Builder()
            .withParameterDescription("parmDesc")
            .withParameterValue(1.2f)
            .withParameterName("name")
            .withItemResponseTheoryModel("theory")
            .withItemKey("itemKey")
            .withSegmentPosition(1)
            .withDimension("dimension")
            .build();

        Item parentItem = new Item("parent");
        Item segmentItem = new Item("segment");

        ItemProperty relevantProperty = new ItemProperty("propType", "propValue");

        SegmentItemInformation info = new SegmentItemInformation.Builder()
            .withContentLevelSpecifications(Collections.singletonList(spec))
            .withControlParameters(Collections.singletonList(itemControlParameter))
            .withItemGroups(Collections.singletonList(itemGroup))
            .withItemMeasurements(Collections.singletonList(itemMeasurement))
            .withParentItems(Collections.singletonList(parentItem))
            .withSegmentItems(Collections.singletonList(segmentItem))
            .withPoolFilterProperties(Collections.singletonList(relevantProperty))
            .withSegment(segment)
            .build();

        when(segmentService.findSegmentItemInformation(SEGMENT_KEY)).thenReturn(Optional.of(info));

        String url = "/segment-items/" + SEGMENT_KEY;

        MvcResult result = http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        SegmentItemInformation parsedResponse = objectMapper.readValue(result.getResponse().getContentAsByteArray(), SegmentItemInformation.class);

        assertThat(parsedResponse.getPoolFilterProperties()).containsExactly(relevantProperty);
        assertThat(parsedResponse.getItemGroups()).containsExactly(itemGroup);
        assertThat(parsedResponse.getSegmentItems()).containsExactly(segmentItem);
        assertThat(parsedResponse.getParentItems()).containsExactly(parentItem);
        assertThat(parsedResponse.getItemMeasurements()).containsExactly(itemMeasurement);
        assertThat(parsedResponse.getContentLevelSpecifications()).containsExactly(spec);
        assertThat(parsedResponse.getSegment().getKey()).isEqualTo(segment.getKey());
        assertThat(parsedResponse.getSegment()).isEqualTo(segment);
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        when(segmentService.findSegmentItemInformation(SEGMENT_KEY)).thenReturn(Optional.empty());

        String url = "/segment-items/" + SEGMENT_KEY;

        http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
