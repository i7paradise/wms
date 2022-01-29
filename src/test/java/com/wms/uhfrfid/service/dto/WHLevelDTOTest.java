package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WHLevelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WHLevelDTO.class);
        WHLevelDTO wHLevelDTO1 = new WHLevelDTO();
        wHLevelDTO1.setId(1L);
        WHLevelDTO wHLevelDTO2 = new WHLevelDTO();
        assertThat(wHLevelDTO1).isNotEqualTo(wHLevelDTO2);
        wHLevelDTO2.setId(wHLevelDTO1.getId());
        assertThat(wHLevelDTO1).isEqualTo(wHLevelDTO2);
        wHLevelDTO2.setId(2L);
        assertThat(wHLevelDTO1).isNotEqualTo(wHLevelDTO2);
        wHLevelDTO1.setId(null);
        assertThat(wHLevelDTO1).isNotEqualTo(wHLevelDTO2);
    }
}
