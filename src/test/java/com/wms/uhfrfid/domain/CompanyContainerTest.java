package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyContainerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyContainer.class);
        CompanyContainer companyContainer1 = new CompanyContainer();
        companyContainer1.setId(1L);
        CompanyContainer companyContainer2 = new CompanyContainer();
        companyContainer2.setId(companyContainer1.getId());
        assertThat(companyContainer1).isEqualTo(companyContainer2);
        companyContainer2.setId(2L);
        assertThat(companyContainer1).isNotEqualTo(companyContainer2);
        companyContainer1.setId(null);
        assertThat(companyContainer1).isNotEqualTo(companyContainer2);
    }
}
