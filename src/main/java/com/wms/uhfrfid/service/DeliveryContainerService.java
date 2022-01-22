package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryContainer;
import com.wms.uhfrfid.repository.DeliveryContainerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryContainer}.
 */
@Service
@Transactional
public class DeliveryContainerService {

    private final Logger log = LoggerFactory.getLogger(DeliveryContainerService.class);

    private final DeliveryContainerRepository deliveryContainerRepository;

    public DeliveryContainerService(DeliveryContainerRepository deliveryContainerRepository) {
        this.deliveryContainerRepository = deliveryContainerRepository;
    }

    /**
     * Save a deliveryContainer.
     *
     * @param deliveryContainer the entity to save.
     * @return the persisted entity.
     */
    public DeliveryContainer save(DeliveryContainer deliveryContainer) {
        log.debug("Request to save DeliveryContainer : {}", deliveryContainer);
        return deliveryContainerRepository.save(deliveryContainer);
    }

    /**
     * Partially update a deliveryContainer.
     *
     * @param deliveryContainer the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryContainer> partialUpdate(DeliveryContainer deliveryContainer) {
        log.debug("Request to partially update DeliveryContainer : {}", deliveryContainer);

        return deliveryContainerRepository
            .findById(deliveryContainer.getId())
            .map(existingDeliveryContainer -> {
                if (deliveryContainer.getSupplierRFIDTag() != null) {
                    existingDeliveryContainer.setSupplierRFIDTag(deliveryContainer.getSupplierRFIDTag());
                }

                return existingDeliveryContainer;
            })
            .map(deliveryContainerRepository::save);
    }

    /**
     * Get all the deliveryContainers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryContainer> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryContainers");
        return deliveryContainerRepository.findAll(pageable);
    }

    /**
     * Get one deliveryContainer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryContainer> findOne(Long id) {
        log.debug("Request to get DeliveryContainer : {}", id);
        return deliveryContainerRepository.findById(id);
    }

    /**
     * Delete the deliveryContainer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeliveryContainer : {}", id);
        deliveryContainerRepository.deleteById(id);
    }
}
