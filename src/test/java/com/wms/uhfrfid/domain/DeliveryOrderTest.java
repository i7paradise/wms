package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryOrder.class);
        DeliveryOrder deliveryOrder1 = new DeliveryOrder();
        deliveryOrder1.setId(1L);
        DeliveryOrder deliveryOrder2 = new DeliveryOrder();
        deliveryOrder2.setId(deliveryOrder1.getId());
        assertThat(deliveryOrder1).isEqualTo(deliveryOrder2);
        deliveryOrder2.setId(2L);
        assertThat(deliveryOrder1).isNotEqualTo(deliveryOrder2);
        deliveryOrder1.setId(null);
        assertThat(deliveryOrder1).isNotEqualTo(deliveryOrder2);
    }
}
