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
