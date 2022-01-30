package com.wms.uhfrfid.service.mapper.v2;

import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.domain.DeliveryOrderItem;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTO;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTOV2;
import com.wms.uhfrfid.service.dto.DeliveryOrderItemDTO;
import com.wms.uhfrfid.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link DeliveryOrder} and its DTO {@link DeliveryOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeliveryOrderV2Mapper extends EntityMapper<DeliveryOrderDTOV2, DeliveryOrder> {
    @Mapping(target = "deliveryOrder", ignore = true)
    DeliveryOrderItem toEntity(DeliveryOrderItemDTO s);

    @Mapping(target = "deliveryOrder", ignore = true)
    DeliveryOrderItemDTO toEntity(DeliveryOrderItem s);
}
