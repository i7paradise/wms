package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyProductMapperTest {

    private CompanyProductMapper companyProductMapper;

    @BeforeEach
    public void setUp() {
        companyProductMapper = new CompanyProductMapperImpl();
    }
}
