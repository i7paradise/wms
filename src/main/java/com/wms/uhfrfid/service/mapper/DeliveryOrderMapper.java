package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryOrder} and its DTO {@link DeliveryOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeliveryOrderMapper extends EntityMapper<DeliveryOrderDTO, DeliveryOrder> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeliveryOrderDTO toDtoId(DeliveryOrder deliveryOrder);
}
