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

package tds.assessment.model.itembank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblitem", catalog = "itembank")
public class TblItem {
    private String key;
    private long id;
    private long bankKey;
    private String itemType;
    private int scorePoints;
    private String filePath;
    private String fileName;
    private Long version;
    private String itemId; //always empty string

    /**
     * Empty constructor for frameworks
     */
    private TblItem() {
    }

    private TblItem(Builder builder) {
        key = String.format("%s-%s", builder.bankKey, builder.id);
        id = builder.id;
        bankKey = builder.bankKey;
        itemType = builder.itemType;
        scorePoints = builder.scorePoints;
        filePath = builder.filePath;
        fileName = builder.fileName;
        version = builder.version;
        itemId = "";

    }

    public static final class Builder {
        private long id;
        private long bankKey;
        private String itemType;
        private int scorePoints;
        private String filePath;
        private String fileName;
        private Long version;

        public Builder() {
        }

        public Builder withItemType(String itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withBankKey(long bankKey) {
            this.bankKey = bankKey;
            return this;
        }

        public Builder withScorePoints(int scorePoints) {
            this.scorePoints = scorePoints;
            return this;
        }

        public Builder withFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withVersion(Long version) {
            this.version = version;
            return this;
        }

        public TblItem build() {
            return new TblItem(this);
        }
    }

    @Column(name = "_efk_item")
    public long getId() {
        return id;
    }

    @Column(name = "_efk_itembank")
    public long getBankKey() {
        return bankKey;
    }

    @Column(name = "filepath")
    public String getFilePath() {
        return filePath;
    }

    @Column(name = "filename")
    public String getFileName() {
        return fileName;
    }

    @Column(name = "loadconfig")
    public Long getVersion() {
        return version;
    }

    @Column(name = "itemtype")
    public String getItemType() {
        return itemType;
    }

    @Column(name = "scorepoint")
    public int getScorePoints() {
        return scorePoints;
    }

    @Column(name = "itemid")
    public String getItemId() {
        return itemId;
    }

    @Id
    @Column(name = "_key")
    public String getKey() {
        return key;
    }

    /* Note: NEVER USE THESE SETTERS - They are only for Hibernate compatibility - use the builder */
    private void setKey(final String key) {
        this.key = key;
    }

    private void setId(final long id) {
        this.id = id;
    }

    private void setBankKey(final long bankKey) {
        this.bankKey = bankKey;
    }

    private void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    private void setScorePoints(final int scorePoints) {
        this.scorePoints = scorePoints;
    }

    private void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    private void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    private void setVersion(final Long version) {
        this.version = version;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }
}
