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
@Table(name = "tblclient", schema = "itembank")
public class Client {
    public static final String DEFAULT_HOME_PATH = "/usr/local/tomcat/resources/tds/";
    private long key;
    private String name;
    private String homePath;

    public Client(final int key, final String name, final String homePath) {
        this.key = key;
        this.name = name;
        this.homePath = homePath;
    }

    public void setKey(final long key) {
        this.key = key;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setHomePath(final String homePath) {
        this.homePath = homePath;
    }

    @Id
    @Column(name = "_key")
    public long getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getHomePath() {
        return homePath;
    }
}
