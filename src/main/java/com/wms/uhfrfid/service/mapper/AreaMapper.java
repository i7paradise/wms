package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Area;
import com.wms.uhfrfid.service.dto.AreaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Area} and its DTO {@link AreaDTO}.
 */
@Mapper(componentModel = "spring", uses = { WarehouseMapper.class })
public interface AreaMapper extends EntityMapper<AreaDTO, Area> {
    @Mapping(target = "warehouse", source = "warehouse", qualifiedByName = "name")
    AreaDTO toDto(Area s);

    @Named("type")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    AreaDTO toDtoType(Area area);
}
