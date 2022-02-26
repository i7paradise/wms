package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Door;
import com.wms.uhfrfid.service.dto.DoorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Door} and its DTO {@link DoorDTO}.
 */
@Mapper(componentModel = "spring", uses = { AreaMapper.class })
public interface DoorMapper extends EntityMapper<DoorDTO, Door> {
    @Mapping(target = "area", source = "area", qualifiedByName = "type")
    DoorDTO toDto(Door s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoorDTO toDtoId(Door door);
}
