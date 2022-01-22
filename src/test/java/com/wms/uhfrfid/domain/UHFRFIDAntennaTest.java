package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UHFRFIDAntennaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UHFRFIDAntenna.class);
        UHFRFIDAntenna uHFRFIDAntenna1 = new UHFRFIDAntenna();
        uHFRFIDAntenna1.setId(1L);
        UHFRFIDAntenna uHFRFIDAntenna2 = new UHFRFIDAntenna();
        uHFRFIDAntenna2.setId(uHFRFIDAntenna1.getId());
        assertThat(uHFRFIDAntenna1).isEqualTo(uHFRFIDAntenna2);
        uHFRFIDAntenna2.setId(2L);
        assertThat(uHFRFIDAntenna1).isNotEqualTo(uHFRFIDAntenna2);
        uHFRFIDAntenna1.setId(null);
        assertThat(uHFRFIDAntenna1).isNotEqualTo(uHFRFIDAntenna2);
    }
}
