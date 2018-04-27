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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import tds.assessment.model.configs.Tool;
import tds.assessment.model.configs.ToolDependency;
import tds.assessment.model.configs.ToolType;
import tds.assessment.repositories.loader.configs.ToolDependenciesRepository;
import tds.assessment.repositories.loader.configs.ToolRepository;
import tds.assessment.repositories.loader.configs.ToolTypeRepository;
import tds.assessment.services.AssessmentToolConfigService;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentToolConfigServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentToolConfigService service;

    @Mock
    private ToolTypeRepository toolTypeRepository;

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private ToolDependenciesRepository toolDependenciesRepository;

    @Captor
    private ArgumentCaptor<List<ToolType>> toolTypeCaptor;

    @Captor
    private ArgumentCaptor<List<Tool>> toolCaptor;

    @Captor
    private ArgumentCaptor<List<ToolDependency>> toolDependencyCaptor;

    @Before
    public void setup() {
        this.service = new AssessmentToolConfigServiceImpl(toolTypeRepository, toolRepository, toolDependenciesRepository);
    }

    @Test
    public void shouldLoadTools() {
        service.loadTools(mockTestPackage);
        verify(toolTypeRepository).save(toolTypeCaptor.capture());
        verify(toolRepository).save(toolCaptor.capture());
        verify(toolDependenciesRepository).save(toolDependencyCaptor.capture());

        List<ToolType> savedToolTypes = toolTypeCaptor.getValue();

        List<ToolType> languageTypes = savedToolTypes.stream()
            .filter(type -> type.getToolTypeIdentity().getName().equalsIgnoreCase("Language"))
            .collect(Collectors.toList());

        assertThat(languageTypes).hasSize(2);
        assertThat(languageTypes.get(0).isRequired()).isTrue();
        assertThat(languageTypes.get(0).isSelectable()).isTrue();
        assertThat(languageTypes.get(0).isVisible()).isTrue();

        List<Tool> savedTools = toolCaptor.getValue();
        List<Tool> languageOptions = savedTools.stream()
            .filter(tool -> tool.getToolIdentity().getType().equalsIgnoreCase("Language"))
            .collect(Collectors.toList());

        assertThat(languageOptions).hasSize(2);
        assertThat(languageOptions.get(0).getToolIdentity().getCode()).isEqualTo("ENU");
        assertThat(languageOptions.get(0).getValue()).isEqualTo("English");

        List<ToolDependency> savedDependencies = toolDependencyCaptor.getValue();
        assertThat(savedDependencies).hasSize(4);
    }
}
