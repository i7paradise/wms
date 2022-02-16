package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.OrderContainer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the OrderContainer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderContainerRepositoryImpl extends OrderContainerRepository {

    List<OrderContainer> findByOrderItemId(Long id);
}
