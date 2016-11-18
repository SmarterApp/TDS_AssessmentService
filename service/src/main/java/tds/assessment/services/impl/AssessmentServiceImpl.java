package tds.assessment.services.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import tds.assessment.Assessment;
import tds.assessment.Form;
import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.Segment;
import tds.assessment.repositories.AssessmentQueryRepository;
import tds.assessment.repositories.FormQueryRepository;
import tds.assessment.repositories.ItemQueryRepository;
import tds.assessment.services.AssessmentService;

@Service
class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentQueryRepository assessmentQueryRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final FormQueryRepository formQueryRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentQueryRepository assessmentQueryRepository,
                                 ItemQueryRepository itemQueryRepository,
                                 FormQueryRepository formQueryRepository) {
        this.formQueryRepository = formQueryRepository;
        this.assessmentQueryRepository = assessmentQueryRepository;
        this.itemQueryRepository = itemQueryRepository;
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
            assessment.setItemConstraints(itemConstraints);

            assignPropertiesToItems(items, itemProperties);
            assignFormsAndItemsToAssessmentSegments(assessment, forms, items);
        }

        return maybeAssessment;
    }

    private static void assignFormsAndItemsToAssessmentSegments(Assessment assessment, List<Form> forms, List<Item> items) {
        for (Segment segment : assessment.getSegments()) {
            segment.setForms(forms.stream()
                .filter(form -> form.getSegmentKey().equals(segment.getKey())).collect(Collectors.toList()));
            segment.setItems(items.stream()
                .filter(item -> item.getSegmentKey().equals(segment.getKey())).collect(Collectors.toList()));
        }
    }

    private static void assignPropertiesToItems(List<Item> items, List<ItemProperty> itemProperties) {
        Multimap<String, ItemProperty> itemIdToPropertyMap = ArrayListMultimap.create();
        itemProperties.stream()
                .filter(property -> property.getItemId() != null)
                .forEach(property -> itemIdToPropertyMap.put(property.getItemId(), property));

        for (Item item : items) {
            item.setItemProperties(new ArrayList<>(itemIdToPropertyMap.get(item.getId())));
        }
    }
}
