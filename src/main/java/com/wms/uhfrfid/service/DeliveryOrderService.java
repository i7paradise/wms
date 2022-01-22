package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.repository.DeliveryOrderRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeliveryOrder}.
 */
@Service
@Transactional
public class DeliveryOrderService {

    private final Logger log = LoggerFactory.getLogger(DeliveryOrderService.class);

    private final DeliveryOrderRepository deliveryOrderRepository;

    public DeliveryOrderService(DeliveryOrderRepository deliveryOrderRepository) {
        this.deliveryOrderRepository = deliveryOrderRepository;
    }

    /**
     * Save a deliveryOrder.
     *
     * @param deliveryOrder the entity to save.
     * @return the persisted entity.
     */
    public DeliveryOrder save(DeliveryOrder deliveryOrder) {
        log.debug("Request to save DeliveryOrder : {}", deliveryOrder);
        return deliveryOrderRepository.save(deliveryOrder);
    }

    /**
     * Partially update a deliveryOrder.
     *
     * @param deliveryOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryOrder> partialUpdate(DeliveryOrder deliveryOrder) {
        log.debug("Request to partially update DeliveryOrder : {}", deliveryOrder);

        return deliveryOrderRepository
            .findById(deliveryOrder.getId())
            .map(existingDeliveryOrder -> {
                if (deliveryOrder.getDoNumber() != null) {
                    existingDeliveryOrder.setDoNumber(deliveryOrder.getDoNumber());
                }
                if (deliveryOrder.getPlacedDate() != null) {
                    existingDeliveryOrder.setPlacedDate(deliveryOrder.getPlacedDate());
                }
                if (deliveryOrder.getStatus() != null) {
                    existingDeliveryOrder.setStatus(deliveryOrder.getStatus());
                }
                if (deliveryOrder.getCode() != null) {
                    existingDeliveryOrder.setCode(deliveryOrder.getCode());
                }

                return existingDeliveryOrder;
            })
            .map(deliveryOrderRepository::save);
    }

    /**
     * Get all the deliveryOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryOrder> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryOrders");
        return deliveryOrderRepository.findAll(pageable);
    }

    /**
     * Get one deliveryOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryOrder> findOne(Long id) {
        log.debug("Request to get DeliveryOrder : {}", id);
        return deliveryOrderRepository.findById(id);
    }

    /**
     * Delete the deliveryOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeliveryOrder : {}", id);
        deliveryOrderRepository.deleteById(id);
    }
}
