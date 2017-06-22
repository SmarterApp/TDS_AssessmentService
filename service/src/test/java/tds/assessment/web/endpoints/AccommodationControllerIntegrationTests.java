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

import tds.accommodation.Accommodation;
import tds.assessment.services.AccommodationsService;
import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccommodationController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class})
public class AccommodationControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private AccommodationsService mockAccommodationsService;

    @Test
    public void shouldFindAccommodationsByKey() throws Exception {
        Accommodation accommodation = new Accommodation.Builder()
            .withDefaultAccommodation(true)
            .withAccommodationCode("code")
            .withAccommodationType("type")
            .withAccommodationValue("value")
            .withAllowChange(true)
            .withDependsOnToolType("depends")
            .withAllowCombine(false)
            .withDisableOnGuestSession(true)
            .withEntryControl(false)
            .withFunctional(true)
            .withSegmentPosition(99)
            .withToolMode("toolMode")
            .withToolTypeSortOrder(25)
            .withToolValueSortOrder(50)
            .withVisible(true)
            .build();

        when(mockAccommodationsService.findAccommodationsByAssessmentKey("SBAC", "key")).thenReturn(singletonList(accommodation));
        String requestUri = UriComponentsBuilder.fromUriString("/SBAC/assessments/accommodations?assessmentKey=key")
            .build()
            .toUriString();

        http.perform(get(requestUri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].defaultAccommodation", is(true)))
            .andExpect(jsonPath("[0].code", is("code")))
            .andExpect(jsonPath("[0].value", is("value")))
            .andExpect(jsonPath("[0].allowChange", is(true)))
            .andExpect(jsonPath("[0].dependsOnToolType", is("depends")))
            .andExpect(jsonPath("[0].allowCombine", is(false)))
            .andExpect(jsonPath("[0].disableOnGuestSession", is(true)))
            .andExpect(jsonPath("[0].entryControl", is(false)))
            .andExpect(jsonPath("[0].functional", is(true)))
            .andExpect(jsonPath("[0].segmentPosition", is(99)))
            .andExpect(jsonPath("[0].toolMode", is("toolMode")))
            .andExpect(jsonPath("[0].toolTypeSortOrder", is(25)))
            .andExpect(jsonPath("[0].toolValueSortOrder", is(50)))
            .andExpect(jsonPath("[0].visible", is(true)))
            .andExpect(jsonPath("[0].type", is("type")));
    }

    @Test
    public void shouldFindAccommodations() throws Exception {
        Accommodation accommodation = new Accommodation.Builder()
            .withDefaultAccommodation(true)
            .withAccommodationCode("code")
            .withAccommodationType("type")
            .withAccommodationValue("value")
            .withAllowChange(true)
            .withDependsOnToolType("depends")
            .withAllowCombine(false)
            .withDisableOnGuestSession(true)
            .withEntryControl(false)
            .withFunctional(true)
            .withSegmentPosition(99)
            .withToolMode("toolMode")
            .withToolTypeSortOrder(25)
            .withToolValueSortOrder(50)
            .withVisible(true)
            .build();

        when(mockAccommodationsService.findAccommodationsByAssessmentId("SBAC", "id")).thenReturn(singletonList(accommodation));

        String requestUri = UriComponentsBuilder.fromUriString("/SBAC/assessments/accommodations?assessmentId=id")
            .build()
            .toUriString();

        http.perform(get(requestUri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].defaultAccommodation", is(true)))
            .andExpect(jsonPath("[0].code", is("code")))
            .andExpect(jsonPath("[0].value", is("value")))
            .andExpect(jsonPath("[0].allowChange", is(true)))
            .andExpect(jsonPath("[0].dependsOnToolType", is("depends")))
            .andExpect(jsonPath("[0].allowCombine", is(false)))
            .andExpect(jsonPath("[0].disableOnGuestSession", is(true)))
            .andExpect(jsonPath("[0].entryControl", is(false)))
            .andExpect(jsonPath("[0].functional", is(true)))
            .andExpect(jsonPath("[0].segmentPosition", is(99)))
            .andExpect(jsonPath("[0].toolMode", is("toolMode")))
            .andExpect(jsonPath("[0].toolTypeSortOrder", is(25)))
            .andExpect(jsonPath("[0].toolValueSortOrder", is(50)))
            .andExpect(jsonPath("[0].visible", is(true)))
            .andExpect(jsonPath("[0].type", is("type")));
    }

    @Test
    public void shouldRequireAccommodationId() throws Exception {
        String requestUri = UriComponentsBuilder.fromUriString("/SBAC/assessments/accommodations")
            .build()
            .toUriString();

        http.perform(get(requestUri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
