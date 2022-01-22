package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryItemProduct;
import com.wms.uhfrfid.repository.DeliveryItemProductRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryItemProduct}.
 */
@Service
@Transactional
public class DeliveryItemProductService {

    private final Logger log = LoggerFactory.getLogger(DeliveryItemProductService.class);

    private final DeliveryItemProductRepository deliveryItemProductRepository;

    public DeliveryItemProductService(DeliveryItemProductRepository deliveryItemProductRepository) {
        this.deliveryItemProductRepository = deliveryItemProductRepository;
    }

    /**
     * Save a deliveryItemProduct.
     *
     * @param deliveryItemProduct the entity to save.
     * @return the persisted entity.
     */
    public DeliveryItemProduct save(DeliveryItemProduct deliveryItemProduct) {
        log.debug("Request to save DeliveryItemProduct : {}", deliveryItemProduct);
        return deliveryItemProductRepository.save(deliveryItemProduct);
    }

    /**
     * Partially update a deliveryItemProduct.
     *
     * @param deliveryItemProduct the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryItemProduct> partialUpdate(DeliveryItemProduct deliveryItemProduct) {
        log.debug("Request to partially update DeliveryItemProduct : {}", deliveryItemProduct);

        return deliveryItemProductRepository
            .findById(deliveryItemProduct.getId())
            .map(existingDeliveryItemProduct -> {
                if (deliveryItemProduct.getRfidTAG() != null) {
                    existingDeliveryItemProduct.setRfidTAG(deliveryItemProduct.getRfidTAG());
                }

                return existingDeliveryItemProduct;
            })
            .map(deliveryItemProductRepository::save);
    }

    /**
     * Get all the deliveryItemProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryItemProduct> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryItemProducts");
        return deliveryItemProductRepository.findAll(pageable);
    }

    /**
     * Get one deliveryItemProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryItemProduct> findOne(Long id) {
        log.debug("Request to get DeliveryItemProduct : {}", id);
        return deliveryItemProductRepository.findById(id);
    }

    /**
     * Delete the deliveryItemProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeliveryItemProduct : {}", id);
        deliveryItemProductRepository.deleteById(id);
    }
}
