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

import tds.assessment.Assessment;
import tds.assessment.AssessmentInfo;
import tds.assessment.services.AssessmentInfoService;

/**
 * Controller that can handle {@link tds.assessment.AssessmentInfo} interaction
 */
@RestController
public class AssessmentInfoController {
    private final AssessmentInfoService assessmentInfoService;

    @Autowired
    public AssessmentInfoController(final AssessmentInfoService assessmentInfoService) {
        this.assessmentInfoService = assessmentInfoService;
    }

    @GetMapping(value = "/{clientName}/assessments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<AssessmentInfo>> findAssessmentInfos(@PathVariable final String clientName,
                                                             @RequestParam(required = false) final String grade,
                                                             @RequestParam(required = false) final String... assessmentKeys) {
        List<AssessmentInfo> assessmentInfos;

        if (grade == null && assessmentKeys.length == 0) {
            throw new IllegalArgumentException("Either assessmentKeys or grades query params must be present.");
        }

        assessmentInfos = (grade == null)
            ? assessmentInfoService.findAssessmentInfo(clientName, assessmentKeys)
            : assessmentInfoService.findAssessmentInfoForGrade(clientName, grade);

        return ResponseEntity.ok(assessmentInfos);
    }
}
