package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.UHFRFIDReader;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryOrder entity.
 */
@Repository
@Primary
public interface XSPSUHFRFIDReaderRepository extends UHFRFIDReaderRepository {
    //TODO define Query
    Page<UHFRFIDReader> findUHFReaderByName(String name, Pageable pageable);
}