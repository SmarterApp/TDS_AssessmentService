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

import java.util.List;
import java.util.Set;

import tds.assessment.model.configs.AccommodationFamily;
import tds.assessment.model.configs.AssessmentGrade;
import tds.assessment.model.configs.Client;
import tds.assessment.model.configs.Grade;
import tds.assessment.model.configs.Language;
import tds.assessment.model.configs.Subject;
import tds.assessment.model.configs.TimeWindow;
import tds.assessment.repositories.ConfigsDataCommandRepository;
import tds.assessment.repositories.loader.configs.AccommodationFamilyRepository;
import tds.assessment.repositories.loader.configs.AssessmentGradeRepository;
import tds.assessment.repositories.loader.configs.ClientRepository;
import tds.assessment.repositories.loader.configs.GradeRepository;
import tds.assessment.repositories.loader.configs.LanguageRepository;
import tds.assessment.repositories.loader.configs.SubjectRepository;
import tds.assessment.repositories.loader.configs.TimeWindowRepository;
import tds.assessment.services.AssessmentConfigSeedDataLoaderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentConfigSeedDataLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentConfigSeedDataLoaderService service;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ConfigsDataCommandRepository configsDataCommandRepository;

    @Mock
    private TimeWindowRepository timeWindowRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private AccommodationFamilyRepository accommodationFamilyRepository;

    @Mock
    private AssessmentGradeRepository assessmentGradeRepository;

    @Captor
    private ArgumentCaptor<Client> clientArgumentCaptor;

    @Captor
    private ArgumentCaptor<TimeWindow> timeWindowArgumentCaptor;

    @Captor
    private ArgumentCaptor<Set<Grade>> gradeArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<AssessmentGrade>> assessmentGradeArgumentCaptor;

    @Captor
    private ArgumentCaptor<Subject> subjectArgumentCaptor;

    @Captor
    private ArgumentCaptor<AccommodationFamily> accommodationFamilyArgumentCaptor;

    @Captor
    private ArgumentCaptor<Set<Language>> languageArgumentCaptor;

    @Before
    public void setup() {
        service = new AssessmentConfigSeedDataLoaderServiceImpl(clientRepository, configsDataCommandRepository, timeWindowRepository,
            gradeRepository, subjectRepository, languageRepository, accommodationFamilyRepository, assessmentGradeRepository);
    }

    @Test
    public void shouldLoadSeedData() {
        service.loadSeedData(mockTestPackage);

        verify(configsDataCommandRepository).copyTesteeAttributeSeedDataForClient(mockTestPackage.getPublisher());
        verify(configsDataCommandRepository).copyTesteeRelationshipAttributeSeedDataForClient(mockTestPackage.getPublisher());

        verify(clientRepository).save(clientArgumentCaptor.capture());
        Client savedClient = clientArgumentCaptor.getValue();
        assertThat(savedClient.getName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedClient.getDefaultLanguage()).isEqualTo("ENU");

        verify(timeWindowRepository).save(timeWindowArgumentCaptor.capture());
        TimeWindow savedTimeWindow = timeWindowArgumentCaptor.getValue();
        assertThat(savedTimeWindow.getTimeWindowIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedTimeWindow.getStartDate()).isNotNull();
        assertThat(savedTimeWindow.getEndDate()).isNotNull();
        assertThat(savedTimeWindow.getTimeWindowIdentity().getWindowId()).isEqualTo("ANNUAL");

        verify(gradeRepository).save(gradeArgumentCaptor.capture());
        assertThat(gradeArgumentCaptor.getValue().size()).isEqualTo(1);
        Grade savedGrade = (Grade) gradeArgumentCaptor.getValue().toArray()[0];
        assertThat(savedGrade.getGradeLabel()).isEqualTo("Grade 11");
        assertThat(savedGrade.getGradeIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedGrade.getGradeIdentity().getGradeCode()).isEqualTo("11");

        verify(assessmentGradeRepository).save(assessmentGradeArgumentCaptor.capture());
        List<AssessmentGrade> savedAssessmentGrades = assessmentGradeArgumentCaptor.getValue();
        assertThat(savedAssessmentGrades).hasSize(2); // 1 for each assessment
        assertThat(savedAssessmentGrades.get(0).getAssessmentGradeIdentity().getAssessmentId())
            .isEqualTo(mockTestPackage.getAssessments().get(0).getId());
        assertThat(savedAssessmentGrades.get(0).getAssessmentGradeIdentity().getClientName())
            .isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedAssessmentGrades.get(0).getAssessmentGradeIdentity().getGrade())
            .isEqualTo("11");

        assertThat(savedAssessmentGrades.get(1).getAssessmentGradeIdentity().getAssessmentId())
            .isEqualTo(mockTestPackage.getAssessments().get(1).getId());
        assertThat(savedAssessmentGrades.get(1).getAssessmentGradeIdentity().getClientName())
            .isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedAssessmentGrades.get(1).getAssessmentGradeIdentity().getGrade())
            .isEqualTo("11");

        verify(subjectRepository).save(subjectArgumentCaptor.capture());
        Subject savedSubject = subjectArgumentCaptor.getValue();
        assertThat(savedSubject.getSubjectCode()).isEqualTo("MATH");
        assertThat(savedSubject.getSubjectIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedSubject.getSubjectIdentity().getSubject()).isEqualTo("MATH");

        verify(accommodationFamilyRepository).save(accommodationFamilyArgumentCaptor.capture());
        AccommodationFamily savedAccommodationFamily = accommodationFamilyArgumentCaptor.getValue();
        assertThat(savedAccommodationFamily.getAccommodationFamilyIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedAccommodationFamily.getAccommodationFamilyIdentity().getFamily()).isEqualTo("MATH");
        assertThat(savedAccommodationFamily.getLabel()).isEqualTo("MATH");
    }

    @Test
    public void shouldLoadLanguageSuccessfully() {
        service.loadLanguage(mockTestPackage);
        verify(languageRepository).save(languageArgumentCaptor.capture());
        Set<Language> languages = languageArgumentCaptor.getValue();
        assertThat(languages).hasSize(1);
        Language savedLanguage = (Language) languages.toArray()[0];
        assertThat(savedLanguage.getLanguageIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedLanguage.getLanguageIdentity().getLanguageCode()).isEqualTo("ENU");
        assertThat(savedLanguage.getLanguageLabel()).isEqualTo("English");
    }
}
