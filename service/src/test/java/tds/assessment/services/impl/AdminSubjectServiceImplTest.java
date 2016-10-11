package tds.assessment.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.repositories.AdminSubjectQueryRepository;
import tds.assessment.services.AdminSubjectService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminSubjectServiceImplTest {
    private AdminSubjectQueryRepository queryRepository;
    private AdminSubjectService service;

    @Before
    public void setUp() {
        queryRepository = mock(AdminSubjectQueryRepository.class);
        service = new AdminSubjectServiceImpl(queryRepository);
    }

    @After
    public void tearDown() {}

    @Test
    public void shouldReturnSetOfAdminSubjects() {
        SetOfAdminSubject adminSubject = new SetOfAdminSubject("theKey", "assessment", false, "dynamic", 50F);

        when(queryRepository.findByKey("theKey")).thenReturn(Optional.of(adminSubject));
        Optional<SetOfAdminSubject> maybeSetOfAdminSubject = service.findSetOfAdminByKey("theKey");
        verify(queryRepository).findByKey("theKey");

        assertThat(maybeSetOfAdminSubject.get()).isEqualTo(adminSubject);
    }
}
