package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.WHLevel;
import com.wms.uhfrfid.service.dto.WHLevelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WHLevel} and its DTO {@link WHLevelDTO}.
 */
@Mapper(componentModel = "spring", uses = { BayMapper.class })
public interface WHLevelMapper extends EntityMapper<WHLevelDTO, WHLevel> {
    @Mapping(target = "bay", source = "bay", qualifiedByName = "name")
    WHLevelDTO toDto(WHLevel s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WHLevelDTO toDtoName(WHLevel wHLevel);
}
