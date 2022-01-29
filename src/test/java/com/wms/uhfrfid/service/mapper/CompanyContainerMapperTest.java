package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyContainerMapperTest {

    private CompanyContainerMapper companyContainerMapper;

    @BeforeEach
    public void setUp() {
        companyContainerMapper = new CompanyContainerMapperImpl();
    }
}
