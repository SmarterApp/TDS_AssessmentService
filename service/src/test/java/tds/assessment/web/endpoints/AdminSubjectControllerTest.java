package tds.assessment.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.services.AdminSubjectService;
import tds.assessment.web.resources.SetOfAdminSubjectResource;
import tds.common.web.exceptions.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminSubjectControllerTest {
    private AdminSubjectController controller;
    private AdminSubjectService service;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        service = mock(AdminSubjectService.class);
        controller = new AdminSubjectController(service);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindAdminSubjectByKey() {
        SetOfAdminSubject subject = new SetOfAdminSubject("theKey", "assessment", false, "alg");

        when(service.findSetOfAdminObjectByKey("theKey")).thenReturn(Optional.of(subject));
        ResponseEntity<SetOfAdminSubjectResource> response = controller.findSetOfAdminSubject("theKey");
        verify(service).findSetOfAdminObjectByKey("theKey");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSetOfAdminSubject().getKey()).isEqualTo("theKey");
        assertThat(response.getBody().getId().getHref()).isEqualTo("http://localhost/assessments/admin-subject/theKey");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowSubjectByKeyCannotBeFound() {
        when(service.findSetOfAdminObjectByKey("theKey")).thenReturn(Optional.empty());
        controller.findSetOfAdminSubject("theKey");
    }
}
