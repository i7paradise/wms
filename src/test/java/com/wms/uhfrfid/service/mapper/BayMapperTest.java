package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BayMapperTest {

    private BayMapper bayMapper;

    @BeforeEach
    public void setUp() {
        bayMapper = new BayMapperImpl();
    }
}
