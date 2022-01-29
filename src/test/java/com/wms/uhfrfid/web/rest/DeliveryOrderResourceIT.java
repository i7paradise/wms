package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import com.wms.uhfrfid.repository.DeliveryOrderRepository;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTO;
import com.wms.uhfrfid.service.mapper.DeliveryOrderMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeliveryOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryOrderResourceIT {

    private static final String DEFAULT_DO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DO_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DeliveryOrderStatus DEFAULT_STATUS = DeliveryOrderStatus.COMPLETED;
    private static final DeliveryOrderStatus UPDATED_STATUS = DeliveryOrderStatus.PENDING;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/delivery-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryOrderMockMvc;

    private DeliveryOrder deliveryOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryOrder createEntity(EntityManager em) {
        DeliveryOrder deliveryOrder = new DeliveryOrder()
            .doNumber(DEFAULT_DO_NUMBER)
            .placedDate(DEFAULT_PLACED_DATE)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE);
        return deliveryOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryOrder createUpdatedEntity(EntityManager em) {
        DeliveryOrder deliveryOrder = new DeliveryOrder()
            .doNumber(UPDATED_DO_NUMBER)
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);
        return deliveryOrder;
    }

    @BeforeEach
    public void initTest() {
        deliveryOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryOrder() throws Exception {
        int databaseSizeBeforeCreate = deliveryOrderRepository.findAll().size();
        // Create the DeliveryOrder
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);
        restDeliveryOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryOrder testDeliveryOrder = deliveryOrderList.get(deliveryOrderList.size() - 1);
        assertThat(testDeliveryOrder.getDoNumber()).isEqualTo(DEFAULT_DO_NUMBER);
        assertThat(testDeliveryOrder.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testDeliveryOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDeliveryOrder.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createDeliveryOrderWithExistingId() throws Exception {
        // Create the DeliveryOrder with an existing ID
        deliveryOrder.setId(1L);
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        int databaseSizeBeforeCreate = deliveryOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDoNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOrderRepository.findAll().size();
        // set the field null
        deliveryOrder.setDoNumber(null);

        // Create the DeliveryOrder, which fails.
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        restDeliveryOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlacedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOrderRepository.findAll().size();
        // set the field null
        deliveryOrder.setPlacedDate(null);

        // Create the DeliveryOrder, which fails.
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        restDeliveryOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOrderRepository.findAll().size();
        // set the field null
        deliveryOrder.setStatus(null);

        // Create the DeliveryOrder, which fails.
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        restDeliveryOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOrderRepository.findAll().size();
        // set the field null
        deliveryOrder.setCode(null);

        // Create the DeliveryOrder, which fails.
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        restDeliveryOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveryOrders() throws Exception {
        // Initialize the database
        deliveryOrderRepository.saveAndFlush(deliveryOrder);

        // Get all the deliveryOrderList
        restDeliveryOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].doNumber").value(hasItem(DEFAULT_DO_NUMBER)))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getDeliveryOrder() throws Exception {
        // Initialize the database
        deliveryOrderRepository.saveAndFlush(deliveryOrder);

        // Get the deliveryOrder
        restDeliveryOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryOrder.getId().intValue()))
            .andExpect(jsonPath("$.doNumber").value(DEFAULT_DO_NUMBER))
            .andExpect(jsonPath("$.placedDate").value(DEFAULT_PLACED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryOrder() throws Exception {
        // Get the deliveryOrder
        restDeliveryOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryOrder() throws Exception {
        // Initialize the database
        deliveryOrderRepository.saveAndFlush(deliveryOrder);

        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();

        // Update the deliveryOrder
        DeliveryOrder updatedDeliveryOrder = deliveryOrderRepository.findById(deliveryOrder.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryOrder are not directly saved in db
        em.detach(updatedDeliveryOrder);
        updatedDeliveryOrder.doNumber(UPDATED_DO_NUMBER).placedDate(UPDATED_PLACED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(updatedDeliveryOrder);

        restDeliveryOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
        DeliveryOrder testDeliveryOrder = deliveryOrderList.get(deliveryOrderList.size() - 1);
        assertThat(testDeliveryOrder.getDoNumber()).isEqualTo(UPDATED_DO_NUMBER);
        assertThat(testDeliveryOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testDeliveryOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryOrder.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryOrder() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();
        deliveryOrder.setId(count.incrementAndGet());

        // Create the DeliveryOrder
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryOrder() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();
        deliveryOrder.setId(count.incrementAndGet());

        // Create the DeliveryOrder
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryOrder() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();
        deliveryOrder.setId(count.incrementAndGet());

        // Create the DeliveryOrder
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryOrderWithPatch() throws Exception {
        // Initialize the database
        deliveryOrderRepository.saveAndFlush(deliveryOrder);

        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();

        // Update the deliveryOrder using partial update
        DeliveryOrder partialUpdatedDeliveryOrder = new DeliveryOrder();
        partialUpdatedDeliveryOrder.setId(deliveryOrder.getId());

        partialUpdatedDeliveryOrder.doNumber(UPDATED_DO_NUMBER).placedDate(UPDATED_PLACED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);

        restDeliveryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryOrder))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
        DeliveryOrder testDeliveryOrder = deliveryOrderList.get(deliveryOrderList.size() - 1);
        assertThat(testDeliveryOrder.getDoNumber()).isEqualTo(UPDATED_DO_NUMBER);
        assertThat(testDeliveryOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testDeliveryOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryOrder.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryOrderWithPatch() throws Exception {
        // Initialize the database
        deliveryOrderRepository.saveAndFlush(deliveryOrder);

        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();

        // Update the deliveryOrder using partial update
        DeliveryOrder partialUpdatedDeliveryOrder = new DeliveryOrder();
        partialUpdatedDeliveryOrder.setId(deliveryOrder.getId());

        partialUpdatedDeliveryOrder.doNumber(UPDATED_DO_NUMBER).placedDate(UPDATED_PLACED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE);

        restDeliveryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryOrder))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
        DeliveryOrder testDeliveryOrder = deliveryOrderList.get(deliveryOrderList.size() - 1);
        assertThat(testDeliveryOrder.getDoNumber()).isEqualTo(UPDATED_DO_NUMBER);
        assertThat(testDeliveryOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testDeliveryOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDeliveryOrder.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryOrder() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();
        deliveryOrder.setId(count.incrementAndGet());

        // Create the DeliveryOrder
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryOrder() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();
        deliveryOrder.setId(count.incrementAndGet());

        // Create the DeliveryOrder
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryOrder() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderRepository.findAll().size();
        deliveryOrder.setId(count.incrementAndGet());

        // Create the DeliveryOrder
        DeliveryOrderDTO deliveryOrderDTO = deliveryOrderMapper.toDto(deliveryOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryOrder in the database
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryOrder() throws Exception {
        // Initialize the database
        deliveryOrderRepository.saveAndFlush(deliveryOrder);

        int databaseSizeBeforeDelete = deliveryOrderRepository.findAll().size();

        // Delete the deliveryOrder
        restDeliveryOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryOrder> deliveryOrderList = deliveryOrderRepository.findAll();
        assertThat(deliveryOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
