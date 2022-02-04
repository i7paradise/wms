package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import com.wms.uhfrfid.service.dto.UHFRFIDAntennaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UHFRFIDAntenna} and its DTO {@link UHFRFIDAntennaDTO}.
 */
@Mapper(componentModel = "spring", uses = { UHFRFIDReaderMapper.class })
public interface UHFRFIDAntennaMapper extends EntityMapper<UHFRFIDAntennaDTO, UHFRFIDAntenna> {
    @Mapping(target = "uhfRFIDReader", source = "uhfRFIDReader", qualifiedByName = "name")
    UHFRFIDAntennaDTO toDto(UHFRFIDAntenna s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UHFRFIDAntennaDTO toDtoName(UHFRFIDAntenna uHFRFIDAntenna);
}
