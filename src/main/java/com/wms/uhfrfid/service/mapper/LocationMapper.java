package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Location;
import com.wms.uhfrfid.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring", uses = { WarehouseMapper.class })
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "warehouse", source = "warehouse", qualifiedByName = "name")
    LocationDTO toDto(Location s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoName(Location location);
}
