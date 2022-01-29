package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoorMapperTest {

    private DoorMapper doorMapper;

    @BeforeEach
    public void setUp() {
        doorMapper = new DoorMapperImpl();
    }
}
