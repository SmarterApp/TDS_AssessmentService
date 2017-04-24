package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
}
