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

package tds.assessment.repositories.loader.itembank.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tds.assessment.model.itembank.Client;
import tds.assessment.repositories.loader.itembank.ItemBankDataCommandRepository;

@Repository
public class ItemBankDataCommandRepositoryImpl implements ItemBankDataCommandRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemBankDataCommandRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Client insertClient(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("name", clientName)
            .addValue("homePath", Client.DEFAULT_HOME_PATH);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final String SQL =
            "INSERT INTO tblclient \n" +
                "( \n" +
                "   name, \n" +
                "   homepath \n" +
                ") \n" +
                "VALUES " +
                "( \n" +
                "   :name, \n" +
                "   :homePath \n" +
                ")";

        jdbcTemplate.update(SQL, parameters, keyHolder);
        return new Client(keyHolder.getKey().intValue(), clientName, Client.DEFAULT_HOME_PATH);
    }

    @Override
    public void updateTblTestAdminVersion(final String key, final String version) {
        final SqlParameterSource parameters = new MapSqlParameterSource("key", key)
            .addValue("version",version);

        final String SQL = "UPDATE \n" +
            "   tbltestadmin \n" +
            "SET \n" +
            "   updateconfig = :version \n" +
            "WHERE \n" +
            "   _key = :key";

        jdbcTemplate.update(SQL, parameters);
    }

    private static String getItemOrStimulusId(final float bankKey, final float key) {
        return String.format("%s-%s", bankKey, key);
    }
}
