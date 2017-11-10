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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.assessment.Assessment;
import tds.assessment.services.AssessmentService;
import tds.common.web.exceptions.NotFoundException;

/**
 * Controller handling Assessments
 */
@RestController
class AssessmentController {
    private final AssessmentService service;

    @Autowired
    public AssessmentController(final AssessmentService service) {
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
    ResponseEntity<Assessment> findAssessment(@PathVariable final String clientName,
                                              @PathVariable final String key) throws NotFoundException {
        final Assessment assessment = service.findAssessment(clientName, key)
            .orElseThrow(() -> new NotFoundException("Could not find set of admin subject for %s", key));

        return ResponseEntity.ok(assessment);
    }

    @DeleteMapping(value = "/{clientName}/assessments/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> removeAssessment(@PathVariable final String clientName,
                                       @PathVariable final String key) throws NotFoundException {
        service.removeAssessment(clientName, key);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
