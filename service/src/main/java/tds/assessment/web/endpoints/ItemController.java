package tds.assessment.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;
import tds.assessment.services.ItemService;
import tds.common.web.exceptions.NotFoundException;

/**
 * Controller handling item related entities
 */
@RestController
@RequestMapping("/assessments/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Endpoint to find the list of {@link tds.assessment.ItemConstraint}s for the assessment and client
     *
     * @param clientName    the client environment name
     * @param assessmentId  the assessment id of the assessment that the constraints belong to
     * @return {@link org.springframework.http.ResponseEntity} containing a list of  {@link tds.assessment.ItemConstraint}}
     * @throws tds.common.web.exceptions.NotFoundException if no results are found
     */
    @RequestMapping(value = "/constraints/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<ItemConstraint>> findItemConstraints(@PathVariable final String clientName,
                                                       @PathVariable final String assessmentId) throws NotFoundException {
        final List<ItemConstraint> itemConstraints = itemService.findItemConstraints(clientName, assessmentId);

        if (itemConstraints.isEmpty()) {
            throw new NotFoundException(String.format("Could not find item constraints for assessmentId {} and client name {}.",
                    assessmentId, clientName));
        }

        return ResponseEntity.ok(itemConstraints);
    }

    /**
     * Endpoint to find the list of {@link tds.assessment.ItemProperty} objects for the {@link tds.assessment.Segment}
     *
     * @param segmentKey  the segment key of the segment that the constraints belong to
     * @return {@link org.springframework.http.ResponseEntity} containing a list of  {@link tds.assessment.ItemProperty}}
     * @throws tds.common.web.exceptions.NotFoundException if no results are found
     */
    @RequestMapping(value = "/properties/{segmentKey}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<ItemProperty>> findItemProperties(@PathVariable final String segmentKey) throws NotFoundException {
        final List<ItemProperty> itemProperties = itemService.findItemProperties(segmentKey);

        if (itemProperties.isEmpty()) {
            throw new NotFoundException(String.format("Could not find item properties for segment key {}.",
                    segmentKey));
        }

        return ResponseEntity.ok(itemProperties);
    }
}

