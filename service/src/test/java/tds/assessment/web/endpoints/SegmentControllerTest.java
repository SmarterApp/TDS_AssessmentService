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

import java.util.Optional;

import tds.assessment.SegmentItemInformation;
import tds.assessment.services.SegmentService;
import tds.common.web.exceptions.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SegmentControllerTest {
    @Mock
    private SegmentService mockSegmentService;

    private SegmentController segmentController;

    @Before
    public void setUp() {
       segmentController = new SegmentController(mockSegmentService);
    }

    @After
    public void tearDown() {
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundIfSegmentInformationIsEmpty() {
        when(mockSegmentService.findSegmentItemInformation("segmentKey")).thenReturn(Optional.empty());

        segmentController.getSegmentInformation("segmentKey");
    }

    @Test
    public void shouldReturnSegmentInformation() {
        SegmentItemInformation segmentItemInformation = new SegmentItemInformation.Builder().build();
        when(mockSegmentService.findSegmentItemInformation("segmentKey")).thenReturn(Optional.of(segmentItemInformation));

        ResponseEntity<SegmentItemInformation> response = segmentController.getSegmentInformation("segmentKey");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(segmentItemInformation);
    }
}