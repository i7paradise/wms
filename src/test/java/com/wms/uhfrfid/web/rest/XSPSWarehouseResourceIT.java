package com.wms.uhfrfid.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wms.uhfrfid.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the XSPSWarehouseResource REST controller.
 *
 * @see XSPSWarehouseResource
 */
@IntegrationTest
class XSPSWarehouseResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        XSPSWarehouseResource xSPSWarehouseResource = new XSPSWarehouseResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(xSPSWarehouseResource).build();
    }

    /**
     * Test getWarehouse
     */
    @Test
    void testGetWarehouse() throws Exception {
        restMockMvc.perform(get("/api/xsps-warehouse/get-warehouse")).andExpect(status().isOk());
    }
}
