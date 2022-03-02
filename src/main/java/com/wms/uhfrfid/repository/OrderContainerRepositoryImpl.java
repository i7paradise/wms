package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.OrderContainer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data SQL repository for the OrderContainer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderContainerRepositoryImpl extends OrderContainerRepository {

    List<OrderContainer> findByOrderItemId(Long id);

    void deleteByIdIn(Set<Long> ids);

    @Query(value = """
            SELECT count(distinct oc.id), order_item_id
            FROM ORDER_CONTAINER oc
            JOIN ORDER_ITEM_PRODUCT oip on oip.order_container_id = oc.id
            WHERE order_item_id = :orderItemId
            GROUP BY order_item_id
        """, nativeQuery = true)
    Integer countScanned(@Param("orderItemId") Long orderItemId);
}
