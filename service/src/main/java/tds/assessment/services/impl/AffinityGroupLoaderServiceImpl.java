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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.AffinityGroup;
import tds.assessment.model.itembank.AffinityGroupItem;
import tds.assessment.model.itembank.AffinityGroupItemIdentity;
import tds.assessment.repositories.loader.AffinityGroupItemRepository;
import tds.assessment.repositories.loader.AffinityGroupRepository;
import tds.assessment.services.AffinityGroupLoaderService;
import tds.testpackage.model.BlueprintElement;
import tds.testpackage.model.Property;
import tds.testpackage.model.TestPackage;

import static tds.assessment.model.BlueprintElementTypes.AFFINITY_GROUP;

@Service
public class AffinityGroupLoaderServiceImpl implements AffinityGroupLoaderService {
    private final AffinityGroupRepository affinityGroupRepository;
    private final AffinityGroupItemRepository affinityGroupItemRepository;

    @Autowired
    public AffinityGroupLoaderServiceImpl(final AffinityGroupRepository affinityGroupRepository,
                                          final AffinityGroupItemRepository affinityGroupItemRepository) {
        this.affinityGroupRepository = affinityGroupRepository;
        this.affinityGroupItemRepository = affinityGroupItemRepository;
    }

    @Override
    public void loadAffinityGroups(final TestPackage testPackage, final List<ItemMetadataWrapper> itemMetadataWrappers) {
        // No need for recursive searching since affinitygroups are always at the root level
        Map<String, BlueprintElement> affinityGroupBpElements = testPackage.getBlueprint().stream()
            .filter(bpElement -> bpElement.getType().equalsIgnoreCase(AFFINITY_GROUP))
            .collect(Collectors.toMap(BlueprintElement::getId, Function.identity()));

        // Get the affinity group blueprint elements from the blueprint, and map the item selection properties of the blueprint
        // to the affinity group
        List<AffinityGroup> affinityGroups = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> segment.segmentBlueprint().stream()
                    .filter(segBpElement -> affinityGroupBpElements.containsKey(segBpElement.getIdRef()))
                    .map(segBpElement -> {
                            final Map<String, String> itemSelectionProperties = segBpElement.itemSelection().stream()
                                .collect(Collectors.toMap(p -> p.getName().toLowerCase(), Property::getValue));

                            return new AffinityGroup.Builder(segment.getKey(), segBpElement.getIdRef())
                                .withMinItems(segBpElement.getMinExamItems())
                                .withMaxItems(segBpElement.getMaxExamItems())
                                // Begin optional properties with defaults
                                .withStrictMax(itemSelectionProperties.containsKey("isstrictmax") && Boolean.parseBoolean(itemSelectionProperties.get("isstrictmax")))
                                .withWeight(itemSelectionProperties.containsKey("bpweight")
                                    ? Float.parseFloat(itemSelectionProperties.get("bpweight"))
                                    : null)
                                .withStartAbility(itemSelectionProperties.containsKey("startability")
                                    ? Float.parseFloat(itemSelectionProperties.get("startability"))
                                    : null)
                                .withStartInfo(itemSelectionProperties.containsKey("startinfo")
                                    ? Float.parseFloat(itemSelectionProperties.get("startinfo"))
                                    : null)
                                .withAbilityWeight(itemSelectionProperties.containsKey("abilityweight")
                                    ? Float.parseFloat(itemSelectionProperties.get("abilityweight"))
                                    : null)
                                .withPrecisionTarget(itemSelectionProperties.containsKey("precisiontarget")
                                    ? Float.parseFloat(itemSelectionProperties.get("precisiontarget"))
                                    : null)
                                .withPrecisionTargetMetWeight(itemSelectionProperties.containsKey("precisiontargetmetweight")
                                    ? Float.parseFloat(itemSelectionProperties.get("precisiontargetmetweight"))
                                    : null)
                                .withPrecisionTargetNotMetWeight(itemSelectionProperties.containsKey("precisiontargetnotmetweight")
                                    ? Float.parseFloat(itemSelectionProperties.get("precisiontargetnotmetweight"))
                                    : null)
                                .withVersion(Long.parseLong(testPackage.getVersion()))
                                .withUpdatedVersion(Long.parseLong(testPackage.getVersion()))
                                .build();
                        }
                    )
                )
            )
            .collect(Collectors.toList());

        affinityGroupRepository.save(affinityGroups);

        // Map each item that belongs to an affinity group
        List<AffinityGroupItem> affinityGroupItems = itemMetadataWrappers.stream()
            .flatMap(wrapper -> wrapper.getItem().getBlueprintReferences().stream()
                .filter(ref -> affinityGroupBpElements.containsKey(ref.getIdRef()))
                .map(ref -> new AffinityGroupItem(
                    new AffinityGroupItemIdentity(wrapper.getSegmentKey(), ref.getIdRef(), wrapper.getItem().getKey())))
            )
            .collect(Collectors.toList());

        affinityGroupItemRepository.save(affinityGroupItems);
    }
}
