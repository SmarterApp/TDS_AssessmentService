package tds.assessment.repositories;

import java.util.List;

import tds.assessment.ItemGroup;

/**
 * Handles ItemGroups
 */
public interface ItemGroupQueryRepository {
    /**
     * Finds the {@link tds.assessment.ItemGroup} associated with the segment
     * @param segmentKey the segment key
     * @return List of {@link tds.assessment.ItemGroup}
     */
    List<ItemGroup> findItemGroupsBySegment(final String segmentKey);
}
