package com.wms.uhfrfid.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import com.wms.uhfrfid.repository.DeliveryOrderItemRepository;
import com.wms.uhfrfid.repository.ReceptionRepository;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTOV2;
import com.wms.uhfrfid.service.mapper.v2.DeliveryOrderV2Mapper;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the ReceptionResource REST controller.
 *
 * @see ReceptionResource
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceptionResourceIT {

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired //TODO remove
    private DeliveryOrderItemRepository deliveryOrderItemRepository;

    @Autowired
    private DeliveryOrderV2Mapper deliveryOrderMapper;

    @Autowired
    private MockMvc restReceptionMockMvc;

    private DeliveryOrderDTOV2 deliveryOrderDTO;

    private static DeliveryOrder createEntity() {
        return new DeliveryOrder()
            .doNumber(UUID.randomUUID().toString())
            .placedDate(Instant.ofEpochMilli(0L))
            .status(DeliveryOrderStatus.PENDING)
            .code("AAAAAAAAAA");
    }

    @BeforeEach
    @Transactional
    public void initTest() {
        DeliveryOrder deliveryOrder = createEntity();
        deliveryOrder.addDeliveryOrderItem(DeliveryOrderItemResourceIT.createEntity(null));
        receptionRepository.save(deliveryOrder);
        deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);
    }

    @Test
    void getAllReceptions() throws Exception {
        restReceptionMockMvc
            .perform(get(ReceptionResource.PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryOrderDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].doNumber").value(hasItem(deliveryOrderDTO.getDoNumber())))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(deliveryOrderDTO.getPlacedDate().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(deliveryOrderDTO.getStatus().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(deliveryOrderDTO.getCode())));
    }

    @Test
    @Transactional
    void getReception() throws Exception {
        restReceptionMockMvc
            .perform(get(ReceptionResource.PATH + "/{id}", deliveryOrderDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(deliveryOrderDTO.getId().intValue()))
            .andExpect(jsonPath("$.doNumber").value(deliveryOrderDTO.getDoNumber()))
            .andExpect(jsonPath("$.placedDate").value(deliveryOrderDTO.getPlacedDate().toString()))
            .andExpect(jsonPath("$.status").value(deliveryOrderDTO.getStatus().toString()))
            .andExpect(jsonPath("$.code").value(deliveryOrderDTO.getCode()))
            .andExpect(jsonPath("$.deliveryOrderItems").isArray())//            .andExpect(jsonPath())
        ;
    }
}
