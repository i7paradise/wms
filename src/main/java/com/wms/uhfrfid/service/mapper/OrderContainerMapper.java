package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.OrderContainer;
import com.wms.uhfrfid.service.dto.OrderContainerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderContainer} and its DTO {@link OrderContainerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderContainerMapper extends EntityMapper<OrderContainerDTO, OrderContainer> {}
