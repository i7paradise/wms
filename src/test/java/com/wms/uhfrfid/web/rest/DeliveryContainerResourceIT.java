package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DeliveryContainer;
import com.wms.uhfrfid.repository.DeliveryContainerRepository;
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
 * Integration tests for the {@link DeliveryContainerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryContainerResourceIT {

    private static final String DEFAULT_SUPPLIER_RFID_TAG = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_RFID_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/delivery-containers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryContainerRepository deliveryContainerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryContainerMockMvc;

    private DeliveryContainer deliveryContainer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryContainer createEntity(EntityManager em) {
        DeliveryContainer deliveryContainer = new DeliveryContainer().supplierRFIDTag(DEFAULT_SUPPLIER_RFID_TAG);
        return deliveryContainer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryContainer createUpdatedEntity(EntityManager em) {
        DeliveryContainer deliveryContainer = new DeliveryContainer().supplierRFIDTag(UPDATED_SUPPLIER_RFID_TAG);
        return deliveryContainer;
    }

    @BeforeEach
    public void initTest() {
        deliveryContainer = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryContainer() throws Exception {
        int databaseSizeBeforeCreate = deliveryContainerRepository.findAll().size();
        // Create the DeliveryContainer
        restDeliveryContainerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryContainer testDeliveryContainer = deliveryContainerList.get(deliveryContainerList.size() - 1);
        assertThat(testDeliveryContainer.getSupplierRFIDTag()).isEqualTo(DEFAULT_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void createDeliveryContainerWithExistingId() throws Exception {
        // Create the DeliveryContainer with an existing ID
        deliveryContainer.setId(1L);

        int databaseSizeBeforeCreate = deliveryContainerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryContainerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeliveryContainers() throws Exception {
        // Initialize the database
        deliveryContainerRepository.saveAndFlush(deliveryContainer);

        // Get all the deliveryContainerList
        restDeliveryContainerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryContainer.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierRFIDTag").value(hasItem(DEFAULT_SUPPLIER_RFID_TAG)));
    }

    @Test
    @Transactional
    void getDeliveryContainer() throws Exception {
        // Initialize the database
        deliveryContainerRepository.saveAndFlush(deliveryContainer);

        // Get the deliveryContainer
        restDeliveryContainerMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryContainer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryContainer.getId().intValue()))
            .andExpect(jsonPath("$.supplierRFIDTag").value(DEFAULT_SUPPLIER_RFID_TAG));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryContainer() throws Exception {
        // Get the deliveryContainer
        restDeliveryContainerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryContainer() throws Exception {
        // Initialize the database
        deliveryContainerRepository.saveAndFlush(deliveryContainer);

        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();

        // Update the deliveryContainer
        DeliveryContainer updatedDeliveryContainer = deliveryContainerRepository.findById(deliveryContainer.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryContainer are not directly saved in db
        em.detach(updatedDeliveryContainer);
        updatedDeliveryContainer.supplierRFIDTag(UPDATED_SUPPLIER_RFID_TAG);

        restDeliveryContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeliveryContainer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeliveryContainer))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
        DeliveryContainer testDeliveryContainer = deliveryContainerList.get(deliveryContainerList.size() - 1);
        assertThat(testDeliveryContainer.getSupplierRFIDTag()).isEqualTo(UPDATED_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryContainer() throws Exception {
        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();
        deliveryContainer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryContainer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryContainer() throws Exception {
        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();
        deliveryContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryContainer() throws Exception {
        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();
        deliveryContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryContainerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryContainerWithPatch() throws Exception {
        // Initialize the database
        deliveryContainerRepository.saveAndFlush(deliveryContainer);

        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();

        // Update the deliveryContainer using partial update
        DeliveryContainer partialUpdatedDeliveryContainer = new DeliveryContainer();
        partialUpdatedDeliveryContainer.setId(deliveryContainer.getId());

        restDeliveryContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryContainer))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
        DeliveryContainer testDeliveryContainer = deliveryContainerList.get(deliveryContainerList.size() - 1);
        assertThat(testDeliveryContainer.getSupplierRFIDTag()).isEqualTo(DEFAULT_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryContainerWithPatch() throws Exception {
        // Initialize the database
        deliveryContainerRepository.saveAndFlush(deliveryContainer);

        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();

        // Update the deliveryContainer using partial update
        DeliveryContainer partialUpdatedDeliveryContainer = new DeliveryContainer();
        partialUpdatedDeliveryContainer.setId(deliveryContainer.getId());

        partialUpdatedDeliveryContainer.supplierRFIDTag(UPDATED_SUPPLIER_RFID_TAG);

        restDeliveryContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryContainer))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
        DeliveryContainer testDeliveryContainer = deliveryContainerList.get(deliveryContainerList.size() - 1);
        assertThat(testDeliveryContainer.getSupplierRFIDTag()).isEqualTo(UPDATED_SUPPLIER_RFID_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryContainer() throws Exception {
        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();
        deliveryContainer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryContainer() throws Exception {
        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();
        deliveryContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryContainer() throws Exception {
        int databaseSizeBeforeUpdate = deliveryContainerRepository.findAll().size();
        deliveryContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryContainerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryContainer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryContainer in the database
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryContainer() throws Exception {
        // Initialize the database
        deliveryContainerRepository.saveAndFlush(deliveryContainer);

        int databaseSizeBeforeDelete = deliveryContainerRepository.findAll().size();

        // Delete the deliveryContainer
        restDeliveryContainerMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryContainer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryContainer> deliveryContainerList = deliveryContainerRepository.findAll();
        assertThat(deliveryContainerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
