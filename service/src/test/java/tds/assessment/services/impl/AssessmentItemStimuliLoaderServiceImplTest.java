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
import java.util.stream.Collectors;

import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.ItemContentLevel;
import tds.assessment.model.itembank.TblAdminItem;
import tds.assessment.model.itembank.TblAdminStimulus;
import tds.assessment.model.itembank.TblAdminStrand;
import tds.assessment.model.itembank.TblItemProperty;
import tds.assessment.model.itembank.TblSetOfItemStimulus;
import tds.assessment.model.itembank.TblSetOfItemStrand;
import tds.assessment.model.itembank.TblStrand;
import tds.assessment.repositories.loader.ItemContentLevelRepository;
import tds.assessment.repositories.loader.TblAdminStimuliRepository;
import tds.assessment.repositories.loader.TblAdminStrandsRepository;
import tds.assessment.repositories.loader.TblItemPropertiesRepository;
import tds.assessment.repositories.loader.TblSetOfAdminItemsRepository;
import tds.assessment.repositories.loader.TblSetOfItemStimuliRepository;
import tds.assessment.repositories.loader.TblSetOfItemStrandsRepository;
import tds.assessment.repositories.loader.TblStrandRepository;
import tds.assessment.services.AssessmentItemBankLoaderService;
import tds.assessment.services.AssessmentItemStimuliLoaderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentItemStimuliLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentItemStimuliLoaderService service;

    // Only used to get keyToStrands map - this service is tested separately
    private AssessmentItemBankLoaderService itemBankLoaderService;

    @Mock
    private TblAdminStrandsRepository tblAdminStrandsRepository;

    @Mock
    private TblAdminStimuliRepository tblAdminStimuliRepository;

    @Mock
    private ItemContentLevelRepository itemContentLevelRepository;

    @Mock
    private TblSetOfAdminItemsRepository tblSetOfAdminItemsRepository;

    @Mock
    private TblSetOfItemStrandsRepository tblSetOfItemStrandsRepository;

    @Mock
    private TblItemPropertiesRepository tblItemPropertiesRepository;

    @Mock
    private TblSetOfItemStimuliRepository tblSetOfItemStimuliRepository;

    @Mock
    private TblStrandRepository tblStrandRepository;

    @Captor
    private ArgumentCaptor<List<TblItemProperty>> propertiesArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<ItemContentLevel>> itemContentLevelArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TblSetOfItemStrand>> itemStrandArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TblAdminItem>> adminItemArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TblAdminStimulus>> adminStimuliArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TblAdminStrand>> adminStrandArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TblSetOfItemStimulus>> itemStimuliArgumentCaptor;

    private Map<String, TblStrand> keyToStrands;

    @Before
    public void setup() {
        service = new AssessmentItemStimuliLoaderServiceImpl(tblAdminStrandsRepository, tblAdminStimuliRepository,
            itemContentLevelRepository, tblSetOfAdminItemsRepository, tblSetOfItemStrandsRepository, tblItemPropertiesRepository, tblSetOfItemStimuliRepository);

        itemBankLoaderService = new AssessmentItemBankLoaderServiceImpl(null, null, null, tblStrandRepository);
        keyToStrands = itemBankLoaderService.loadStrands(mockTestPackage.getBlueprint(),
            "SBAC_PT-MATH", new Client(0, "SBAC_PT", null), "8185");
    }

    @Test
    public void shouldLoadItemProperties() {
        service.loadItemProperties(Lists.newArrayList(mockItemMetadataMap.values()));

        verify(tblItemPropertiesRepository).save(propertiesArgumentCaptor.capture());

        List<TblItemProperty> savedProperties = propertiesArgumentCaptor.getValue();
        // 3 properties per item - 20 items
        assertThat(savedProperties).hasSize(60);

        List<TblItemProperty> itemProperties = savedProperties.stream()
            .filter(prop -> prop.getTblItemPropertyIdentity().getItemId().equals("187-2029"))
            .collect(Collectors.toList());

        assertThat(itemProperties).hasSize(3);

        // Language property
        TblItemProperty languageProp = itemProperties.stream()
            .filter(prop -> prop.getTblItemPropertyIdentity().getName().equals("Language"))
            .findFirst().get();

        assertThat(languageProp.getTblItemPropertyIdentity().getValue()).isEqualTo("ENU");
        assertThat(languageProp.getTblItemPropertyIdentity().getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018");
    }

    @Test
    public void shouldLoadAdminItems() {
        service.loadAdminItems(mockTestPackage, Lists.newArrayList(mockItemMetadataMap.values()), keyToStrands);

        verify(tblSetOfAdminItemsRepository).save(adminItemArgumentCaptor.capture());

        List<TblAdminItem> savedAdminItems = adminItemArgumentCaptor.getValue();
        assertThat(savedAdminItems).hasSize(20);

        TblAdminItem singleItem = savedAdminItems.stream()
            .filter(item -> item.getTblAdminItemIdentifier().getItemId().equals("187-2029"))
            .findFirst().get();

        assertThat(singleItem.getGroupKey()).isEqualTo("I-187-2029_A");
        assertThat(singleItem.getGroupId()).isEqualTo("I-187-2029");
        assertThat(singleItem.getItemPosition()).isEqualTo(1);
        assertThat(singleItem.isFieldTest()).isFalse();
        assertThat(singleItem.isActive()).isTrue();
        assertThat(singleItem.getIrtB()).isEqualTo("0.678030000000000");
        assertThat(singleItem.getBlockId()).isEqualTo("A");
        assertThat(singleItem.isRequired()).isTrue();
        assertThat(singleItem.isPrintable()).isFalse();
        assertThat(singleItem.getClientName()).isEqualTo("SBAC_PT");
        assertThat(singleItem.getResponseMimeType()).isEqualTo("text/plain");
        assertThat(singleItem.getTestCohort()).isEqualTo(1);
        assertThat(singleItem.getStrandKey()).isEqualTo("SBAC_PT-3|G-SRT|A|NA|NA");
        assertThat(singleItem.getVersion()).isEqualTo(8185);
        assertThat(singleItem.getClaimName()).isEqualTo("SBAC_PT-3");
        assertThat(singleItem.getIrtA()).isEqualTo(.53279f);
        assertThat(singleItem.getIrtC()).isEqualTo(0f);
        assertThat(singleItem.getIrtModel()).isEqualTo("IRT3PLn");
        assertThat(singleItem.getbVector()).isEqualTo("0.678030000000000");
        assertThat(singleItem.getClString()).isEqualTo("G11Math_DOK2;SBAC_PT-3|G-SRT|A|NA|NA");
        assertThat(singleItem.getFtWeight()).isEqualTo(1);

        TblAdminItem groupedItem = savedAdminItems.stream()
            .filter(item -> item.getTblAdminItemIdentifier().getItemId().equals("187-1432"))
            .findFirst().get();

        assertThat(groupedItem.getGroupKey()).isEqualTo("G-187-3688-0_A");
        assertThat(groupedItem.getGroupId()).isEqualTo("G-187-3688-0");
        assertThat(groupedItem.getItemPosition()).isEqualTo(2);
        assertThat(groupedItem.getbVector()).isEqualTo("-0.401550000000000;0.762370000000000");
    }

    @Test
    public void shouldLinkItemsToStrands() {
        service.loadLinkItemsToStrands(Lists.newArrayList(mockItemMetadataMap.values()), keyToStrands, 8185L);

        verify(itemContentLevelRepository).save(itemContentLevelArgumentCaptor.capture());

        List<ItemContentLevel> savedItemContentLevels = itemContentLevelArgumentCaptor.getValue();
        // Every referenced claim and target
        assertThat(savedItemContentLevels).hasSize(108);

        List<ItemContentLevel> itemContentLevelsForItem = savedItemContentLevels.stream()
            .filter(level -> level.getItemContentLevelIdentity().getItemId().equals("187-2029"))
            .collect(Collectors.toList());

        assertThat(itemContentLevelsForItem).hasSize(6);

        itemContentLevelsForItem.forEach(level ->
            assertThat(level.getItemContentLevelIdentity().getSegmentKey().equalsIgnoreCase("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018")));

        verify(tblSetOfItemStrandsRepository).save(itemStrandArgumentCaptor.capture());

        List<TblSetOfItemStrand> savedItemStrands = itemStrandArgumentCaptor.getValue();
        assertThat(savedItemStrands).hasSize(20); //20 items in total

        TblSetOfItemStrand savedItemStrand = savedItemStrands.stream()
            .filter(itemStrand -> itemStrand.getTblSetOfItemStrandIdentity().getItemId().equals("187-2029"))
            .findFirst().get();

        assertThat(savedItemStrand.getTblSetOfItemStrandIdentity().getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018");
        assertThat(savedItemStrand.getTblSetOfItemStrandIdentity().getStrandKey()).isEqualTo("SBAC_PT-3|G-SRT|A|NA|NA"); // leaf element
    }

    @Test
    public void shouldLoadAdminStimuli() {
        service.loadAdminStimuli(mockTestPackage);

        verify(tblAdminStimuliRepository).save(adminStimuliArgumentCaptor.capture());

        List<TblAdminStimulus> adminStimuli = adminStimuliArgumentCaptor.getValue();

        assertThat(adminStimuli).hasSize(1);

        TblAdminStimulus stimulus = adminStimuli.get(0);
        assertThat(stimulus.getTblAdminStimulusIdentifier().getStimulusKey()).isEqualTo("187-3688");
        assertThat(stimulus.getTblAdminStimulusIdentifier().getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-Perf-MATH-11-2017-2018");
        assertThat(stimulus.getNumItemsRequired()).isEqualTo(-1);
        assertThat(stimulus.getMaxItems()).isEqualTo(-1);
        assertThat(stimulus.getVersion()).isEqualTo(8185);
        assertThat(stimulus.getGroupId()).isEqualTo("G-187-3688-0");
    }

    @Test
    public void shouldLoadAdminStrands() {
        service.loadAdminStrands(mockTestPackage, keyToStrands);

        verify(tblAdminStrandsRepository).save(adminStrandArgumentCaptor.capture());
        List<TblAdminStrand> savedAdminStrands = adminStrandArgumentCaptor.getValue();

        assertThat(savedAdminStrands).hasSize(62);

        TblAdminStrand savedContentLevel = savedAdminStrands.stream()
            .filter(strand -> strand.getStrandKey().equals("SBAC_PT-1|F-IF|K|m|F-IF.1"))
            .findFirst().get();

        assertThat(savedContentLevel.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018");
        assertThat(savedContentLevel.getMinItems()).isEqualTo(0);
        assertThat(savedContentLevel.getMaxItems()).isEqualTo(1);
        assertThat(savedContentLevel.getAdaptiveCut()).isNull();
        assertThat(savedContentLevel.getBpWeight()).isEqualTo(1.0f);
        assertThat(savedContentLevel.getVersion()).isEqualTo(8185);

        TblAdminStrand savedStrand = savedAdminStrands.stream()
            .filter(strand -> strand.getStrandKey().equals("SBAC_PT-1"))
            .findFirst().get();

        assertThat(savedStrand.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-CAT-MATH-11-2017-2018");
        assertThat(savedStrand.getKey()).isEqualTo(savedStrand.getSegmentKey() + "-" + savedStrand.getStrandKey());
        assertThat(savedStrand.getMinItems()).isEqualTo(4);
        assertThat(savedStrand.getMaxItems()).isEqualTo(4);
        assertThat(savedStrand.getAdaptiveCut()).isEqualTo(-31.7137f);
        assertThat(savedStrand.getStartAbility()).isEqualTo(-31.7137f);
        assertThat(savedStrand.getStartInfo()).isEqualTo(0);
        assertThat(savedStrand.getScalar()).isEqualTo(5.0f);
        assertThat(savedStrand.getBpWeight()).isEqualTo(1.0f);
        assertThat(savedStrand.getVersion()).isEqualTo(8185);
        assertThat(savedStrand.getLoadMin()).isNull();
        assertThat(savedStrand.getLoadMax()).isNull();
    }

    @Test
    public void shouldLinkItemsToStimuli() {
        service.loadLinkItemsToStimuli(mockTestPackage);

        verify(tblSetOfItemStimuliRepository).save(itemStimuliArgumentCaptor.capture());
        List<TblSetOfItemStimulus> savedItemStimuli = itemStimuliArgumentCaptor.getValue();

        assertThat(savedItemStimuli).hasSize(2);
        TblSetOfItemStimulus item1434 = savedItemStimuli.stream()
            .filter(itemStim -> itemStim.getTblSetOfItemStimulusIdentity().getItemKey().equals("187-1434"))
            .findFirst().get();

        assertThat(item1434.getTblSetOfItemStimulusIdentity().getStimulusKey()).isEqualTo("187-3688");
        assertThat(item1434.getTblSetOfItemStimulusIdentity().getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-Perf-MATH-11-2017-2018");

        TblSetOfItemStimulus item1432 = savedItemStimuli.stream()
            .filter(itemStim -> itemStim.getTblSetOfItemStimulusIdentity().getItemKey().equals("187-1432"))
            .findFirst().get();

        assertThat(item1432.getTblSetOfItemStimulusIdentity().getStimulusKey()).isEqualTo("187-3688");
        assertThat(item1432.getTblSetOfItemStimulusIdentity().getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-IRP-Perf-MATH-11-2017-2018");
    }
}
