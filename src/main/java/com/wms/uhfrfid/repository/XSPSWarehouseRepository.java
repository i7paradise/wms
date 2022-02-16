package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.Warehouse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Warehouse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XSPSWarehouseRepository extends JpaRepository<Warehouse, Long> {}
