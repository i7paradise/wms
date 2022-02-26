package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContainerCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContainerCategoryDTO.class);
        ContainerCategoryDTO containerCategoryDTO1 = new ContainerCategoryDTO();
        containerCategoryDTO1.setId(1L);
        ContainerCategoryDTO containerCategoryDTO2 = new ContainerCategoryDTO();
        assertThat(containerCategoryDTO1).isNotEqualTo(containerCategoryDTO2);
        containerCategoryDTO2.setId(containerCategoryDTO1.getId());
        assertThat(containerCategoryDTO1).isEqualTo(containerCategoryDTO2);
        containerCategoryDTO2.setId(2L);
        assertThat(containerCategoryDTO1).isNotEqualTo(containerCategoryDTO2);
        containerCategoryDTO1.setId(null);
        assertThat(containerCategoryDTO1).isNotEqualTo(containerCategoryDTO2);
    }
}
