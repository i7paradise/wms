package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.DoorAntenna;
import com.wms.uhfrfid.service.dto.DoorAntennaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DoorAntenna} and its DTO {@link DoorAntennaDTO}.
 */
@Mapper(componentModel = "spring", uses = { DoorMapper.class, UHFRFIDAntennaMapper.class })
public interface DoorAntennaMapper extends EntityMapper<DoorAntennaDTO, DoorAntenna> {
    @Mapping(target = "door", source = "door", qualifiedByName = "name")
    @Mapping(target = "uhfRFIDAntenna", source = "uhfRFIDAntenna", qualifiedByName = "name")
    DoorAntennaDTO toDto(DoorAntenna s);
}
