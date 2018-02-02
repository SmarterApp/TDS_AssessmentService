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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import tds.assessment.exceptions.TestPackageLoaderException;
import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.ItemScoreDimension;
import tds.common.Algorithm;
import tds.testpackage.model.BlueprintReference;
import tds.testpackage.model.Grade;
import tds.testpackage.model.Item;
import tds.testpackage.model.ItemGroup;
import tds.testpackage.model.ItemScoreParameter;
import tds.testpackage.model.TestPackage;

public class AssessmentLoaderServiceBaseTest {
    @Value("classpath:V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml")
    private Resource testPackageXml;

    protected TestPackage mockTestPackage;

    protected Map<String, ItemMetadataWrapper> mockItemMetadataMap;

    @Before
    public void deserializeTestPackage() throws IOException {
        final XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new Jdk8Module());

        mockTestPackage = xmlMapper.readValue(this.getClass().getResourceAsStream("/V2-(SBAC_PT)IRP-GRADE-11-MATH-EXAMPLE.xml"),
            TestPackage.class);

        mockItemMetadataMap = mapItemsToItemMetadata();
    }

    private Map<String, ItemMetadataWrapper> mapItemsToItemMetadata() {
        return mockTestPackage.getAssessments().stream().flatMap(
            assessment -> assessment.getSegments().stream().flatMap(segment -> {
                    if (segment.getAlgorithmType().equals(Algorithm.FIXED_FORM.getType())) {
                        return segment.segmentForms().stream()
                            .flatMap(form -> form.itemGroups().stream()
                                .flatMap(itemGroup -> itemGroup.items().stream()
                                    .map(item -> new ItemMetadataWrapper(item, assessment.getGrades(), segment.getKey(),
                                        itemGroup.getKey(), false))
                                )
                            );
                    } else if (segment.getAlgorithmType().contains("adaptive")) {
                        return segment.pool().stream()
                            .flatMap(itemGroup -> itemGroup.items().stream()
                                .map(item -> new ItemMetadataWrapper(item, assessment.getGrades(), segment.getKey(),
                                    itemGroup.getKey(), true))
                            );
                    } else {
                        throw new TestPackageLoaderException("Unrecognized selection algorithm");
                    }
                }
            )).collect(Collectors.toMap(itemWrapper -> itemWrapper.getItem().getKey(), itemWrapper -> itemWrapper));
    }
}
