package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bay.class);
        Bay bay1 = new Bay();
        bay1.setId(1L);
        Bay bay2 = new Bay();
        bay2.setId(bay1.getId());
        assertThat(bay1).isEqualTo(bay2);
        bay2.setId(2L);
        assertThat(bay1).isNotEqualTo(bay2);
        bay1.setId(null);
        assertThat(bay1).isNotEqualTo(bay2);
    }
}
