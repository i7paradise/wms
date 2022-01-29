package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Bay;
import com.wms.uhfrfid.service.dto.BayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bay} and its DTO {@link BayDTO}.
 */
@Mapper(componentModel = "spring", uses = { WHRowMapper.class })
public interface BayMapper extends EntityMapper<BayDTO, Bay> {
    @Mapping(target = "whrow", source = "whrow", qualifiedByName = "name")
    BayDTO toDto(Bay s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BayDTO toDtoName(Bay bay);
}
