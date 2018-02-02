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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import tds.assessment.model.itembank.AffinityGroup;
import tds.assessment.model.itembank.AffinityGroupItem;
import tds.assessment.repositories.loader.AffinityGroupItemRepository;
import tds.assessment.repositories.loader.AffinityGroupRepository;
import tds.assessment.services.AffinityGroupLoaderService;
import tds.testpackage.model.Item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AffinityGroupLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private static final Set<String> affinityGroupIds = ImmutableSet.of("G11Math_DOK2", "G11Math_Claim2/4", "G11Math_Claim1_MC/MS");

    private AffinityGroupLoaderService service;

    @Mock
    private AffinityGroupRepository affinityGroupRepository;

    @Mock
    private AffinityGroupItemRepository affinityGroupItemRepository;

    @Captor
    private ArgumentCaptor<List<AffinityGroup>> affinityGroupArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<AffinityGroupItem>> affinityGroupItemArgumentCaptor;

    @Before
    public void setup() {
        service = new AffinityGroupLoaderServiceImpl(affinityGroupRepository, affinityGroupItemRepository);
    }

    @Test
    public void shouldLoadAffinityGroup() {
        service.loadAffinityGroups(mockTestPackage, Lists.newArrayList(mockItemMetadataMap.values()));

        verify(affinityGroupRepository).save(affinityGroupArgumentCaptor.capture());
        verify(affinityGroupItemRepository).save(affinityGroupItemArgumentCaptor.capture());
        List<AffinityGroup> savedAffinityGroups = affinityGroupArgumentCaptor.getValue();

        assertThat(savedAffinityGroups).hasSize(3);

        savedAffinityGroups.forEach(affinityGroup -> assertThat(affinityGroup.getAffinityGroupId().getGroupId()).isIn(affinityGroupIds));

        AffinityGroup dok2AffinityGroup = savedAffinityGroups.stream()
            .filter(group -> group.getAffinityGroupId().getGroupId().equalsIgnoreCase("G11Math_DOK2"))
            .findFirst().get();

        assertThat(dok2AffinityGroup.getAbilityWeight()).isNull();
        assertThat(dok2AffinityGroup.getMinItems()).isEqualTo(2);
        assertThat(dok2AffinityGroup.getMaxItems()).isEqualTo(8);
        assertThat(dok2AffinityGroup.isStrictMax()).isFalse();
        assertThat(dok2AffinityGroup.getWeight()).isEqualTo(1);
        assertThat(dok2AffinityGroup.getAffinityGroupId().getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018");

        List<AffinityGroupItem> savedAffinityGroupItems = affinityGroupItemArgumentCaptor.getValue();

        assertThat(savedAffinityGroupItems).hasSize(18);

        AffinityGroupItem savedItem = savedAffinityGroupItems.stream()
            .filter(item -> item.getAffinityGroupItemIdentity().getItemId().equalsIgnoreCase("187-2029"))
            .findFirst().get();

        assertThat(savedItem.getAffinityGroupItemIdentity().getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018");
        assertThat(savedItem.getAffinityGroupItemIdentity().getAffinityGroupId()).isEqualTo("G11Math_DOK2");
    }

}
