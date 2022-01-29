package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyContainerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyContainerDTO.class);
        CompanyContainerDTO companyContainerDTO1 = new CompanyContainerDTO();
        companyContainerDTO1.setId(1L);
        CompanyContainerDTO companyContainerDTO2 = new CompanyContainerDTO();
        assertThat(companyContainerDTO1).isNotEqualTo(companyContainerDTO2);
        companyContainerDTO2.setId(companyContainerDTO1.getId());
        assertThat(companyContainerDTO1).isEqualTo(companyContainerDTO2);
        companyContainerDTO2.setId(2L);
        assertThat(companyContainerDTO1).isNotEqualTo(companyContainerDTO2);
        companyContainerDTO1.setId(null);
        assertThat(companyContainerDTO1).isNotEqualTo(companyContainerDTO2);
    }
}
