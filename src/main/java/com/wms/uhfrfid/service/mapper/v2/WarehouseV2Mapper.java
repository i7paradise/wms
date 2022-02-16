package com.wms.uhfrfid.service.mapper.v2;

import com.wms.uhfrfid.domain.Location;
import com.wms.uhfrfid.domain.Order;
import com.wms.uhfrfid.domain.OrderItem;
import com.wms.uhfrfid.domain.Warehouse;
import com.wms.uhfrfid.service.dto.LocationDTO;
import com.wms.uhfrfid.service.dto.OrderDTO;
import com.wms.uhfrfid.service.dto.OrderDTOV2;
import com.wms.uhfrfid.service.dto.OrderItemDTO;
import com.wms.uhfrfid.service.dto.WarehouseDTOV2;
import com.wms.uhfrfid.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WarehouseV2Mapper extends EntityMapper<WarehouseDTOV2, Warehouse> {
    @Mapping(target = "warehouse", ignore = true)
    Location toEntity(LocationDTO s);

    @Mapping(target = "warehouse", ignore = true)
    LocationDTO toEntity(Location s);
}
