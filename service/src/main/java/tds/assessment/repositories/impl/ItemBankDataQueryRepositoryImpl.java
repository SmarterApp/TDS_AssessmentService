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

package tds.assessment.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import tds.assessment.model.itembank.Client;
import tds.assessment.model.itembank.TblTestAdmin;
import tds.assessment.repositories.ItemBankDataQueryRepository;

@Repository
public class ItemBankDataQueryRepositoryImpl implements ItemBankDataQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemBankDataQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Client> findClient(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
            "SELECT \n" +
                "   _key, \n" +
                "   name, \n" +
                "   homepath \n" +
                "FROM \n" +
                "   tblclient \n" +
                "WHERE \n" +
                "   name = :clientName";

        Optional<Client> maybeClient;
        try {
            maybeClient = Optional.of(jdbcTemplate.queryForObject(SQL, parameters, (resultSet, i) -> new Client(
                resultSet.getInt("_key"),
                resultSet.getString("name"),
                resultSet.getString("homepath")
            )));
        } catch (EmptyResultDataAccessException e) {
            maybeClient = Optional.empty();
        }

        return maybeClient;
    }

    @Override
    public long generateFormKey() {
        final String SQL =
            "SELECT \n" +
                "   MAX(_efk_itskey) + 1 \n" +
                "FROM \n" +
                "   testform";

        return jdbcTemplate.queryForObject(SQL, new MapSqlParameterSource(), Long.class);
    }
}
