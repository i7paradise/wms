package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WHLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WHLevel.class);
        WHLevel wHLevel1 = new WHLevel();
        wHLevel1.setId(1L);
        WHLevel wHLevel2 = new WHLevel();
        wHLevel2.setId(wHLevel1.getId());
        assertThat(wHLevel1).isEqualTo(wHLevel2);
        wHLevel2.setId(2L);
        assertThat(wHLevel1).isNotEqualTo(wHLevel2);
        wHLevel1.setId(null);
        assertThat(wHLevel1).isNotEqualTo(wHLevel2);
    }
}
