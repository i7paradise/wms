package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryOrderItem;
import com.wms.uhfrfid.repository.DeliveryOrderItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryOrderItem}.
 */
@Service
@Transactional
public class DeliveryOrderItemService {

    private final Logger log = LoggerFactory.getLogger(DeliveryOrderItemService.class);

    private final DeliveryOrderItemRepository deliveryOrderItemRepository;

    public DeliveryOrderItemService(DeliveryOrderItemRepository deliveryOrderItemRepository) {
        this.deliveryOrderItemRepository = deliveryOrderItemRepository;
    }

    /**
     * Save a deliveryOrderItem.
     *
     * @param deliveryOrderItem the entity to save.
     * @return the persisted entity.
     */
    public DeliveryOrderItem save(DeliveryOrderItem deliveryOrderItem) {
        log.debug("Request to save DeliveryOrderItem : {}", deliveryOrderItem);
        return deliveryOrderItemRepository.save(deliveryOrderItem);
    }

    /**
     * Partially update a deliveryOrderItem.
     *
     * @param deliveryOrderItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryOrderItem> partialUpdate(DeliveryOrderItem deliveryOrderItem) {
        log.debug("Request to partially update DeliveryOrderItem : {}", deliveryOrderItem);

        return deliveryOrderItemRepository
            .findById(deliveryOrderItem.getId())
            .map(existingDeliveryOrderItem -> {
                if (deliveryOrderItem.getUnitQuantity() != null) {
                    existingDeliveryOrderItem.setUnitQuantity(deliveryOrderItem.getUnitQuantity());
                }
                if (deliveryOrderItem.getContainerQuantity() != null) {
                    existingDeliveryOrderItem.setContainerQuantity(deliveryOrderItem.getContainerQuantity());
                }
                if (deliveryOrderItem.getStatus() != null) {
                    existingDeliveryOrderItem.setStatus(deliveryOrderItem.getStatus());
                }

                return existingDeliveryOrderItem;
            })
            .map(deliveryOrderItemRepository::save);
    }

    /**
     * Get all the deliveryOrderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryOrderItem> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryOrderItems");
        return deliveryOrderItemRepository.findAll(pageable);
    }

    /**
     * Get one deliveryOrderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryOrderItem> findOne(Long id) {
        log.debug("Request to get DeliveryOrderItem : {}", id);
        return deliveryOrderItemRepository.findById(id);
    }

    /**
     * Delete the deliveryOrderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeliveryOrderItem : {}", id);
        deliveryOrderItemRepository.deleteById(id);
    }
}
