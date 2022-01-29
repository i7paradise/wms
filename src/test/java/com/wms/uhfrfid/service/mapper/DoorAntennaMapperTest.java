package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoorAntennaMapperTest {

    private DoorAntennaMapper doorAntennaMapper;

    @BeforeEach
    public void setUp() {
        doorAntennaMapper = new DoorAntennaMapperImpl();
    }
}
