package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.CompanyProduct;
import com.wms.uhfrfid.repository.CompanyProductRepository;
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

    public CompanyProductService(CompanyProductRepository companyProductRepository) {
        this.companyProductRepository = companyProductRepository;
    }

    /**
     * Save a companyProduct.
     *
     * @param companyProduct the entity to save.
     * @return the persisted entity.
     */
    public CompanyProduct save(CompanyProduct companyProduct) {
        log.debug("Request to save CompanyProduct : {}", companyProduct);
        return companyProductRepository.save(companyProduct);
    }

    /**
     * Partially update a companyProduct.
     *
     * @param companyProduct the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyProduct> partialUpdate(CompanyProduct companyProduct) {
        log.debug("Request to partially update CompanyProduct : {}", companyProduct);

        return companyProductRepository
            .findById(companyProduct.getId())
            .map(existingCompanyProduct -> {
                if (companyProduct.getQuantity() != null) {
                    existingCompanyProduct.setQuantity(companyProduct.getQuantity());
                }
                if (companyProduct.getSku() != null) {
                    existingCompanyProduct.setSku(companyProduct.getSku());
                }
                if (companyProduct.getStockingRatio() != null) {
                    existingCompanyProduct.setStockingRatio(companyProduct.getStockingRatio());
                }

                return existingCompanyProduct;
            })
            .map(companyProductRepository::save);
    }

    /**
     * Get all the companyProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyProduct> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyProducts");
        return companyProductRepository.findAll(pageable);
    }

    /**
     * Get one companyProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyProduct> findOne(Long id) {
        log.debug("Request to get CompanyProduct : {}", id);
        return companyProductRepository.findById(id);
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
