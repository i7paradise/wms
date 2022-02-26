package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderContainerMapperTest {

    private OrderContainerMapper orderContainerMapper;

    @BeforeEach
    public void setUp() {
        orderContainerMapper = new OrderContainerMapperImpl();
    }
}
