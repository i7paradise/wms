package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.OrderContainer;
import com.wms.uhfrfid.service.dto.OrderContainerImplDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link OrderContainer} and its DTO {@link OrderContainerImplDTO}.
 */
@Mapper(componentModel = "spring", uses = {CompanyContainerMapper.class, OrderItemMapper.class})
public interface OrderContainerImplMapper extends EntityMapper<OrderContainerImplDTO, OrderContainer> {
    @Override
    @Mapping(target = "companyContainer", source = "companyContainer", qualifiedByName = "id")
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "id")
    OrderContainerImplDTO toDto(OrderContainer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderContainerImplDTO toDtoId(OrderContainer orderContainer);
}
