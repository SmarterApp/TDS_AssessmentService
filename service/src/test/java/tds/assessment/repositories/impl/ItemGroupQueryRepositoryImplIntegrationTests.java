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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import tds.assessment.ItemGroup;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ItemGroupQueryRepositoryImplIntegrationTests {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ItemGroupQueryRepositoryImpl itemGroupQueryRepository;

    @Before
    public void setUp() {
        itemGroupQueryRepository = new ItemGroupQueryRepositoryImpl(jdbcTemplate);

        String insertSQL =
            "INSERT INTO tbladminstimulus (_fk_stimulus, _fk_adminsubject, numitemsrequired, maxitems, groupid) \n" +
                "VALUES ('200-1026', '(SBAC)SBAC-OP-ADAPTIVE-G5E-ELA-5-Spring-2015-2016', -1, 2, 'G-200-1026-0')";

        jdbcTemplate.update(insertSQL, new MapSqlParameterSource());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindItemGroups() {
        List<ItemGroup> itemGroups = itemGroupQueryRepository.findItemGroupsBySegment("(SBAC)SBAC-OP-ADAPTIVE-G5E-ELA-5-Spring-2015-2016");

        assertThat(itemGroups).hasSize(1);

        ItemGroup itemGroup = itemGroups.get(0);

        assertThat(itemGroup.getGroupId()).isEqualTo("G-200-1026-0");
        assertThat(itemGroup.getMaxItems()).isEqualTo(2);
        assertThat(itemGroup.getBpWeight()).isEqualTo(1f);
        assertThat(itemGroup.getRequiredItemCount()).isEqualTo(-1);
    }
}