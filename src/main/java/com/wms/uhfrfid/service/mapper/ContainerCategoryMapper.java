package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.ContainerCategory;
import com.wms.uhfrfid.service.dto.ContainerCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContainerCategory} and its DTO {@link ContainerCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
public interface ContainerCategoryMapper extends EntityMapper<ContainerCategoryDTO, ContainerCategory> {
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "id")
    ContainerCategoryDTO toDto(ContainerCategory s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContainerCategoryDTO toDtoId(ContainerCategory containerCategory);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ContainerCategoryDTO toDtoName(ContainerCategory containerCategory);
}
