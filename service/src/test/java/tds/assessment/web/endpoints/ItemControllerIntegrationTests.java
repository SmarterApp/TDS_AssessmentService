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

import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.services.ItemService;
import tds.common.web.advice.ExceptionAdvice;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
@Import(ExceptionAdvice.class)
public class ItemControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private ItemService itemService;

    @Test
    public void shouldReturn404ForEmptyConstraints() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "assesssment-1";

        when(itemService.findItemConstraints(clientName, assessmentId)).thenReturn(
                new ArrayList<>());

        URI uri = UriComponentsBuilder.fromUriString(String.format("/assessments/items/constraints/{}/{}", clientName, assessmentId))
                .build().toUri();

        http.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturnItemConstraints() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "assesssment-1";

        List<ItemConstraint> constraints = new ArrayList<>();
        constraints.add(
            new ItemConstraint.Builder()
                .withAssessmentId(assessmentId)
                .withInclusive(true)
                .withPropertyName("Language")
                .withPropertyValue("ENU")
                .withToolType("Language")
                .withToolValue("ENU")
                .build()
        );
        constraints.add(
                new ItemConstraint.Builder()
                        .withAssessmentId(assessmentId)
                        .withInclusive(false)
                        .withPropertyName("--ITEMTYPE--")
                        .withPropertyValue("ER")
                        .withToolType("TOOLTYPE")
                        .withToolValue("ERRRRR")
                        .build()
        );

        when(itemService.findItemConstraints(clientName, assessmentId)).thenReturn(
                constraints);

        URI uri = UriComponentsBuilder.fromUriString(String.format("/assessments/items/constraints/%s/%s", clientName, assessmentId))
                .build().toUri();

        http.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].assessmentId", is(assessmentId)))
                .andExpect(jsonPath("[0].inclusive", is(true)))
                .andExpect(jsonPath("[0].propertyName", is("Language")))
                .andExpect(jsonPath("[0].propertyValue", is("ENU")))
                .andExpect(jsonPath("[0].toolType", is("Language")))
                .andExpect(jsonPath("[0].toolValue", is("ENU")))
                .andExpect(jsonPath("[1].assessmentId", is(assessmentId)))
                .andExpect(jsonPath("[1].inclusive", is(false)))
                .andExpect(jsonPath("[1].propertyName", is("--ITEMTYPE--")))
                .andExpect(jsonPath("[1].propertyValue", is("ER")))
                .andExpect(jsonPath("[1].toolType", is("TOOLTYPE")))
                .andExpect(jsonPath("[1].toolValue", is("ERRRRR")));

    }

    @Test
    public void shouldReturn404ForEmptyProperties() throws Exception {
        final String assessmentKey = "assesssment-key-1";

        when(itemService.findItemProperties(assessmentKey)).thenReturn(
                new ArrayList<>());

        URI uri = UriComponentsBuilder.fromUriString(String.format("/assessments/items/properties/{}", assessmentKey))
                .build().toUri();

        http.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturnItemProperties() throws Exception {
        final String segmentKey = "segment-key-1";

        List<ItemProperty> properties = new ArrayList<>();
        properties.add(
                new ItemProperty("Language", "ENU", "English", "187-123")
        );
        properties.add(
                new ItemProperty("--ITEMTYPE--", "SA", "Short Answer", "187-123")
        );
        properties.add(
                new ItemProperty("Language", "ESN", "Spanish", "187-234")
        );

        when(itemService.findItemProperties(segmentKey)).thenReturn(
                properties);

        URI uri = UriComponentsBuilder.fromUriString(String.format("/assessments/items/properties/%s", segmentKey))
                .build().toUri();

        http.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].itemId", is("187-123")))
                .andExpect(jsonPath("[0].name", is("Language")))
                .andExpect(jsonPath("[0].value", is("ENU")))
                .andExpect(jsonPath("[0].description", is("English")))
                .andExpect(jsonPath("[1].itemId", is("187-123")))
                .andExpect(jsonPath("[1].name", is("--ITEMTYPE--")))
                .andExpect(jsonPath("[1].value", is("SA")))
                .andExpect(jsonPath("[1].description", is("Short Answer")))
                .andExpect(jsonPath("[2].itemId", is("187-234")))
                .andExpect(jsonPath("[2].name", is("Language")))
                .andExpect(jsonPath("[2].value", is("ESN")))
                .andExpect(jsonPath("[2].description", is("Spanish")));

    }
}
