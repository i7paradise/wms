package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.OrderItemProduct;
import com.wms.uhfrfid.repository.OrderItemProductRepository;
import com.wms.uhfrfid.service.dto.OrderItemProductDTO;
import com.wms.uhfrfid.service.mapper.OrderItemProductMapper;
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
 * Integration tests for the {@link OrderItemProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemProductResourceIT {

    private static final String DEFAULT_RFID_TAG = "AAAAAAAAAA";
    private static final String UPDATED_RFID_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-item-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderItemProductRepository orderItemProductRepository;

    @Autowired
    private OrderItemProductMapper orderItemProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemProductMockMvc;

    private OrderItemProduct orderItemProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemProduct createEntity(EntityManager em) {
        OrderItemProduct orderItemProduct = new OrderItemProduct().rfidTAG(DEFAULT_RFID_TAG);
        return orderItemProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItemProduct createUpdatedEntity(EntityManager em) {
        OrderItemProduct orderItemProduct = new OrderItemProduct().rfidTAG(UPDATED_RFID_TAG);
        return orderItemProduct;
    }

    @BeforeEach
    public void initTest() {
        orderItemProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderItemProduct() throws Exception {
        int databaseSizeBeforeCreate = orderItemProductRepository.findAll().size();
        // Create the OrderItemProduct
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);
        restOrderItemProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItemProduct testOrderItemProduct = orderItemProductList.get(orderItemProductList.size() - 1);
        assertThat(testOrderItemProduct.getRfidTAG()).isEqualTo(DEFAULT_RFID_TAG);
    }

    @Test
    @Transactional
    void createOrderItemProductWithExistingId() throws Exception {
        // Create the OrderItemProduct with an existing ID
        orderItemProduct.setId(1L);
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        int databaseSizeBeforeCreate = orderItemProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRfidTAGIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemProductRepository.findAll().size();
        // set the field null
        orderItemProduct.setRfidTAG(null);

        // Create the OrderItemProduct, which fails.
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        restOrderItemProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItemProducts() throws Exception {
        // Initialize the database
        orderItemProductRepository.saveAndFlush(orderItemProduct);

        // Get all the orderItemProductList
        restOrderItemProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItemProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].rfidTAG").value(hasItem(DEFAULT_RFID_TAG)));
    }

    @Test
    @Transactional
    void getOrderItemProduct() throws Exception {
        // Initialize the database
        orderItemProductRepository.saveAndFlush(orderItemProduct);

        // Get the orderItemProduct
        restOrderItemProductMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItemProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItemProduct.getId().intValue()))
            .andExpect(jsonPath("$.rfidTAG").value(DEFAULT_RFID_TAG));
    }

    @Test
    @Transactional
    void getNonExistingOrderItemProduct() throws Exception {
        // Get the orderItemProduct
        restOrderItemProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderItemProduct() throws Exception {
        // Initialize the database
        orderItemProductRepository.saveAndFlush(orderItemProduct);

        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();

        // Update the orderItemProduct
        OrderItemProduct updatedOrderItemProduct = orderItemProductRepository.findById(orderItemProduct.getId()).get();
        // Disconnect from session so that the updates on updatedOrderItemProduct are not directly saved in db
        em.detach(updatedOrderItemProduct);
        updatedOrderItemProduct.rfidTAG(UPDATED_RFID_TAG);
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(updatedOrderItemProduct);

        restOrderItemProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
        OrderItemProduct testOrderItemProduct = orderItemProductList.get(orderItemProductList.size() - 1);
        assertThat(testOrderItemProduct.getRfidTAG()).isEqualTo(UPDATED_RFID_TAG);
    }

    @Test
    @Transactional
    void putNonExistingOrderItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();
        orderItemProduct.setId(count.incrementAndGet());

        // Create the OrderItemProduct
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();
        orderItemProduct.setId(count.incrementAndGet());

        // Create the OrderItemProduct
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();
        orderItemProduct.setId(count.incrementAndGet());

        // Create the OrderItemProduct
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemProductMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemProductWithPatch() throws Exception {
        // Initialize the database
        orderItemProductRepository.saveAndFlush(orderItemProduct);

        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();

        // Update the orderItemProduct using partial update
        OrderItemProduct partialUpdatedOrderItemProduct = new OrderItemProduct();
        partialUpdatedOrderItemProduct.setId(orderItemProduct.getId());

        restOrderItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItemProduct))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
        OrderItemProduct testOrderItemProduct = orderItemProductList.get(orderItemProductList.size() - 1);
        assertThat(testOrderItemProduct.getRfidTAG()).isEqualTo(DEFAULT_RFID_TAG);
    }

    @Test
    @Transactional
    void fullUpdateOrderItemProductWithPatch() throws Exception {
        // Initialize the database
        orderItemProductRepository.saveAndFlush(orderItemProduct);

        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();

        // Update the orderItemProduct using partial update
        OrderItemProduct partialUpdatedOrderItemProduct = new OrderItemProduct();
        partialUpdatedOrderItemProduct.setId(orderItemProduct.getId());

        partialUpdatedOrderItemProduct.rfidTAG(UPDATED_RFID_TAG);

        restOrderItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItemProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItemProduct))
            )
            .andExpect(status().isOk());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
        OrderItemProduct testOrderItemProduct = orderItemProductList.get(orderItemProductList.size() - 1);
        assertThat(testOrderItemProduct.getRfidTAG()).isEqualTo(UPDATED_RFID_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingOrderItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();
        orderItemProduct.setId(count.incrementAndGet());

        // Create the OrderItemProduct
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemProductDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();
        orderItemProduct.setId(count.incrementAndGet());

        // Create the OrderItemProduct
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = orderItemProductRepository.findAll().size();
        orderItemProduct.setId(count.incrementAndGet());

        // Create the OrderItemProduct
        OrderItemProductDTO orderItemProductDTO = orderItemProductMapper.toDto(orderItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItemProduct in the database
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItemProduct() throws Exception {
        // Initialize the database
        orderItemProductRepository.saveAndFlush(orderItemProduct);

        int databaseSizeBeforeDelete = orderItemProductRepository.findAll().size();

        // Delete the orderItemProduct
        restOrderItemProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItemProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItemProduct> orderItemProductList = orderItemProductRepository.findAll();
        assertThat(orderItemProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
