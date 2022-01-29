package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.DeliveryOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {}
