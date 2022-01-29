package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.UHFRFIDReader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UHFRFIDReader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UHFRFIDReaderRepository extends JpaRepository<UHFRFIDReader, Long> {}
