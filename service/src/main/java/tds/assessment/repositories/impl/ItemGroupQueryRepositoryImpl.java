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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import tds.assessment.ItemGroup;
import tds.assessment.repositories.ItemGroupQueryRepository;

@Repository
public class ItemGroupQueryRepositoryImpl implements ItemGroupQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemGroupQueryRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ItemGroup> findItemGroupsBySegment(final String segmentKey) {
        SqlParameterSource parameters = new MapSqlParameterSource("segmentKey", segmentKey);

        String SQL =
            "SELECT \n" +
            "   groupID as itemGroup, \n" +
            "   numItemsRequired as itemsRequired, \n" +
            "   maxItems, \n" +
            "   bpweight \n" +
            "FROM tbladminstimulus \n" +
            "WHERE _fk_AdminSubject = :segmentKey ";

        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) -> new ItemGroup(
            rs.getString("itemGroup"),
            rs.getInt("itemsRequired"),
            rs.getInt("maxItems"),
            rs.getFloat("bpweight")
        ));
    }
}
