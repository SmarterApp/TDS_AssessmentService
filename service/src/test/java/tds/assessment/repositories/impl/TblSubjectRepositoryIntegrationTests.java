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

package tds.assessment.repositories.impl;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import tds.assessment.model.itembank.TblSubject;
import tds.assessment.repositories.loader.TblSubjectRepository;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class TblSubjectRepositoryIntegrationTests {
    @Autowired
    private TblSubjectRepository tblSubjectRepository;

    @Test
    public void shouldSaveSubject() {
        TblSubject subject = random(TblSubject.class);
        tblSubjectRepository.save(subject);

        List<TblSubject> retSubjects =  Lists.newArrayList(tblSubjectRepository.findAll());
        assertThat(retSubjects.size()).isEqualTo(1);
        assertThat(retSubjects.get(0)).isEqualTo(subject);
    }
}
