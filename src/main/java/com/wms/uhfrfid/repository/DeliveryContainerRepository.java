package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.DeliveryContainer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryContainer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryContainerRepository extends JpaRepository<DeliveryContainer, Long> {}
