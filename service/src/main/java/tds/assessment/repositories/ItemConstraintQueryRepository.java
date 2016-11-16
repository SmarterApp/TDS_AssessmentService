package tds.assessment.repositories;

import java.util.List;
import java.util.Map;

import tds.assessment.ItemConstraint;

/**
 * Repository for retrieving {@link tds.assessment.ItemConstraint} data.
 */
public interface ItemConstraintQueryRepository {

    /**
     * Gets the list of item constraints for the specified accommodations
     *
     * @param clientName    the client name for the installation
     * @param assessmentId  the {@link tds.assessment.Assessment}s id
     * @return  The list of item constraints for this exam
     */
    List<ItemConstraint> findItemConstraints(String clientName, String assessmentId);
}
