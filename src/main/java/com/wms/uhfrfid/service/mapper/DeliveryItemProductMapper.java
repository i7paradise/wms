package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.DeliveryItemProduct;
import com.wms.uhfrfid.service.dto.DeliveryItemProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryItemProduct} and its DTO {@link DeliveryItemProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { DeliveryContainerMapper.class })
public interface DeliveryItemProductMapper extends EntityMapper<DeliveryItemProductDTO, DeliveryItemProduct> {
    @Mapping(target = "deliveryContainer", source = "deliveryContainer", qualifiedByName = "id")
    DeliveryItemProductDTO toDto(DeliveryItemProduct s);
}
