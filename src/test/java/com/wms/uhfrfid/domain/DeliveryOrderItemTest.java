package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryOrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryOrderItem.class);
        DeliveryOrderItem deliveryOrderItem1 = new DeliveryOrderItem();
        deliveryOrderItem1.setId(1L);
        DeliveryOrderItem deliveryOrderItem2 = new DeliveryOrderItem();
        deliveryOrderItem2.setId(deliveryOrderItem1.getId());
        assertThat(deliveryOrderItem1).isEqualTo(deliveryOrderItem2);
        deliveryOrderItem2.setId(2L);
        assertThat(deliveryOrderItem1).isNotEqualTo(deliveryOrderItem2);
        deliveryOrderItem1.setId(null);
        assertThat(deliveryOrderItem1).isNotEqualTo(deliveryOrderItem2);
    }
}
