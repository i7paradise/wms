package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderContainerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderContainer.class);
        OrderContainer orderContainer1 = new OrderContainer();
        orderContainer1.setId(1L);
        OrderContainer orderContainer2 = new OrderContainer();
        orderContainer2.setId(orderContainer1.getId());
        assertThat(orderContainer1).isEqualTo(orderContainer2);
        orderContainer2.setId(2L);
        assertThat(orderContainer1).isNotEqualTo(orderContainer2);
        orderContainer1.setId(null);
        assertThat(orderContainer1).isNotEqualTo(orderContainer2);
    }
}
