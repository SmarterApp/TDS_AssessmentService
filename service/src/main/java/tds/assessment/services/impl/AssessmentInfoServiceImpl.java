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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tds.assessment.AssessmentInfo;
import tds.assessment.AssessmentWindow;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.services.AssessmentInfoService;
import tds.assessment.services.AssessmentWindowService;

@Service
public class AssessmentInfoServiceImpl implements AssessmentInfoService {
    private final AssessmentQueryRepository assessmentQueryRepository;
    private final AssessmentWindowService assessmentWindowService;

    @Autowired
    public AssessmentInfoServiceImpl(final AssessmentQueryRepository assessmentQueryRepository,
                                     final AssessmentWindowService assessmentWindowService) {
        this.assessmentQueryRepository = assessmentQueryRepository;
        this.assessmentWindowService = assessmentWindowService;
    }

    @Override
    public List<AssessmentInfo> findAssessmentInfo(final String clientName, final String... assessmentKeys) {
        List<AssessmentInfo> assessments = assessmentQueryRepository.findAssessmentInfoByKeys(clientName, assessmentKeys);
        String[] assessmentIds = assessments.stream().map(AssessmentInfo::getId).toArray(size -> new String[size]);
        Map<String, List<AssessmentWindow>> assessmentWindows = assessmentWindowService.findAssessmentWindowsForAssessmentIds(
            clientName, assessmentIds);

        return assessments.stream()
            .map(assessmentInfo ->
                new AssessmentInfo.Builder()
                    .fromAssessmentInfo(assessmentInfo)
                    .withAssessmentWindows(assessmentWindows.get(assessmentInfo.getKey()))
                    .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<AssessmentInfo> findAssessmentInfoForGrade(final String clientName, final String grade) {
        List<AssessmentInfo> assessments =  assessmentQueryRepository.findAssessmentInfoForGrade(clientName, grade);
        String[] assessmentIds = assessments.stream().map(AssessmentInfo::getId).toArray(size -> new String[size]);
        Map<String, List<AssessmentWindow>> assessmentWindows = assessmentWindowService.findAssessmentWindowsForAssessmentIds(
            clientName, assessmentIds);

        return assessments.stream()
            .map(assessmentInfo ->
                new AssessmentInfo.Builder()
                    .fromAssessmentInfo(assessmentInfo)
                    .withAssessmentWindows(assessmentWindows.get(assessmentInfo.getKey()))
                    .build())
            .collect(Collectors.toList());
    }
}
