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
import java.util.UUID;

@Entity
@Table(name = "tblitemselectionparm", schema = "itembank")
public class TblItemSelectionParameter {
    private UUID key;
    private String segmentKey;
    private String segmentId;
    private String name;
    private String value;
    private String label;

    public TblItemSelectionParameter(final String segmentKey, final String segmentId, final String name, final String value) {
        this.segmentKey = segmentKey;
        this.segmentId = segmentId;
        this.name = name;
        this.value = value;
        this.key = UUID.randomUUID();
        this.label = "";
    }

    public void setSegmentKey(final String segmentKey) {
        this.segmentKey = segmentKey;
    }

    public void setSegmentId(final String segmentId) {
        this.segmentId = segmentId;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public void setKey(final UUID key) {
        this.key = key;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    @Id
    @Column(name = "_key")
    public UUID getKey() {
        return key;
    }

    @Column(name = "_fk_adminsubject")
    public String getSegmentKey() {
        return segmentKey;
    }

    @Column(name = "bpelementid")
    public String getSegmentId() {
        return segmentId;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
