package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.WHRow;
import com.wms.uhfrfid.service.dto.WHRowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WHRow} and its DTO {@link WHRowDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocationMapper.class })
public interface WHRowMapper extends EntityMapper<WHRowDTO, WHRow> {
    @Mapping(target = "location", source = "location", qualifiedByName = "name")
    WHRowDTO toDto(WHRow s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WHRowDTO toDtoName(WHRow wHRow);
}
