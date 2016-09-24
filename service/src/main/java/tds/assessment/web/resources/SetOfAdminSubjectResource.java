package tds.assessment.web.resources;

import org.springframework.hateoas.ResourceSupport;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.web.endpoints.AdminSubjectController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HATEOAS resource representing a {@link tds.assessment.SetOfAdminSubject}
 */
public class SetOfAdminSubjectResource extends ResourceSupport {
    private final SetOfAdminSubject setOfAdminSubject;

    public SetOfAdminSubjectResource(SetOfAdminSubject setOfAdminSubject) {
        this.setOfAdminSubject = setOfAdminSubject;
        this.add(linkTo(
            methodOn(AdminSubjectController.class)
                .findSetOfAdminSubject(setOfAdminSubject.getKey()))
            .withSelfRel());

    }

    /**
     * @return {@link tds.assessment.SetOfAdminSubject}
     */
    public SetOfAdminSubject getSetOfAdminSubject() {
        return setOfAdminSubject;
    }
}
