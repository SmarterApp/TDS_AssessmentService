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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.assessment.repositories.AssessmentCommandRepository;

@Repository
public class AssessmentCommandRepositoryImpl implements AssessmentCommandRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final String[] ITEMBANK_TABLE_NAMES = {
        "aa_itemcl",
        "tblsetofitemstrands",
        "tblsetofitemstimuli",
        "tblitemprops",
        "setoftestgrades",
        "testcohort",
        "tblitemselectionparm",
        "tbladminstrand",
        "tblsetofadminitems",
        "itemscoredimension",
        "tbladminstimulus",
        "testform",
        "testformitem",
        "affinitygroup",
        "affinitygroupitem",
        "tblsetofadminsubjects"
    };

    private static final String[] CONFIGS_TABLE_NAMES = {
        "client_testmode",
        "client_testformproperties",
        "client_testwindow",
        "client_testgrades",
        "client_testeligibility",
        "client_test_itemtypes",
        "client_test_itemconstraint",
        "client_testtooltype",
        "client_segmentproperties",
        "client_testproperties"
    };

    @Autowired
    public AssessmentCommandRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void removeAssessmentData(final String clientName, final Assessment assessment) {
        final List<String> SQL = new ArrayList<>();

        // If the assessment is multi-segmented, remove segment data first
        if (assessment.isSegmented()) {
            for (Segment segment : assessment.getSegments()) {
                SQL.addAll(generateItembankDeleteSQL(segment.getKey()));
                SQL.addAll(generateConfigsDeleteQL(clientName, segment.getSegmentId()));
            }
        }

        SQL.addAll(generateItembankDeleteSQL(assessment.getKey()));
        SQL.addAll(generateConfigsDeleteQL(clientName, assessment.getAssessmentId()));


        jdbcTemplate.batchUpdate(SQL.toArray(new String[SQL.size()]));
    }

    private List<String> generateItembankDeleteSQL(final String key) {
        final List<String> SQL = Arrays.stream(ITEMBANK_TABLE_NAMES)
            .map(table -> table.equals("tblsetofadminsubjects")
                ? String.format("DELETE FROM itembank.%s WHERE _key = '%s';", table, key)
                : String.format("DELETE FROM itembank.%s WHERE _fk_adminsubject = '%s';", table, key))
            .collect(Collectors.toList());

        // Delete items that are not being used by any assessment
        SQL.add(
            "DELETE FROM \n" +
                "   tblitem \n" +
                "WHERE \n" +
                "   _key NOT IN ( \n" +
                "   SELECT \n" +
                "       _fk_item \n" +
                "   FROM \n" +
                "       tblsetofadminitems \n" +
                "   );"
        );

        // Delete stimuli that are not being used by any assessment
        SQL.add(
            "DELETE FROM \n" +
                "   tblstimulus  \n" +
                "WHERE \n" +
                "   _key NOT IN ( \n" +
                "   SELECT \n" +
                "       _fk_stimulus \n" +
                "   FROM \n" +
                "       tblsetofitemstimuli \n" +
                ");"
        );

        // Delete strands that are not being used by any assessment
        SQL.add(
            "DELETE FROM \n" +
                "   tblstrand \n" +
                "WHERE \n" +
                "   _key NOT IN ( \n" +
                "   SELECT \n" +
                "       _fk_strand \n" +
                "   FROM \n" +
                "       tbladminstrand \n" +
                ");"
        );

        return SQL;
    }

    private List<String> generateConfigsDeleteQL(final String clientName, final String id) {
        final List<String> SQL = Arrays.stream(CONFIGS_TABLE_NAMES)
            .map(table -> {
                switch (table) {
                    case "client_segmentproperties":
                        return String.format("DELETE FROM configs.%s WHERE clientname = '%s' AND parenttest = '%s';", table, clientName, id);
                    case "client_testtooltype":
                        return String.format("DELETE FROM configs.%s WHERE clientname = '%s' AND context = '%s';", table, clientName, id);
                    default:
                        return String.format("DELETE FROM configs.%s WHERE clientname = '%s' AND testid = '%s';", table, clientName, id);
                }
            })
            .collect(Collectors.toList());

        return SQL;
    }
}
