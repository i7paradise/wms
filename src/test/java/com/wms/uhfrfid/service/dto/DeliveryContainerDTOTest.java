package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryContainerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryContainerDTO.class);
        DeliveryContainerDTO deliveryContainerDTO1 = new DeliveryContainerDTO();
        deliveryContainerDTO1.setId(1L);
        DeliveryContainerDTO deliveryContainerDTO2 = new DeliveryContainerDTO();
        assertThat(deliveryContainerDTO1).isNotEqualTo(deliveryContainerDTO2);
        deliveryContainerDTO2.setId(deliveryContainerDTO1.getId());
        assertThat(deliveryContainerDTO1).isEqualTo(deliveryContainerDTO2);
        deliveryContainerDTO2.setId(2L);
        assertThat(deliveryContainerDTO1).isNotEqualTo(deliveryContainerDTO2);
        deliveryContainerDTO1.setId(null);
        assertThat(deliveryContainerDTO1).isNotEqualTo(deliveryContainerDTO2);
    }
}
