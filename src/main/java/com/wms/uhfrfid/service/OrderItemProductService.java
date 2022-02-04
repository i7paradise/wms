package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.OrderItemProduct;
import com.wms.uhfrfid.repository.OrderItemProductRepository;
import com.wms.uhfrfid.service.dto.OrderItemProductDTO;
import com.wms.uhfrfid.service.mapper.OrderItemProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderItemProduct}.
 */
@Service
@Transactional
public class OrderItemProductService {

    private final Logger log = LoggerFactory.getLogger(OrderItemProductService.class);

    private final OrderItemProductRepository orderItemProductRepository;

    private final OrderItemProductMapper orderItemProductMapper;

    public OrderItemProductService(OrderItemProductRepository orderItemProductRepository, OrderItemProductMapper orderItemProductMapper) {
        this.orderItemProductRepository = orderItemProductRepository;
        this.orderItemProductMapper = orderItemProductMapper;
    }

    /**
     * Save a orderItemProduct.
     *
     * @param orderItemProductDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderItemProductDTO save(OrderItemProductDTO orderItemProductDTO) {
        log.debug("Request to save OrderItemProduct : {}", orderItemProductDTO);
        OrderItemProduct orderItemProduct = orderItemProductMapper.toEntity(orderItemProductDTO);
        orderItemProduct = orderItemProductRepository.save(orderItemProduct);
        return orderItemProductMapper.toDto(orderItemProduct);
    }

    /**
     * Partially update a orderItemProduct.
     *
     * @param orderItemProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItemProductDTO> partialUpdate(OrderItemProductDTO orderItemProductDTO) {
        log.debug("Request to partially update OrderItemProduct : {}", orderItemProductDTO);

        return orderItemProductRepository
            .findById(orderItemProductDTO.getId())
            .map(existingOrderItemProduct -> {
                orderItemProductMapper.partialUpdate(existingOrderItemProduct, orderItemProductDTO);

                return existingOrderItemProduct;
            })
            .map(orderItemProductRepository::save)
            .map(orderItemProductMapper::toDto);
    }

    /**
     * Get all the orderItemProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItemProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItemProducts");
        return orderItemProductRepository.findAll(pageable).map(orderItemProductMapper::toDto);
    }

    /**
     * Get one orderItemProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItemProductDTO> findOne(Long id) {
        log.debug("Request to get OrderItemProduct : {}", id);
        return orderItemProductRepository.findById(id).map(orderItemProductMapper::toDto);
    }

    /**
     * Delete the orderItemProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderItemProduct : {}", id);
        orderItemProductRepository.deleteById(id);
    }
}
