package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.CompanyContainer;
import com.wms.uhfrfid.service.dto.CompanyContainerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyContainer} and its DTO {@link CompanyContainerDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, ContainerMapper.class })
public interface CompanyContainerMapper extends EntityMapper<CompanyContainerDTO, CompanyContainer> {
    @Mapping(target = "company", source = "company", qualifiedByName = "name")
    @Mapping(target = "container", source = "container", qualifiedByName = "name")
    CompanyContainerDTO toDto(CompanyContainer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyContainerDTO toDtoId(CompanyContainer companyContainer);
}
