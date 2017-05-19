package tds.assessment.repositories.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import tds.assessment.Item;
import tds.assessment.ItemFileMetadata;
import tds.assessment.ItemFileType;
import tds.assessment.ItemProperty;
import tds.assessment.repositories.ItemQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ItemQueryRepositoryImplIntegrationTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ItemQueryRepository repository;

    @Before
    public void setUp() {

        // Single-segmented test
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
                "   (_key, _efk_itembank, _efk_item, itemtype, filepath, filename, scorepoint, itemid)\n" +
                "VALUES \n" +
                "   ('187-1234', 187, 1234, 'ER', 'item-187-1234/', 'item-187-1234.xml', 1, ''),\n" +
                "   ('187-1235', 187, 1235, 'MI','item-187-1235/' , 'item-187-1235.xml', -1, 'clientid'),\n" +
                "   ('187-1236', 187, 1236, 'WER','item-187-1236/' , 'item-187-1236.xml', 1, ''),\n" +
                "   ('187-1237', 187, 1237, 'MC','item-187-1237/' , 'item-187-1237.xml', 0, '')";

        final String tblSetAdminItemsInsertSQL =
            "INSERT INTO itembank.tblsetofadminitems \n" +
                "   (_fk_item, _fk_adminsubject, groupid, groupkey, blockid, itemposition, isfieldtest, isactive, isrequired, " +
                "   strandname, isprintable, responsemimetype, notforscoring, irt_model, irt_a, irt_b, irt_c, clstring, bvector) \n" +
                "VALUES \n" +
                "   ('187-1234', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'G-1', 'GK-1', 'A', 1, 0, 1, 1, 'strand1', 0, 'text/plain', 0, NULL, NULL, NULL, NULL, NULL, NULL),\n" +
                "   ('187-1235', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 'G-2', 'GK-2', 'A', 2, 0, 1, 1, 'strand2', 0, 'text/xml', 1, NULL, NULL, NULL, NULL, NULL, NULL),\n" +
                "   ('187-1236', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 'G-3', 'GK-3', 'A', 1, 0, 1, 1, 'strand3', 1, 'foo', 0, 'IRT3PLn', 0.54343, '1.2160500288009644', 0, 'SBAC-2-W;SBAC-2-W|9-5', 1.21605), \n" +
                "   ('187-1237', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 'G-4', 'GK-4', 'A', 1, 1, 1, 1, 'silver strand', 0, 'bar', 1, NULL, NULL, NULL, NULL, NULL, NULL)";

        final String tblItemPropsInsertSQL =
            "INSERT INTO itembank.tblitemprops (_fk_item, propname, propvalue, propdescription, _fk_adminsubject, isactive) VALUES \n" +
                "('item-1', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 1),\n" +
                "('item-23', 'Language', 'ENU', 'Supported Language', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 0),\n" +
                "('item-7', '--ITEMTYPE--', 'ER', 'Extended Response', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 1),\n" +
                "('item-2', 'Language', 'ENU-Braille', 'Supported Language', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 1);";

        final String tblTestFormInsertSQL =
            "INSERT INTO testform (_fk_adminsubject, _efk_itsbank, _efk_itskey, formid, language, _key, itsid, iteration, loadconfig, updateconfig, cohort)\n" +
                "VALUES\n" +
                "('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 528, 528, 'PracTest::MG8::S1::SP14', 'ENU', '187-528', NULL, 0, 8233, NULL, 'Default'),\n" +
                "('(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 529, 529, 'PracTest::MG8::S1::SP14::Braille', 'ENU-Braille', '187-529', NULL, 0, 8233, NULL, 'Default'),\n" +
                "('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 532, 532, 'PracTest::MG8::S2::SP14::Braille', 'ENU', '187-532', NULL, 0, 8233, NULL, 'Default'),\n" +
                "('(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 534, 534, 'PracTest::MG8::S2::SP14', 'ENU-Braille', '187-534', NULL, 0, 8233, NULL, 'Default')";
        final String tblTestFormItemInsertSQL =
            "INSERT INTO testformitem (_fk_item, _efk_itsformkey, formposition, _fk_adminsubject, _fk_testform, isactive)\n" +
                "VALUES\n" +
                "('187-1234', 528, 1, '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', '187-528', 1),\n" +
                "('187-1234', 529, 1, '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', '187-529', 1),\n" +
                "('187-1235', 528, 2, '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', '187-528', 1),\n" +
                "('187-1235', 529, 2, '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', '187-529', 1),\n" +
                "('187-1236', 532, 1, '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', '187-532', 1),\n" +
                "('187-1236', 534, 1, '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', '187-534', 1)\n";

        final String tblClientInsertFromSQL =
            "INSERT INTO itembank.tblclient (_key, name, description, homepath) \n" +
                "VALUES \n" +
                "(1, 'SBAC', NULL, '/usr/local/tomcat/resources/tds/'),\n" +
                "(2, 'SBAC_PT', NULL, '/usr/local/tomcat/resources/tds/')";

        final String tblItemBankInsertFromSQL =
            "INSERT INTO itembank.tblitembank (_fk_client, homepath, itempath, stimulipath, name, _efk_itembank, _key, contract) \n" +
                "VALUES \n" +
                "(1, 'bank/', 'items/', 'stimuli/', NULL, 200, 200, NULL), \n" +
                "(2, 'bank/', 'items/', 'stimuli/', NULL, 187, 187, NULL)";

        final String tblStimulusInsertFromSQL =
            "INSERT INTO itembank.tblstimulus (_efk_itembank, _efk_itskey, clientid, filepath, filename, version, datelastupdated, _key, contentsize, loadconfig, updateconfig) \n" +
                "VALUES\n" +
                "(187, 4321, NULL, 'stim-187-4321/', 'stim-187-4321.xml', NULL, '2016-08-10 19:05:52', '187-4321', NULL, 8233, NULL), \n" +
                "(187, 5321, NULL, 'stim-187-5321/', 'stim-187-5321.xml', NULL, '2016-08-10 19:05:52', '187-5321', NULL, 8185, NULL), \n" +
                "(187, 6321, NULL, 'stim-187-6321/', 'stim-187-6321.xml', NULL, '2016-08-10 19:05:52', '187-6321', NULL, 8185, NULL), \n" +
                "(187, 7321, NULL, 'stim-187-7321/', 'stim-187-7321.xml', NULL, '2016-08-10 19:05:52', '187-7321', NULL, 8185, NULL)";

        final String tblSetOfItemsStimuli =
            "INSERT INTO itembank.tblsetofitemstimuli (_fk_item, _fk_stimulus, _fk_adminsubject, loadconfig) VALUES \n" +
                "('187-1234', '187-4321', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 8233), \n" +
                "('187-1235', '187-5321', '(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015', 8185), \n" +
                "('187-1236', '187-6321', '(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015', 8185), \n" +
                "('187-1237', '187-7321', '(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016', 8185);";

        final String tblStrand =
            "INSERT INTO itembank.tblstrand (_fk_subject, name, _key) \n" +
                "VALUES ('ELA', 'contentLevel', 'strand1')";

        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL1);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2a);
        jdbcTemplate.update(tblSetOfAdminSubjectsInsertSQL2b);
        jdbcTemplate.update(tblItemPropsInsertSQL);
        jdbcTemplate.update(tblItemInsertSQL);
        jdbcTemplate.update(tblSetAdminItemsInsertSQL);
        jdbcTemplate.update(tblTestFormInsertSQL);
        jdbcTemplate.update(tblTestFormItemInsertSQL);
        jdbcTemplate.update(tblClientInsertFromSQL);
        jdbcTemplate.update(tblItemBankInsertFromSQL);
        jdbcTemplate.update(tblStimulusInsertFromSQL);
        jdbcTemplate.update(tblSetOfItemsStimuli);
        jdbcTemplate.update(tblStrand);
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
            } else if (property.getItemId().equals("item-7")) {
                prop2 = property;
            }
        }

        assertThat(prop1).isNotNull();
        assertThat(prop2).isNotNull();

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
    public void shouldRetrieveSingleItemNonSegmentedAdaptiveTest() {
        List<Item> items = repository.findItemsForAssessment("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(items).hasSize(1);

        Item item1 = items.get(0);

        assertThat(item1).isNotNull();

        assertThat(item1.getId()).isEqualTo("187-1237");
        assertThat(item1.getGroupId()).isEqualTo("G-4");
        assertThat(item1.getGroupKey()).isEqualTo("GK-4");
        assertThat(item1.getBlockId()).isEqualTo("A");
        assertThat(item1.getItemType()).isEqualTo("MC");
        assertThat(item1.getPosition()).isEqualTo(1);
        assertThat(item1.getSegmentKey()).isEqualTo("(SBAC_PT)IRP-Perf-ELA-11-Summer-2015-2016");
        assertThat(item1.getStrand()).isEqualTo("silver strand");
        assertThat(item1.isFieldTest()).isTrue();
        assertThat(item1.isRequired()).isTrue();
        assertThat(item1.getFormKey()).isNull();
        assertThat(item1.getItemFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/items/item-187-1237/item-187-1237.xml");
        assertThat(item1.getStimulusFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/stimuli/stim-187-7321/stim-187-7321.xml");
        assertThat(item1.isPrintable()).isFalse();
        assertThat(item1.isActive()).isFalse();
    }

    @Test
    public void shouldRetrieveSingleItemSegmentedTest() {
        List<Item> items = repository.findItemsForAssessment("(SBAC_PT)SBAC-Mathematics-8-Spring-2013-2015");

        // Validate items in each segment
        assertThat(items).hasSize(6);
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

        assertThat(item1Seg1).isNotNull();
        assertThat(item2Seg1).isNotNull();
        assertThat(item1Seg2).isNotNull();

        assertThat(item1Seg1.getGroupId()).isEqualTo("G-1");
        assertThat(item1Seg1.getGroupKey()).isEqualTo("GK-1");
        assertThat(item1Seg1.getBlockId()).isEqualTo("A");
        assertThat(item1Seg1.getItemType()).isEqualTo("ER");
        assertThat(item1Seg1.getPosition()).isEqualTo(1);
        assertThat(item1Seg1.getFormPosition()).isEqualTo(1);
        assertThat(item1Seg1.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
        assertThat(item1Seg1.isFieldTest()).isFalse();
        assertThat(item1Seg1.isRequired()).isTrue();
        assertThat(item1Seg1.getStrand()).isEqualTo("strand1");
        assertThat(item1Seg1.getItemFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/items/item-187-1234/item-187-1234.xml");
        assertThat(item1Seg1.getStimulusFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/stimuli/stim-187-4321/stim-187-4321.xml");
        assertThat(item1Seg1.isPrintable()).isFalse();
        assertThat(item1Seg1.getContentLevel()).isEqualTo("contentLevel");
        assertThat(item1Seg1.getMaxScore()).isEqualTo(1);
        assertThat(item1Seg1.getMimeType()).isEqualTo("text/plain");
        assertThat(item1Seg1.getItemKey()).isEqualTo(1234);
        assertThat(item1Seg1.getBankKey()).isEqualTo(187);

        assertThat(item2Seg1.getGroupId()).isEqualTo("G-2");
        assertThat(item2Seg1.getGroupKey()).isEqualTo("GK-2");
        assertThat(item2Seg1.getItemType()).isEqualTo("MI");
        assertThat(item2Seg1.getPosition()).isEqualTo(2);
        assertThat(item2Seg1.getFormPosition()).isEqualTo(2);
        assertThat(item2Seg1.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG1-MATH-8-Spring-2013-2015");
        assertThat(item2Seg1.isFieldTest()).isFalse();
        assertThat(item2Seg1.isRequired()).isTrue();
        assertThat(item2Seg1.getStrand()).isEqualTo("strand2");
        assertThat(item2Seg1.getItemFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/items/item-187-1235/item-187-1235.xml");
        assertThat(item2Seg1.getStimulusFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/stimuli/stim-187-5321/stim-187-5321.xml");
        assertThat(item2Seg1.isPrintable()).isFalse();
        assertThat(item2Seg1.getClientId()).isEqualTo("clientid");
        assertThat(item2Seg1.isNotForScoring()).isTrue();

        assertThat(item1Seg2.getGroupId()).isEqualTo("G-3");
        assertThat(item1Seg2.getGroupKey()).isEqualTo("GK-3");
        assertThat(item1Seg2.getItemType()).isEqualTo("WER");
        assertThat(item1Seg2.getPosition()).isEqualTo(1);
        assertThat(item1Seg2.getFormPosition()).isEqualTo(1);
        assertThat(item1Seg2.getSegmentKey()).isEqualTo("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");
        assertThat(item1Seg2.isFieldTest()).isFalse();
        assertThat(item1Seg2.isRequired()).isTrue();
        assertThat(item1Seg2.getStrand()).isEqualTo("strand3");
        assertThat(item1Seg2.getItemFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/items/item-187-1236/item-187-1236.xml");
        assertThat(item1Seg2.getStimulusFilePath()).isEqualTo("/usr/local/tomcat/resources/tds/bank/stimuli/stim-187-6321/stim-187-6321.xml");
        assertThat(item1Seg2.isPrintable()).isTrue();
    }

    @Test
    public void shouldRetrieveNoProperties() {
        List<ItemProperty> properties = repository.findActiveItemsProperties("InvalidAssessment");
        assertThat(properties.isEmpty());
    }

    @Test
    public void shouldRetrieveStimulusItem() {
        Optional<ItemFileMetadata> maybeItemFile = repository.findItemFileMetadataByStimulusKey("SBAC_PT", 187, 4321);

        assertThat(maybeItemFile).isPresent();

        ItemFileMetadata itemFileMetadata = maybeItemFile.get();

        assertThat(itemFileMetadata.getItemType()).isEqualTo(ItemFileType.STIMULUS);
        assertThat(itemFileMetadata.getFileName()).isEqualTo("stim-187-4321.xml");
        assertThat(itemFileMetadata.getFilePath()).isEqualTo("stim-187-4321/");
        assertThat(itemFileMetadata.getId()).isEqualTo("187-4321");
    }

    @Test
    public void shouldHandleStimulusItemNotFound() {
        Optional<ItemFileMetadata> maybeItemFile = repository.findItemFileMetadataByStimulusKey("SBAC_PT", 999, 4321);

        assertThat(maybeItemFile).isNotPresent();
    }

    @Test
    public void shouldRetrieveItem() {
        Optional<ItemFileMetadata> maybeItemFile = repository.findItemFileMetadataByItemKey("SBAC_PT", 187, 1234);

        assertThat(maybeItemFile).isPresent();

        ItemFileMetadata itemFileMetadata = maybeItemFile.get();

        assertThat(itemFileMetadata.getItemType()).isEqualTo(ItemFileType.ITEM);
        assertThat(itemFileMetadata.getFileName()).isEqualTo("item-187-1234.xml");
        assertThat(itemFileMetadata.getFilePath()).isEqualTo("item-187-1234/");
        assertThat(itemFileMetadata.getId()).isEqualTo("187-1234");
    }

    @Test
    public void shouldHandleItemNotFound() {
        Optional<ItemFileMetadata> maybeItemFile = repository.findItemFileMetadataByItemKey("SBAC_PT", 999, 1234);

        assertThat(maybeItemFile).isNotPresent();
    }

    @Test
    public void shouldFindItemsForSegmentKey() {
        List<Item> items = repository.findItemsForSegment("(SBAC_PT)SBAC-SEG2-MATH-8-Spring-2013-2015");

        assertThat(items.get(0)).isEqualToComparingFieldByField(items.get(1));
        assertThat(items).hasSize(2);

        Item item = items.get(0);

        assertThat(item.getId()).isEqualTo("187-1236");
        assertThat(item.getItemType()).isEqualTo("WER");
        assertThat(item.getGroupId()).isEqualTo("G-3");
        assertThat(item.getGroupKey()).isEqualTo("GK-3");
        assertThat(item.getBlockId()).isEqualTo("A");
        assertThat(item.getPosition()).isEqualTo(1);
        assertThat(item.getStrand()).isEqualTo("strand3");
        assertThat(item.getItemResponseTheoryModel()).isEqualTo("IRT3PLN");
        assertThat(item.getItemResponseTheoryAParameter()).isEqualTo(0.54343f);
        assertThat(item.getItemResponseTheoryBParameter()).isEqualTo("1.2160500288009644");
        assertThat(item.getItemResponseTheoryCParameter()).isEqualTo(0f);
        assertThat(item.getbVector()).isEqualTo("1.21605");
        assertThat(item.getClaims()).isEqualTo("SBAC-2-W;SBAC-2-W|9-5");
        assertThat(item.isActive()).isTrue();
        assertThat(item.getFormPosition()).isEqualTo(1);

        Item item2 = items.get(1);

        assertThat(item2.getId()).isEqualTo("187-1236");
        assertThat(item2.getItemType()).isEqualTo("WER");
        assertThat(item2.getGroupId()).isEqualTo("G-3");
        assertThat(item2.getGroupKey()).isEqualTo("GK-3");
        assertThat(item2.getBlockId()).isEqualTo("A");
        assertThat(item2.getPosition()).isEqualTo(1);
        assertThat(item2.getStrand()).isEqualTo("strand3");
        assertThat(item2.getItemResponseTheoryModel()).isEqualTo("IRT3PLN");
        assertThat(item2.getItemResponseTheoryAParameter()).isEqualTo(0.54343f);
        assertThat(item2.getItemResponseTheoryBParameter()).isEqualTo("1.2160500288009644");
        assertThat(item2.getItemResponseTheoryCParameter()).isEqualTo(0f);
        assertThat(item2.getbVector()).isEqualTo("1.21605");
        assertThat(item2.getClaims()).isEqualTo("SBAC-2-W;SBAC-2-W|9-5");
        assertThat(item2.isActive()).isTrue();
        assertThat(item2.getFormPosition()).isEqualTo(1);
    }
}
