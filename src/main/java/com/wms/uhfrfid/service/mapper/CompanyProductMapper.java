package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.CompanyProduct;
import com.wms.uhfrfid.service.dto.CompanyProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyProduct} and its DTO {@link CompanyProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContainerCategoryMapper.class, CompanyMapper.class, ProductMapper.class })
public interface CompanyProductMapper extends EntityMapper<CompanyProductDTO, CompanyProduct> {
    @Mapping(target = "containerCategory", source = "containerCategory", qualifiedByName = "name")
    @Mapping(target = "company", source = "company", qualifiedByName = "name")
    @Mapping(target = "product", source = "product", qualifiedByName = "name")
    CompanyProductDTO toDto(CompanyProduct s);

    @Named("sku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    CompanyProductDTO toDtoSku(CompanyProduct companyProduct);
}
