package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DeliveryItemProduct;
import com.wms.uhfrfid.repository.DeliveryItemProductRepository;
import com.wms.uhfrfid.service.dto.DeliveryItemProductDTO;
import com.wms.uhfrfid.service.mapper.DeliveryItemProductMapper;
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

    private final DeliveryItemProductMapper deliveryItemProductMapper;

    public DeliveryItemProductService(
        DeliveryItemProductRepository deliveryItemProductRepository,
        DeliveryItemProductMapper deliveryItemProductMapper
    ) {
        this.deliveryItemProductRepository = deliveryItemProductRepository;
        this.deliveryItemProductMapper = deliveryItemProductMapper;
    }

    /**
     * Save a deliveryItemProduct.
     *
     * @param deliveryItemProductDTO the entity to save.
     * @return the persisted entity.
     */
    public DeliveryItemProductDTO save(DeliveryItemProductDTO deliveryItemProductDTO) {
        log.debug("Request to save DeliveryItemProduct : {}", deliveryItemProductDTO);
        DeliveryItemProduct deliveryItemProduct = deliveryItemProductMapper.toEntity(deliveryItemProductDTO);
        deliveryItemProduct = deliveryItemProductRepository.save(deliveryItemProduct);
        return deliveryItemProductMapper.toDto(deliveryItemProduct);
    }

    /**
     * Partially update a deliveryItemProduct.
     *
     * @param deliveryItemProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeliveryItemProductDTO> partialUpdate(DeliveryItemProductDTO deliveryItemProductDTO) {
        log.debug("Request to partially update DeliveryItemProduct : {}", deliveryItemProductDTO);

        return deliveryItemProductRepository
            .findById(deliveryItemProductDTO.getId())
            .map(existingDeliveryItemProduct -> {
                deliveryItemProductMapper.partialUpdate(existingDeliveryItemProduct, deliveryItemProductDTO);

                return existingDeliveryItemProduct;
            })
            .map(deliveryItemProductRepository::save)
            .map(deliveryItemProductMapper::toDto);
    }

    /**
     * Get all the deliveryItemProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryItemProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryItemProducts");
        return deliveryItemProductRepository.findAll(pageable).map(deliveryItemProductMapper::toDto);
    }

    /**
     * Get one deliveryItemProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeliveryItemProductDTO> findOne(Long id) {
        log.debug("Request to get DeliveryItemProduct : {}", id);
        return deliveryItemProductRepository.findById(id).map(deliveryItemProductMapper::toDto);
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
