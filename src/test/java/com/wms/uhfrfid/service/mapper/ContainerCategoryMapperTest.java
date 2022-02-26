package com.wms.uhfrfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContainerCategoryMapperTest {

    private ContainerCategoryMapper containerCategoryMapper;

    @BeforeEach
    public void setUp() {
        containerCategoryMapper = new ContainerCategoryMapperImpl();
    }
}
