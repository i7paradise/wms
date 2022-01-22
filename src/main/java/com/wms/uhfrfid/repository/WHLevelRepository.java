package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.WHLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WHLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WHLevelRepository extends JpaRepository<WHLevel, Long> {}
