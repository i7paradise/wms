package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryOrderDTO.class);
        DeliveryOrderDTO deliveryOrderDTO1 = new DeliveryOrderDTO();
        deliveryOrderDTO1.setId(1L);
        DeliveryOrderDTO deliveryOrderDTO2 = new DeliveryOrderDTO();
        assertThat(deliveryOrderDTO1).isNotEqualTo(deliveryOrderDTO2);
        deliveryOrderDTO2.setId(deliveryOrderDTO1.getId());
        assertThat(deliveryOrderDTO1).isEqualTo(deliveryOrderDTO2);
        deliveryOrderDTO2.setId(2L);
        assertThat(deliveryOrderDTO1).isNotEqualTo(deliveryOrderDTO2);
        deliveryOrderDTO1.setId(null);
        assertThat(deliveryOrderDTO1).isNotEqualTo(deliveryOrderDTO2);
    }
}
