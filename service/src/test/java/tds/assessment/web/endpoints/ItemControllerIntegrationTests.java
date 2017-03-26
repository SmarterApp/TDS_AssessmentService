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

import java.util.Optional;

import tds.assessment.ItemFileMetadata;
import tds.assessment.ItemFileType;
import tds.assessment.services.ItemService;
import tds.common.configuration.JacksonObjectMapperConfiguration;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class, JacksonObjectMapperConfiguration.class})
public class ItemControllerIntegrationTests {
    @MockBean
    private ItemService mockItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc http;

    @Test
    public void shouldReturnStimulusItemMetadata() throws Exception {
        String url = "/assessments/item/metadata?clientName=SBAC_PT&bankKey=1&stimulusKey=3";

        ItemFileMetadata itemFileMetadata = ItemFileMetadata.create(ItemFileType.STIMULUS, "1-3", "stimulusFile", "stimulusFile/");
        when(mockItemService.findItemFileMetadataByStimulusKey("SBAC_PT", 1, 3)).thenReturn(Optional.of(itemFileMetadata));

        MvcResult result = http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        ItemFileMetadata parsedResponse = objectMapper.readValue(result.getResponse().getContentAsByteArray(), ItemFileMetadata.class);
        assertThat(parsedResponse.getId()).isEqualTo("1-3");
        assertThat(parsedResponse.getFileName()).isEqualTo("stimulusFile");
        assertThat(parsedResponse.getFilePath()).isEqualTo("stimulusFile/");
        assertThat(parsedResponse.getItemType()).isEqualTo(ItemFileType.STIMULUS);
    }

    @Test
    public void shouldReturnItemMetadata() throws Exception {
        String url = "/assessments/item/metadata?clientName=SBAC_PT&bankKey=5&itemKey=3";

        ItemFileMetadata itemFileMetadata = ItemFileMetadata.create(ItemFileType.ITEM, "5-3", "itemFile", "itemFile/");
        when(mockItemService.findItemFileMetadataByItemKey("SBAC_PT", 5, 3)).thenReturn(Optional.of(itemFileMetadata));

        MvcResult result = http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        ItemFileMetadata parsedResponse = objectMapper.readValue(result.getResponse().getContentAsByteArray(), ItemFileMetadata.class);
        assertThat(parsedResponse.getId()).isEqualTo("5-3");
        assertThat(parsedResponse.getFileName()).isEqualTo("itemFile");
        assertThat(parsedResponse.getFilePath()).isEqualTo("itemFile/");
        assertThat(parsedResponse.getItemType()).isEqualTo(ItemFileType.ITEM);
    }

    @Test
    public void shouldReturnBadRequestWhenItemKeyAndStimulusKeyNull() throws Exception {
        String url = "/assessments/item/metadata?clientName=SBAC_PT&bankKey=5";

        http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundWhenItemMetadataNotFound() throws Exception {
        String url = "/assessments/item/metadata?clientName=SBAC_PT&bankKey=5&itemKey=3";

        when(mockItemService.findItemFileMetadataByItemKey("SBAC_PT", 5, 3)).thenReturn(Optional.empty());

        http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
