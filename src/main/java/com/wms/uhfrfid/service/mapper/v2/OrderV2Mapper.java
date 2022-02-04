package com.wms.uhfrfid.service.mapper.v2;

import com.wms.uhfrfid.domain.Order;
import com.wms.uhfrfid.domain.OrderItem;
import com.wms.uhfrfid.service.dto.OrderDTO;
import com.wms.uhfrfid.service.dto.OrderDTOV2;
import com.wms.uhfrfid.service.dto.OrderItemDTO;
import com.wms.uhfrfid.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderV2Mapper extends EntityMapper<OrderDTOV2, Order> {
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemDTO s);

    @Mapping(target = "order", ignore = true)
    OrderItemDTO toEntity(OrderItem s);
}
