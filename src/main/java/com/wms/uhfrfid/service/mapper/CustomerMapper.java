package com.wms.uhfrfid.service.mapper;

import com.wms.uhfrfid.domain.Customer;
import com.wms.uhfrfid.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {}
