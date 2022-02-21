package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.OrderItem;
import com.wms.uhfrfid.domain.enumeration.OrderItemStatus;
import com.wms.uhfrfid.repository.OrderItemRepository;
import com.wms.uhfrfid.security.SecurityUtils;
import com.wms.uhfrfid.service.dto.OrderItemDTO;
import com.wms.uhfrfid.service.mapper.OrderItemMapper;
import com.wms.uhfrfid.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class ReceptionItemService extends OrderItemService {

    private final Logger log = LoggerFactory.getLogger(ReceptionItemService.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public ReceptionItemService(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        super(orderItemRepository, orderItemMapper);
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public OrderItemDTO save(OrderItemDTO orderItemDTO) {
        OrderItem byId;
        try {
            byId = orderItemRepository.getById(orderItemDTO.getId());
        } catch (EntityNotFoundException e) {
            throw new BadRequestAlertException("Entity not found", "order-item", "idnotfound");
        }
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        checkRightToUpdate(byId, userLogin);


        log.debug("Request to save OrderItem : {}", orderItemDTO);
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem = orderItemRepository.save(orderItem);

        //TODO log in table user-activity
        return orderItemMapper.toDto(orderItem);
    }

    private void checkRightToUpdate(OrderItem orderItem, String userLogin) {
        //TODO user right to update
        if ("admin".equals(userLogin)) {
            return;
        }
        if (orderItem.getStatus() == OrderItemStatus.FULLY_PLACED) {
            throw new BadRequestAlertException("Can not update", "order-item", OrderItemStatus.FULLY_PLACED.name());
        }
    }
}
