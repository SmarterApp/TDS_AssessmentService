package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Strand;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentService;

@Service
class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentQueryRepository assessmentQueryRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final FormQueryRepository formQueryRepository;
    private final StrandQueryRepository strandQueryRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentQueryRepository assessmentQueryRepository,
                                 ItemQueryRepository itemQueryRepository,
                                 FormQueryRepository formQueryRepository,
                                 StrandQueryRepository strandQueryRepository) {
        this.formQueryRepository = formQueryRepository;
        this.assessmentQueryRepository = assessmentQueryRepository;
        this.itemQueryRepository = itemQueryRepository;
        this.strandQueryRepository = strandQueryRepository;
    }

    @Override
    public Optional<Assessment> findAssessmentByKey(final String clientName, final String assessmentKey) {
        Optional<Assessment> maybeAssessment = assessmentQueryRepository.findAssessmentByKey(assessmentKey);

        if (maybeAssessment.isPresent()) {
            Assessment assessment = maybeAssessment.get();
            List<Form> forms = formQueryRepository.findFormsForAssessment(assessmentKey);
            List<Item> items = itemQueryRepository.findItemsForAssessment(assessmentKey);
            List<ItemProperty> itemProperties = itemQueryRepository.findActiveItemsProperties(assessmentKey);
            List<ItemConstraint> itemConstraints = assessmentQueryRepository.findItemConstraintsForAssessment(clientName,
                    assessment.getAssessmentId());
            Set<Strand> strands = strandQueryRepository.findStrands(assessmentKey);

            AssessmentAssembler.assemble(assessment, items, itemProperties, forms, itemConstraints, strands);
        }

        return maybeAssessment;
    }

}
