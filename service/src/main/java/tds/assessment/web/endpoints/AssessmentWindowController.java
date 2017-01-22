package tds.assessment.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentWindowParameters;
import tds.assessment.services.AssessmentWindowService;

@RestController
public class AssessmentWindowController {
    private final AssessmentWindowService assessmentWindowService;

    @Autowired
    public AssessmentWindowController(AssessmentWindowService assessmentWindowService) {
        this.assessmentWindowService = assessmentWindowService;
    }

    @GetMapping(value = "{clientName}/assessments/{assessmentId}/windows/student/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<AssessmentWindow>> findAssessmentWindows(@PathVariable final String clientName,
                                                                 @PathVariable final String assessmentId,
                                                                 @PathVariable final long studentId,
                                                                 @RequestParam(required = false) final Integer shiftWindowStart,
                                                                 @RequestParam(required = false) final Integer shiftWindowEnd,
                                                                 @RequestParam(required = false) final Integer shiftFormStart,
                                                                 @RequestParam(required = false) final Integer shiftFormEnd,
                                                                 @RequestParam(required = false) final String formList
    ) {
        AssessmentWindowParameters assessmentWindowParameters = new AssessmentWindowParameters
            .Builder(studentId, clientName, assessmentId)
            .withShiftWindowStart(shiftWindowStart == null ? 0 : shiftWindowStart)
            .withShiftWindowEnd(shiftWindowEnd == null ? 0 : shiftWindowEnd)
            .withShiftFormStart(shiftFormStart == null ? 0 : shiftFormStart)
            .withShiftFormEnd(shiftFormEnd == null ? 0 : shiftFormEnd)
            .withFormList(formList)
            .build();

        return ResponseEntity.ok(assessmentWindowService.findAssessmentWindows(assessmentWindowParameters));
    }
}
