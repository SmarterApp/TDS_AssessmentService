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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import tds.assessment.model.configs.AssessmentItemConstraint;
import tds.assessment.model.configs.AssessmentItemType;
import tds.assessment.repositories.loader.configs.AssessmentItemConstraintRepository;
import tds.assessment.repositories.loader.configs.AssessmentItemTypeRepository;
import tds.assessment.services.AssessmentItemConfigLoaderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentItemConfigLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentItemConfigLoaderService service;

    @Mock
    private AssessmentItemTypeRepository assessmentItemTypeRepository;

    @Mock
    private AssessmentItemConstraintRepository assessmentItemConstraintRepository;

    @Captor
    private ArgumentCaptor<Set<AssessmentItemType>> assessmentItemTypeArgumentCaptor;

    @Captor
    private ArgumentCaptor<Set<AssessmentItemConstraint>> assessmentItemConstraintArgumentCaptor;

    @Before
    public void setup() {
        service = new AssessmentItemConfigLoaderServiceImpl(assessmentItemTypeRepository, assessmentItemConstraintRepository);
    }

    @Test
    public void shouldLoadItemTypes() {
        service.loadItemTypes(mockTestPackage);

        verify(assessmentItemTypeRepository).save(assessmentItemTypeArgumentCaptor.capture());
        Set<AssessmentItemType> savedItemTypes = assessmentItemTypeArgumentCaptor.getValue();
        // 8 different types of items in this test package example
        assertThat(savedItemTypes).hasSize(8);
        // Only one ER between the two assessments
        AssessmentItemType savedItemType = savedItemTypes.stream()
            .filter(type -> type.getAssessmentItemTypeIdentity().getItemType().equalsIgnoreCase("ER"))
            .findFirst().get();
        assertThat(savedItemType.getAssessmentItemTypeIdentity().getAssessmentId()).isEqualTo("SBAC-IRP-Perf-MATH-11");
        assertThat(savedItemType.getAssessmentItemTypeIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
    }

    @Test
    public void shouldLoadItemConstraints() {
        service.loadItemConstraints(mockTestPackage);

        verify(assessmentItemConstraintRepository).save(assessmentItemConstraintArgumentCaptor.capture());
        Set<AssessmentItemConstraint> savedItemConstraints = assessmentItemConstraintArgumentCaptor.getValue();
        assertThat(savedItemConstraints).hasSize(10); // 8 items + 2 languages

        AssessmentItemConstraint languagePerfItemConstraint = savedItemConstraints.stream()
            .filter(constraint ->
                constraint.getAssessmentItemConstraintIdentity().getPropertyName().equalsIgnoreCase("Language") &&
                constraint.getAssessmentItemConstraintIdentity().getAssessmentId().equalsIgnoreCase("SBAC-IRP-Perf-MATH-11")
            ).findFirst().get();

        assertThat(languagePerfItemConstraint.getToolType()).isEqualTo("Language");
        assertThat(languagePerfItemConstraint.getToolValue()).isEqualTo("ENU");
        assertThat(languagePerfItemConstraint.getAssessmentItemConstraintIdentity().getPropertyValue()).isEqualTo("ENU");
        assertThat(languagePerfItemConstraint.getAssessmentItemConstraintIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());

        AssessmentItemConstraint itemTypePerfItemConstraint = savedItemConstraints.stream()
            .filter(constraint ->
                constraint.getAssessmentItemConstraintIdentity().getPropertyValue().equalsIgnoreCase("ER") &&
                    constraint.getAssessmentItemConstraintIdentity().getAssessmentId().equalsIgnoreCase("SBAC-IRP-Perf-MATH-11")
            ).findFirst().get();

        assertThat(itemTypePerfItemConstraint.getToolType()).isEqualTo("Item Types Exclusion");
        assertThat(itemTypePerfItemConstraint.getToolValue()).isEqualTo("TDS_ItemTypeExcl_ER");
        assertThat(itemTypePerfItemConstraint.getAssessmentItemConstraintIdentity().getPropertyValue()).isEqualTo("ER");
        assertThat(itemTypePerfItemConstraint.getAssessmentItemConstraintIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
    }
}
