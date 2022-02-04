package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemProduct.class);
        OrderItemProduct orderItemProduct1 = new OrderItemProduct();
        orderItemProduct1.setId(1L);
        OrderItemProduct orderItemProduct2 = new OrderItemProduct();
        orderItemProduct2.setId(orderItemProduct1.getId());
        assertThat(orderItemProduct1).isEqualTo(orderItemProduct2);
        orderItemProduct2.setId(2L);
        assertThat(orderItemProduct1).isNotEqualTo(orderItemProduct2);
        orderItemProduct1.setId(null);
        assertThat(orderItemProduct1).isNotEqualTo(orderItemProduct2);
    }
}
