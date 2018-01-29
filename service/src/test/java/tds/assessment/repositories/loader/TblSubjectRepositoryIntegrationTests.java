/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.assessment.repositories.loader;

import com.google.common.collect.Lists;
import org.hibernate.engine.spi.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import tds.assessment.model.itembank.TblSubject;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
@EnableTransactionManagement
@ContextConfiguration(classes = {PersistenceContext.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class})
public class TblSubjectRepositoryIntegrationTests {
    @Autowired
    private TblSubjectRepository tblSubjectRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void shouldSaveSubject() {
        TblSubject subject = random(TblSubject.class);
//        entityManager.getTransaction().begin();
        TblSubject retSubject = tblSubjectRepository.save(subject);
        assertThat(retSubject).isNotNull();
//        tblSubjectRepository.flush();
        entityManager.flush();
        entityManager.clear();

        List<TblSubject> retSubjects =  Lists.newArrayList(tblSubjectRepository.findAll());
        assertThat(retSubjects.size()).isEqualTo(1);
        assertThat(retSubjects.get(0)).isEqualTo(subject);
    }

    @Test
    public void shouldSaveSubject2() {
        TblSubject subject = random(TblSubject.class);
        entityManager.getTransaction().begin();
        entityManager.persist(subject);
        entityManager.flush();
        entityManager.getTransaction().commit();
        List<TblSubject> retSubjects =  Lists.newArrayList(tblSubjectRepository.findAll());
        assertThat(retSubjects.size()).isEqualTo(1);
        assertThat(retSubjects.get(0)).isEqualTo(subject);
    }
}
