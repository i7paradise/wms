package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Warehouse;
import com.wms.uhfrfid.service.dto.WarehouseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Warehouse} and its DTO {@link WarehouseDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {
    @Mapping(target = "company", source = "company", qualifiedByName = "name")
    WarehouseDTO toDto(Warehouse s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WarehouseDTO toDtoName(Warehouse warehouse);
}
