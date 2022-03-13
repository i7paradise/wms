package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Order;
import com.wms.uhfrfid.domain.User;
import com.wms.uhfrfid.domain.enumeration.OrderStatus;
import com.wms.uhfrfid.repository.ShippingRepository;
import com.wms.uhfrfid.repository.UserRepository;
import com.wms.uhfrfid.service.dto.OrderDTO;
import com.wms.uhfrfid.service.dto.OrderDTOV2;
import com.wms.uhfrfid.service.mapper.v2.OrderV2Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
public class ShippingService {

    private final Logger log = LoggerFactory.getLogger(ShippingService.class);

    private final ShippingRepository shippingRepository;
    private final UserRepository userRepository;
    private final OrderV2Mapper orderV2Mapper;

    public ShippingService(
    	ShippingRepository shippingRepository,
        UserRepository userRepository,
        OrderV2Mapper orderV2Mapper
    ) {
        this.shippingRepository = shippingRepository;
        this.userRepository = userRepository;
        this.orderV2Mapper = orderV2Mapper;
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(String userLogin, Pageable pageable) {
        User user = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new IllegalArgumentException("TODO ShippingService user not found"));
        //TODO add user to the query
        return shippingRepository.findOrdersByStatusIn(Arrays.asList(OrderStatus.DRAFT, OrderStatus.IN_PROGRESS), pageable)
            .map(orderV2Mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<OrderDTOV2> findOne(Long id, String userLogin) {
        log.debug("Request to get Order : {}", id);
        //TODO implement find by user
        return shippingRepository
            .findById(id)
            .map(e -> {
                e.getOrderItems();
                return e;
            })
            .map(orderV2Mapper::toDto);
    }

    public void statusForward(Order order) {
        next(order.getStatus())
            .ifPresent(e -> {
                order.setStatus(e);
                shippingRepository.save(order);
            });
    }

    private static Optional<OrderStatus> next(OrderStatus status) {
        if (status == OrderStatus.DRAFT) {
            return Optional.of(OrderStatus.IN_PROGRESS);
        }
        return Optional.empty();
    }
}
