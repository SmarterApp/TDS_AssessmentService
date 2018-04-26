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
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import tds.assessment.model.configs.AssessmentProperties;
import tds.assessment.model.configs.SegmentProperties;
import tds.assessment.model.configs.TestMode;
import tds.assessment.model.configs.TestWindow;
import tds.assessment.repositories.loader.configs.AssessmentPropertiesRepository;
import tds.assessment.repositories.loader.configs.SegmentPropertiesRepository;
import tds.assessment.repositories.loader.configs.TestModeRepository;
import tds.assessment.repositories.loader.configs.TestWindowRepository;
import tds.assessment.services.AssessmentSegmentConfigLoaderService;
import tds.common.Algorithm;
import tds.testpackage.model.Assessment;
import tds.testpackage.model.Grade;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentSegmentConfigLoaderServiceImpl implements AssessmentSegmentConfigLoaderService {
    private static final String HIGH_SCHOOL_GRADE_STRING = "HS";
    private static final int MIN_HIGH_SCHOOL_GRADE = 9;
    private static final int MAX_HIGH_SCHOOL_GRADE = 12;
    private static final int HIGH_SCHOOL_GRADE_RANGE_LENGTH = 4;

    private final AssessmentPropertiesRepository assessmentPropertiesRepository;
    private final TestModeRepository testModeRepository;
    private final SegmentPropertiesRepository segmentPropertiesRepository;
    private final TestWindowRepository testWindowRepository;

    @Autowired
    public AssessmentSegmentConfigLoaderServiceImpl(final AssessmentPropertiesRepository assessmentPropertiesRepository,
                                                    final TestModeRepository testModeRepository,
                                                    final SegmentPropertiesRepository segmentPropertiesRepository,
                                                    final TestWindowRepository testWindowRepository) {
        this.assessmentPropertiesRepository = assessmentPropertiesRepository;
        this.testModeRepository = testModeRepository;
        this.segmentPropertiesRepository = segmentPropertiesRepository;
        this.testWindowRepository = testWindowRepository;
    }

    @Override
    public void loadAssessmentProperties(final TestPackage testPackage) {
        List<AssessmentProperties> assessmentProperties = testPackage.getAssessments().stream()
            .map(assessment ->
                new AssessmentProperties.Builder(testPackage.getPublisher(), assessment.getId())
                    .withLabel(assessment.getLabel())
                    .withScoreByTds(true)
                    .withSubjectName(testPackage.getSubject())
                    .withGradeLabel(createGradeLabel(assessment.getGrades()))
                    .build())
            .collect(Collectors.toList());

        assessmentPropertiesRepository.save(assessmentProperties);
    }

    @Override
    public void loadSegmentProperties(final TestPackage testPackage) {
        List<SegmentProperties> segmentProperties = testPackage.getAssessments().stream()
            .filter(Assessment::isSegmented)
            .flatMap(assessment -> assessment.getSegments().stream()
                .map(segment ->
                    new SegmentProperties.Builder(testPackage.getPublisher(), assessment.getId(), segment.getId())
                        .withAssessmentKey(assessment.getKey())
                        .withEntryApprovalRequired(segment.entryApproval())
                        .withExitApprovalRequired(segment.exitApproval())
                        .withLabel(segment.getLabel().orElse(segment.getId()))
                        .withPosition(segment.position())
                        .withItemReviewRequired(false)
                        .withPermeable(1)
                        .build())
            )
            .collect(Collectors.toList());

        segmentPropertiesRepository.save(segmentProperties);
    }

    @Override
    public void loadTestWindow(final TestPackage testPackage) {
        Calendar calendar = Calendar.getInstance();
        Timestamp now = new Timestamp(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 10);
        Timestamp tenYearsFromNow = new Timestamp(calendar.getTimeInMillis());

        List<TestWindow> testWindows = testPackage.getAssessments().stream()
            .map(assessment -> new TestWindow.Builder()
                .withClientName(testPackage.getPublisher())
                .withAssessmentId(assessment.getId())
                .withStartDate(now)
                .withEndDate(tenYearsFromNow)
                .withNumberOfOpportunities(3)
                .build())
            .collect(Collectors.toList());

        testWindowRepository.save(testWindows);
    }

    @Override
    public void loadTestMode(final TestPackage testPackage) {
        List<TestMode> testModes = testPackage.getAssessments().stream()
            .map(assessment -> new TestMode.Builder(assessment.getKey())
                .withClientName(testPackage.getPublisher())
                // If the assessment is single-segmented, just get the segment's algorithm
                .withAlgorithm(assessment.isSegmented()
                    ? Algorithm.VIRTUAL.getType()
                    : assessment.getSegments().get(0).getAlgorithmType())
                .withAssessmentId(assessment.getId())
                .withSegmented(assessment.isSegmented())
                .build())
            .collect(Collectors.toList());

        testModeRepository.save(testModes);
    }

    /**
     * Creates the grade string from the list of grades. Port of "itembank._maketestgradelabel" stored procedure
     *
     * Rules:
     *      If no grades are present, return an empty string
     *      If a single grade is present, return it
     *      If the grades is a contiguous range, hyphenate it (i.e. 7, 8, 9 =  7-9)
     *      If the grade range is a standard high school range (i.e. 9-12), return "HS"
     *      Otherwise, return the list of grades comma delimited, in ascending order.
     *      If non-numeric grades are present, they should be listed first
     *
     * @param grades The list of grade strings
     * @return The properly formatted grade string
     */
    private String createGradeLabel(final List<tds.testpackage.model.Grade> grades) {
        /* This logic corresponds to logic found in legacy - ReportingDLL.TestGradeSpan_F(), line 1353 */
        if (grades.size() == 0) {
            return StringUtils.EMPTY;
        } else if (grades.size() == 1) {
            return "Grade " + grades.get(0).getValue();
        }

        int minGrade = Integer.MAX_VALUE;
        int maxGrade = Integer.MIN_VALUE;
        List<Integer> intGrades = new ArrayList<>();

        for (Grade grade : grades) {
            if (StringUtils.isNumeric(grade.getValue())) {
                int g = Integer.parseInt(grade.getValue());
                minGrade = Math.min(minGrade, g);
                maxGrade = Math.max(maxGrade, g);
                intGrades.add(g);
            }
        }

        final String gradeStr;

        if (intGrades.size() == grades.size()) {
            // If its 9 - 12, "HS" for High School
            if (minGrade == MIN_HIGH_SCHOOL_GRADE && maxGrade == MAX_HIGH_SCHOOL_GRADE && grades.size() == HIGH_SCHOOL_GRADE_RANGE_LENGTH) {
                gradeStr = HIGH_SCHOOL_GRADE_STRING;
            } else if (maxGrade - minGrade + 1 == intGrades.size()) {
                gradeStr = String.format("Grades %s-%s", minGrade, maxGrade);
            } else {
                gradeStr = "Grades " + StringUtils.join(intGrades, ", ");
            }
        } else {
            gradeStr = "Grades " + StringUtils.join(grades, ", ");
        }

        return gradeStr;
    }
}
