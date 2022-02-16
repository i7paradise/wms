package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the OrderContainerImplResource REST controller.
 *
 * @see OrderContainerImplResource
 */
@IntegrationTest
class OrderContainerImplResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        OrderContainerImplResource orderContainerImplResource = new OrderContainerImplResource(null);
        restMockMvc = MockMvcBuilders.standaloneSetup(orderContainerImplResource).build();
    }

    /**
     * Test findOrderContainers
     */
    @Test
    void testFindOrderContainers() throws Exception {
        restMockMvc.perform(get("/api/order-container-impl/find-order-containers")).andExpect(status().isOk());
    }

    /**
     * Test createOrderContainersWithTags
     */
    @Test
    void testCreateOrderContainersWithTags() throws Exception {
        restMockMvc.perform(post("/api/order-container-impl/create-order-containers-with-tags")).andExpect(status().isOk());
    }

    /**
     * Test deleteContainer
     */
    @Test
    void testDeleteContainer() throws Exception {
        restMockMvc.perform(delete("/api/order-container-impl/delete-container")).andExpect(status().isOk());
    }
}
