package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.CompanyContainer;
import com.wms.uhfrfid.repository.CompanyContainerRepository;
import com.wms.uhfrfid.service.dto.CompanyContainerDTO;
import com.wms.uhfrfid.service.mapper.CompanyContainerMapper;
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

    private final CompanyContainerMapper companyContainerMapper;

    public CompanyContainerService(CompanyContainerRepository companyContainerRepository, CompanyContainerMapper companyContainerMapper) {
        this.companyContainerRepository = companyContainerRepository;
        this.companyContainerMapper = companyContainerMapper;
    }

    /**
     * Save a companyContainer.
     *
     * @param companyContainerDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyContainerDTO save(CompanyContainerDTO companyContainerDTO) {
        log.debug("Request to save CompanyContainer : {}", companyContainerDTO);
        CompanyContainer companyContainer = companyContainerMapper.toEntity(companyContainerDTO);
        companyContainer = companyContainerRepository.save(companyContainer);
        return companyContainerMapper.toDto(companyContainer);
    }

    /**
     * Partially update a companyContainer.
     *
     * @param companyContainerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyContainerDTO> partialUpdate(CompanyContainerDTO companyContainerDTO) {
        log.debug("Request to partially update CompanyContainer : {}", companyContainerDTO);

        return companyContainerRepository
            .findById(companyContainerDTO.getId())
            .map(existingCompanyContainer -> {
                companyContainerMapper.partialUpdate(existingCompanyContainer, companyContainerDTO);

                return existingCompanyContainer;
            })
            .map(companyContainerRepository::save)
            .map(companyContainerMapper::toDto);
    }

    /**
     * Get all the companyContainers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyContainerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyContainers");
        return companyContainerRepository.findAll(pageable).map(companyContainerMapper::toDto);
    }

    /**
     * Get one companyContainer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyContainerDTO> findOne(Long id) {
        log.debug("Request to get CompanyContainer : {}", id);
        return companyContainerRepository.findById(id).map(companyContainerMapper::toDto);
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
