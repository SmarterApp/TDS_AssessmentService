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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import tds.assessment.services.AssessmentLoaderService;
import tds.common.ValidationError;
import tds.common.web.resources.NoContentResponseResource;
import tds.testpackage.model.TestPackage;

/**
 * Controller for loading assessments
 */
@RestController
public class AssessmentLoaderController {
    private final AssessmentLoaderService service;

    @Autowired
    public AssessmentLoaderController(final AssessmentLoaderService assessmentLoaderService) {
        this.service = assessmentLoaderService;
    }

    @PostMapping(value = "/assessments/{testPackageName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<NoContentResponseResource> loadTestPackage(@PathVariable final String testPackageName,
                                                              @RequestBody final TestPackage testPackage) {
        Optional<ValidationError> maybeError = service.loadTestPackage(testPackageName, testPackage);

        return maybeError
            .map(validationError -> new ResponseEntity<>(new NoContentResponseResource(validationError), HttpStatus.UNPROCESSABLE_ENTITY))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.CREATED));

    }
}
