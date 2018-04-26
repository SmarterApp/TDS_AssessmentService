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

package tds.assessment.services;

import tds.testpackage.model.TestPackage;

/**
 * An interface for a service responsible for loading segment and assessment configuration test package data
 */
public interface AssessmentSegmentConfigLoaderService {
    /**
     * Loads all {@link tds.assessment.model.configs.AssessmentProperties} for each assessment in the test package
     *
     * @param testPackage The test package containing assessment property configuration data to load
     */
    void loadAssessmentProperties(final TestPackage testPackage);

    /**
     * Creates and loads a {@link tds.assessment.model.configs.TestMode} for each assessment in the test package
     *
     * @param testPackage The test package containing testmode configuration data to load
     */
    void loadTestMode(final TestPackage testPackage);

    /**
     * Creates and loads a {@link tds.assessment.model.configs.SegmentProperties} for each segment in the test package
     *
     * @param testPackage The test package containing segment configuration data to load
     */
    void loadSegmentProperties(final TestPackage testPackage);

    /**
     * Creates and loads a {@link tds.assessment.model.configs.TestWindow} for each assessment in the test package
     *
     * @param testPackage The test package containing test window configuration data to load
     */
    void loadTestWindow(final TestPackage testPackage);
}
