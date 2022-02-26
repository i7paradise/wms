package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Order;
import com.wms.uhfrfid.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    OrderDTO toDto(Order s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoId(Order order);
}
