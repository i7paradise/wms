package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UHFRFIDReaderMapperTest {

    private UHFRFIDReaderMapper uHFRFIDReaderMapper;

    @BeforeEach
    public void setUp() {
        uHFRFIDReaderMapper = new UHFRFIDReaderMapperImpl();
    }
}