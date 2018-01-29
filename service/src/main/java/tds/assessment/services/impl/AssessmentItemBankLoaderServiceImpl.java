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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.ItemMetadataWrapper;
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
import tds.common.Algorithm;
import tds.testpackage.model.BlueprintElement;
import tds.testpackage.model.Item;
import tds.testpackage.model.Stimulus;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentItemBankLoaderServiceImpl implements AssessmentItemBankLoaderService {
    private final static Set<String> CLAIMS_AND_TARGET_TYPES =
        ImmutableSet.of("claim", "target", "strand", "contentlevel");
    private final TblSubjectRepository tblSubjectRepository;
    private final TblItemRepository tblItemRepository;
    private final TblStimuliRepository tblStimuliRepository;
    private final TblStrandRepository tblStrandRepository;

    @Autowired
    public AssessmentItemBankLoaderServiceImpl(final TblSubjectRepository tblSubjectRepository,
                                               final TblItemRepository tblItemRepository,
                                               final TblStimuliRepository tblStimuliRepository,
                                               final TblStrandRepository tblStrandRepository) {
        this.tblSubjectRepository = tblSubjectRepository;
        this.tblStimuliRepository = tblStimuliRepository;
        this.tblItemRepository = tblItemRepository;
        this.tblStrandRepository = tblStrandRepository;

    }

    @Override
    public void loadSubject(final TestPackage testPackage, final Client client, final String subjectKey) {
        final TblSubject tblSubject = new TblSubject(testPackage.getSubject(), subjectKey, client.getKey(), Long.parseLong(testPackage.getVersion()));
        tblSubjectRepository.save(tblSubject);
    }

    @Override
    public void loadTblStimuli(final TestPackage testPackage) {
        final int bankKey = testPackage.getBankKey();
        final String version = testPackage.getVersion();

        List<TblStimulus> stimuli = testPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                if (segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType())) {
                    return segment.segmentForms().stream()
                        .flatMap(form -> form.itemGroups().stream()
                            .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                            .map(itemGroup -> itemGroup.getStimulus())
                            .map(stimulus -> mapStimuliToTblStimuli(bankKey, version, stimulus)));

                } else if (segment.getAlgorithmType().contains("adaptive")) {
                    return segment.pool().stream()
                        .filter(itemGroup -> itemGroup.getStimulus().isPresent())
                        .map(itemGroup -> itemGroup.getStimulus())
                        .map(stimulus -> mapStimuliToTblStimuli(bankKey, version, stimulus));
                } else {
                    throw new TestPackageLoaderException("Unrecognized selection algorithm");
                }
            })
        ).collect(Collectors.toList());

        tblStimuliRepository.save(stimuli);
    }


    @Override
    public Map<String, TblStrand> loadStrands(final List<BlueprintElement> blueprintElements, final String subjectKey,
                                              final Client client, final String version) {
        final List<TblStrand> tblStrands = new ArrayList<>();
        final int treeLevel = 1;
        // Begin the recursive call with null parent and
        loadBlueprintElementsHelper(blueprintElements, tblStrands, client, null, subjectKey, version, treeLevel);
        tblStrandRepository.save(tblStrands);

        return tblStrands.stream()
            .collect(Collectors.toMap(TblStrand::getName, Function.identity()));
    }

    @Override
    public void loadTblItems(final TestPackage testPackage, final List<ItemMetadataWrapper> itemMetadataWrappers) {
        List<TblItem> items = itemMetadataWrappers.stream()
            .map(itemWrapper -> mapItemToTblItem(testPackage.getBankKey(), testPackage.getVersion(), itemWrapper.getItem()))
            .collect(Collectors.toList());

        tblItemRepository.save(items);
    }

    private List<TblStrand> loadBlueprintElementsHelper(final List<BlueprintElement> blueprintElements,
                                                        final List<TblStrand> tblStrands,
                                                        final Client client,
                                                        final String parentKey,
                                                        final String subjectKey,
                                                        final String version,
                                                        final int treeLevel) {
        /**
         * BIG TODO:
         *  The code below assumes each blueprint element id will contain it's hierarchy embedded, as is the case in legacy.
         *  We are still waiting on feedback from SmarterBalanced, PCG, and AIR on what these blueprint elements should actually
         *  look like.
         *
         *  We need to check whether the content level/target names will contain the entire hierarchy embedded into each string.
         *  For example, in the legacy test specification packages, the strand with the key "SBAC_PT-4|OA|D|NA|NA" contains the
         *  parent keys of its hierarchy embedded within the key (delimited by pipes '|'). If this is not the case, and we should infer the
         *  string based on the name of the parent keys programmatically, the logic below will need to be updated
         */

        // Recursively create the tblStrands we will insert into the database
        for (BlueprintElement bpElement : blueprintElements) {
            // For claims and targets, the convention is to prepend the client name to the id
            final String key = CLAIMS_AND_TARGET_TYPES.contains(bpElement.getType())
                ? client.getName() + '-' + bpElement.getId()
                : bpElement.getId();

            // Leaf node - Let's create the strand out of this blueprint element and add it to the list we will persist
            if (bpElement.blueprintElements().size() == 0) {
                TblStrand tblStrand = new TblStrand.Builder()
                    .withName(bpElement.getId())
                    .withParentKey(parentKey)
                    .withKey(key)
                    .withClientKey(client.getKey())
                    .withTreeLevel(treeLevel)
                    .withVersion(Long.parseLong(version))
                    .withType(bpElement.getType())
                    .withSubjectKey(subjectKey)
                    // this is important - we want to flag this as a "leaf" target node for use later in the loading process
                    .withLeafTarget(true)
                    .build();
                tblStrands.add(tblStrand);
            } else {
                TblStrand tblStrand = new TblStrand.Builder()
                    .withName(bpElement.getId())
                    .withParentKey(parentKey)
                    .withKey(key)
                    .withClientKey(client.getKey())
                    .withTreeLevel(treeLevel)
                    .withVersion(Long.parseLong(version))
                    .withType(bpElement.getType())
                    .withSubjectKey(subjectKey)
                    .withLeafTarget(false)
                    .build();
                tblStrands.add(tblStrand);

                // Recursively load each blueprint element
                loadBlueprintElementsHelper(bpElement.blueprintElements(),
                    tblStrands, client, key, subjectKey, version, treeLevel + 1);
            }
        }

        return tblStrands;
    }

    private static TblItem mapItemToTblItem(final int bankKey, final String version, final Item item) {
        final String fileName = String.format("item-%s-%s.xml", bankKey, item.getId());
        final String filePath = String.format("item-%s-%s/", bankKey, item.getId());

        return new TblItem.Builder()
            .withId(Integer.parseInt(item.getId()))
            .withItemType(item.getType())
            .withBankKey(bankKey)
            .withVersion(Long.parseLong(version))
            .withScorePoints(item.getItemScoreDimension().getScorePoints())
            .withFileName(fileName)
            .withFilePath(filePath)
            .build();
    }

    private static TblStimulus mapStimuliToTblStimuli(final int bankKey, final String version, final Optional<Stimulus> stimulus) {
        final String fileName = String.format("stim-%s-%s.xml", bankKey, stimulus.get().getId());
        final String filePath = String.format("stim-%s-%s/", bankKey, stimulus.get().getId());
        return new TblStimulus.Builder()
            .withKey(Integer.parseInt(stimulus.get().getId()))
            .withBankKey(bankKey)
            .withVersion(Long.parseLong(version))
            .withFileName(fileName)
            .withFilePath(filePath)
            .build();
    }
}
