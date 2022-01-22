package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryContainerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryContainer.class);
        DeliveryContainer deliveryContainer1 = new DeliveryContainer();
        deliveryContainer1.setId(1L);
        DeliveryContainer deliveryContainer2 = new DeliveryContainer();
        deliveryContainer2.setId(deliveryContainer1.getId());
        assertThat(deliveryContainer1).isEqualTo(deliveryContainer2);
        deliveryContainer2.setId(2L);
        assertThat(deliveryContainer1).isNotEqualTo(deliveryContainer2);
        deliveryContainer1.setId(null);
        assertThat(deliveryContainer1).isNotEqualTo(deliveryContainer2);
    }
}
