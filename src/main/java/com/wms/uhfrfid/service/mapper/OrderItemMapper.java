package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.OrderItem;
import com.wms.uhfrfid.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyProductMapper.class, OrderMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "compganyProduct", source = "compganyProduct", qualifiedByName = "sku")
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    OrderItemDTO toDto(OrderItem s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderItemDTO toDtoId(OrderItem orderItem);
}
