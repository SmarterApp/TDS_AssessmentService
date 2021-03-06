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

import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.accommodation.Accommodation;
import tds.accommodation.AccommodationDependency;
import tds.assessment.repositories.AccommodationsQueryRepository;
import tds.common.data.mapping.ResultSetMapperUtility;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccommodationsQueryRepositoryImplIntegrationTests {
    @Autowired
    private AccommodationsQueryRepository repository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {

        Instant now = Instant.now();

        String testModeInsertSQL = "INSERT INTO configs.client_testmode VALUES ('SBAC_PT','SBAC-Mathematics-11','online','virtual',0,1,50,0,0,1,0,'(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',X'0431F6515F2D11E6B2C80243FCF25EAB');";
        String segmentTestToolTypeInsertSQL = "INSERT INTO configs.client_testtooltype (clientname, toolname, allowchange, rtsfieldname, isrequired, isselectable, dateentered, contexttype, context, testmode) VALUES ('SBAC_PT', 'toolTypeSegmented', 0, 'rts', 1, 0, :dateentered, 'SEGMENT', 'SBAC-SEG1-MATH-11', 'ALL');";
        String segmentTestToolInsertSQL = "INSERT INTO configs.client_testtool (clientname, type, code, value, isdefault, allowcombine, context, contexttype, testmode) VALUES ('SBAC_PT', 'toolTypeSegmented', 'toolTypeSegmented', 'segmentValue', 1, 0, 'SBAC-SEG1-MATH-11', 'SEGMENT', 'ALL');";
        String segmentPropertiesInsertSQL = "INSERT INTO configs.client_segmentproperties (ispermeable, clientname, entryapproval, exitapproval, segmentid, segmentposition, parenttest, modekey) VALUES (1, 'SBAC_PT', 1, 1, 'SBAC-SEG1-MATH-11', 99, 'SBAC-Mathematics-11', '(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015');";
        String defaultToolTypeInsertSQL = "INSERT INTO configs.client_testtooltype (clientname, toolname, allowchange, rtsfieldname, isrequired, isselectable, dateentered, contexttype, context, testmode) VALUES ('SBAC_PT', 'toolTypeDefault', 0, 'rts', 1, 0, :dateentered, 'TEST', '*', 'ALL')  ";
        String defaultTestToolInsertSQL = "INSERT INTO configs.client_testtool (clientname, type, code, value, isdefault, allowcombine, context, contexttype, testmode) VALUES ('SBAC_PT', 'toolTypeDefault', 'toolTypeDefault', 'defaultTool', 1, 0, '*', 'TEST', 'ALL');";

        String nonSegmentTestToolTypeInsertSQL = "INSERT INTO configs.client_testtooltype (clientname, context, contexttype, toolname, allowchange, rtsfieldname, isrequired, isselectable, dateentered, testmode) " +
            "VALUES ('SBAC_PT', 'SBAC-Mathematics-11', 'TEST', 'Language', 0, 'rts', 1, 0, :dateentered, 'ALL');";
        String noSegmentTestToolInsertEnglishSQL = "INSERT INTO configs.client_testtool (clientname, context, contexttype, type, code, value, isdefault, allowcombine, testmode) " +
            "VALUES ('SBAC_PT', 'SBAC-Mathematics-11', 'TEST', 'Language', 'ENU', 'ENU', 1, 0, 'ALL');";
        String noSegmentTestToolInsertFrenchSQL = "INSERT INTO configs.client_testtool (clientname, context, contexttype, type, code, value, isdefault, allowcombine, testmode) " +
            "VALUES ('SBAC_PT', 'SBAC-Mathematics-11', 'TEST', 'Language', 'FRN', 'FRN', 1, 0, 'ALL');";
        
        String toolDependencies = "INSERT INTO configs.client_tooldependencies (context, contexttype, iftype, ifvalue, isdefault, thentype, thenvalue, clientname, _key, testmode) " +
            "VALUES ('SBAC-Mathematics-11','TEST','Language','ENU',0,'Streamlined Mode','TDS_SLM1','SBAC_PT',X'16DE7974D75943C4BE266DE4835608C4','ALL'), \n" +
            "('SBAC-Mathematics-11','TEST','Language','ENU-Braille',1,'Emboss Request Type','TDS_ERT_OR','SBAC_PT',X'17F829A06A434DC0945A62CA53ADA0C6','ALL')";
        
        SqlParameterSource parameters = new MapSqlParameterSource("dateentered", ResultSetMapperUtility.mapJodaInstantToTimestamp(now));

        jdbcTemplate.update(testModeInsertSQL, parameters);
        jdbcTemplate.update(segmentTestToolTypeInsertSQL, parameters);
        jdbcTemplate.update(segmentTestToolInsertSQL, parameters);
        jdbcTemplate.update(segmentPropertiesInsertSQL, parameters);
        jdbcTemplate.update(defaultToolTypeInsertSQL, parameters);
        jdbcTemplate.update(defaultTestToolInsertSQL, parameters);
        jdbcTemplate.update(nonSegmentTestToolTypeInsertSQL, parameters);
        jdbcTemplate.update(toolDependencies, parameters);
        jdbcTemplate.update(noSegmentTestToolInsertEnglishSQL, parameters);
        jdbcTemplate.update(noSegmentTestToolInsertFrenchSQL, parameters);
    }

    @Test
    public void shouldFindAccommodationsByAssessmentKey() {
        Set<String> languages = new HashSet<>();
        languages.add("ENU");
        List<Accommodation> accommodationList = repository.findAssessmentAccommodationsByKey("(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015", languages);

        assertThat(accommodationList).hasSize(3);

        Accommodation englishAccommodation = null;
        Accommodation defaultAccommodation = null;
        Accommodation segmentedAccommodation = null;
        
        for (Accommodation accommodation : accommodationList) {
            if (accommodation.getType().equalsIgnoreCase("Language")) {
                englishAccommodation = accommodation;
            } else if (accommodation.getType().equalsIgnoreCase("toolTypeDefault")) {
                defaultAccommodation = accommodation;
            } else if (accommodation.getType().equalsIgnoreCase("toolTypeSegmented")) {
                segmentedAccommodation = accommodation;
            }
        }

        assertThat(englishAccommodation).isNotNull();
        assertThat(englishAccommodation.getCode()).isEqualTo("ENU");
        assertThat(englishAccommodation.getType()).isEqualTo("Language");
        assertThat(englishAccommodation.getValue()).isEqualTo("ENU");
        assertThat(englishAccommodation.getDependsOnToolType()).isNull();
        assertThat(englishAccommodation.getSegmentPosition()).isEqualTo(0);
        assertThat(englishAccommodation.getToolMode()).isEqualTo("ALL");
        assertThat(englishAccommodation.getToolTypeSortOrder()).isEqualTo(0);
        assertThat(englishAccommodation.getToolValueSortOrder()).isEqualTo(0);
        assertThat(englishAccommodation.getTypeMode()).isEqualTo("ALL");
        assertThat(englishAccommodation.isAllowChange()).isFalse();
        assertThat(englishAccommodation.isAllowCombine()).isFalse();
        assertThat(englishAccommodation.isDefaultAccommodation()).isTrue();
        assertThat(englishAccommodation.isDisableOnGuestSession()).isFalse();
        assertThat(englishAccommodation.isEntryControl()).isFalse();
        assertThat(englishAccommodation.isFunctional()).isTrue();
        assertThat(englishAccommodation.isSelectable()).isFalse();
        assertThat(englishAccommodation.isVisible()).isTrue();
        assertThat(englishAccommodation.getTypeTotal()).isEqualTo(2);
        assertThat(englishAccommodation.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015");

        assertThat(defaultAccommodation).isNotNull();
        assertThat(defaultAccommodation.getCode()).isEqualTo("toolTypeDefault");
        assertThat(defaultAccommodation.getType()).isEqualTo("toolTypeDefault");
        assertThat(defaultAccommodation.getValue()).isEqualTo("defaultTool");
        assertThat(defaultAccommodation.getDependsOnToolType()).isNull();
        assertThat(defaultAccommodation.getSegmentPosition()).isEqualTo(0);
        assertThat(defaultAccommodation.getToolMode()).isEqualTo("ALL");
        assertThat(defaultAccommodation.getToolTypeSortOrder()).isEqualTo(0);
        assertThat(defaultAccommodation.getToolValueSortOrder()).isEqualTo(0);
        assertThat(defaultAccommodation.getTypeMode()).isEqualTo("ALL");
        assertThat(defaultAccommodation.isAllowChange()).isFalse();
        assertThat(defaultAccommodation.isAllowCombine()).isFalse();
        assertThat(defaultAccommodation.isDefaultAccommodation()).isTrue();
        assertThat(defaultAccommodation.isDisableOnGuestSession()).isFalse();
        assertThat(defaultAccommodation.isEntryControl()).isFalse();
        assertThat(defaultAccommodation.isFunctional()).isTrue();
        assertThat(defaultAccommodation.isSelectable()).isFalse();
        assertThat(defaultAccommodation.isVisible()).isTrue();
        assertThat(defaultAccommodation.getTypeTotal()).isEqualTo(1);
        assertThat(defaultAccommodation.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015");
        
        assertThat(segmentedAccommodation).isNotNull();
    }

    @Test
    public void shouldFindAccommodationsByAssessmentIdAndClientName() {
        List<Accommodation> accommodationList = repository.findAssessmentAccommodationsById("SBAC_PT", "SBAC-Mathematics-11");
        assertThat(accommodationList).hasSize(4);
    }
    
    @Test
    public void shouldFindAccommodationDependenciesByAssessmentIdAndClientName() {
        List<AccommodationDependency> dependencies = repository.findAssessmentAccommodationDependencies("SBAC_PT", "SBAC-Mathematics-11");
        assertThat(dependencies).hasSize(2);
    }
}
