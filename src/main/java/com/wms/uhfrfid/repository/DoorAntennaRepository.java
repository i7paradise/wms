package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.DoorAntenna;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DoorAntenna entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoorAntennaRepository extends JpaRepository<DoorAntenna, Long> {}
