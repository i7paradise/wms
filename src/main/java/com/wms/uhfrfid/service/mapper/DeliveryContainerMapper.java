package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.DeliveryContainer;
import com.wms.uhfrfid.service.dto.DeliveryContainerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryContainer} and its DTO {@link DeliveryContainerDTO}.
 */
@Mapper(componentModel = "spring", uses = { DeliveryOrderItemMapper.class, CompanyContainerMapper.class })
public interface DeliveryContainerMapper extends EntityMapper<DeliveryContainerDTO, DeliveryContainer> {
    @Mapping(target = "deliveryOrderItem", source = "deliveryOrderItem", qualifiedByName = "id")
    @Mapping(target = "companyContainer", source = "companyContainer", qualifiedByName = "id")
    DeliveryContainerDTO toDto(DeliveryContainer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeliveryContainerDTO toDtoId(DeliveryContainer deliveryContainer);
}
