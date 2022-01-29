package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.DeliveryOrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryOrderItemRepository extends JpaRepository<DeliveryOrderItem, Long> {}
