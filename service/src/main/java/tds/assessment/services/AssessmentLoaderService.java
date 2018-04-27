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

import java.util.Optional;

import tds.common.ValidationError;
import tds.testpackage.model.TestPackage;

/**
 * An interface for a service responsible for loading an entire {@link tds.testpackage.model.TestPackage} into the item bank
 */
public interface AssessmentLoaderService {
    /**
     * Loads a {@link tds.testpackage.model.TestPackage} into the item bank
     *
     * @param testPackageName The test package name (typically the file name, without the file extension)
     * @param testPackage     The test package to load
     * @return An error that occurred during the loading of the test package, if one occurs
     */
    Optional<ValidationError> loadTestPackage(final String testPackageName, final TestPackage testPackage);
}
