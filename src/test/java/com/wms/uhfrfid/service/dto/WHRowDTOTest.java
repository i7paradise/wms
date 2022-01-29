package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WHRowDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WHRowDTO.class);
        WHRowDTO wHRowDTO1 = new WHRowDTO();
        wHRowDTO1.setId(1L);
        WHRowDTO wHRowDTO2 = new WHRowDTO();
        assertThat(wHRowDTO1).isNotEqualTo(wHRowDTO2);
        wHRowDTO2.setId(wHRowDTO1.getId());
        assertThat(wHRowDTO1).isEqualTo(wHRowDTO2);
        wHRowDTO2.setId(2L);
        assertThat(wHRowDTO1).isNotEqualTo(wHRowDTO2);
        wHRowDTO1.setId(null);
        assertThat(wHRowDTO1).isNotEqualTo(wHRowDTO2);
    }
}
