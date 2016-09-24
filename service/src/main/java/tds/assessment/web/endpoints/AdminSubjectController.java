package tds.assessment.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.services.AdminSubjectService;
import tds.assessment.web.resources.SetOfAdminSubjectResource;
import tds.common.web.exceptions.NotFoundException;

/**
 * Controller handling admin subject related entities
 */
@RestController
@RequestMapping("/assessments/admin-subject")
public class AdminSubjectController {
    private final AdminSubjectService service;

    @Autowired
    public AdminSubjectController(AdminSubjectService service) {
        this.service = service;
    }

    /**
     * Endpoint to find {@link tds.assessment.web.resources.SetOfAdminSubjectResource} by key
     *
     * @param key unique key for a {@link tds.assessment.SetOfAdminSubject}
     * @return {@link org.springframework.http.ResponseEntity} containing a {@link tds.assessment.web.resources.SetOfAdminSubjectResource}
     * @throws tds.common.web.exceptions.NotFoundException if entity cannot be found
     */
    @RequestMapping(value = "/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SetOfAdminSubjectResource> findSetOfAdminSubject(@PathVariable final String key) throws NotFoundException {
        final SetOfAdminSubject setOfAdminSubject = service.findSetOfAdminObjectByKey(key)
            .orElseThrow(() -> new NotFoundException("Could not find set of admin subject for %s", key));

        return ResponseEntity.ok(new SetOfAdminSubjectResource(setOfAdminSubject));
    }

}
