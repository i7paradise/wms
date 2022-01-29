package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UHFRFIDReaderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UHFRFIDReaderDTO.class);
        UHFRFIDReaderDTO uHFRFIDReaderDTO1 = new UHFRFIDReaderDTO();
        uHFRFIDReaderDTO1.setId(1L);
        UHFRFIDReaderDTO uHFRFIDReaderDTO2 = new UHFRFIDReaderDTO();
        assertThat(uHFRFIDReaderDTO1).isNotEqualTo(uHFRFIDReaderDTO2);
        uHFRFIDReaderDTO2.setId(uHFRFIDReaderDTO1.getId());
        assertThat(uHFRFIDReaderDTO1).isEqualTo(uHFRFIDReaderDTO2);
        uHFRFIDReaderDTO2.setId(2L);
        assertThat(uHFRFIDReaderDTO1).isNotEqualTo(uHFRFIDReaderDTO2);
        uHFRFIDReaderDTO1.setId(null);
        assertThat(uHFRFIDReaderDTO1).isNotEqualTo(uHFRFIDReaderDTO2);
    }
}
