package tds.assessment.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.assessment.Assessment;
import tds.assessment.services.AssessmentService;
import tds.common.web.exceptions.NotFoundException;

/**
 * Controller handling admin subject related entities
 */
@RestController
class AssessmentController {
    private final AssessmentService service;

    @Autowired
    public AssessmentController(AssessmentService service) {
        this.service = service;
    }

    /**
     * Endpoint to find {@link tds.assessment.Assessment} by key
     *
     * @param key unique key for a {@link tds.assessment.Assessment}
     * @return {@link org.springframework.http.ResponseEntity} containing a {@link tds.assessment.Assessment}}
     * @throws tds.common.web.exceptions.NotFoundException if entity cannot be found
     */
    @GetMapping(value = "/{clientName}/assessments/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Assessment> findAssessment(@PathVariable final String clientName, @PathVariable final String key)
            throws NotFoundException {
        final Assessment assessment = service.findAssessmentByKey(clientName, key)
            .orElseThrow(() -> new NotFoundException("Could not find set of admin subject for %s", key));

        return ResponseEntity.ok(assessment);
    }


}
