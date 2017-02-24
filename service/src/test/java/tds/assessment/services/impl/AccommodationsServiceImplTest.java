package tds.assessment.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tds.accommodation.Accommodation;
import tds.assessment.Assessment;
import tds.assessment.Item;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.assessment.services.AccommodationsService;
import tds.assessment.services.AssessmentService;
import tds.common.Algorithm;
import tds.common.web.exceptions.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccommodationsServiceImplTest {
    @Mock
    private AssessmentService assessmentService;

    @Mock
    private AccommodationsQueryRepository accommodationsQueryRepository;

    private AccommodationsService accommodationsService;

    @Captor
    private ArgumentCaptor<Set<String>> languageCaptor;

    @Before
    public void setUp() {
        accommodationsService = new AccommodationServiceImpl(assessmentService, accommodationsQueryRepository);
    }

    @After
    public void tearDown() {
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenAssessmentCannotBeFound() {
        when(assessmentService.findAssessment("clientName", "id")).thenReturn(Optional.empty());
        accommodationsService.findAccommodationsByAssessmentKey("clientName", "id");
    }

    @Test
    public void shouldFindSegmentedAccommodations() {
        Segment segmentOne = new Segment("key1", Algorithm.ADAPTIVE_2);
        Segment segmentTwo = new Segment("key2", Algorithm.ADAPTIVE_2);
        Set<String> languages = new HashSet<>(Arrays.asList("ENU"));
        
        Assessment assessment = new Assessment();
        assessment.setKey("key");
        assessment.setLanguageCodes(languages);
        assessment.setSegments(Arrays.asList(segmentOne, segmentTwo));

        Accommodation accommodation = new Accommodation.Builder().build();

        when(assessmentService.findAssessment("clientName", "key")).thenReturn(Optional.of(assessment));
        when(accommodationsQueryRepository.findAssessmentAccommodationsByKey("key", languages))
            .thenReturn(Collections.singletonList(accommodation));

        List<Accommodation> accommodationList = accommodationsService.findAccommodationsByAssessmentKey("clientName", "key");

        assertThat(accommodationList).containsExactly(accommodation);
    }

    @Test
    public void shouldFindNonSegmentedAccommodations() {
        List<ItemProperty> languages1 = Arrays.asList(
            new ItemProperty("Language", "ENU"),
            new ItemProperty("Language", "Braille")
        );
        Item item1 = new Item("item1");
        item1.setItemProperties(languages1);

        List<ItemProperty> languages2 = Arrays.asList(
            new ItemProperty("Language", "FRN"),
            new ItemProperty("Some other", "prop")
        );
        Item item2 = new Item("item2");
        item2.setItemProperties(languages2);

        Segment segmentOne = new Segment("key1", Algorithm.ADAPTIVE_2);
        segmentOne.setItems(Arrays.asList(item1, item2));

        Assessment assessment = new Assessment();
        assessment.setKey("key");
        assessment.setLanguageCodes(new HashSet<>(Arrays.asList("ENU", "Braille", "FRN")));
        assessment.setSegments(Collections.singletonList(segmentOne));

        Accommodation accommodation = new Accommodation.Builder().build();

        when(assessmentService.findAssessment("clientName", "key")).thenReturn(Optional.of(assessment));

        when(accommodationsQueryRepository
            .findAssessmentAccommodationsByKey(isA(String.class), anySetOf(String.class)))
            .thenReturn(Collections.singletonList(accommodation));

        List<Accommodation> accommodations = accommodationsService.findAccommodationsByAssessmentKey("clientName", "key");

        verify(accommodationsQueryRepository)
            .findAssessmentAccommodationsByKey(isA(String.class), languageCaptor.capture());

        assertThat(accommodations).containsExactly(accommodation);
        assertThat(languageCaptor.getValue()).containsOnly("ENU", "FRN", "Braille");
    }

    @Test
    public void shouldFindAccommodationsById() {
        Accommodation accommodation = new Accommodation.Builder().build();

        when(accommodationsQueryRepository.findAssessmentAccommodationsById("SBAC_PT", "assessmentId")).thenReturn(Collections.singletonList(accommodation));
        List<Accommodation> accommodations = accommodationsService.findAccommodationsByAssessmentId("SBAC_PT", "assessmentId");

        assertThat(accommodations).containsExactly(accommodation);
    }
}

