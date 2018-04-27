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
 * An interface for a service responsible for loading tool configuration data for a test package
 */
public interface AssessmentToolConfigService {
    /**
     * Loads tool-specific configuration data for all assessments and segments in a test package
     *
     * @param testPackage The test package containing the tool configuration data to load
     */
    void loadTools(final TestPackage testPackage);
}
