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

package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import tds.accommodation.Accommodation;
import tds.assessment.Assessment;
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.assessment.services.AccommodationsService;
import tds.assessment.services.AssessmentService;
import tds.common.cache.CacheType;
import tds.common.web.exceptions.NotFoundException;

@Service
public class AccommodationServiceImpl implements AccommodationsService {
    private final AssessmentService assessmentService;
    private final AccommodationsQueryRepository accommodationsQueryRepository;

    @Autowired
    public AccommodationServiceImpl(final AssessmentService assessmentService, final AccommodationsQueryRepository accommodationsQueryRepository) {
        this.assessmentService = assessmentService;
        this.accommodationsQueryRepository = accommodationsQueryRepository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public List<Accommodation> findAccommodationsByAssessmentKey(final String clientName, final String assessmentKey) {
        //Implements the replacement for CommonDLL.TestKeyAccommodations_FN
        Optional<Assessment> maybeAssessment = assessmentService.findAssessment(clientName, assessmentKey);
        if (!maybeAssessment.isPresent()) {
            throw new NotFoundException("Could not find assessment for %s", assessmentKey);
        }

        Assessment assessment = maybeAssessment.get();
        return accommodationsQueryRepository.findAssessmentAccommodationsByKey(assessment.getKey(), assessment.getLanguageCodes());
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public List<Accommodation> findAccommodationsByAssessmentId(String clientName, String assessmentId) {
        return accommodationsQueryRepository.findAssessmentAccommodationsById(clientName, assessmentId);
    }
}
