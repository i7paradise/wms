package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DeliveryItemProduct;
import com.wms.uhfrfid.repository.DeliveryItemProductRepository;
import com.wms.uhfrfid.service.dto.DeliveryItemProductDTO;
import com.wms.uhfrfid.service.mapper.DeliveryItemProductMapper;
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
 * Integration tests for the {@link DeliveryItemProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryItemProductResourceIT {

    private static final String DEFAULT_RFID_TAG = "AAAAAAAAAA";
    private static final String UPDATED_RFID_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/delivery-item-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryItemProductRepository deliveryItemProductRepository;

    @Autowired
    private DeliveryItemProductMapper deliveryItemProductMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryItemProductMockMvc;

    private DeliveryItemProduct deliveryItemProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryItemProduct createEntity(EntityManager em) {
        DeliveryItemProduct deliveryItemProduct = new DeliveryItemProduct().rfidTAG(DEFAULT_RFID_TAG);
        return deliveryItemProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryItemProduct createUpdatedEntity(EntityManager em) {
        DeliveryItemProduct deliveryItemProduct = new DeliveryItemProduct().rfidTAG(UPDATED_RFID_TAG);
        return deliveryItemProduct;
    }

    @BeforeEach
    public void initTest() {
        deliveryItemProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryItemProduct() throws Exception {
        int databaseSizeBeforeCreate = deliveryItemProductRepository.findAll().size();
        // Create the DeliveryItemProduct
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);
        restDeliveryItemProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryItemProduct testDeliveryItemProduct = deliveryItemProductList.get(deliveryItemProductList.size() - 1);
        assertThat(testDeliveryItemProduct.getRfidTAG()).isEqualTo(DEFAULT_RFID_TAG);
    }

    @Test
    @Transactional
    void createDeliveryItemProductWithExistingId() throws Exception {
        // Create the DeliveryItemProduct with an existing ID
        deliveryItemProduct.setId(1L);
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        int databaseSizeBeforeCreate = deliveryItemProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryItemProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRfidTAGIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryItemProductRepository.findAll().size();
        // set the field null
        deliveryItemProduct.setRfidTAG(null);

        // Create the DeliveryItemProduct, which fails.
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        restDeliveryItemProductMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveryItemProducts() throws Exception {
        // Initialize the database
        deliveryItemProductRepository.saveAndFlush(deliveryItemProduct);

        // Get all the deliveryItemProductList
        restDeliveryItemProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryItemProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].rfidTAG").value(hasItem(DEFAULT_RFID_TAG)));
    }

    @Test
    @Transactional
    void getDeliveryItemProduct() throws Exception {
        // Initialize the database
        deliveryItemProductRepository.saveAndFlush(deliveryItemProduct);

        // Get the deliveryItemProduct
        restDeliveryItemProductMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryItemProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryItemProduct.getId().intValue()))
            .andExpect(jsonPath("$.rfidTAG").value(DEFAULT_RFID_TAG));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryItemProduct() throws Exception {
        // Get the deliveryItemProduct
        restDeliveryItemProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryItemProduct() throws Exception {
        // Initialize the database
        deliveryItemProductRepository.saveAndFlush(deliveryItemProduct);

        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();

        // Update the deliveryItemProduct
        DeliveryItemProduct updatedDeliveryItemProduct = deliveryItemProductRepository.findById(deliveryItemProduct.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryItemProduct are not directly saved in db
        em.detach(updatedDeliveryItemProduct);
        updatedDeliveryItemProduct.rfidTAG(UPDATED_RFID_TAG);
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(updatedDeliveryItemProduct);

        restDeliveryItemProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryItemProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
        DeliveryItemProduct testDeliveryItemProduct = deliveryItemProductList.get(deliveryItemProductList.size() - 1);
        assertThat(testDeliveryItemProduct.getRfidTAG()).isEqualTo(UPDATED_RFID_TAG);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();
        deliveryItemProduct.setId(count.incrementAndGet());

        // Create the DeliveryItemProduct
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryItemProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryItemProductDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();
        deliveryItemProduct.setId(count.incrementAndGet());

        // Create the DeliveryItemProduct
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();
        deliveryItemProduct.setId(count.incrementAndGet());

        // Create the DeliveryItemProduct
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemProductMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryItemProductWithPatch() throws Exception {
        // Initialize the database
        deliveryItemProductRepository.saveAndFlush(deliveryItemProduct);

        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();

        // Update the deliveryItemProduct using partial update
        DeliveryItemProduct partialUpdatedDeliveryItemProduct = new DeliveryItemProduct();
        partialUpdatedDeliveryItemProduct.setId(deliveryItemProduct.getId());

        partialUpdatedDeliveryItemProduct.rfidTAG(UPDATED_RFID_TAG);

        restDeliveryItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryItemProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryItemProduct))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
        DeliveryItemProduct testDeliveryItemProduct = deliveryItemProductList.get(deliveryItemProductList.size() - 1);
        assertThat(testDeliveryItemProduct.getRfidTAG()).isEqualTo(UPDATED_RFID_TAG);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryItemProductWithPatch() throws Exception {
        // Initialize the database
        deliveryItemProductRepository.saveAndFlush(deliveryItemProduct);

        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();

        // Update the deliveryItemProduct using partial update
        DeliveryItemProduct partialUpdatedDeliveryItemProduct = new DeliveryItemProduct();
        partialUpdatedDeliveryItemProduct.setId(deliveryItemProduct.getId());

        partialUpdatedDeliveryItemProduct.rfidTAG(UPDATED_RFID_TAG);

        restDeliveryItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryItemProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryItemProduct))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
        DeliveryItemProduct testDeliveryItemProduct = deliveryItemProductList.get(deliveryItemProductList.size() - 1);
        assertThat(testDeliveryItemProduct.getRfidTAG()).isEqualTo(UPDATED_RFID_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();
        deliveryItemProduct.setId(count.incrementAndGet());

        // Create the DeliveryItemProduct
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryItemProductDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();
        deliveryItemProduct.setId(count.incrementAndGet());

        // Create the DeliveryItemProduct
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryItemProduct() throws Exception {
        int databaseSizeBeforeUpdate = deliveryItemProductRepository.findAll().size();
        deliveryItemProduct.setId(count.incrementAndGet());

        // Create the DeliveryItemProduct
        DeliveryItemProductDTO deliveryItemProductDTO = deliveryItemProductMapper.toDto(deliveryItemProduct);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryItemProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryItemProductDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryItemProduct in the database
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryItemProduct() throws Exception {
        // Initialize the database
        deliveryItemProductRepository.saveAndFlush(deliveryItemProduct);

        int databaseSizeBeforeDelete = deliveryItemProductRepository.findAll().size();

        // Delete the deliveryItemProduct
        restDeliveryItemProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryItemProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryItemProduct> deliveryItemProductList = deliveryItemProductRepository.findAll();
        assertThat(deliveryItemProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
