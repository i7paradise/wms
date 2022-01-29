package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryOrderItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryOrderItemDTO.class);
        DeliveryOrderItemDTO deliveryOrderItemDTO1 = new DeliveryOrderItemDTO();
        deliveryOrderItemDTO1.setId(1L);
        DeliveryOrderItemDTO deliveryOrderItemDTO2 = new DeliveryOrderItemDTO();
        assertThat(deliveryOrderItemDTO1).isNotEqualTo(deliveryOrderItemDTO2);
        deliveryOrderItemDTO2.setId(deliveryOrderItemDTO1.getId());
        assertThat(deliveryOrderItemDTO1).isEqualTo(deliveryOrderItemDTO2);
        deliveryOrderItemDTO2.setId(2L);
        assertThat(deliveryOrderItemDTO1).isNotEqualTo(deliveryOrderItemDTO2);
        deliveryOrderItemDTO1.setId(null);
        assertThat(deliveryOrderItemDTO1).isNotEqualTo(deliveryOrderItemDTO2);
    }
}
