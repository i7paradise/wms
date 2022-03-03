package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.Order;
import com.wms.uhfrfid.domain.enumeration.OrderStatus;
import com.wms.uhfrfid.domain.enumeration.OrderType;
import com.wms.uhfrfid.repository.ReceptionRepository;
import com.wms.uhfrfid.service.dto.OrderDTOV2;
import com.wms.uhfrfid.service.mapper.v2.OrderV2Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private OrderV2Mapper orderMapper;

    @Autowired
    private MockMvc restReceptionMockMvc;

    private OrderDTOV2 orderDTO;

    private static Order createEntity() {
        return new Order()
            .type(OrderType.RECEPTION)
            .transactionNumber(UUID.randomUUID().toString())
            .placedDate(Instant.ofEpochMilli(0L))
            .status(OrderStatus.IN_PROGRESS)
            .code("AAAAAAAAAA");
    }

    @BeforeEach
    @Transactional
    public void initTest() {
        Order order = createEntity();
        order.addOrderItem(OrderItemResourceIT.createEntity(null));
        receptionRepository.save(order);
        orderDTO = orderMapper.toDto(order);
    }

    @Test
    @Transactional
    void getAllReceptions() throws Exception {
        restReceptionMockMvc
            .perform(get(ReceptionResource.PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(orderDTO.getTransactionNumber())))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(orderDTO.getPlacedDate().toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(orderDTO.getStatus().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(orderDTO.getCode())))
            .andExpect(jsonPath("$.orderItems").doesNotExist())
        ;
    }

    @Test
    @Transactional
    void getReception() throws Exception {
        restReceptionMockMvc
            .perform(get(ReceptionResource.PATH + "/{id}", orderDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(orderDTO.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(orderDTO.getTransactionNumber()))
            .andExpect(jsonPath("$.placedDate").value(orderDTO.getPlacedDate().toString()))
            .andExpect(jsonPath("$.status").value(orderDTO.getStatus().toString()))
            .andExpect(jsonPath("$.code").value(orderDTO.getCode()))
            .andExpect(jsonPath("$.orderItems").isArray())//            .andExpect(jsonPath())
        ;
    }
}
