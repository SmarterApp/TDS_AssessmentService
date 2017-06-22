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
