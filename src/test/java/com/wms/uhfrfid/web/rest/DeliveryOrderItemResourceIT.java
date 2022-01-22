package com.wms.uhfrfid.web.rest;

import static com.wms.uhfrfid.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DeliveryOrderItem;
import com.wms.uhfrfid.domain.enumeration.DeliveryOrderItemStatus;
import com.wms.uhfrfid.repository.DeliveryOrderItemRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link DeliveryOrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryOrderItemResourceIT {

    private static final BigDecimal DEFAULT_UNIT_QUANTITY = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_QUANTITY = new BigDecimal(1);

    private static final BigDecimal DEFAULT_CONTAINER_QUANTITY = new BigDecimal(0);
    private static final BigDecimal UPDATED_CONTAINER_QUANTITY = new BigDecimal(1);

    private static final DeliveryOrderItemStatus DEFAULT_STATUS = DeliveryOrderItemStatus.IN_PROGRESS;
    private static final DeliveryOrderItemStatus UPDATED_STATUS = DeliveryOrderItemStatus.VERIFIED;

    private static final String ENTITY_API_URL = "/api/delivery-order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryOrderItemRepository deliveryOrderItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryOrderItemMockMvc;

    private DeliveryOrderItem deliveryOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryOrderItem createEntity(EntityManager em) {
        DeliveryOrderItem deliveryOrderItem = new DeliveryOrderItem()
            .unitQuantity(DEFAULT_UNIT_QUANTITY)
            .containerQuantity(DEFAULT_CONTAINER_QUANTITY)
            .status(DEFAULT_STATUS);
        return deliveryOrderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryOrderItem createUpdatedEntity(EntityManager em) {
        DeliveryOrderItem deliveryOrderItem = new DeliveryOrderItem()
            .unitQuantity(UPDATED_UNIT_QUANTITY)
            .containerQuantity(UPDATED_CONTAINER_QUANTITY)
            .status(UPDATED_STATUS);
        return deliveryOrderItem;
    }

    @BeforeEach
    public void initTest() {
        deliveryOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliveryOrderItem() throws Exception {
        int databaseSizeBeforeCreate = deliveryOrderItemRepository.findAll().size();
        // Create the DeliveryOrderItem
        restDeliveryOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isCreated());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryOrderItem testDeliveryOrderItem = deliveryOrderItemList.get(deliveryOrderItemList.size() - 1);
        assertThat(testDeliveryOrderItem.getUnitQuantity()).isEqualByComparingTo(DEFAULT_UNIT_QUANTITY);
        assertThat(testDeliveryOrderItem.getContainerQuantity()).isEqualByComparingTo(DEFAULT_CONTAINER_QUANTITY);
        assertThat(testDeliveryOrderItem.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createDeliveryOrderItemWithExistingId() throws Exception {
        // Create the DeliveryOrderItem with an existing ID
        deliveryOrderItem.setId(1L);

        int databaseSizeBeforeCreate = deliveryOrderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnitQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOrderItemRepository.findAll().size();
        // set the field null
        deliveryOrderItem.setUnitQuantity(null);

        // Create the DeliveryOrderItem, which fails.

        restDeliveryOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContainerQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOrderItemRepository.findAll().size();
        // set the field null
        deliveryOrderItem.setContainerQuantity(null);

        // Create the DeliveryOrderItem, which fails.

        restDeliveryOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryOrderItemRepository.findAll().size();
        // set the field null
        deliveryOrderItem.setStatus(null);

        // Create the DeliveryOrderItem, which fails.

        restDeliveryOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveryOrderItems() throws Exception {
        // Initialize the database
        deliveryOrderItemRepository.saveAndFlush(deliveryOrderItem);

        // Get all the deliveryOrderItemList
        restDeliveryOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitQuantity").value(hasItem(sameNumber(DEFAULT_UNIT_QUANTITY))))
            .andExpect(jsonPath("$.[*].containerQuantity").value(hasItem(sameNumber(DEFAULT_CONTAINER_QUANTITY))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDeliveryOrderItem() throws Exception {
        // Initialize the database
        deliveryOrderItemRepository.saveAndFlush(deliveryOrderItem);

        // Get the deliveryOrderItem
        restDeliveryOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, deliveryOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.unitQuantity").value(sameNumber(DEFAULT_UNIT_QUANTITY)))
            .andExpect(jsonPath("$.containerQuantity").value(sameNumber(DEFAULT_CONTAINER_QUANTITY)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeliveryOrderItem() throws Exception {
        // Get the deliveryOrderItem
        restDeliveryOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliveryOrderItem() throws Exception {
        // Initialize the database
        deliveryOrderItemRepository.saveAndFlush(deliveryOrderItem);

        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();

        // Update the deliveryOrderItem
        DeliveryOrderItem updatedDeliveryOrderItem = deliveryOrderItemRepository.findById(deliveryOrderItem.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryOrderItem are not directly saved in db
        em.detach(updatedDeliveryOrderItem);
        updatedDeliveryOrderItem.unitQuantity(UPDATED_UNIT_QUANTITY).containerQuantity(UPDATED_CONTAINER_QUANTITY).status(UPDATED_STATUS);

        restDeliveryOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeliveryOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeliveryOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
        DeliveryOrderItem testDeliveryOrderItem = deliveryOrderItemList.get(deliveryOrderItemList.size() - 1);
        assertThat(testDeliveryOrderItem.getUnitQuantity()).isEqualTo(UPDATED_UNIT_QUANTITY);
        assertThat(testDeliveryOrderItem.getContainerQuantity()).isEqualTo(UPDATED_CONTAINER_QUANTITY);
        assertThat(testDeliveryOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingDeliveryOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();
        deliveryOrderItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliveryOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();
        deliveryOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliveryOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();
        deliveryOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryOrderItemWithPatch() throws Exception {
        // Initialize the database
        deliveryOrderItemRepository.saveAndFlush(deliveryOrderItem);

        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();

        // Update the deliveryOrderItem using partial update
        DeliveryOrderItem partialUpdatedDeliveryOrderItem = new DeliveryOrderItem();
        partialUpdatedDeliveryOrderItem.setId(deliveryOrderItem.getId());

        partialUpdatedDeliveryOrderItem.containerQuantity(UPDATED_CONTAINER_QUANTITY).status(UPDATED_STATUS);

        restDeliveryOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
        DeliveryOrderItem testDeliveryOrderItem = deliveryOrderItemList.get(deliveryOrderItemList.size() - 1);
        assertThat(testDeliveryOrderItem.getUnitQuantity()).isEqualByComparingTo(DEFAULT_UNIT_QUANTITY);
        assertThat(testDeliveryOrderItem.getContainerQuantity()).isEqualByComparingTo(UPDATED_CONTAINER_QUANTITY);
        assertThat(testDeliveryOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryOrderItemWithPatch() throws Exception {
        // Initialize the database
        deliveryOrderItemRepository.saveAndFlush(deliveryOrderItem);

        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();

        // Update the deliveryOrderItem using partial update
        DeliveryOrderItem partialUpdatedDeliveryOrderItem = new DeliveryOrderItem();
        partialUpdatedDeliveryOrderItem.setId(deliveryOrderItem.getId());

        partialUpdatedDeliveryOrderItem
            .unitQuantity(UPDATED_UNIT_QUANTITY)
            .containerQuantity(UPDATED_CONTAINER_QUANTITY)
            .status(UPDATED_STATUS);

        restDeliveryOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliveryOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
        DeliveryOrderItem testDeliveryOrderItem = deliveryOrderItemList.get(deliveryOrderItemList.size() - 1);
        assertThat(testDeliveryOrderItem.getUnitQuantity()).isEqualByComparingTo(UPDATED_UNIT_QUANTITY);
        assertThat(testDeliveryOrderItem.getContainerQuantity()).isEqualByComparingTo(UPDATED_CONTAINER_QUANTITY);
        assertThat(testDeliveryOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDeliveryOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();
        deliveryOrderItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliveryOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();
        deliveryOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliveryOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = deliveryOrderItemRepository.findAll().size();
        deliveryOrderItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryOrderItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeliveryOrderItem in the database
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliveryOrderItem() throws Exception {
        // Initialize the database
        deliveryOrderItemRepository.saveAndFlush(deliveryOrderItem);

        int databaseSizeBeforeDelete = deliveryOrderItemRepository.findAll().size();

        // Delete the deliveryOrderItem
        restDeliveryOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliveryOrderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeliveryOrderItem> deliveryOrderItemList = deliveryOrderItemRepository.findAll();
        assertThat(deliveryOrderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
