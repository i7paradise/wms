package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryOrder entity.
 */
@Repository
public interface ReceptionRepository extends DeliveryOrderRepository {
    //TODO define Query
    Page<DeliveryOrder> findDeliveryOrdersByStatus(DeliveryOrderStatus status, Pageable pageable);
}
