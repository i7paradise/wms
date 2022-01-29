package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.Door;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Door entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoorRepository extends JpaRepository<Door, Long> {}
