package com.wms.uhfrfid.service.mapper.v2;

import com.wms.uhfrfid.domain.UHFRFIDReader;
import com.wms.uhfrfid.domain.DeliveryOrderItem;
import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTO;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTOV2;
import com.wms.uhfrfid.service.dto.DeliveryOrderItemDTO;
import com.wms.uhfrfid.service.dto.UHFRFIDAntennaDTO;
import com.wms.uhfrfid.service.mapper.CompanyProductMapper;
import com.wms.uhfrfid.service.mapper.DeliveryOrderMapper;
import com.wms.uhfrfid.service.mapper.EntityMapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link UHFRFIDReader} and its DTO {@link UHFRFIDReaderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UHFRFIDReaderV2Mapper extends EntityMapper<UHFRFIDReaderDTOV2, UHFRFIDReader> {
    @Mapping(target = "uhfRFIDReader", ignore = true)
    UHFRFIDAntenna toEntity(UHFRFIDAntennaDTO s);

    @Mapping(target = "uhfRFIDReader", ignore = true)
    UHFRFIDAntennaDTO toEntity(UHFRFIDAntenna s);
}