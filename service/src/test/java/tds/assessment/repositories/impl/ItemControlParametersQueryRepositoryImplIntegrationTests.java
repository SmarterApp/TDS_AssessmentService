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

import tds.assessment.ItemControlParameter;
import tds.assessment.repositories.ItemControlParametersQueryRepository;
import tds.common.data.mysql.UuidAdapter;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ItemControlParametersQueryRepositoryImplIntegrationTests {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ItemControlParametersQueryRepository itemControlParametersQueryRepository;

    @Before
    public void setUp() {
        itemControlParametersQueryRepository = new ItemControlParametersQueryRepositoryImpl(jdbcTemplate);

        final String itemSelectionParmSQL = "INSERT INTO tblitemselectionparm " +
            "(_fk_adminsubject, bpelementid, name, value, label, _key) " +
            "VALUES ('(SBAC_PT)SBAC-IRP-CAT-Calc-MATH-7-Summer-2015-2016', 'SBAC-IRP-CAT-Calc-MATH-7', 'proficientPLevel', '3', 'Level at which student is considered proficient for the test', :key);";

        final String performanceLevelsSQL = "INSERT INTO performancelevels " +
            "(_fk_content, plevel, thetalo, thetahi) " +
            "VALUES ('(SBAC_PT)SBAC-IRP-CAT-Calc-MATH-7-Summer-2015-2016', 3, 99.9, 0)";

        SqlParameterSource parameters = new MapSqlParameterSource("key", UuidAdapter.getBytesFromUUID(UUID.randomUUID()));
        jdbcTemplate.update(itemSelectionParmSQL, parameters);
        jdbcTemplate.update(performanceLevelsSQL, new MapSqlParameterSource());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldFindItemControlParameters() {
        List<ItemControlParameter> controlParameters = itemControlParametersQueryRepository.findControlParametersForSegment("(SBAC_PT)SBAC-IRP-CAT-Calc-MATH-7-Summer-2015-2016");

        assertThat(controlParameters).hasSize(2);

        ItemControlParameter selectionParameter = null;
        ItemControlParameter performanceParameter = null;

        for (ItemControlParameter parameter : controlParameters) {
            if (parameter.getName().equals("proficientPLevel")) {
                selectionParameter = parameter;
            } else {
                performanceParameter = parameter;
            }
        }

        assertThat(performanceParameter).isNotNull();
        assertThat(selectionParameter).isNotNull();

        assertThat(selectionParameter.getBlueprintElementId()).isEqualTo("SBAC-IRP-CAT-Calc-MATH-7");
        assertThat(selectionParameter.getValue()).isEqualTo("3");

        assertThat(performanceParameter.getBlueprintElementId()).isEqualTo("SBAC-IRP-CAT-Calc-MATH-7");
        assertThat(performanceParameter.getValue()).isEqualTo("99.9000015258789");
        assertThat(performanceParameter.getName()).isEqualTo("proficientTheta");
    }
}