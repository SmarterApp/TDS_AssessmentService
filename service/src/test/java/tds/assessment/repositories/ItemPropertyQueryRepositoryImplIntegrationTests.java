package tds.assessment.repositories;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import tds.assessment.ItemProperty;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ItemPropertyQueryRepositoryImplIntegrationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ItemPropertyQueryRepository repository;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        final String tblitemPropsInsertSQL = "insert into itembank.tblitemprops (_fk_item, propname, propvalue, propdescription, _fk_adminsubject, isactive) \n" +
                "values \n" +
                "('item-99', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 1),\n" +
                "('item-1', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1),\n" +
                "('item-7', '--ITEMTYPE--', 'ER', 'Extended Response', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1),\n" +
                "('item-23', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 0),\n" +
                "('item-2', 'Language', 'Braille-ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-7-Spring-2013-2015', 1);";

        jdbcTemplate.update(tblitemPropsInsertSQL);
    }

    @Test
    public void shouldRetrieveBothActiveItemPropsForSeg2() {
        List<ItemProperty> properties = repository.findActiveItemsProperties("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(properties).hasSize(2);

        ItemProperty prop1 = null;
        ItemProperty prop2 = null;

        for (ItemProperty property : properties) {
            if (property.getItemId().equals("item-1")) {
                prop1 = property;
                continue;
            } else if (property.getItemId().equals("item-7")) {
                prop2 = property;
                continue;
            }
        }

        assertThat(prop1.getItemId()).isEqualTo("item-1");
        assertThat(prop1.getName()).isEqualTo("Language");
        assertThat(prop1.getValue()).isEqualTo("ENU");
        assertThat(prop1.getDescription()).isEqualTo("Supported Language");
        assertThat(prop2.getItemId()).isEqualTo("item-7");
        assertThat(prop2.getName()).isEqualTo("--ITEMTYPE--");
        assertThat(prop2.getValue()).isEqualTo("ER");
        assertThat(prop2.getDescription()).isEqualTo("Extended Response");
    }

    @Test
    public void shouldRetrieveNoProperties() {
        List<ItemProperty> properties = repository.findActiveItemsProperties("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
    }

}
