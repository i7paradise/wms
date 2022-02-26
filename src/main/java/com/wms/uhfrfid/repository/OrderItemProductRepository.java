package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.OrderItemProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderItemProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemProductRepository extends JpaRepository<OrderItemProduct, Long> {}
