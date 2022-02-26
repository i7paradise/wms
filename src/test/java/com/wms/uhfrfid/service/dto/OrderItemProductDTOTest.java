package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemProductDTO.class);
        OrderItemProductDTO orderItemProductDTO1 = new OrderItemProductDTO();
        orderItemProductDTO1.setId(1L);
        OrderItemProductDTO orderItemProductDTO2 = new OrderItemProductDTO();
        assertThat(orderItemProductDTO1).isNotEqualTo(orderItemProductDTO2);
        orderItemProductDTO2.setId(orderItemProductDTO1.getId());
        assertThat(orderItemProductDTO1).isEqualTo(orderItemProductDTO2);
        orderItemProductDTO2.setId(2L);
        assertThat(orderItemProductDTO1).isNotEqualTo(orderItemProductDTO2);
        orderItemProductDTO1.setId(null);
        assertThat(orderItemProductDTO1).isNotEqualTo(orderItemProductDTO2);
    }
}
