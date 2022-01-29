package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DoorAntennaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoorAntenna.class);
        DoorAntenna doorAntenna1 = new DoorAntenna();
        doorAntenna1.setId(1L);
        DoorAntenna doorAntenna2 = new DoorAntenna();
        doorAntenna2.setId(doorAntenna1.getId());
        assertThat(doorAntenna1).isEqualTo(doorAntenna2);
        doorAntenna2.setId(2L);
        assertThat(doorAntenna1).isNotEqualTo(doorAntenna2);
        doorAntenna1.setId(null);
        assertThat(doorAntenna1).isNotEqualTo(doorAntenna2);
    }
}
