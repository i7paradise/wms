package com.wms.uhfrfid.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import com.wms.uhfrfid.repository.ReceptionRepository;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTO;
import com.wms.uhfrfid.service.mapper.DeliveryOrderMapper;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    private MockMvc restReceptionMockMvc;

    private DeliveryOrderDTO deliveryOrderDTO;

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
        receptionRepository.saveAndFlush(deliveryOrder);
        deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);
    }

    @Test
    void getAllReceptions() throws Exception {
        ResultActions resultActions = restReceptionMockMvc
            .perform(get(ReceptionResource.PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        checkReceptionFields(resultActions);
    }

    @Test
    void getReception() throws Exception {
        ResultActions resultActions = restReceptionMockMvc
            .perform(get(ReceptionResource.PATH, deliveryOrderDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        checkReceptionFields(resultActions);
    }

    private void checkReceptionFields(ResultActions resultActions) throws Exception {
        resultActions
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryOrderDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].doNumber").value(hasItem(deliveryOrderDTO.getDoNumber())))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(deliveryOrderDTO.getPlacedDate().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(deliveryOrderDTO.getStatus().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(deliveryOrderDTO.getCode())));
    }
}
