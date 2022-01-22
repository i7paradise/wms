package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.CompanyContainer;
import com.wms.uhfrfid.repository.CompanyContainerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyContainer}.
 */
@Service
@Transactional
public class CompanyContainerService {

    private final Logger log = LoggerFactory.getLogger(CompanyContainerService.class);

    private final CompanyContainerRepository companyContainerRepository;

    public CompanyContainerService(CompanyContainerRepository companyContainerRepository) {
        this.companyContainerRepository = companyContainerRepository;
    }

    /**
     * Save a companyContainer.
     *
     * @param companyContainer the entity to save.
     * @return the persisted entity.
     */
    public CompanyContainer save(CompanyContainer companyContainer) {
        log.debug("Request to save CompanyContainer : {}", companyContainer);
        return companyContainerRepository.save(companyContainer);
    }

    /**
     * Partially update a companyContainer.
     *
     * @param companyContainer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyContainer> partialUpdate(CompanyContainer companyContainer) {
        log.debug("Request to partially update CompanyContainer : {}", companyContainer);

        return companyContainerRepository
            .findById(companyContainer.getId())
            .map(existingCompanyContainer -> {
                if (companyContainer.getRfidTag() != null) {
                    existingCompanyContainer.setRfidTag(companyContainer.getRfidTag());
                }
                if (companyContainer.getColor() != null) {
                    existingCompanyContainer.setColor(companyContainer.getColor());
                }

                return existingCompanyContainer;
            })
            .map(companyContainerRepository::save);
    }

    /**
     * Get all the companyContainers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyContainer> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyContainers");
        return companyContainerRepository.findAll(pageable);
    }

    /**
     * Get one companyContainer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyContainer> findOne(Long id) {
        log.debug("Request to get CompanyContainer : {}", id);
        return companyContainerRepository.findById(id);
    }

    /**
     * Delete the companyContainer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyContainer : {}", id);
        companyContainerRepository.deleteById(id);
    }
}
