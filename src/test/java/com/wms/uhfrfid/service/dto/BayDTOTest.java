package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BayDTO.class);
        BayDTO bayDTO1 = new BayDTO();
        bayDTO1.setId(1L);
        BayDTO bayDTO2 = new BayDTO();
        assertThat(bayDTO1).isNotEqualTo(bayDTO2);
        bayDTO2.setId(bayDTO1.getId());
        assertThat(bayDTO1).isEqualTo(bayDTO2);
        bayDTO2.setId(2L);
        assertThat(bayDTO1).isNotEqualTo(bayDTO2);
        bayDTO1.setId(null);
        assertThat(bayDTO1).isNotEqualTo(bayDTO2);
    }
}
