package com.wms.uhfrfid.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import com.wms.uhfrfid.repository.ReceptionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private static final String DEFAULT_DO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DO_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DeliveryOrderStatus DEFAULT_STATUS = DeliveryOrderStatus.PENDING;
    private static final DeliveryOrderStatus UPDATED_STATUS = DeliveryOrderStatus.COMPLETED;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private MockMvc restReceptionMockMvc;

    private DeliveryOrder deliveryOrder;

    private static DeliveryOrder createEntity() {
        return new DeliveryOrder().doNumber(DEFAULT_DO_NUMBER).placedDate(DEFAULT_PLACED_DATE).status(DEFAULT_STATUS).code(DEFAULT_CODE);
    }

    private static DeliveryOrder createUpdatedEntity() {
        return new DeliveryOrder().doNumber(UPDATED_DO_NUMBER).placedDate(UPDATED_PLACED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);
    }

    @BeforeEach
    @Transactional
    public void initTest() {
        receptionRepository.saveAndFlush(deliveryOrder = createEntity());
    }

    @Test
    void getAllDeliveryOrders() throws Exception {
        // Get all the deliveryOrderList
        restReceptionMockMvc
            .perform(get(ReceptionResource.PATH + "/all-open"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].doNumber").value(hasItem(DEFAULT_DO_NUMBER)))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
}
