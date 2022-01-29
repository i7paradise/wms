package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryOrderMapperTest {

    private DeliveryOrderMapper deliveryOrderMapper;

    @BeforeEach
    public void setUp() {
        deliveryOrderMapper = new DeliveryOrderMapperImpl();
    }
}
