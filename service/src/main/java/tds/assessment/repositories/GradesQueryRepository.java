package tds.assessment.repositories;

import java.util.List;

/**
 * A repository for retrieving assessment grades
 */
public interface GradesQueryRepository {
    /**
     * Finds all eligible grades for the {@link tds.assessment.Assessment}
     *
     * @param assessmentKey The key of the {@link tds.assessment.Assessment} to find eligible grades for
     * @return The list of eligible grades for the assessment
     */
    List<String> findGrades(final String assessmentKey);
}
