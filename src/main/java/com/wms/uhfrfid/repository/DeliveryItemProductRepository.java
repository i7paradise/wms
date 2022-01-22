package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.DeliveryItemProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryItemProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryItemProductRepository extends JpaRepository<DeliveryItemProduct, Long> {}
