package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.services.AssessmentService;

@Service
class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentQueryRepository assessmentQueryRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentQueryRepository assessmentQueryRepository) {
        this.assessmentQueryRepository = assessmentQueryRepository;
    }

    @Override
    public Optional<Assessment> findAssessmentByKey(String assessmentKey) {
        return assessmentQueryRepository.findAssessmentByKey(assessmentKey);
    }
}
