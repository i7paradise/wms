package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UHFRFIDAntennaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UHFRFIDAntennaDTO.class);
        UHFRFIDAntennaDTO uHFRFIDAntennaDTO1 = new UHFRFIDAntennaDTO();
        uHFRFIDAntennaDTO1.setId(1L);
        UHFRFIDAntennaDTO uHFRFIDAntennaDTO2 = new UHFRFIDAntennaDTO();
        assertThat(uHFRFIDAntennaDTO1).isNotEqualTo(uHFRFIDAntennaDTO2);
        uHFRFIDAntennaDTO2.setId(uHFRFIDAntennaDTO1.getId());
        assertThat(uHFRFIDAntennaDTO1).isEqualTo(uHFRFIDAntennaDTO2);
        uHFRFIDAntennaDTO2.setId(2L);
        assertThat(uHFRFIDAntennaDTO1).isNotEqualTo(uHFRFIDAntennaDTO2);
        uHFRFIDAntennaDTO1.setId(null);
        assertThat(uHFRFIDAntennaDTO1).isNotEqualTo(uHFRFIDAntennaDTO2);
    }
}
