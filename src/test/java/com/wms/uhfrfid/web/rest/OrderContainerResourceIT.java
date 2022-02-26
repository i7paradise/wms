package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.OrderContainer;
import com.wms.uhfrfid.repository.OrderContainerRepository;
import com.wms.uhfrfid.service.dto.OrderContainerDTO;
import com.wms.uhfrfid.service.mapper.OrderContainerMapper;
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
 * Integration tests for the {@link OrderContainerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderContainerResourceIT {

    private static final String DEFAULT_SUPPLIER_RFID_TAG = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_RFID_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-containers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderContainerRepository orderContainerRepository;

    @Autowired
    private OrderContainerMapper orderContainerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderContainerMockMvc;

    private OrderContainer orderContainer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderContainer createEntity(EntityManager em) {
        OrderContainer orderContainer = new OrderContainer().supplierRFIDTag(DEFAULT_SUPPLIER_RFID_TAG);
        return orderContainer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderContainer createUpdatedEntity(EntityManager em) {
        OrderContainer orderContainer = new OrderContainer().supplierRFIDTag(UPDATED_SUPPLIER_RFID_TAG);
        return orderContainer;
    }

    @BeforeEach
    public void initTest() {
        orderContainer = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderContainer() throws Exception {
        int databaseSizeBeforeCreate = orderContainerRepository.findAll().size();
        // Create the OrderContainer
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);
        restOrderContainerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeCreate + 1);
        OrderContainer testOrderContainer = orderContainerList.get(orderContainerList.size() - 1);
        assertThat(testOrderContainer.getSupplierRFIDTag()).isEqualTo(DEFAULT_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void createOrderContainerWithExistingId() throws Exception {
        // Create the OrderContainer with an existing ID
        orderContainer.setId(1L);
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);

        int databaseSizeBeforeCreate = orderContainerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderContainerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderContainers() throws Exception {
        // Initialize the database
        orderContainerRepository.saveAndFlush(orderContainer);

        // Get all the orderContainerList
        restOrderContainerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderContainer.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierRFIDTag").value(hasItem(DEFAULT_SUPPLIER_RFID_TAG)));
    }

    @Test
    @Transactional
    void getOrderContainer() throws Exception {
        // Initialize the database
        orderContainerRepository.saveAndFlush(orderContainer);

        // Get the orderContainer
        restOrderContainerMockMvc
            .perform(get(ENTITY_API_URL_ID, orderContainer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderContainer.getId().intValue()))
            .andExpect(jsonPath("$.supplierRFIDTag").value(DEFAULT_SUPPLIER_RFID_TAG));
    }

    @Test
    @Transactional
    void getNonExistingOrderContainer() throws Exception {
        // Get the orderContainer
        restOrderContainerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderContainer() throws Exception {
        // Initialize the database
        orderContainerRepository.saveAndFlush(orderContainer);

        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();

        // Update the orderContainer
        OrderContainer updatedOrderContainer = orderContainerRepository.findById(orderContainer.getId()).get();
        // Disconnect from session so that the updates on updatedOrderContainer are not directly saved in db
        em.detach(updatedOrderContainer);
        updatedOrderContainer.supplierRFIDTag(UPDATED_SUPPLIER_RFID_TAG);
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(updatedOrderContainer);

        restOrderContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderContainerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
        OrderContainer testOrderContainer = orderContainerList.get(orderContainerList.size() - 1);
        assertThat(testOrderContainer.getSupplierRFIDTag()).isEqualTo(UPDATED_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void putNonExistingOrderContainer() throws Exception {
        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();
        orderContainer.setId(count.incrementAndGet());

        // Create the OrderContainer
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderContainerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderContainer() throws Exception {
        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();
        orderContainer.setId(count.incrementAndGet());

        // Create the OrderContainer
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderContainer() throws Exception {
        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();
        orderContainer.setId(count.incrementAndGet());

        // Create the OrderContainer
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContainerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderContainerWithPatch() throws Exception {
        // Initialize the database
        orderContainerRepository.saveAndFlush(orderContainer);

        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();

        // Update the orderContainer using partial update
        OrderContainer partialUpdatedOrderContainer = new OrderContainer();
        partialUpdatedOrderContainer.setId(orderContainer.getId());

        partialUpdatedOrderContainer.supplierRFIDTag(UPDATED_SUPPLIER_RFID_TAG);

        restOrderContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderContainer))
            )
            .andExpect(status().isOk());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
        OrderContainer testOrderContainer = orderContainerList.get(orderContainerList.size() - 1);
        assertThat(testOrderContainer.getSupplierRFIDTag()).isEqualTo(UPDATED_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void fullUpdateOrderContainerWithPatch() throws Exception {
        // Initialize the database
        orderContainerRepository.saveAndFlush(orderContainer);

        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();

        // Update the orderContainer using partial update
        OrderContainer partialUpdatedOrderContainer = new OrderContainer();
        partialUpdatedOrderContainer.setId(orderContainer.getId());

        partialUpdatedOrderContainer.supplierRFIDTag(UPDATED_SUPPLIER_RFID_TAG);

        restOrderContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderContainer))
            )
            .andExpect(status().isOk());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
        OrderContainer testOrderContainer = orderContainerList.get(orderContainerList.size() - 1);
        assertThat(testOrderContainer.getSupplierRFIDTag()).isEqualTo(UPDATED_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingOrderContainer() throws Exception {
        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();
        orderContainer.setId(count.incrementAndGet());

        // Create the OrderContainer
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderContainerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderContainer() throws Exception {
        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();
        orderContainer.setId(count.incrementAndGet());

        // Create the OrderContainer
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderContainer() throws Exception {
        int databaseSizeBeforeUpdate = orderContainerRepository.findAll().size();
        orderContainer.setId(count.incrementAndGet());

        // Create the OrderContainer
        OrderContainerDTO orderContainerDTO = orderContainerMapper.toDto(orderContainer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderContainerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderContainerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderContainer in the database
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderContainer() throws Exception {
        // Initialize the database
        orderContainerRepository.saveAndFlush(orderContainer);

        int databaseSizeBeforeDelete = orderContainerRepository.findAll().size();

        // Delete the orderContainer
        restOrderContainerMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderContainer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderContainer> orderContainerList = orderContainerRepository.findAll();
        assertThat(orderContainerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
