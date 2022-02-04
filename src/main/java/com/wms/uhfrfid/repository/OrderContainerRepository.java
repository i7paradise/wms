package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.OrderContainer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderContainer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderContainerRepository extends JpaRepository<OrderContainer, Long> {}
