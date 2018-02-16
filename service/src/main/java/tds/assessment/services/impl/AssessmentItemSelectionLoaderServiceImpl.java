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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import tds.assessment.model.ItemMetadataWrapper;
import tds.assessment.model.itembank.ItemMeasurementParameter;
import tds.assessment.model.itembank.ItemScoreDimension;
import tds.assessment.model.itembank.MeasurementModel;
import tds.assessment.model.itembank.MeasurementParameter;
import tds.assessment.model.itembank.TblItemSelectionParameter;
import tds.assessment.repositories.loader.itembank.ItemMeasurementParameterRepository;
import tds.assessment.repositories.loader.itembank.ItemScoreDimensionsRepository;
import tds.assessment.repositories.loader.itembank.MeasurementModelRepository;
import tds.assessment.repositories.loader.itembank.MeasurementParameterRepository;
import tds.assessment.repositories.loader.itembank.TblItemSelectionParameterRepository;
import tds.assessment.services.AssessmentItemSelectionLoaderService;
import tds.testpackage.model.TestPackage;

@Service
public class AssessmentItemSelectionLoaderServiceImpl implements AssessmentItemSelectionLoaderService {
    private static final Set<String> ALGORITHM_PARAMETER_NAMES = new HashSet<>(Arrays.asList(
        "ftstartpos",
        "ftendpos",
        "bpweight",
        "startability",
        "startinfo",
        "cset1size",
        "cset1order",
        "cset2random",
        "cset2initialrandom",
        "abilityoffset",
        "itemweight",
        "precisiontarget",
        "adaptivecut",
        "toocloseses",
        "slope",
        "intercept",
        "abilityweight",
        "computeabilityestimates",
        "rcabilityweight",
        "precisiontargetmetweight",
        "precisiontargetnotmetweight",
        "terminationoverallinfo",
        "terminationrcinfo",
        "terminationmincount",
        "terminationtooclose",
        "terminationflagsand"
    ));

    /**
     * These measurement models and parameters are hardcoded and inserted in the load_measurementparameters stored procedure
     */
    private final Set<MeasurementModel> measurementModels = ImmutableSet.of(
        new MeasurementModel(1, "IRT3PLN"),
        new MeasurementModel(2, "IRTPCL"),
        new MeasurementModel(3, "RAW"),
        new MeasurementModel(4, "IRT3PL"),
        new MeasurementModel(5, "IRTGPC")
    );

    private final Set<MeasurementParameter> measurementParameters = new HashSet<>(ImmutableList.of(
        // IRT3pln
        new MeasurementParameter(1, 0, "a", "Slope (a)"),
        new MeasurementParameter(1, 1, "b", "Difficulty (b)"),
        new MeasurementParameter(1, 2, "c", "Guessing (c)"),
        // IRTPCL
        new MeasurementParameter(2, 0, "b0", "Difficulty cut 0 (b0)"),
        new MeasurementParameter(2, 1, "b1", "Difficulty cut 1 (b1)"),
        new MeasurementParameter(2, 2, "b2", "Difficulty cut 2 (b2)"),
        new MeasurementParameter(2, 3, "b3", "Difficulty cut 3 (b3)"),
        new MeasurementParameter(2, 4, "b4", "Difficulty cut 4 (b4)"),
        new MeasurementParameter(2, 5, "b5", "Difficulty cut 5 (b5)"),
        // IRT3PL
        new MeasurementParameter(4, 0, "a", "Slope (a)"),
        new MeasurementParameter(4, 1, "b", "Difficulty (b)"),
        new MeasurementParameter(4, 2, "c", "Guessing (c)"),
        // IRTGPC
        new MeasurementParameter(5, 0, "a", "Slope (a)"),
        new MeasurementParameter(5, 1, "b0", "Difficulty cut 0 (b0)"),
        new MeasurementParameter(5, 2, "b1", "Difficulty cut 1 (b1)"),
        new MeasurementParameter(5, 3, "b2", "Difficulty cut 2 (b2)"),
        new MeasurementParameter(5, 4, "b3", "Difficulty cut 3 (b3)"),
        new MeasurementParameter(5, 5, "b4", "Difficulty cut 4 (b4)"),
        new MeasurementParameter(5, 6, "b5", "Difficulty cut 5 (b5)")
    ));

    private final ItemScoreDimensionsRepository itemScoreDimensionsRepository;
    private final MeasurementModelRepository measurementModelRepository;
    private final MeasurementParameterRepository measurementParameterRepository;
    private final ItemMeasurementParameterRepository itemMeasurementParameterRepository;
    private final TblItemSelectionParameterRepository tblItemSelectionParameterRepository;

    @Autowired
    public AssessmentItemSelectionLoaderServiceImpl(final ItemScoreDimensionsRepository itemScoreDimensionsRepository,
                                                    final MeasurementModelRepository measurementModelRepository,
                                                    final MeasurementParameterRepository measurementParameterRepository,
                                                    final ItemMeasurementParameterRepository itemMeasurementParameterRepository,
                                                    final TblItemSelectionParameterRepository tblItemSelectionParameterRepository) {
        this.itemScoreDimensionsRepository = itemScoreDimensionsRepository;
        this.measurementModelRepository = measurementModelRepository;
        this.measurementParameterRepository = measurementParameterRepository;
        this.itemMeasurementParameterRepository = itemMeasurementParameterRepository;
        this.tblItemSelectionParameterRepository = tblItemSelectionParameterRepository;
    }

    @Override
    public void loadScoringSeedData() {
        // Load the scoring measurement seed data (refer to loader_measurementparameters stored procedure in legacy)
        measurementModelRepository.save(measurementModels);
        measurementParameterRepository.save(measurementParameters);
    }

    @Override
    public void loadAdminItemMeasurementParameters(final Map<String, ItemMetadataWrapper> itemIdToItemMetadata) {
        // Create a bi-directional mapping of the measurement model strings to their keys for quick lookup
        BiMap<String, Integer> measurementModelKeys = HashBiMap.create(
            measurementModels.stream()
                .collect(Collectors.toMap(MeasurementModel::getModelName, MeasurementModel::getModelNumber))
        );

        // <ModelName>-<ParamName> -> Parameter key/num
        Map<String, Integer> modelToMeasurementParameterMap = measurementParameters.stream()
            .collect(Collectors.toMap(
                param -> measurementModelKeys.inverse().get(param.getMeasurementParameterIdentity().getMeasurementModelKey()).toUpperCase() + "-" + param.getParameterName(),
                param -> param.getMeasurementParameterIdentity().getParameterNumber())
            );

        // Create a map for quick look up of the item dimensions
        Map<String, ItemScoreDimension> itemScoreDimensionsMap = itemIdToItemMetadata.values().stream()
            .map(wrapper -> new ItemScoreDimension.Builder()
                    .withDimension(wrapper.getItem().getItemScoreDimension().getDimension().orElse(""))
                    .withRecodeRule("")
                    .withScorePoints(wrapper.getItem().getItemScoreDimension().getScorePoints())
                    .withWeight((float) wrapper.getItem().getItemScoreDimension().getWeight())
                    .withKey(UUID.randomUUID())
                    .withSegmentKey(wrapper.getSegmentKey())
                    .withItemId(wrapper.getItem().getKey())
                    .withMeasurementModelKey(measurementModelKeys.get(wrapper.getItem().getItemScoreDimension().getMeasurementModel().toUpperCase()))
                    .build()
            ).collect(Collectors.toMap(ItemScoreDimension::getItemId, Function.identity()));

        itemScoreDimensionsRepository.save(itemScoreDimensionsMap.values());

        // Stream over each item score dimension and create a list of ItemMeasurementParameters
        List<ItemMeasurementParameter> itemMeasurementParameters = itemScoreDimensionsMap.values().stream()
            .map(dimension -> itemIdToItemMetadata.get(dimension.getItemId()))
            .flatMap(wrapper -> wrapper.getItem().getItemScoreDimension().itemScoreParameters().stream()
                .map(param ->
                    // This part is tricky - we need to get the foreign key of the parameter - which is not keyable based on on only the parameter name.
                    // This is because parameter keys can be different for different measurement model/param name combinations. For example, the param name "b1"
                    // has a param number of "1" for the IRTPCL model, while the number is "2" for the IRTGPC model. Lets look up based on model name and param num
                    new ItemMeasurementParameter(itemScoreDimensionsMap.get(wrapper.getItem().getKey()).getKey(),
                        modelToMeasurementParameterMap.get(wrapper.getItem().getItemScoreDimension().getMeasurementModel().toUpperCase() + "-" + param.getMeasurementParameter()),
                        (float) param.getValue())
                )
            )
            .collect(Collectors.toList());

        itemMeasurementParameterRepository.save(itemMeasurementParameters);
    }

    @Override
    public void loadItemSelectionParams(final TestPackage testPackage) {
        // Get all the item selection parameters for CAT segments that are not in the known "algorithm property names" list
        final List<TblItemSelectionParameter> parameters = testPackage.getAssessments().stream()
            .flatMap(assessment -> assessment.getSegments().stream())
            .filter(segment -> segment.getAlgorithmType().contains("adaptive"))
            .flatMap(segment -> segment.segmentBlueprint().stream()
                .filter(bpElement -> bpElement.getIdRef().equalsIgnoreCase(segment.getId()))
                .flatMap(segmentBpElement -> segmentBpElement.itemSelection().stream()
                    .filter(param -> !ALGORITHM_PARAMETER_NAMES.contains(param.getName().toLowerCase()))
                    .map(param -> new TblItemSelectionParameter(segment.getKey(), segment.getId(), param.getName(), param.getValue()))
                )
            )
            .collect(Collectors.toList());

        tblItemSelectionParameterRepository.save(parameters);
    }
}
