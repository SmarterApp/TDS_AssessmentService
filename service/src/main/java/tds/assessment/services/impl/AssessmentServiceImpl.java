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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tds.accommodation.AccommodationDependency;
import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Strand;
import tds.assessment.model.SegmentMetadata;
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.assessment.repositories.AssessmentCommandRepository;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.GradesQueryRepository;
import tds.assessment.repositories.ItemGroupQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentConfigLoaderService;
import tds.assessment.services.AssessmentService;
import tds.common.Algorithm;
import tds.common.cache.CacheType;
import tds.common.web.exceptions.NotFoundException;

@Service
class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentQueryRepository assessmentQueryRepository;
    private final AssessmentCommandRepository assessmentCommandRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final FormQueryRepository formQueryRepository;
    private final StrandQueryRepository strandQueryRepository;
    private final AccommodationsQueryRepository accommodationsQueryRepository;
    private final GradesQueryRepository gradesQueryRepository;
    private final ItemGroupQueryRepository itemGroupQueryRepository;

    @Autowired
    public AssessmentServiceImpl(final AssessmentQueryRepository assessmentQueryRepository,
                                 final AssessmentCommandRepository assessmentCommandRepository,
                                 final ItemQueryRepository itemQueryRepository,
                                 final FormQueryRepository formQueryRepository,
                                 final StrandQueryRepository strandQueryRepository,
                                 final AccommodationsQueryRepository accommodationsQueryRepository,
                                 final GradesQueryRepository gradesQueryRepository,
                                 final ItemGroupQueryRepository itemGroupQueryRepository) {
        this.assessmentQueryRepository = assessmentQueryRepository;
        this.assessmentCommandRepository = assessmentCommandRepository;
        this.itemQueryRepository = itemQueryRepository;
        this.formQueryRepository = formQueryRepository;
        this.strandQueryRepository = strandQueryRepository;
        this.accommodationsQueryRepository = accommodationsQueryRepository;
        this.gradesQueryRepository = gradesQueryRepository;
        this.itemGroupQueryRepository = itemGroupQueryRepository;
    }

    @Override
//    @Cacheable(CacheType.LONG_TERM)
    public Optional<Assessment> findAssessment(final String clientName, final String assessmentKey) {
        Optional<Assessment> maybeAssessment = assessmentQueryRepository.findAssessmentByKey(clientName, assessmentKey);
        List<Form> forms = new ArrayList<>();

        if (maybeAssessment.isPresent()) {
            Assessment assessment = maybeAssessment.get();
            Set<Strand> strands = strandQueryRepository.findStrands(assessmentKey);
            List<ItemConstraint> itemConstraints = assessmentQueryRepository.findItemConstraintsForAssessment(clientName,
                assessment.getAssessmentId());
            List<ItemProperty> itemProperties = itemQueryRepository.findActiveItemsProperties(assessmentKey);
            List<Item> items = itemQueryRepository.findItemsForAssessment(assessmentKey);
            List<AccommodationDependency> accommodationDependencies = accommodationsQueryRepository.findAssessmentAccommodationDependencies(clientName,
                assessment.getAssessmentId());
            List<String> grades = gradesQueryRepository.findGrades(assessmentKey);

            if (assessment.getSegments().stream().anyMatch(s -> s.getSelectionAlgorithm().equals(Algorithm.FIXED_FORM))) {
                forms = formQueryRepository.findFormsForAssessment(assessmentKey);
            }

            assessment.getSegments().forEach(
                segment -> segment.setItemGroups(itemGroupQueryRepository.findItemGroupsBySegment(segment.getKey()))
            );

            AssessmentAssembler.assemble(assessment, strands, itemConstraints, itemProperties, items, forms,
                accommodationDependencies, grades);
        }

        return maybeAssessment;
    }

    @Override
    public Optional<Assessment> findAssessmentBySegmentKey(final String segmentKey) {
        SegmentMetadata metadata = assessmentQueryRepository.findSegmentMetadata(segmentKey)
            .orElseThrow(() -> new NotFoundException("Could not find a segment for key %s", segmentKey));

        return findAssessment(metadata.getClientName(), metadata.getParentKey() == null ? segmentKey : metadata.getParentKey());
    }

    @Override
    @Transactional
    public void removeAssessment(final String clientName, final boolean safeDelete, final String... keys) {
        Arrays.asList(keys).forEach(key -> {
            if (safeDelete) {
                final Assessment assessment = findAssessment(clientName, key)
                    .orElseThrow(() -> new NotFoundException("Could not find set of admin subject for %s", key));

                assessmentCommandRepository.removeAssessmentData(clientName, assessment);
            }  else {
                assessmentCommandRepository.removeItemBankAssessmentData(key);
                // If this is a multi-segmented assessment, delete all the segment-specific data as well
                assessmentQueryRepository.findSegmentKeysByAssessmentKey(key)
                    .forEach(segmentKey -> assessmentCommandRepository.removeItemBankAssessmentData(key));
            }
        });
    }
}
