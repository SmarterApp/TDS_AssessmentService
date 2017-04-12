package tds.assessment.repositories;

import java.util.List;

import tds.assessment.ItemControlParameter;

/**
 * Item control parameters repository
 */
public interface ItemControlParametersQueryRepository {
    /**
     * Finds the {@link tds.assessment.ItemControlParameter} for a segment
     *
     * @param segmentKey the segment key
     * @return list of the {@link tds.assessment.ItemControlParameter}
     */
    List<ItemControlParameter> findControlParametersForSegment(final String segmentKey);
}
