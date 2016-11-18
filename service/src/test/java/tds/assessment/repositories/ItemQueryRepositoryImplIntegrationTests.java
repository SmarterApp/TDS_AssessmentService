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

import tds.assessment.Item;
import tds.assessment.ItemConstraint;
import tds.assessment.ItemProperty;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ItemQueryRepositoryImplIntegrationTests {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ItemQueryRepository repository;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // Non-segmented, test
        final String tblSetOfAdminSubjectsInsertSQL1 = "INSERT INTO itembank.tblsetofadminsubjects VALUES ('(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016','SBAC_PT', 'SBAC_PT-ELA','IRP-Perf-ELA-11'," +
                "0,1,4,4,1,1,NULL,NULL,1,4,NULL,'virtual',NULL,5,1,20,1,5,NULL,NULL,1,1,8185,8185,5,0,'SBAC_PT',NULL,'ABILITY',NULL,1,NULL,1,1,NULL,NULL,0,0,0,0," +
                "0,'bp1',NULL,NULL,'summative');";
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

        final String tblItemInsertSQL =
                "INSERT INTO itembank.tblitem\n" +
                "   (_key, _efk_itembank, _efk_item, itemtype)\n" +
                "VALUES \n" +
                "   ('187-1234', 187, 123, 'ER'),\n" +
                "   ('187-1235', 187, 124, 'MI'),\n" +
                "   ('187-1236', 187, 125, 'WER'),\n" +
                "   ('187-1237', 187, 126, 'MC')";

        final String tblSetAdminItemsInsertSQL =
                "INSERT INTO itembank.tblsetofadminitems \n" +
                "   (_fk_item, _fk_adminsubject, groupid, groupkey, itemposition, isfieldtest, isactive, isrequired, strandname)\n" +
                "VALUES \n" +
                "   ('187-1234', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'G-1', 'GK-1', 1, 0, 1, 1, 'strand1'),\n" +
                "   ('187-1235', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'G-2', 'GK-2', 2, 0, 1, 1, 'strand2'),\n" +
                "   ('187-1236', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 'G-3', 'GK-3', 1, 0, 1, 1, 'strand3'),\n" +
                "   ('187-1237', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 'G-4', 'GK-4', 1, 1, 1, 1, 'silver strand')";

        final String tblItemPropsInsertSQL =
                "INSERT INTO itembank.tblitemprops (_fk_item, propname, propvalue, propdescription, _fk_adminsubject, isactive) VALUES \n" +
                "('item-1', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1),\n" +
                "('item-23', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 0),\n" +
                "('item-7', '--ITEMTYPE--', 'ER', 'Extended Response', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 1),\n" +
                "('item-2', 'Language', 'ENU-Braille', 'Supported Language', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 1);";

        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(tblItemPropsInsertSQL);
        jdbcTemplate.update(tblItemInsertSQL);
        jdbcTemplate.update(tblSetAdminItemsInsertSQL);
    }

    @Test
    public void shouldRetrieveThreeActiveItemPropsForMultiSeg() {
        List<ItemProperty> properties = repository.findActiveItemsProperties("(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015");
        assertThat(properties).hasSize(3);

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
    public void shouldRetrieveActiveItemPropsForNonSegAssesssment() {
        List<ItemProperty> properties = repository.findActiveItemsProperties("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(properties).hasSize(1);
        ItemProperty prop1 = properties.get(0);
        assertThat(prop1.getItemId()).isEqualTo("item-2");
        assertThat(prop1.getName()).isEqualTo("Language");
        assertThat(prop1.getValue()).isEqualTo("ENU-Braille");
        assertThat(prop1.getDescription()).isEqualTo("Supported Language");
    }

    @Test
    public void shouldRetrieveSingleItemNonSegmentedTest() {
        List<Item> items = repository.findItemsForAssessment("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(items).hasSize(1);
        Item item1 = items.get(0);
        assertThat(item1.getId()).isEqualTo("187-1237");
        assertThat(item1.getGroupId()).isEqualTo("G-4");
        assertThat(item1.getGroupKey()).isEqualTo("GK-4");
        assertThat(item1.getItemType()).isEqualTo("MC");
        assertThat(item1.getPosition()).isEqualTo(1);
        assertThat(item1.getSegmentKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(item1.getStrand()).isEqualTo("silver strand");
        assertThat(item1.isActive()).isTrue();
        assertThat(item1.isFieldTest()).isTrue();
        assertThat(item1.isRequired()).isTrue();
    }

    @Test
    public void shouldRetrieveSingleItemSegmentedTest() {
        List<Item> items = repository.findItemsForAssessment("(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015");

        // Validate items in each segment
        assertThat(items).hasSize(3);
        Item item1Seg1 = null;
        Item item2Seg1 = null;
        Item item1Seg2 = null;
        for (Item item : items) {
            if ("187-1234".equals(item.getId())) {
                item1Seg1 = item;
            } else if ("187-1235".equals(item.getId())) {
                item2Seg1 = item;
            } else if ("187-1236".equals(item.getId())) {
                item1Seg2 = item;
            }
        }
        assertThat(item1Seg1.getGroupId()).isEqualTo("G-1");
        assertThat(item1Seg1.getGroupKey()).isEqualTo("GK-1");
        assertThat(item1Seg1.getItemType()).isEqualTo("ER");
        assertThat(item1Seg1.getPosition()).isEqualTo(1);
        assertThat(item1Seg1.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
        assertThat(item1Seg1.isActive()).isTrue();
        assertThat(item1Seg1.isFieldTest()).isFalse();
        assertThat(item1Seg1.isRequired()).isTrue();
        assertThat(item1Seg1.getStrand()).isEqualTo("strand1");
        assertThat(item2Seg1.getGroupId()).isEqualTo("G-2");
        assertThat(item2Seg1.getGroupKey()).isEqualTo("GK-2");
        assertThat(item2Seg1.getItemType()).isEqualTo("MI");
        assertThat(item2Seg1.getPosition()).isEqualTo(2);
        assertThat(item2Seg1.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
        assertThat(item2Seg1.isActive()).isTrue();
        assertThat(item2Seg1.isFieldTest()).isFalse();
        assertThat(item2Seg1.isRequired()).isTrue();
        assertThat(item2Seg1.getStrand()).isEqualTo("strand2");
        assertThat(item1Seg2.getGroupId()).isEqualTo("G-3");
        assertThat(item1Seg2.getGroupKey()).isEqualTo("GK-3");
        assertThat(item1Seg2.getItemType()).isEqualTo("WER");
        assertThat(item1Seg2.getPosition()).isEqualTo(1);
        assertThat(item1Seg2.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(item1Seg2.isActive()).isTrue();
        assertThat(item1Seg2.isFieldTest()).isFalse();
        assertThat(item1Seg2.isRequired()).isTrue();
        assertThat(item1Seg2.getStrand()).isEqualTo("strand3");
    }

    @Test
    public void shouldRetrieveNoProperties() {
        List<ItemProperty> properties = repository.findActiveItemsProperties("InvalidAssessment");
        assertThat(properties.isEmpty());
    }

}
