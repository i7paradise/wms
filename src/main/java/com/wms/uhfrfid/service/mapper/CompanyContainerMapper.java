package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.CompanyContainer;
import com.wms.uhfrfid.service.dto.CompanyContainerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyContainer} and its DTO {@link CompanyContainerDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContainerCategoryMapper.class, CompanyMapper.class })
public interface CompanyContainerMapper extends EntityMapper<CompanyContainerDTO, CompanyContainer> {
    @Mapping(target = "containerCategory", source = "containerCategory", qualifiedByName = "name")
    @Mapping(target = "company", source = "company", qualifiedByName = "name")
    CompanyContainerDTO toDto(CompanyContainer s);
}
