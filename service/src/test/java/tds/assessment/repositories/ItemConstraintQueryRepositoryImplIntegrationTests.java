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

import tds.assessment.ItemConstraint;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ItemConstraintQueryRepositoryImplIntegrationTests
{
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ItemConstraintQueryRepository repository;

    @Before
    public void setUp() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        final String itemConstraintsInsertSql =
            "INSERT INTO configs.client_test_itemconstraint" +
            "(clientname, testid, propname, propvalue, tooltype, toolvalue, item_in)" +
            "VALUES " +
            "('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'ER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_ER', 0), \n" +
            "('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'MI', 'Item Types Exclusion', 'TDS_ItemTypeExcl_MI', 0), \n" +
            "('SBAC_PT', 'IRP-Perf-ELA-11', '--ITEMTYPE--', 'WER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_WER', 0), \n" +
            "('SBAC_PT', 'IRP-Perf-ELA-11', 'Language', 'ENU', 'Language', 'ENU', 1), \n" +
            "('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'ER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_ER', 0), \n" +
            "('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'MI', 'Item Types Exclusion', 'TDS_ItemTypeExcl_MI', 0), \n" +
            "('SBAC_PT', 'IRP-Perf-ELA-3', '--ITEMTYPE--', 'WER', 'Item Types Exclusion', 'TDS_ItemTypeExcl_WER', 0), \n" +
            "('SBAC_PT', 'IRP-Perf-ELA-3', 'Language', 'ENU', 'Language', 'ENU', 1)";

        jdbcTemplate.update(itemConstraintsInsertSql);
    }

    @Test
    public void shouldRetrieveAllForExam() {
        final String assessmentId = "IRP-Perf-ELA-11";
        List<ItemConstraint> itemConstraints = repository.findItemConstraints("SBAC_PT", "IRP-Perf-ELA-11");
        assertThat(itemConstraints).hasSize(4);
        ItemConstraint languageConstraint = null;
        ItemConstraint erConstraint = null;

        for (ItemConstraint constraint : itemConstraints) {
            if (constraint.getPropertyValue().equals("ER")) {
                erConstraint = constraint;
                continue;
            } else if (constraint.getPropertyName().equals("Language")) {
                languageConstraint = constraint;
                continue;
            }
        }

        assertThat(languageConstraint.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(languageConstraint.getPropertyName()).isEqualTo("Language");
        assertThat(languageConstraint.getPropertyValue()).isEqualTo("ENU");
        assertThat(languageConstraint.getToolType()).isEqualTo("Language");
        assertThat(languageConstraint.getToolValue()).isEqualTo("ENU");
        assertThat(languageConstraint.isInclusive()).isTrue();
        assertThat(erConstraint.getAssessmentId()).isEqualTo(assessmentId);
        assertThat(erConstraint.getPropertyName()).isEqualTo("--ITEMTYPE--");
        assertThat(erConstraint.getPropertyValue()).isEqualTo("ER");
        assertThat(erConstraint.getToolType()).isEqualTo("Item Types Exclusion");
        assertThat(erConstraint.getToolValue()).isEqualTo("TDS_ItemTypeExcl_ER");
        assertThat(erConstraint.isInclusive()).isFalse();
    }

    @Test
    public void shouldRetrieveNoConstraintsForExam() {
        List<ItemConstraint> itemConstraints = repository.findItemConstraints("SBAC_PT", "NoExam");
        assertThat(itemConstraints).isEmpty();
    }
}
