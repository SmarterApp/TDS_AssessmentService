package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.repositories.ItemConstraintQueryRepository;
import tds.assessment.repositories.ItemPropertyQueryRepository;
import tds.assessment.services.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemConstraintQueryRepository itemConstraintQueryRepository;
    private final ItemPropertyQueryRepository itemPropertyQueryRepository;

    @Autowired
    public ItemServiceImpl(ItemConstraintQueryRepository itemConstraintQueryRepository,
                           ItemPropertyQueryRepository itemPropertyQueryRepository) {
        this.itemConstraintQueryRepository = itemConstraintQueryRepository;
        this.itemPropertyQueryRepository = itemPropertyQueryRepository;
    }

    @Override
    public List<ItemConstraint> findItemConstraints(String clientName, String assessmentId) {
        return itemConstraintQueryRepository.findItemConstraints(clientName, assessmentId);
    }

    @Override
    public List<ItemProperty> findItemProperties(String segmentKey) {
        return itemPropertyQueryRepository.findActiveItemsProperties(segmentKey);
    }
}
