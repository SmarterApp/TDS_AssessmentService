package tds.assessment;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import tds.common.Algorithm;

import static org.assertj.core.api.Assertions.assertThat;

public class SegmentTest {
    @Test
    public void shouldGetAnItemWithMultipleLanguagesFromAdaptiveSegment() {
        final ItemProperty englishLanguageItemProperty = new ItemProperty("Language",
            "ENU",
            "English language",
            "187-1234");
        final ItemProperty brailleLanguageItemProperty = new ItemProperty("Language",
            "ENU-Braille",
            "Braille language",
            "187-1234");
        final List<ItemProperty> itemProperties = new ArrayList<>();
        itemProperties.add(englishLanguageItemProperty);
        itemProperties.add(brailleLanguageItemProperty);

        final Item itemWithMultipleLanguages = new Item("187-1234");
        itemWithMultipleLanguages.setItemProperties(itemProperties);

        final List<Item> items = new ArrayList<>();
        items.add(itemWithMultipleLanguages);

        final Segment segment = new Segment("segment-key", Algorithm.ADAPTIVE_2);

        segment.setItems(items);

        List<Item> result = segment.getItems("ENU-Braille");
        assertThat(result).hasSize(1);
    }
}