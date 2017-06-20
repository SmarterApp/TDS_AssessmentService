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

package tds.assessment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ItemFileMetadata {
    /**
     * @return the {@link tds.assessment.ItemFileType}
     */
    public abstract ItemFileType getItemType();

    /**
     * @return the id for the item file
     */
    public abstract String getId();

    /**
     * @return the file name for the item
     */
    public abstract String getFileName();

    /**
     * @return the file path
     */
    public abstract String getFilePath();

    @JsonCreator
    public static ItemFileMetadata create(
        @JsonProperty("itemType") final ItemFileType newItemType,
        @JsonProperty("id") final String newId,
        @JsonProperty("fileName") final String newFileName,
        @JsonProperty("filePath") final String newFilePath) {
        return new AutoValue_ItemFileMetadata(newItemType, newId, newFileName, newFilePath);
    }
}
