package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DoorAntennaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoorAntennaDTO.class);
        DoorAntennaDTO doorAntennaDTO1 = new DoorAntennaDTO();
        doorAntennaDTO1.setId(1L);
        DoorAntennaDTO doorAntennaDTO2 = new DoorAntennaDTO();
        assertThat(doorAntennaDTO1).isNotEqualTo(doorAntennaDTO2);
        doorAntennaDTO2.setId(doorAntennaDTO1.getId());
        assertThat(doorAntennaDTO1).isEqualTo(doorAntennaDTO2);
        doorAntennaDTO2.setId(2L);
        assertThat(doorAntennaDTO1).isNotEqualTo(doorAntennaDTO2);
        doorAntennaDTO1.setId(null);
        assertThat(doorAntennaDTO1).isNotEqualTo(doorAntennaDTO2);
    }
}
