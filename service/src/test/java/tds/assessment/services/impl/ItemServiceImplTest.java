package tds.assessment.services.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import tds.assessment.ItemConstraint;
import tds.assessment.repositories.ItemConstraintQueryRepository;
import tds.assessment.repositories.ItemPropertyQueryRepository;
import tds.assessment.repositories.impl.ItemPropertyQueryRepositoryImpl;
import tds.assessment.services.ItemService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemServiceImplTest {
    private ItemService itemService;
    private ItemConstraintQueryRepository itemConstraintQueryRepository;
    private ItemPropertyQueryRepository itemPropertyQueryRepository;

    @Before
    public void setUp() {
        itemConstraintQueryRepository = mock(ItemConstraintQueryRepository.class);
        itemService = new ItemServiceImpl(itemConstraintQueryRepository, itemPropertyQueryRepository);
    }

    @Test
    public void shouldReturnListOfItemConstraints() {
        final String assessmentId = "assesssment-1";
        List<ItemConstraint> constraints = new ArrayList<>();
        constraints.add(
                new ItemConstraint.Builder()
                        .withAssessmentId(assessmentId)
                        .withInclusive(true)
                        .withPropertyName("Language")
                        .withPropertyValue("ENU")
                        .withToolType("Language")
                        .withToolValue("ENU")
                        .build()
        );
        constraints.add(
                new ItemConstraint.Builder()
                        .withAssessmentId(assessmentId)
                        .withInclusive(false)
                        .withPropertyName("--ITEMTYPE--")
                        .withPropertyValue("ER")
                        .withToolType("TOOLTYPE")
                        .withToolValue("ERRRRR")
                        .build()
        );

        when(itemConstraintQueryRepository.findItemConstraints("SBAC_PT", assessmentId)).thenReturn(constraints);
        List<ItemConstraint> returnedConstraints = itemService.findItemConstraints("SBAC_PT", assessmentId);
        verify(itemConstraintQueryRepository).findItemConstraints("SBAC_PT", assessmentId);
        assertThat(returnedConstraints).isEqualTo(constraints);
    }
}
