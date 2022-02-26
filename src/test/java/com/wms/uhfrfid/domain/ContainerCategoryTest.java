package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContainerCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContainerCategory.class);
        ContainerCategory containerCategory1 = new ContainerCategory();
        containerCategory1.setId(1L);
        ContainerCategory containerCategory2 = new ContainerCategory();
        containerCategory2.setId(containerCategory1.getId());
        assertThat(containerCategory1).isEqualTo(containerCategory2);
        containerCategory2.setId(2L);
        assertThat(containerCategory1).isNotEqualTo(containerCategory2);
        containerCategory1.setId(null);
        assertThat(containerCategory1).isNotEqualTo(containerCategory2);
    }
}
