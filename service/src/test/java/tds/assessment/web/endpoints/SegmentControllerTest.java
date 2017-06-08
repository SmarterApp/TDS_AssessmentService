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