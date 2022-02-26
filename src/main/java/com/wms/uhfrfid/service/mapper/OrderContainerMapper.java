package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.OrderContainer;
import com.wms.uhfrfid.service.dto.OrderContainerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderContainer} and its DTO {@link OrderContainerDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyContainerMapper.class, OrderItemMapper.class })
public interface OrderContainerMapper extends EntityMapper<OrderContainerDTO, OrderContainer> {
    @Mapping(target = "companyContainer", source = "companyContainer", qualifiedByName = "id")
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "id")
    OrderContainerDTO toDto(OrderContainer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderContainerDTO toDtoId(OrderContainer orderContainer);
}
