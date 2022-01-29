package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryOrderItem;
import com.wms.uhfrfid.repository.DeliveryOrderItemRepository;
import com.wms.uhfrfid.service.dto.DeliveryOrderItemDTO;
import com.wms.uhfrfid.service.mapper.DeliveryOrderItemMapper;
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

    private final DeliveryOrderItemMapper deliveryOrderItemMapper;

    public DeliveryOrderItemService(
        DeliveryOrderItemRepository deliveryOrderItemRepository,
        DeliveryOrderItemMapper deliveryOrderItemMapper
    ) {
        this.deliveryOrderItemRepository = deliveryOrderItemRepository;
        this.deliveryOrderItemMapper = deliveryOrderItemMapper;
    }

    /**
     * Save a deliveryOrderItem.
     *
     * @param deliveryOrderItemDTO the entity to save.
     * @return the persisted entity.
     */
    public DeliveryOrderItemDTO save(DeliveryOrderItemDTO deliveryOrderItemDTO) {
        log.debug("Request to save DeliveryOrderItem : {}", deliveryOrderItemDTO);
        DeliveryOrderItem deliveryOrderItem = deliveryOrderItemMapper.toEntity(deliveryOrderItemDTO);
        deliveryOrderItem = deliveryOrderItemRepository.save(deliveryOrderItem);
        return deliveryOrderItemMapper.toDto(deliveryOrderItem);
    }

    /**
     * Partially update a deliveryOrderItem.
     *
     * @param deliveryOrderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryOrderItemDTO> partialUpdate(DeliveryOrderItemDTO deliveryOrderItemDTO) {
        log.debug("Request to partially update DeliveryOrderItem : {}", deliveryOrderItemDTO);

        return deliveryOrderItemRepository
            .findById(deliveryOrderItemDTO.getId())
            .map(existingDeliveryOrderItem -> {
                deliveryOrderItemMapper.partialUpdate(existingDeliveryOrderItem, deliveryOrderItemDTO);

                return existingDeliveryOrderItem;
            })
            .map(deliveryOrderItemRepository::save)
            .map(deliveryOrderItemMapper::toDto);
    }

    /**
     * Get all the deliveryOrderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryOrderItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryOrderItems");
        return deliveryOrderItemRepository.findAll(pageable).map(deliveryOrderItemMapper::toDto);
    }

    /**
     * Get one deliveryOrderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryOrderItemDTO> findOne(Long id) {
        log.debug("Request to get DeliveryOrderItem : {}", id);
        return deliveryOrderItemRepository.findById(id).map(deliveryOrderItemMapper::toDto);
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
