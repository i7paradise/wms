package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryItemProductMapperTest {

    private DeliveryItemProductMapper deliveryItemProductMapper;

    @BeforeEach
    public void setUp() {
        deliveryItemProductMapper = new DeliveryItemProductMapperImpl();
    }
}
