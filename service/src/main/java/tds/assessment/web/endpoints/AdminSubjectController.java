package tds.assessment.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.services.AdminSubjectService;
import tds.common.web.exceptions.NotFoundException;

/**
 * Controller handling admin subject related entities
 */
@RestController
@RequestMapping("/assessments/admin-subject")
class AdminSubjectController {
    private final AdminSubjectService service;

    @Autowired
    public AdminSubjectController(AdminSubjectService service) {
        this.service = service;
    }

    /**
     * Endpoint to find {@link tds.assessment.SetOfAdminSubject} by key
     *
     * @param key unique key for a {@link tds.assessment.SetOfAdminSubject}
     * @return {@link org.springframework.http.ResponseEntity} containing a {@link tds.assessment.SetOfAdminSubject}
     * @throws tds.common.web.exceptions.NotFoundException if entity cannot be found
     */
    @GetMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<SetOfAdminSubject> findSetOfAdminSubject(@PathVariable final String key) throws NotFoundException {
        final SetOfAdminSubject setOfAdminSubject = service.findSetOfAdminByKey(key)
            .orElseThrow(() -> new NotFoundException("Could not find set of admin subject for %s", key));

        return ResponseEntity.ok(setOfAdminSubject);
    }
}
