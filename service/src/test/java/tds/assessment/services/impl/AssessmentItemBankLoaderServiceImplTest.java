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

package tds.assessment.services.impl;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.TblItem;
import tds.assessment.model.itembank.TblStimulus;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.model.itembank.TblSubject;
import tds.assessment.repositories.loader.TblItemRepository;
import tds.assessment.repositories.loader.TblStimuliRepository;
import tds.assessment.repositories.loader.TblStrandRepository;
import tds.assessment.repositories.loader.TblSubjectRepository;
import tds.assessment.services.AssessmentItemBankLoaderService;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentItemBankLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentItemBankLoaderService service;

    @Mock
    private TblSubjectRepository tblSubjectRepository;

    @Mock
    private TblItemRepository tblItemRepository;

    @Mock
    private TblStimuliRepository tblStimuliRepository;

    @Mock
    private TblStrandRepository tblStrandRepository;

    @Captor
    private ArgumentCaptor<List<TblStrand>> tblStrandsArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TblStimulus>> tblStimuliArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TblItem>> tblItemsArgumentCaptor;

    @Before
    public void setup() {
        service = new AssessmentItemBankLoaderServiceImpl(tblSubjectRepository, tblItemRepository,
            tblStimuliRepository, tblStrandRepository);
    }

    @Test
    public void shouldLoadSubject() {
        final Client client = random(Client.class);
        final String subjectKey = "SBAC_PT-ELA";
        service.loadSubject(mockTestPackage, client, subjectKey);

        ArgumentCaptor<TblSubject> tblSubjectArgumentCaptor = ArgumentCaptor.forClass(TblSubject.class);
        verify(tblSubjectRepository).save(tblSubjectArgumentCaptor.capture());

        TblSubject savedSubject = tblSubjectArgumentCaptor.getValue();
        assertThat(savedSubject).isNotNull();
        assertThat(savedSubject.getClientKey()).isEqualTo(client.getKey());
        assertThat(savedSubject.getKey()).isEqualTo(subjectKey);
        assertThat(savedSubject.getName()).isEqualTo(mockTestPackage.getSubject());
    }

    @Test
    public void shouldLoadStrands() {
        final Client client = new Client(1, "SBAC_PT", Client.DEFAULT_HOME_PATH);
        final String subjectKey = "SBAC_PT-ELA";
        Map<String, TblStrand> tblStrandMap = service.loadStrands(mockTestPackage.getBlueprint(), subjectKey,
            client, mockTestPackage.getVersion());

        assertThat(tblStrandMap.size()).isEqualTo(77); // 77 nested BP elements in the exampleTestPackage
        verify(tblStrandRepository).save(tblStrandsArgumentCaptor.capture());

        assertThat(tblStrandsArgumentCaptor.getValue().size()).isEqualTo(77);

        TblStrand leafLevelTarget = tblStrandMap.get("1|F-IF|K|m|F-IF.1");
        assertThat(leafLevelTarget).isNotNull();
        assertThat(leafLevelTarget.getKey()).isEqualTo("SBAC_PT-1|F-IF|K|m|F-IF.1");
        assertThat(leafLevelTarget.isLeafTarget()).isTrue();
        assertThat(leafLevelTarget.getClientKey()).isEqualTo(client.getKey());
        assertThat(leafLevelTarget.getType()).isEqualTo("contentlevel");
        assertThat(leafLevelTarget.getParentKey()).isEqualTo("SBAC_PT-1|F-IF|K|m");
        assertThat(leafLevelTarget.getTreeLevel()).isEqualTo(5);
        assertThat(leafLevelTarget.getSubjectKey()).isEqualTo(subjectKey);

        TblStrand claim = tblStrandMap.get("1");
        assertThat(claim).isNotNull();
        assertThat(claim.getKey()).isEqualTo("SBAC_PT-1");
        assertThat(claim.isLeafTarget()).isFalse();
        assertThat(claim.getClientKey()).isEqualTo(client.getKey());
        assertThat(claim.getType()).isEqualTo("strand");
        assertThat(claim.getParentKey()).isNull();
        assertThat(claim.getTreeLevel()).isEqualTo(1);
        assertThat(claim.getSubjectKey()).isEqualTo(subjectKey);
    }

    @Test
    public void shouldLoadTblStimuli() {
        service.loadTblStimuli(mockTestPackage);
        verify(tblStimuliRepository).save(tblStimuliArgumentCaptor.capture());

        List<TblStimulus> savedTblStimulus = tblStimuliArgumentCaptor.getValue();

        assertThat(savedTblStimulus.size()).isEqualTo(1);
        TblStimulus retTblStimulus = savedTblStimulus.get(0);

        assertThat(retTblStimulus.getBankKey()).isEqualTo(187);
        assertThat(retTblStimulus.getKey()).isEqualTo(3688);
        assertThat(retTblStimulus.getId()).isEqualTo("187-3688");
        assertThat(retTblStimulus.getFilePath()).isEqualTo("stim-187-3688/");
        assertThat(retTblStimulus.getFileName()).isEqualTo("stim-187-3688.xml");
        assertThat(retTblStimulus.getVersion()).isEqualTo(Long.parseLong(mockTestPackage.getVersion()));
    }

    @Test
    public void shouldLoadTblItem() {
        service.loadTblItems(mockTestPackage, Lists.newArrayList(mockItemMetadataMap.values()));
        verify(tblItemRepository).save(tblItemsArgumentCaptor.capture());

        List<TblItem> savedTblitems = tblItemsArgumentCaptor.getValue();
        assertThat(savedTblitems).hasSize(20);

        TblItem savedItem = savedTblitems.stream()
            .filter(item -> item.getId() == 2029)
            .findFirst().get();

        assertThat(savedItem.getBankKey()).isEqualTo(187);
        assertThat(savedItem.getKey()).isEqualTo("187-2029");
        assertThat(savedItem.getItemType()).isEqualTo("GI");
        assertThat(savedItem.getScorePoints()).isEqualTo(1);
        assertThat(savedItem.getFilePath()).isEqualTo("item-187-2029/");
        assertThat(savedItem.getFileName()).isEqualTo("item-187-2029.xml");
        assertThat(savedItem.getVersion()).isEqualTo(8185);
    }
}
