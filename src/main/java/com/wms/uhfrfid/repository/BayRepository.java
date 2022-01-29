package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.Bay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BayRepository extends JpaRepository<Bay, Long> {}
