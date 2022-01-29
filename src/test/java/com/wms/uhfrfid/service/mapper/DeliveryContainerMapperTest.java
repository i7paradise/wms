package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryContainerMapperTest {

    private DeliveryContainerMapper deliveryContainerMapper;

    @BeforeEach
    public void setUp() {
        deliveryContainerMapper = new DeliveryContainerMapperImpl();
    }
}
