package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WHRowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WHRow.class);
        WHRow wHRow1 = new WHRow();
        wHRow1.setId(1L);
        WHRow wHRow2 = new WHRow();
        wHRow2.setId(wHRow1.getId());
        assertThat(wHRow1).isEqualTo(wHRow2);
        wHRow2.setId(2L);
        assertThat(wHRow1).isNotEqualTo(wHRow2);
        wHRow1.setId(null);
        assertThat(wHRow1).isNotEqualTo(wHRow2);
    }
}
