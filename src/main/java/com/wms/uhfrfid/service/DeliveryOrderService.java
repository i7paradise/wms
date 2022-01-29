package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.repository.DeliveryOrderRepository;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTO;
import com.wms.uhfrfid.service.mapper.DeliveryOrderMapper;
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

    private final DeliveryOrderMapper deliveryOrderMapper;

    public DeliveryOrderService(DeliveryOrderRepository deliveryOrderRepository, DeliveryOrderMapper deliveryOrderMapper) {
        this.deliveryOrderRepository = deliveryOrderRepository;
        this.deliveryOrderMapper = deliveryOrderMapper;
    }

    /**
     * Save a deliveryOrder.
     *
     * @param deliveryOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public DeliveryOrderDTO save(DeliveryOrderDTO deliveryOrderDTO) {
        log.debug("Request to save DeliveryOrder : {}", deliveryOrderDTO);
        DeliveryOrder deliveryOrder = deliveryOrderMapper.toEntity(deliveryOrderDTO);
        deliveryOrder = deliveryOrderRepository.save(deliveryOrder);
        return deliveryOrderMapper.toDto(deliveryOrder);
    }

    /**
     * Partially update a deliveryOrder.
     *
     * @param deliveryOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryOrderDTO> partialUpdate(DeliveryOrderDTO deliveryOrderDTO) {
        log.debug("Request to partially update DeliveryOrder : {}", deliveryOrderDTO);

        return deliveryOrderRepository
            .findById(deliveryOrderDTO.getId())
            .map(existingDeliveryOrder -> {
                deliveryOrderMapper.partialUpdate(existingDeliveryOrder, deliveryOrderDTO);

                return existingDeliveryOrder;
            })
            .map(deliveryOrderRepository::save)
            .map(deliveryOrderMapper::toDto);
    }

    /**
     * Get all the deliveryOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryOrders");
        return deliveryOrderRepository.findAll(pageable).map(deliveryOrderMapper::toDto);
    }

    /**
     * Get one deliveryOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryOrderDTO> findOne(Long id) {
        log.debug("Request to get DeliveryOrder : {}", id);
        return deliveryOrderRepository.findById(id).map(deliveryOrderMapper::toDto);
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
