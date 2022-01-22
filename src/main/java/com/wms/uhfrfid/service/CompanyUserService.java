package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.CompanyUser;
import com.wms.uhfrfid.repository.CompanyUserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyUser}.
 */
@Service
@Transactional
public class CompanyUserService {

    private final Logger log = LoggerFactory.getLogger(CompanyUserService.class);

    private final CompanyUserRepository companyUserRepository;

    public CompanyUserService(CompanyUserRepository companyUserRepository) {
        this.companyUserRepository = companyUserRepository;
    }

    /**
     * Save a companyUser.
     *
     * @param companyUser the entity to save.
     * @return the persisted entity.
     */
    public CompanyUser save(CompanyUser companyUser) {
        log.debug("Request to save CompanyUser : {}", companyUser);
        return companyUserRepository.save(companyUser);
    }

    /**
     * Partially update a companyUser.
     *
     * @param companyUser the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyUser> partialUpdate(CompanyUser companyUser) {
        log.debug("Request to partially update CompanyUser : {}", companyUser);

        return companyUserRepository
            .findById(companyUser.getId())
            .map(existingCompanyUser -> {
                return existingCompanyUser;
            })
            .map(companyUserRepository::save);
    }

    /**
     * Get all the companyUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyUser> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyUsers");
        return companyUserRepository.findAll(pageable);
    }

    /**
     * Get one companyUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyUser> findOne(Long id) {
        log.debug("Request to get CompanyUser : {}", id);
        return companyUserRepository.findById(id);
    }

    /**
     * Delete the companyUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyUser : {}", id);
        companyUserRepository.deleteById(id);
    }
}
