package com.wms.uhfrfid.repository;

import java.util.List;

import com.wms.uhfrfid.domain.Order;
import com.wms.uhfrfid.domain.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Order entity.
 */
@Repository
public interface ReceptionRepository extends OrderRepository {
    //TODO define Query
    Page<Order> findOrdersByStatusIn(List<OrderStatus> listStatus, Pageable pageable);
}
