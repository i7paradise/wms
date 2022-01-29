package com.wms.uhfrfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wms.uhfrfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContainerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Container.class);
        Container container1 = new Container();
        container1.setId(1L);
        Container container2 = new Container();
        container2.setId(container1.getId());
        assertThat(container1).isEqualTo(container2);
        container2.setId(2L);
        assertThat(container1).isNotEqualTo(container2);
        container1.setId(null);
        assertThat(container1).isNotEqualTo(container2);
    }
}
