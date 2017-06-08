package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import tds.assessment.ItemFileMetadata;
import tds.assessment.ItemFileType;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.services.ItemService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {
    @Mock
    private ItemQueryRepository mockItemQueryRepository;

    private ItemService itemService;

    @Before
    public void setUp() {
        itemService = new ItemServiceImpl(mockItemQueryRepository);
    }

    @Test
    public void shouldFindItem() {
        ItemFileMetadata itemFileMetadata = ItemFileMetadata.create(ItemFileType.ITEM, "id", "fileName", "filePath");

        when(mockItemQueryRepository.findItemFileMetadataByItemKey("SBAC_PT", 1, 2)).thenReturn(Optional.of(itemFileMetadata));

        assertThat(itemService.findItemFileMetadataByItemKey("SBAC_PT", 1, 2).get()).isEqualTo(itemFileMetadata);
    }

    @Test
    public void shouldFindStimulus() {
        ItemFileMetadata itemFileMetadata = ItemFileMetadata.create(ItemFileType.STIMULUS, "id", "fileName", "filePath");

        when(mockItemQueryRepository.findItemFileMetadataByStimulusKey("SBAC_PT", 1, 2)).thenReturn(Optional.of(itemFileMetadata));

        assertThat(itemService.findItemFileMetadataByStimulusKey("SBAC_PT", 1, 2).get()).isEqualTo(itemFileMetadata);
    }
}