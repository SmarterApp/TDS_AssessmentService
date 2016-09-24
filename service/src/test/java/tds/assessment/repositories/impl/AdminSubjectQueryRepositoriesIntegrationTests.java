package tds.assessment.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.repositories.AdminSubjectQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AdminSubjectQueryRepositoriesIntegrationTests {
    @Autowired
    private AdminSubjectQueryRepository adminSubjectQueryRepository;

    @Test
    public void shouldReturnEmptyAdminSubjectQueryOptionalWhenNotFound() {
        Optional<SetOfAdminSubject> maybeSetOfAdminSubject = adminSubjectQueryRepository.findByKey("BOGUS");
        assertThat(maybeSetOfAdminSubject).isNotPresent();
    }

    @Test
    public void shouldRetrieveSetOfAdminSubjectForKey() {
        Optional<SetOfAdminSubject> maybeSetOfAdminSubject = adminSubjectQueryRepository.findByKey("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(maybeSetOfAdminSubject).isPresent();
        SetOfAdminSubject adminSubject = maybeSetOfAdminSubject.get();
        assertThat(adminSubject.getKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(adminSubject.getAssessmentId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(adminSubject.getSelectionAlgorithm()).isEqualTo("virtual");
        assertThat(adminSubject.isSegmented()).isTrue();
    }

}
