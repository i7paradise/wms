package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryOrderItemMapperTest {

    private DeliveryOrderItemMapper deliveryOrderItemMapper;

    @BeforeEach
    public void setUp() {
        deliveryOrderItemMapper = new DeliveryOrderItemMapperImpl();
    }
}
