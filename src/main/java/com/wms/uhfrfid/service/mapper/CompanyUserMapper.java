package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.CompanyUser;
import com.wms.uhfrfid.service.dto.CompanyUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyUser} and its DTO {@link CompanyUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, CompanyMapper.class })
public interface CompanyUserMapper extends EntityMapper<CompanyUserDTO, CompanyUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    CompanyUserDTO toDto(CompanyUser s);
}
