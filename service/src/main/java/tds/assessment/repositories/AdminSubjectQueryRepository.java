package tds.assessment.repositories;

import java.util.Optional;

import tds.assessment.SetOfAdminSubject;

/**
 * Queries for admin subject related entities
 */
public interface AdminSubjectQueryRepository {
    /**
     * Finds a {@link tds.assessment.SetOfAdminSubject} by key
     *
     * @param adminSubjectKey the unique key for the admin subject
     * @return {@link tds.assessment.SetOfAdminSubject} if found otherwise empty
     */
    Optional<SetOfAdminSubject> findByKey(String adminSubjectKey);
}
