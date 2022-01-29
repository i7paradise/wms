package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UHFRFIDAntennaMapperTest {

    private UHFRFIDAntennaMapper uHFRFIDAntennaMapper;

    @BeforeEach
    public void setUp() {
        uHFRFIDAntennaMapper = new UHFRFIDAntennaMapperImpl();
    }
}
