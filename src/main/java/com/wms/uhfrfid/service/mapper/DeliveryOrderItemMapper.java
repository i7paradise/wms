package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.DeliveryOrderItem;
import com.wms.uhfrfid.service.dto.DeliveryOrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryOrderItem} and its DTO {@link DeliveryOrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyProductMapper.class, DeliveryOrderMapper.class })
public interface DeliveryOrderItemMapper extends EntityMapper<DeliveryOrderItemDTO, DeliveryOrderItem> {
    @Mapping(target = "compganyProduct", source = "compganyProduct", qualifiedByName = "sku")
    @Mapping(target = "deliveryOrder", source = "deliveryOrder", qualifiedByName = "doNumber")
    DeliveryOrderItemDTO toDto(DeliveryOrderItem s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeliveryOrderItemDTO toDtoId(DeliveryOrderItem deliveryOrderItem);
}
