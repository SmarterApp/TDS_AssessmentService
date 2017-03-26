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
