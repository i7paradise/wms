package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.UHFRFIDReader;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UHFRFIDReader} and its DTO {@link UHFRFIDReaderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UHFRFIDReaderMapper extends EntityMapper<UHFRFIDReaderDTO, UHFRFIDReader> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UHFRFIDReaderDTO toDtoName(UHFRFIDReader uHFRFIDReader);
}
