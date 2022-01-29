package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyProduct.class);
        CompanyProduct companyProduct1 = new CompanyProduct();
        companyProduct1.setId(1L);
        CompanyProduct companyProduct2 = new CompanyProduct();
        companyProduct2.setId(companyProduct1.getId());
        assertThat(companyProduct1).isEqualTo(companyProduct2);
        companyProduct2.setId(2L);
        assertThat(companyProduct1).isNotEqualTo(companyProduct2);
        companyProduct1.setId(null);
        assertThat(companyProduct1).isNotEqualTo(companyProduct2);
    }
}
