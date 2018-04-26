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

package tds.assessment.repositories.loader.configs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tds.assessment.model.itembank.Client;
import tds.assessment.repositories.ConfigsDataCommandRepository;

@Repository
public class ConfigsDataCommandRepositoryImpl implements ConfigsDataCommandRepository {
    private final  NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ConfigsDataCommandRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void copyTesteeAttributeSeedDataForClient(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
            "INSERT INTO configs.client_testeeattribute \n" +
                "( \n" +
                "   clientname, \n" +
                "   tds_id, \n" +
                "   rtsname, \n" +
                "   `type`, \n" +
                "   label, \n" +
                "   reportname, \n" +
                "   atlogin, \n" +
                "   sortorder \n" +
                ") \n" +
                "SELECT \n" +
                "   :clientName, \n" +
                "   tds_id, \n" +
                "   rtsname, \n" +
                "   `type`, \n" +
                "   label, \n" +
                "   reportname, \n" +
                "   atlogin, \n" +
                "   sortorder \n" +
                "FROM \n" +
                "   configs.tds_testeeattribute \n" +
                "WHERE NOT EXISTS ( \n" +
                "       SELECT * FROM configs.client_testeeattribute \n" +
                "       WHERE clientname = :clientName \n" +
                ")";

        jdbcTemplate.update(SQL, parameters);
    }

    @Override
    public void copyTesteeRelationshipAttributeSeedDataForClient(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
            "INSERT INTO configs.client_testeerelationshipattribute \n" +
                "( \n" +
                "   clientname, \n" +
                "   tds_id, \n" +
                "   rtsname, \n" +
                "   label, \n" +
                "   reportname, \n" +
                "   atlogin, \n" +
                "   sortorder, \n" +
                "   relationshiptype \n" +
                ") \n" +
                "SELECT \n" +
                "   :clientName, \n" +
                "   tds_id, \n" +
                "   rtsname, \n" +
                "   label, \n" +
                "   reportname, \n" +
                "   atlogin, \n" +
                "   sortorder, \n" +
                "   relationshiptype \n" +
                "FROM \n " +
                "   configs.tds_testeerelationshipattribute \n" +
                "WHERE NOT EXISTS ( \n" +
                "       SELECT * FROM configs.client_testeerelationshipattribute \n" +
                "       WHERE clientname = :clientName \n" +
                ")";


        jdbcTemplate.update(SQL, parameters);
    }


}
