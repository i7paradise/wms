package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryItemProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryItemProductDTO.class);
        DeliveryItemProductDTO deliveryItemProductDTO1 = new DeliveryItemProductDTO();
        deliveryItemProductDTO1.setId(1L);
        DeliveryItemProductDTO deliveryItemProductDTO2 = new DeliveryItemProductDTO();
        assertThat(deliveryItemProductDTO1).isNotEqualTo(deliveryItemProductDTO2);
        deliveryItemProductDTO2.setId(deliveryItemProductDTO1.getId());
        assertThat(deliveryItemProductDTO1).isEqualTo(deliveryItemProductDTO2);
        deliveryItemProductDTO2.setId(2L);
        assertThat(deliveryItemProductDTO1).isNotEqualTo(deliveryItemProductDTO2);
        deliveryItemProductDTO1.setId(null);
        assertThat(deliveryItemProductDTO1).isNotEqualTo(deliveryItemProductDTO2);
    }
}
