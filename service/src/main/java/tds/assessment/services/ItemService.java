package tds.assessment.services;

import java.util.List;

import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;

/**
 * Service for reading item related data data.
 */
public interface ItemService {
    /**
     * Retrieves the list of item constraints for the specified accommodations
     *
     * @param clientName    the client name for the installation
     * @param assessmentId  the {@link tds.assessment.Assessment}s id
     * @return  The collection of {@link tds.assessment.ItemConstraint} objects
     */
    List<ItemConstraint> findItemConstraints(String clientName, String assessmentId);

    /**
     * Retrieves the list of item properties for the specified assessment
     *
     * @param assessmentKey the {@link tds.assessment.Assessment}s id
     * @return  the collection of {@link tds.assessment.ItemProperty} objects
     */
    List<ItemProperty> findItemProperties(String assessmentKey);
}
