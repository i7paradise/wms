package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.OrderItemProduct;
import com.wms.uhfrfid.service.dto.OrderItemProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItemProduct} and its DTO {@link OrderItemProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderContainerMapper.class })
public interface OrderItemProductMapper extends EntityMapper<OrderItemProductDTO, OrderItemProduct> {
    @Mapping(target = "orderContainer", source = "orderContainer", qualifiedByName = "id")
    OrderItemProductDTO toDto(OrderItemProduct s);
}
