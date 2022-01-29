package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryContainer;
import com.wms.uhfrfid.repository.DeliveryContainerRepository;
import com.wms.uhfrfid.service.dto.DeliveryContainerDTO;
import com.wms.uhfrfid.service.mapper.DeliveryContainerMapper;
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

    private final DeliveryContainerMapper deliveryContainerMapper;

    public DeliveryContainerService(
        DeliveryContainerRepository deliveryContainerRepository,
        DeliveryContainerMapper deliveryContainerMapper
    ) {
        this.deliveryContainerRepository = deliveryContainerRepository;
        this.deliveryContainerMapper = deliveryContainerMapper;
    }

    /**
     * Save a deliveryContainer.
     *
     * @param deliveryContainerDTO the entity to save.
     * @return the persisted entity.
     */
    public DeliveryContainerDTO save(DeliveryContainerDTO deliveryContainerDTO) {
        log.debug("Request to save DeliveryContainer : {}", deliveryContainerDTO);
        DeliveryContainer deliveryContainer = deliveryContainerMapper.toEntity(deliveryContainerDTO);
        deliveryContainer = deliveryContainerRepository.save(deliveryContainer);
        return deliveryContainerMapper.toDto(deliveryContainer);
    }

    /**
     * Partially update a deliveryContainer.
     *
     * @param deliveryContainerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryContainerDTO> partialUpdate(DeliveryContainerDTO deliveryContainerDTO) {
        log.debug("Request to partially update DeliveryContainer : {}", deliveryContainerDTO);

        return deliveryContainerRepository
            .findById(deliveryContainerDTO.getId())
            .map(existingDeliveryContainer -> {
                deliveryContainerMapper.partialUpdate(existingDeliveryContainer, deliveryContainerDTO);

                return existingDeliveryContainer;
            })
            .map(deliveryContainerRepository::save)
            .map(deliveryContainerMapper::toDto);
    }

    /**
     * Get all the deliveryContainers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryContainerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryContainers");
        return deliveryContainerRepository.findAll(pageable).map(deliveryContainerMapper::toDto);
    }

    /**
     * Get one deliveryContainer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryContainerDTO> findOne(Long id) {
        log.debug("Request to get DeliveryContainer : {}", id);
        return deliveryContainerRepository.findById(id).map(deliveryContainerMapper::toDto);
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
