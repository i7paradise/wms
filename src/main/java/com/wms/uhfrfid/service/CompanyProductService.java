package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.CompanyProduct;
import com.wms.uhfrfid.repository.CompanyProductRepository;
import com.wms.uhfrfid.service.dto.CompanyProductDTO;
import com.wms.uhfrfid.service.mapper.CompanyProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyProduct}.
 */
@Service
@Transactional
public class CompanyProductService {

    private final Logger log = LoggerFactory.getLogger(CompanyProductService.class);

    private final CompanyProductRepository companyProductRepository;

    private final CompanyProductMapper companyProductMapper;

    public CompanyProductService(CompanyProductRepository companyProductRepository, CompanyProductMapper companyProductMapper) {
        this.companyProductRepository = companyProductRepository;
        this.companyProductMapper = companyProductMapper;
    }

    /**
     * Save a companyProduct.
     *
     * @param companyProductDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyProductDTO save(CompanyProductDTO companyProductDTO) {
        log.debug("Request to save CompanyProduct : {}", companyProductDTO);
        CompanyProduct companyProduct = companyProductMapper.toEntity(companyProductDTO);
        companyProduct = companyProductRepository.save(companyProduct);
        return companyProductMapper.toDto(companyProduct);
    }

    /**
     * Partially update a companyProduct.
     *
     * @param companyProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyProductDTO> partialUpdate(CompanyProductDTO companyProductDTO) {
        log.debug("Request to partially update CompanyProduct : {}", companyProductDTO);

        return companyProductRepository
            .findById(companyProductDTO.getId())
            .map(existingCompanyProduct -> {
                companyProductMapper.partialUpdate(existingCompanyProduct, companyProductDTO);

                return existingCompanyProduct;
            })
            .map(companyProductRepository::save)
            .map(companyProductMapper::toDto);
    }

    /**
     * Get all the companyProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyProducts");
        return companyProductRepository.findAll(pageable).map(companyProductMapper::toDto);
    }

    /**
     * Get one companyProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyProductDTO> findOne(Long id) {
        log.debug("Request to get CompanyProduct : {}", id);
        return companyProductRepository.findById(id).map(companyProductMapper::toDto);
    }

    /**
     * Delete the companyProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyProduct : {}", id);
        companyProductRepository.deleteById(id);
    }
}
