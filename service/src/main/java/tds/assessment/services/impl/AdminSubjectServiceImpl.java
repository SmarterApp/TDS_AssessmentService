package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import tds.assessment.SetOfAdminSubject;
import tds.assessment.repositories.AdminSubjectQueryRepository;
import tds.assessment.services.AdminSubjectService;

@Service
class AdminSubjectServiceImpl implements AdminSubjectService{
    private final AdminSubjectQueryRepository adminSubjectQueryRepository;

    @Autowired
    public AdminSubjectServiceImpl(AdminSubjectQueryRepository adminSubjectQueryRepository) {
        this.adminSubjectQueryRepository = adminSubjectQueryRepository;
    }

    @Override
    public Optional<SetOfAdminSubject> findSetOfAdminObjectByKey(String setOfAdminObjectKey) {
        return adminSubjectQueryRepository.findByKey(setOfAdminObjectKey);
    }
}
