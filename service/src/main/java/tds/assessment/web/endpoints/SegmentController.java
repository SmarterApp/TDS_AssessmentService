package tds.assessment.web.endpoints;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.assessment.SegmentItemInformation;
import tds.assessment.services.SegmentService;
import tds.common.web.exceptions.NotFoundException;

/**
 * Controller handling segment related information
 */
@RestController
public class SegmentController {
    private final SegmentService segmentService;

    @Autowired
    public SegmentController(final SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @GetMapping(value = "/segment-items/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SegmentItemInformation> getSegmentInformation(@PathVariable String key) {
        SegmentItemInformation segmentItemInformation = segmentService.findSegmentItemInformation(key)
            .orElseThrow(() -> new NotFoundException("Failed to find segment for %s", key));

        return ResponseEntity.ok(segmentItemInformation);
    }
}
