package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.repositories.StrandQueryRepository;
import tds.assessment.services.AssessmentService;
import tds.common.Algorithm;
import tds.common.cache.CacheType;

@Service
class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentQueryRepository assessmentQueryRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final FormQueryRepository formQueryRepository;
    private final StrandQueryRepository strandQueryRepository;
    private final AccommodationsQueryRepository accommodationsQueryRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentQueryRepository assessmentQueryRepository,
                                 ItemQueryRepository itemQueryRepository,
                                 FormQueryRepository formQueryRepository,
                                 StrandQueryRepository strandQueryRepository,
                                 AccommodationsQueryRepository accommodationsQueryRepository) {
        this.assessmentQueryRepository = assessmentQueryRepository;
        this.itemQueryRepository = itemQueryRepository;
        this.formQueryRepository = formQueryRepository;
        this.strandQueryRepository = strandQueryRepository;
        this.accommodationsQueryRepository = accommodationsQueryRepository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
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

            if (assessment.getSegments().stream().anyMatch(s -> s.getSelectionAlgorithm().equals(Algorithm.FIXED_FORM))) {
                forms = formQueryRepository.findFormsForAssessment(assessmentKey);
            }

            AssessmentAssembler.assemble(assessment, strands, itemConstraints, itemProperties, items, forms, accommodationDependencies);
        }

        return maybeAssessment;
    }
}
