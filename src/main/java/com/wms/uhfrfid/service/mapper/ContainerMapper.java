package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Container;
import com.wms.uhfrfid.service.dto.ContainerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Container} and its DTO {@link ContainerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContainerMapper extends EntityMapper<ContainerDTO, Container> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContainerDTO toDtoName(Container container);
}
