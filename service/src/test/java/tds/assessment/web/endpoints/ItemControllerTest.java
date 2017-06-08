package tds.assessment.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import tds.assessment.ItemFileMetadata;
import tds.assessment.ItemFileType;
import tds.assessment.services.ItemService;
import tds.common.web.exceptions.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    @Mock
    private ItemService mockItemService;

    private ItemController itemController;

    @Before
    public void setUp() {
        itemController = new ItemController(mockItemService);
    }

    @After
    public void tearDown() {
    }

    @Test (expected = IllegalArgumentException.class)
    public void itShouldThrowIfStimulusOrItemKeyNotPresent() {
        itemController.findItemFileMetadata("SBAC_PT", 1, null, null);
    }

    @Test (expected = NotFoundException.class)
    public void itShouldThrowNotFoundIfItemFileMetadataCannotBeFound() {
        when(mockItemService.findItemFileMetadataByStimulusKey("SBAC_PT", 1, 2)).thenReturn(Optional.empty());
        itemController.findItemFileMetadata("SBAC_PT", 1, 2L, null);
    }

    @Test
    public void itShouldReturnItemMetadataForStimulus() {
        ItemFileMetadata itemFileMetadata = ItemFileMetadata.create(ItemFileType.STIMULUS, "test", "test", "test");
        when(mockItemService.findItemFileMetadataByStimulusKey("SBAC_PT", 1, 2)).thenReturn(Optional.of(itemFileMetadata));
        ResponseEntity<ItemFileMetadata> response = itemController.findItemFileMetadata("SBAC_PT", 1, 2L, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(itemFileMetadata);
    }

    @Test
    public void itShouldReturnItemMetadataForItem() {
        ItemFileMetadata itemFileMetadata = ItemFileMetadata.create(ItemFileType.ITEM, "test", "test", "test");
        when(mockItemService.findItemFileMetadataByItemKey("SBAC_PT", 1, 4)).thenReturn(Optional.of(itemFileMetadata));
        ResponseEntity<ItemFileMetadata> response = itemController.findItemFileMetadata("SBAC_PT", 1, null, 4L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(itemFileMetadata);
    }
}