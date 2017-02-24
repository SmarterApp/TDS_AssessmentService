package tds.assessment.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import tds.accommodation.Accommodation;
import tds.assessment.services.AccommodationsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccommodationControllerTest {
    @Mock
    private AccommodationsService accommodationsService;

    private AccommodationController controller;

    @Before
    public void setUp() {
        controller = new AccommodationController(accommodationsService);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldReturnAccommodationsByAssessmentKey() {
        Accommodation accommodation = new Accommodation.Builder().build();
        when(accommodationsService.findAccommodationsByAssessmentKey("clientName", "key")).thenReturn(Collections.singletonList(accommodation));

        ResponseEntity<List<Accommodation>> response = controller.findAccommodations("clientName", "key", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsOnly(accommodation);
    }

    @Test
    public void shouldReturnAccommodationsByAssessmentId() {
        Accommodation accommodation = new Accommodation.Builder().build();
        when(accommodationsService.findAccommodationsByAssessmentId("client", "id")).thenReturn(Collections.singletonList(accommodation));

        ResponseEntity<List<Accommodation>> response = controller.findAccommodations("client", null, "id");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsOnly(accommodation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfIdOrKeyNotProvided() {
        controller.findAccommodations("client", null, null);
    }
}
