package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyProductDTO.class);
        CompanyProductDTO companyProductDTO1 = new CompanyProductDTO();
        companyProductDTO1.setId(1L);
        CompanyProductDTO companyProductDTO2 = new CompanyProductDTO();
        assertThat(companyProductDTO1).isNotEqualTo(companyProductDTO2);
        companyProductDTO2.setId(companyProductDTO1.getId());
        assertThat(companyProductDTO1).isEqualTo(companyProductDTO2);
        companyProductDTO2.setId(2L);
        assertThat(companyProductDTO1).isNotEqualTo(companyProductDTO2);
        companyProductDTO1.setId(null);
        assertThat(companyProductDTO1).isNotEqualTo(companyProductDTO2);
    }
}
