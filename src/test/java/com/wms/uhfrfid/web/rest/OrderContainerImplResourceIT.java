package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.service.OrderContainerImplService;
import com.wms.uhfrfid.service.dto.CreateWithTagsDTO;
import com.wms.uhfrfid.service.dto.TagsList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the OrderContainerImplResource REST controller.
 *
 * @see OrderContainerImplResource
 */
@IntegrationTest
@WithMockUser
class OrderContainerImplResourceIT {

    private MockMvc restMockMvc;

    private final OrderContainerImplService mockService = mock(OrderContainerImplService.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        OrderContainerImplResource orderContainerImplResource = new OrderContainerImplResource(mockService);
        restMockMvc = MockMvcBuilders.standaloneSetup(orderContainerImplResource).build();
    }

    /**
     * Test findOrderContainers
     */
    @Test
    void testFindOrderContainers() throws Exception {
        restMockMvc.perform(get("/api/v1/order-containers/from-order-item/1")).andExpect(status().isOk());
    }

    /**
     * Test createOrderContainersWithTags
     */
    @Test
    @Disabled
    void testCreateOrderContainersWithTags() throws Exception {
        CreateWithTagsDTO createWithTagsDTO = new CreateWithTagsDTO();
        createWithTagsDTO.setOrderItemId(1L);
        createWithTagsDTO.setTagsList(new TagsList(Arrays.asList("tag1", "tag2")));
        restMockMvc.perform(post("/api/v1/order-containers/create-with-tags", createWithTagsDTO))
            .andExpect(status().isOk());
    }

    /**
     * Test deleteContainer
     */
    @Test
    void testDeleteContainer() throws Exception {
        restMockMvc.perform(delete("/api/v1/order-containers/1")).andExpect(status().isNoContent());
    }
}
