package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.WHRow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WHRow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WHRowRepository extends JpaRepository<WHRow, Long> {}
