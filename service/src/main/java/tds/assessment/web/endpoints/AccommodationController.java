package tds.assessment.web.endpoints;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tds.accommodation.Accommodation;
import tds.assessment.services.AccommodationsService;

@RestController
public class AccommodationController {
    private final AccommodationsService accommodationsService;

    @Autowired
    public AccommodationController(AccommodationsService accommodationsService) {
        this.accommodationsService = accommodationsService;
    }

    @GetMapping(value = "{clientName}/assessments/accommodations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Accommodation>> findAccommodations(@PathVariable final String clientName,
                                                           @RequestParam(required = false) final String assessmentKey,
                                                           @RequestParam(required = false) final String assessmentId
    ) {
        if (StringUtils.isNotEmpty(assessmentKey)) {
            return ResponseEntity.ok(accommodationsService.findAccommodationsByAssessmentKey(clientName, assessmentKey));
        } else if (StringUtils.isNotEmpty(assessmentId)) {
            return ResponseEntity.ok(accommodationsService.findAccommodationsByAssessmentId(clientName, assessmentId));
        } else {
            throw new IllegalArgumentException("Must provide an assessmentKey or an assessmentId");
        }
    }
}
