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
import java.util.List;
import java.util.stream.Collectors;

import tds.assessment.model.configs.Tool;
import tds.assessment.model.configs.ToolDependency;
import tds.assessment.model.configs.ToolType;
import tds.assessment.repositories.loader.configs.ToolDependenciesRepository;
import tds.assessment.repositories.loader.configs.ToolRepository;
import tds.assessment.repositories.loader.configs.ToolTypeRepository;
import tds.assessment.services.AssessmentToolConfigService;
import tds.testpackage.model.TestPackage;

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
        // First, load the assessment tool types
        List<ToolType> toolTypes = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getTools().stream()
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
            .collect(Collectors.toList());
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

        // Assessment tool options
        List<Tool> tools = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getTools().stream()
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
        toolRepository.save(tools);

        // Assessment tool dependencies
        List<ToolDependency> toolDependencies = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getTools().stream()
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
}
