package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContainerMapperTest {

    private ContainerMapper containerMapper;

    @BeforeEach
    public void setUp() {
        containerMapper = new ContainerMapperImpl();
    }
}
