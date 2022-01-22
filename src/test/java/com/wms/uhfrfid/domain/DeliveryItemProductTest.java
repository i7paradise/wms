package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryItemProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryItemProduct.class);
        DeliveryItemProduct deliveryItemProduct1 = new DeliveryItemProduct();
        deliveryItemProduct1.setId(1L);
        DeliveryItemProduct deliveryItemProduct2 = new DeliveryItemProduct();
        deliveryItemProduct2.setId(deliveryItemProduct1.getId());
        assertThat(deliveryItemProduct1).isEqualTo(deliveryItemProduct2);
        deliveryItemProduct2.setId(2L);
        assertThat(deliveryItemProduct1).isNotEqualTo(deliveryItemProduct2);
        deliveryItemProduct1.setId(null);
        assertThat(deliveryItemProduct1).isNotEqualTo(deliveryItemProduct2);
    }
}
