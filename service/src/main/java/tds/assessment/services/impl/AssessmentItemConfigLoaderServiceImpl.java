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
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.configs.AssessmentItemConstraint;
import tds.assessment.model.configs.AssessmentItemType;
import tds.assessment.repositories.loader.configs.AssessmentItemConstraintRepository;
import tds.assessment.repositories.loader.configs.AssessmentItemTypeRepository;
import tds.assessment.services.AssessmentItemConfigLoaderService;
import tds.common.Algorithm;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentItemConfigLoaderServiceImpl implements AssessmentItemConfigLoaderService {
    private static final String ITEM_TYPE_PROPERTY_NAME = "--ITEMTYPE--";
    private static final String ITEM_TYPE_TOOLTYPE_NAME = "Item Types Exclusion";
    private static final String ITEM_TYPE_TOOLTYPE_VALUE_PREFIX = "TDS_ItemTypeExcl_";
    private static final String LANGUAGE_PROPERTY_NAME = "Language";

    private final AssessmentItemTypeRepository assessmentItemTypeRepository;
    private final AssessmentItemConstraintRepository assessmentItemConstraintRepository;

    @Autowired
    public AssessmentItemConfigLoaderServiceImpl(final AssessmentItemTypeRepository assessmentItemTypeRepository, final AssessmentItemConstraintRepository assessmentItemConstraintRepository) {
        this.assessmentItemTypeRepository = assessmentItemTypeRepository;
        this.assessmentItemConstraintRepository = assessmentItemConstraintRepository;
    }

    @Override
    public void loadItemTypes(final TestPackage testPackage) {
        Set<AssessmentItemType> assessmentItemTypes = testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                    if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                        return segment.segmentForms().stream()
                            .flatMap(form -> form.itemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .map(item -> new AssessmentItemType(testPackage.getPublisher(), assessment.getId(), item.getType()))
                                )
                            );
                    } else if (segment.getAlgorithmType().contains("adaptive")) {
                        return segment.pool().stream()
                            .flatMap(itemGroup -> itemGroup.items().stream()
                                .map(item -> new AssessmentItemType(testPackage.getPublisher(), assessment.getId(), item.getType()))
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                }
            )).collect(Collectors.toSet());

        assessmentItemTypeRepository.save(assessmentItemTypes);
    }

    @Override
    public void loadItemConstraints(final TestPackage testPackage) {
        // Get the set of the distinct item type item constraints
        Set<AssessmentItemConstraint> itemConstraints = testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                    if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                        return segment.segmentForms().stream()
                            .flatMap(form -> form.itemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .map(item ->
                                        new AssessmentItemConstraint.Builder(testPackage.getPublisher(),
                                            assessment.getId(), ITEM_TYPE_PROPERTY_NAME, item.getType(), false)
                                            .withToolType(ITEM_TYPE_TOOLTYPE_NAME)
                                            .withToolValue(ITEM_TYPE_TOOLTYPE_VALUE_PREFIX + item.getType())
                                            .build()
                                    )
                                )
                            );
                    } else if (segment.getAlgorithmType().contains("adaptive")) {
                        return segment.pool().stream()
                            .flatMap(itemGroup -> itemGroup.items().stream()
                                .map(item ->
                                    new AssessmentItemConstraint.Builder(testPackage.getPublisher(),
                                        assessment.getId(), ITEM_TYPE_PROPERTY_NAME, item.getType(), false)
                                        .withToolType(ITEM_TYPE_TOOLTYPE_NAME)
                                        .withToolValue(ITEM_TYPE_TOOLTYPE_VALUE_PREFIX + item.getType())
                                        .build()
                                )
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                }
            )).collect(Collectors.toSet());

        // Get the set of distinct item language constraints
        Set<AssessmentItemConstraint> languageItemConstraints = testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                    if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                        return segment.segmentForms().stream()
                            .flatMap(form -> form.itemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .flatMap(item -> item.getPresentations().stream()
                                        //TODO: do we need a presentation @label attribute?
                                        .map(language -> new AssessmentItemConstraint.Builder(testPackage.getPublisher(),
                                            assessment.getId(), LANGUAGE_PROPERTY_NAME, language.getCode(), true)
                                            .withToolType(LANGUAGE_PROPERTY_NAME)
                                            .withToolValue(language.getCode())
                                            .build())
                                    )
                                )
                            );
                    } else if (segment.getAlgorithmType().contains("adaptive")) {
                        return segment.pool().stream()
                            .flatMap(itemGroup -> itemGroup.items().stream()
                                .flatMap(item -> item.getPresentations().stream()
                                    .map(language -> new AssessmentItemConstraint.Builder(testPackage.getPublisher(),
                                        assessment.getId(), LANGUAGE_PROPERTY_NAME, language.getCode(), true)
                                        .withToolType(LANGUAGE_PROPERTY_NAME)
                                        .withToolValue(language.getCode())
                                        .build())
                                )
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                }
            )).collect(Collectors.toSet());

        itemConstraints.addAll(languageItemConstraints);

        assessmentItemConstraintRepository.save(itemConstraints);
    }
}
