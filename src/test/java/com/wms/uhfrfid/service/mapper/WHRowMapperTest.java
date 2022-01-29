package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WHRowMapperTest {

    private WHRowMapper wHRowMapper;

    @BeforeEach
    public void setUp() {
        wHRowMapper = new WHRowMapperImpl();
    }
}
