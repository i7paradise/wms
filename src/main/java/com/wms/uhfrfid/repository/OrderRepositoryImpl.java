package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.Order;
import com.wms.uhfrfid.domain.enumeration.OrderStatus;
import com.wms.uhfrfid.domain.enumeration.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Order entity.
 */
@Repository
public interface OrderRepositoryImpl extends OrderRepository {
    //TODO define Query
    Page<Order> findOrdersByTypeAndStatusIn(OrderType type, List<OrderStatus> listStatus, Pageable pageable);
}
