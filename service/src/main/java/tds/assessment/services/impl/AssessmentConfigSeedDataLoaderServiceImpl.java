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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
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
import tds.common.Algorithm;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentConfigSeedDataLoaderServiceImpl implements AssessmentConfigSeedDataLoaderService {
    private final ClientRepository clientRepository;
    private final ConfigsDataCommandRepository configsDataCommandRepository;
    private final TimeWindowRepository timeWindowRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final LanguageRepository languageRepository;
    private final AccommodationFamilyRepository accommodationFamilyRepository;
    private final AssessmentGradeRepository assessmentGradeRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AssessmentConfigSeedDataLoaderServiceImpl(final ClientRepository clientRepository,
                                                     final ConfigsDataCommandRepository configsDataCommandRepository,
                                                     final TimeWindowRepository timeWindowRepository,
                                                     final GradeRepository gradeRepository,
                                                     final SubjectRepository subjectRepository,
                                                     final LanguageRepository languageRepository,
                                                     final AccommodationFamilyRepository accommodationFamilyRepository,
                                                     final AssessmentGradeRepository assessmentGradeRepository,
                                                     final JdbcTemplate jdbcTemplate) {
        this.clientRepository = clientRepository;
        this.configsDataCommandRepository = configsDataCommandRepository;
        this.timeWindowRepository = timeWindowRepository;
        this.gradeRepository = gradeRepository;
        this.subjectRepository = subjectRepository;
        this.languageRepository = languageRepository;
        this.accommodationFamilyRepository = accommodationFamilyRepository;
        this.assessmentGradeRepository = assessmentGradeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void loadSeedData(final TestPackage testPackage) {

        final String clientName = testPackage.getPublisher();
        configsDataCommandRepository.copyTesteeAttributeSeedDataForClient(clientName);
        configsDataCommandRepository.copyTesteeRelationshipAttributeSeedDataForClient(clientName);
        clientRepository.save(new Client(clientName));

        insertTimeWindowData(clientName);
        insertClientGradeData(testPackage);

        // Load the client subject and accommodation family
        subjectRepository.save(new Subject(clientName, testPackage.getSubject()));
        accommodationFamilyRepository.save(new AccommodationFamily(clientName, testPackage.getSubject()));
    }

    @Override
    public void loadLanguage(final TestPackage testPackage) {
        Set<Language> languages = testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                    if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                        return segment.segmentForms().stream()
                            .flatMap(form -> form.itemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .flatMap(item -> item.getPresentations().stream()
                                        //TODO: do we need a presentation @label attribute?
                                        .map(language -> new Language(testPackage.getPublisher(), language.getCode(), language.label()))
                                    )
                                )
                            );
                    } else if (segment.getAlgorithmType().contains("adaptive")) {
                        return segment.pool().stream()
                            .flatMap(itemGroup -> itemGroup.items().stream()
                                .flatMap(item -> item.getPresentations().stream()
                                    .map(language -> new Language(testPackage.getPublisher(), language.getCode(), language.label()))
                                )
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                }
            )).collect(Collectors.toSet());

        languageRepository.save(languages);
    }

    private void insertTimeWindowData(final String clientName) {
        Calendar calendar = Calendar.getInstance();
        Timestamp now = new Timestamp(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 10);
        Timestamp tenYearsFromNow = new Timestamp(calendar.getTimeInMillis());
        TimeWindow clientTimeWindow = new TimeWindow(clientName, now, tenYearsFromNow);

        timeWindowRepository.save(clientTimeWindow);
    }

    private void insertClientGradeData(final TestPackage testPackage) {
        Set<Grade> grades = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getGrades().stream())
            .map(grade ->
                //TODO: Update this once label is part of <Grade>
                new Grade(grade.getValue(),
                    StringUtils.isNumeric(grade.getValue()) ? "Grade " + grade.getValue() : grade.getValue(),
                    testPackage.getPublisher()))
            .collect(Collectors.toSet());

        gradeRepository.save(grades);

        List<AssessmentGrade> assessmentGrades = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getGrades().stream()
                .map(grade -> new AssessmentGrade(testPackage.getPublisher(), assessment.getId(), grade.getValue())))
            .collect(Collectors.toList());

        assessmentGradeRepository.save(assessmentGrades);
    }
}
