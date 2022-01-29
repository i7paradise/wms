package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UHFRFIDAntenna entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UHFRFIDAntennaRepository extends JpaRepository<UHFRFIDAntenna, Long> {}
