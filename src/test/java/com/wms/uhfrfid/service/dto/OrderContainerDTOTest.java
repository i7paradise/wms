package com.wms.uhfrfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderContainerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderContainerDTO.class);
        OrderContainerDTO orderContainerDTO1 = new OrderContainerDTO();
        orderContainerDTO1.setId(1L);
        OrderContainerDTO orderContainerDTO2 = new OrderContainerDTO();
        assertThat(orderContainerDTO1).isNotEqualTo(orderContainerDTO2);
        orderContainerDTO2.setId(orderContainerDTO1.getId());
        assertThat(orderContainerDTO1).isEqualTo(orderContainerDTO2);
        orderContainerDTO2.setId(2L);
        assertThat(orderContainerDTO1).isNotEqualTo(orderContainerDTO2);
        orderContainerDTO1.setId(null);
        assertThat(orderContainerDTO1).isNotEqualTo(orderContainerDTO2);
    }
}
