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

import com.sun.org.apache.xpath.internal.Arg;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import tds.assessment.model.configs.AssessmentProperties;
import tds.assessment.model.configs.SegmentProperties;
import tds.assessment.model.configs.TestMode;
import tds.assessment.model.configs.TestWindow;
import tds.assessment.repositories.loader.configs.AssessmentPropertiesRepository;
import tds.assessment.repositories.loader.configs.SegmentPropertiesRepository;
import tds.assessment.repositories.loader.configs.TestModeRepository;
import tds.assessment.repositories.loader.configs.TestWindowRepository;
import tds.assessment.services.AssessmentSegmentConfigLoaderService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AssessmentSegmentConfigLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AssessmentSegmentConfigLoaderService service;

    @Mock
    private AssessmentPropertiesRepository assessmentPropertiesRepository;

    @Mock
    private TestModeRepository testModeRepository;

    @Mock
    private SegmentPropertiesRepository segmentPropertiesRepository;

    @Mock
    private TestWindowRepository testWindowRepository;

    @Captor
    private ArgumentCaptor<List<AssessmentProperties>> assessmentPropertiesArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<SegmentProperties>> segmentPropertiesArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TestWindow>> testWindowArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<TestMode>> testModeArgumentCaptor;

    @Before
    public void setup() {
        service = new AssessmentSegmentConfigLoaderServiceImpl(assessmentPropertiesRepository, testModeRepository,
            segmentPropertiesRepository, testWindowRepository);
    }

    @Test
    public void shouldLoadAssessmentProperties() {
        service.loadAssessmentProperties(mockTestPackage);

        verify(assessmentPropertiesRepository).save(assessmentPropertiesArgumentCaptor.capture());
        List<AssessmentProperties> savedProperties = assessmentPropertiesArgumentCaptor.getValue();
        assertThat(savedProperties).hasSize(2); // 2 assessments in this test package

        AssessmentProperties properties = savedProperties.get(0);
        assertThat(properties.getAccommodationFamily()).isEqualTo("MATH");
        assertThat(properties.getGradeLabel()).isEqualTo("Grade 11");
        assertThat(properties.isScoreByTds()).isTrue();
        assertThat(properties.getLabel()).isEqualTo("IRP CAT Grade 11 Math");
        assertThat(properties.getSubjectName()).isEqualTo("MATH");
        assertThat(properties.getMaxOpportunities()).isEqualTo(3);
        assertThat(properties.getAssessmentPropertiesIdentity().getAssessmentId()).isEqualTo(mockTestPackage.getAssessments().get(0).getId());
        assertThat(properties.getAssessmentPropertiesIdentity().getClientName()).isEqualTo(mockTestPackage.getPublisher());
    }

    @Test
    public void shouldLoadSegmentPropertiesForSingleSegmentedAssessments() {
        service.loadSegmentProperties(mockTestPackage);

        verify(segmentPropertiesRepository).save(segmentPropertiesArgumentCaptor.capture());
        List<SegmentProperties> savedSegmentProperties = segmentPropertiesArgumentCaptor.getValue();
        assertThat(savedSegmentProperties).isEmpty();
    }

    @Test
    public void shouldLoadTestWindow() {
        service.loadTestWindow(mockTestPackage);
        verify(testWindowRepository).save(testWindowArgumentCaptor.capture());
        List<TestWindow> savedTestWindows = testWindowArgumentCaptor.getValue();
        assertThat(savedTestWindows).hasSize(2); // 2 assessments
        TestWindow savedTestWindow = savedTestWindows.get(0);
        assertThat(savedTestWindow.getAssessmentId()).isEqualTo(mockTestPackage.getAssessments().get(0).getId());
        assertThat(savedTestWindow.getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedTestWindow.getEndDate()).isAfter(new Date());
        assertThat(savedTestWindow.getKey()).isNotNull();
        assertThat(savedTestWindow.getNumberOfOpportunities()).isEqualTo(3);
        assertThat(savedTestWindow.getWindowId()).isEqualTo("ANNUAL");
        assertThat(savedTestWindow.getStartDate()).isNotNull();
    }

    @Test
    public void shouldLoadTestMode() {
        service.loadTestMode(mockTestPackage);

        verify(testModeRepository).save(testModeArgumentCaptor.capture());
        List<TestMode> savedTestModes = testModeArgumentCaptor.getValue();
        assertThat(savedTestModes).hasSize(2);

        TestMode savedTestMode = savedTestModes.get(0);
        assertThat(savedTestMode.getAlgorithm()).isEqualTo(mockTestPackage.getAssessments().get(0).getSegments().get(0).getAlgorithmType());
        assertThat(savedTestMode.getClientName()).isEqualTo(mockTestPackage.getPublisher());
        assertThat(savedTestMode.getAssessmentId()).isEqualTo(mockTestPackage.getAssessments().get(0).getId());
        assertThat(savedTestMode.getAssessmentKey()).isEqualTo(mockTestPackage.getAssessments().get(0).getKey());
        assertThat(savedTestMode.getKey()).isNotNull();
        assertThat(savedTestMode.getMode()).isEqualTo("online");
        assertThat(savedTestMode.getSessionType()).isEqualTo(0);
    }
}
