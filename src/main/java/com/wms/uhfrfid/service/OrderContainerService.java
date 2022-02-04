package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.OrderContainer;
import com.wms.uhfrfid.repository.OrderContainerRepository;
import com.wms.uhfrfid.service.dto.OrderContainerDTO;
import com.wms.uhfrfid.service.mapper.OrderContainerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderContainer}.
 */
@Service
@Transactional
public class OrderContainerService {

    private final Logger log = LoggerFactory.getLogger(OrderContainerService.class);

    private final OrderContainerRepository orderContainerRepository;

    private final OrderContainerMapper orderContainerMapper;

    public OrderContainerService(OrderContainerRepository orderContainerRepository, OrderContainerMapper orderContainerMapper) {
        this.orderContainerRepository = orderContainerRepository;
        this.orderContainerMapper = orderContainerMapper;
    }

    /**
     * Save a orderContainer.
     *
     * @param orderContainerDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderContainerDTO save(OrderContainerDTO orderContainerDTO) {
        log.debug("Request to save OrderContainer : {}", orderContainerDTO);
        OrderContainer orderContainer = orderContainerMapper.toEntity(orderContainerDTO);
        orderContainer = orderContainerRepository.save(orderContainer);
        return orderContainerMapper.toDto(orderContainer);
    }

    /**
     * Partially update a orderContainer.
     *
     * @param orderContainerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderContainerDTO> partialUpdate(OrderContainerDTO orderContainerDTO) {
        log.debug("Request to partially update OrderContainer : {}", orderContainerDTO);

        return orderContainerRepository
            .findById(orderContainerDTO.getId())
            .map(existingOrderContainer -> {
                orderContainerMapper.partialUpdate(existingOrderContainer, orderContainerDTO);

                return existingOrderContainer;
            })
            .map(orderContainerRepository::save)
            .map(orderContainerMapper::toDto);
    }

    /**
     * Get all the orderContainers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderContainerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderContainers");
        return orderContainerRepository.findAll(pageable).map(orderContainerMapper::toDto);
    }

    /**
     * Get one orderContainer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderContainerDTO> findOne(Long id) {
        log.debug("Request to get OrderContainer : {}", id);
        return orderContainerRepository.findById(id).map(orderContainerMapper::toDto);
    }

    /**
     * Delete the orderContainer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderContainer : {}", id);
        orderContainerRepository.deleteById(id);
    }
}
