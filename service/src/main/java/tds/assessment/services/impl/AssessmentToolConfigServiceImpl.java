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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.configs.Tool;
import tds.assessment.model.configs.ToolDependency;
import tds.assessment.model.configs.ToolType;
import tds.assessment.repositories.loader.configs.ToolDependenciesRepository;
import tds.assessment.repositories.loader.configs.ToolRepository;
import tds.assessment.repositories.loader.configs.ToolTypeRepository;
import tds.assessment.services.AssessmentToolConfigService;
import tds.common.Algorithm;
import tds.testpackage.model.Presentation;
import tds.testpackage.model.TestPackage;

import static java.util.stream.Collectors.toSet;
import static tds.assessment.services.TestToolDefaultsHelper.TOOL_OPTION_DEFAULTS_MAP;

@Service
public class AssessmentToolConfigServiceImpl implements AssessmentToolConfigService {
    private static final String CONTEXT_TYPE_TEST = "TEST";
    private static final String CONTEXT_TYPE_SEGMENT = "SEGMENT";

    private final ToolTypeRepository toolTypeRepository;
    private final ToolRepository toolRepository;
    private final ToolDependenciesRepository toolDependenciesRepository;

    @Autowired
    public AssessmentToolConfigServiceImpl(final ToolTypeRepository toolTypeRepository, final ToolRepository toolRepository, final ToolDependenciesRepository toolDependenciesRepository) {
        this.toolTypeRepository = toolTypeRepository;
        this.toolRepository = toolRepository;
        this.toolDependenciesRepository = toolDependenciesRepository;
    }

    @Override
    public void loadTools(final TestPackage testPackage) {
        // LanguageCode -> Label
        Map<String, String> languages = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> {
                    if (segment.getAlgorithmType().equalsIgnoreCase(Algorithm.FIXED_FORM.getType())) {
                        return segment.segmentForms().stream()
                            .flatMap(form -> form.itemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .flatMap(item -> item.getPresentations().stream())
                                )
                            );
                    } else if (segment.getAlgorithmType().contains("adaptive")) {
                        return segment.pool().stream()
                            .flatMap(itemGroup -> itemGroup.items().stream()
                                .flatMap(item -> item.getPresentations().stream())
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                })
            ).collect(Collectors.toMap(Presentation::getCode, Presentation::label, (v1, v2) -> v1)); // ignore duplicates

        loadDefaultTools(testPackage);
        loadTestToolTypes(testPackage);
        loadTestTools(testPackage, languages);
        loadToolDependencies(testPackage);
    }

    private void loadDefaultTools(final TestPackage testPackage) {
        // Load the Item Menu tool, which should be enabled by default
        testPackage.getAssessments().forEach(assessment -> {
            toolTypeRepository.save(
                new ToolType.Builder(testPackage.getPublisher(), assessment.getId(), CONTEXT_TYPE_TEST, "Item Tools Menu")
                    .withAllowChange(true)
                    .withArtFieldName("TDSAcc-ITM")
                    .withFunctional(true)
                    .withDateEntered(new Timestamp(System.currentTimeMillis()))
                    .build()
            );

            toolRepository.save(
                new Tool.Builder(testPackage.getPublisher(), assessment.getId(), CONTEXT_TYPE_TEST, "Item Tools Menu", "TDS_ITM1")
                    .withDefaultValue(true)
                    .withValue("On")
                    .build()
            );
        });
    }

    private void loadToolDependencies(final TestPackage testPackage) {
        // Build a map of dependencies based on the assessment id (context of the tooldependency) so that we can eliminate duplicates
        Map<String, Set<ToolDependency>> existingDependencies = testPackage.getAssessments().stream()
            .flatMap(assessment -> toolDependenciesRepository.findByContextAndClientName(assessment.getId(), testPackage.getPublisher()).stream())
            .collect(Collectors.groupingBy(ToolDependency::getContext, toSet()));

        // Assessment tool dependencies
        List<ToolDependency> toolDependencies = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.tools().stream()
                .flatMap(tool -> tool.options().stream()
                    .flatMap(option -> option.dependencies().stream()
                        .map(dependency -> new ToolDependency.Builder()
                            .withClientName(testPackage.getPublisher())
                            .withContext(assessment.getId())
                            .withContextType(CONTEXT_TYPE_TEST)
                            .withIfType(dependency.getIfToolType())
                            .withIfValue(dependency.getIfToolCode())
                            .withThenType(tool.getName())
                            .withThenValue(option.getCode())
                            .withDefaultValue(dependency.defaultValue())
                            .build()
                        )
                        .filter(dependency -> !existingDependencies.containsKey(assessment.getId())
                            || !existingDependencies.get(assessment.getId()).contains(dependency))
                    )
                ))
            .collect(Collectors.toList());

        // Segment tool dependencies
        List<ToolDependency> segmentToolDependencies = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                .flatMap(segment -> segment.tools().stream()
                    .flatMap(tool -> tool.options().stream()
                        .flatMap(option -> option.dependencies().stream()
                            .map(dependency -> new ToolDependency.Builder()
                                .withClientName(testPackage.getPublisher())
                                .withContext(segment.getId())
                                .withContextType(CONTEXT_TYPE_SEGMENT)
                                .withIfType(dependency.getIfToolType())
                                .withIfValue(dependency.getIfToolCode())
                                .withThenType(tool.getName())
                                .withThenValue(option.getCode())
                                .withDefaultValue(dependency.defaultValue())
                                .build()
                            )
                        )
                    ))
            )
            .collect(Collectors.toList());

        toolDependencies.addAll(segmentToolDependencies);
        toolDependenciesRepository.save(toolDependencies);
    }

    private void loadTestTools(final TestPackage testPackage, final Map<String, String> languages) {
        // Assessment tool options
        List<Tool> tools = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.tools().stream()
                .flatMap(tool -> tool.options().stream()
                    .map(option ->
                        new Tool.Builder(testPackage.getPublisher(), assessment.getId(), CONTEXT_TYPE_TEST, tool.getName(), option.getCode())
                            .withDefaultValue(option.defaultValue())
                            .withSortOrder(option.getSortOrder())
                            .withAllowCombine(tool.allowMultipleOptions())
                            .withValue(TOOL_OPTION_DEFAULTS_MAP.get(option.getCode())) // Fetch the default label (if one exists
                            .build()
                    )
                ))
            .collect(Collectors.toList());

        // Segment tool options
        List<Tool> segmentTools = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                    .flatMap(segment -> segment.tools().stream()
                        .flatMap(tool -> tool.options().stream()
                                .map(option ->
                                        new Tool.Builder(testPackage.getPublisher(), segment.getId(), CONTEXT_TYPE_SEGMENT, tool.getName(), option.getCode())
                                            .withDefaultValue(option.defaultValue())
                                            .withSortOrder(option.getSortOrder())
                                            .withAllowCombine(tool.allowMultipleOptions())
                                            .withValue(TOOL_OPTION_DEFAULTS_MAP.get(option.getCode())) // Fetch the default label (if one exists
                                            .build()
                                )
                        ))
            )
            .collect(Collectors.toList());

        tools.addAll(segmentTools);

        // Language tools
        testPackage.getAssessments().forEach(assessment ->
            languages.entrySet().forEach(entry ->
                tools.add(
                    new Tool.Builder(testPackage.getPublisher(), assessment.getId(), CONTEXT_TYPE_TEST, "Language", entry.getKey())
                        .withValue(entry.getValue())
                        .withDefaultValue(entry.getKey().equalsIgnoreCase("ENU"))
                        .withSortOrder(1)
                        .build()
                )
            )
        );

        toolRepository.save(tools);
    }

    private void loadTestToolTypes(final TestPackage testPackage) {
        // Load the Language tooltype
        List<ToolType> toolTypes = testPackage.getAssessments().stream()
            .map(assessment -> new ToolType.Builder(testPackage.getPublisher(), assessment.getId(), CONTEXT_TYPE_TEST, "Language")
                .withArtFieldName("TDSAcc-Language")
                .withRequired(true)
                .withSelectable(true)
                .withVisible(true)
                .withDateEntered(new Timestamp(System.currentTimeMillis()))
                .withFunctional(true)
                .build()
        ).collect(Collectors.toList());

        // Second, load the assessment tool types
        toolTypes.addAll(testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.tools().stream()
                    .map(tool ->
                            new ToolType.Builder(testPackage.getPublisher(), assessment.getId(), CONTEXT_TYPE_TEST, tool.getName())
                                .withArtFieldName(tool.studentPackageFieldName())
                                .withAllowChange(tool.allowChange())
                                .withDateEntered(new Timestamp(System.currentTimeMillis()))
                                .withStudentControlled(tool.studentControl())
                                .withVisible(tool.visible())
                                .withDependsOnToolType(tool.dependsOnToolType().isPresent() ? tool.dependsOnToolType().get() : null)
                                .withFunctional(tool.functional())
                                .withSelectable(tool.selectable())
                                .withRequired(tool.required())
                                .withSortOrder(tool.getSortOrder().isPresent() ? tool.getSortOrder().get() : 0)
                                .build()
                    )
            )
            .collect(Collectors.toList()));
        // Segment tool types
        List<ToolType> segmentToolTypes = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream()
                    .flatMap(segment -> segment.tools().stream()
                            .map(tool ->
                                    new ToolType.Builder(testPackage.getPublisher(), segment.getId(), CONTEXT_TYPE_SEGMENT, tool.getName())
                                        .withArtFieldName(tool.studentPackageFieldName())
                                        .withAllowChange(tool.allowChange())
                                        .withDateEntered(new Timestamp(System.currentTimeMillis()))
                                        .withStudentControlled(tool.studentControl())
                                        .withVisible(tool.visible())
                                        .withDependsOnToolType(tool.dependsOnToolType().isPresent() ? tool.dependsOnToolType().get() : null)
                                        .withFunctional(tool.functional())
                                        .withSelectable(tool.selectable())
                                        .withRequired(tool.required())
                                        .withSortOrder(tool.getSortOrder().isPresent() ? tool.getSortOrder().get() : 0)
                                        .build()
                            )
                    )
            )
            .collect(Collectors.toList());

        toolTypes.addAll(segmentToolTypes);
        toolTypeRepository.save(toolTypes);
    }
}
