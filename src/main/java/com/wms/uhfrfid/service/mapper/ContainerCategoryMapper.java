package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.ContainerCategory;
import com.wms.uhfrfid.service.dto.ContainerCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContainerCategory} and its DTO {@link ContainerCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContainerCategoryMapper extends EntityMapper<ContainerCategoryDTO, ContainerCategory> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContainerCategoryDTO toDtoName(ContainerCategory containerCategory);
}
