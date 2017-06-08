package tds.assessment.repositories.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import tds.assessment.ItemMeasurement;
import tds.assessment.repositories.ItemMeasurementQueryRepository;
import tds.common.data.mysql.UuidAdapter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ItemMeasurementQueryRepositoryImplIntegrationTests {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ItemMeasurementQueryRepository itemMeasurementQueryRepository;

    @Before
    public void setUp() {
        itemMeasurementQueryRepository = new ItemMeasurementQueryRepositoryImpl(jdbcTemplate);

        // Segmented test assessment
        final String tblSetOfAdminSubjectsInsertSQL2 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-Mathematics-8'," +
            "0,1,4,4,1,1,NULL,NULL,0,0,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segment 1
        final String tblSetOfAdminSubjectsInsertSQL2a = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG1-MATH-8'," +
            "0,1,4,4,1,1,NULL,NULL,2,3,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',1,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";
        // Segment2
        final String tblSetOfAdminSubjectsInsertSQL2b = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015','SBAC_PT', 'SBAC_PT-ELA','SBAC-SEG2-MATH-8'," +
            "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'fixedform',NULL,5,1,20,1,5,'(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015',2,0,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
            "0,'bp1',NULL,NULL,'summative');";

        final String tblSetAdminItemsInsertSQL =
            "INSERT INTO itembank.tblsetofadminitems \n" +
                "   (_fk_item, _fk_adminsubject, groupid, groupkey, blockid, itemposition, isfieldtest, isactive, isrequired, " +
                "   strandname, isprintable, responsemimetype, notforscoring, irt_model, irt_a, irt_b, irt_c, clstring, bvector) \n" +
                "VALUES \n" +
                "   ('187-1233', '(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015', 'G-1', 'GK-1', 'A', 1, 0, 1, 1, 'strand 99', 0, 'text/plain', 0, NULL, NULL, NULL, NULL, NULL, NULL),\n" +
                "   ('187-1234', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'G-1', 'GK-1', 'A', 1, 0, 1, 1, 'strand1', 0, 'text/plain', 0, NULL, NULL, NULL, NULL, NULL, NULL),\n" +
                "   ('187-1235', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'G-2', 'GK-2', 'A', 2, 0, 1, 1, 'strand2', 0, 'text/xml', 1, NULL, NULL, NULL, NULL, NULL, NULL),\n" +
                "   ('187-1236', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 'G-3', 'GK-3', 'A', 1, 0, 1, 1, 'strand3', 1, 'foo', 0, 'IRT3PLn', 0.54343, '1.2160500288009644', 0, 'SBAC-2-W;SBAC-2-W|9-5', 1.21605), \n" +
                "   ('187-1237', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 'G-4', 'GK-4', 'A', 1, 1, 1, 1, 'silver strand', 0, 'bar', 1, NULL, NULL, NULL, NULL, NULL, NULL)";

        final String parentItemScoreDimensionSQL = "INSERT INTO itemscoredimension (dimension, recoderule, responsebankscale, scorepoints, surrogateitemid, weight, _key, _efk_surrogateitskey, _fk_adminsubject, _fk_item, _fk_measurementmodel)" +
            " VALUES ('parentDimension', '', NULL, 1, NULL, 1, :key, NULL, '(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015', '187-1233', 1)";

        final String segmentItemScoreDimensionSQL = "INSERT INTO itemscoredimension (dimension, recoderule, responsebankscale, scorepoints, surrogateitemid, weight, _key, _efk_surrogateitskey, _fk_adminsubject, _fk_item, _fk_measurementmodel)" +
            " VALUES ('segmentDimension', '', NULL, 1, NULL, 1, :key, NULL, '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', '187-1234', 1)";

        final String measurementModelSQL = "INSERT INTO measurementmodel (modelnumber, modelname) VALUES (1, 'IRT3PL');";

        final String measureParameterSQL =
            "INSERT INTO measurementparameter (_fk_measurementmodel, parmnum, parmname, parmdescription) VALUES\n" +
            "(1, 0, 'a', 'Slope (a)'),\n" +
            "(1, 1, 'b', 'Difficulty (b)'),\n" +
            "(1, 2, 'c', 'Guessing (c)')";

        final String parentItemMeasurementParameter = "INSERT INTO itemmeasurementparameter (_fk_itemscoredimension, _fk_measurementparameter, parmvalue) VALUES (:key, 1, 0.85457);";
        final String segmentItemMeasurementParameterSQL = "INSERT INTO itemmeasurementparameter (_fk_itemscoredimension, _fk_measurementparameter, parmvalue) VALUES (:key, 1, 1.71271);";

        final SqlParameterSource emptyParameters = new MapSqlParameterSource();
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2, emptyParameters);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a, emptyParameters);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b, emptyParameters);
        jdbcTemplate.update(tblSetAdminItemsInsertSQL, emptyParameters);

        SqlParameterSource parameters = new MapSqlParameterSource("key", UuidAdapter.getBytesFromUUID(UUID.randomUUID()));
        jdbcTemplate.update(parentItemScoreDimensionSQL, parameters);
        jdbcTemplate.update(parentItemMeasurementParameter, parameters);

        parameters = new MapSqlParameterSource("key", UuidAdapter.getBytesFromUUID(UUID.randomUUID()));
        jdbcTemplate.update(segmentItemScoreDimensionSQL, parameters);
        jdbcTemplate.update(segmentItemMeasurementParameterSQL, parameters);

        jdbcTemplate.update(measurementModelSQL, emptyParameters);
        jdbcTemplate.update(measureParameterSQL, emptyParameters);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindItemMeasurements() {
        List<ItemMeasurement> measurements = itemMeasurementQueryRepository.findItemMeasurements("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015", "(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015");
        assertThat(measurements).hasSize(1);

        ItemMeasurement measurement = measurements.get(0);

        assertThat(measurement.getDimension()).isEqualTo("segmentDimension");
        assertThat(measurement.getItemKey()).isEqualTo("187-1234");
        assertThat(measurement.getItemResponseTheoryModel()).isEqualTo("IRT3PL");
        assertThat(measurement.getParameterDescription()).isEqualTo("Difficulty (b)");
        assertThat(measurement.getParameterName()).isEqualTo("b");
        assertThat(measurement.getParameterNumber()).isEqualTo(1);
        assertThat(measurement.getParameterValue()).isEqualTo(1.71271f);
        assertThat(measurement.getSegmentPosition()).isEqualTo(1);
    }
}