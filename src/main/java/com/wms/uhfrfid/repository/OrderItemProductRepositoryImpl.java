package com.wms.uhfrfid.repository;

import com.wms.uhfrfid.domain.ProductsByContainer;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemProductRepositoryImpl extends OrderItemProductRepository {

    @Query(" SELECT orderContainer.id as containerId , count(id) as productsCount " +
        " FROM OrderItemProduct " +
        " WHERE orderContainer.id IS NOT NULL" +
        " GROUP BY orderContainer.id ")
    List<ProductsByContainer> countIdByOrderContainer();

    void deleteByOrderContainerId(Long orderContainerId);
}
