package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Position;
import com.wms.uhfrfid.service.dto.PositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "spring", uses = { WHLevelMapper.class })
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {
    @Mapping(target = "whlevel", source = "whlevel", qualifiedByName = "name")
    PositionDTO toDto(Position s);
}
