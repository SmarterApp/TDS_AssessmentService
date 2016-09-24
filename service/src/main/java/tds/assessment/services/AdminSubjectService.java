package tds.assessment.services;

import java.util.Optional;

import tds.assessment.SetOfAdminSubject;

/**
 * Service handling interaction with the Admin subjects
 */
public interface AdminSubjectService {
    /**
     * @param setOfAdminObjectKey key to the set of admin object
     * @return {@link tds.assessment.SetOfAdminSubject} if found otherwise empty
     */
    Optional<SetOfAdminSubject> findSetOfAdminObjectByKey(String setOfAdminObjectKey);
}
