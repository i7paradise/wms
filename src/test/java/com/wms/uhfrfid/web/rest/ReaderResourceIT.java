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
 * Test class for the ReaderResource REST controller.
 *
 * @see ReaderResource
 */
@IntegrationTest
class ReaderResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ReaderResource readerResource = new ReaderResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(readerResource).build();
    }

    /**
     * Test getReader
     */
    @Test
    void testGetReader() throws Exception {
        restMockMvc.perform(get("/api/reader/get-reader")).andExpect(status().isOk());
    }
}
