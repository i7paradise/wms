package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UHFRFIDReaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UHFRFIDReader.class);
        UHFRFIDReader uHFRFIDReader1 = new UHFRFIDReader();
        uHFRFIDReader1.setId(1L);
        UHFRFIDReader uHFRFIDReader2 = new UHFRFIDReader();
        uHFRFIDReader2.setId(uHFRFIDReader1.getId());
        assertThat(uHFRFIDReader1).isEqualTo(uHFRFIDReader2);
        uHFRFIDReader2.setId(2L);
        assertThat(uHFRFIDReader1).isNotEqualTo(uHFRFIDReader2);
        uHFRFIDReader1.setId(null);
        assertThat(uHFRFIDReader1).isNotEqualTo(uHFRFIDReader2);
    }
}
