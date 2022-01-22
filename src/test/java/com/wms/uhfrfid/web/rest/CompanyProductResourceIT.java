package com.wms.uhfrfid.web.rest;

import static com.wms.uhfrfid.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.CompanyProduct;
import com.wms.uhfrfid.repository.CompanyProductRepository;
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
 * Integration tests for the {@link CompanyProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyProductResourceIT {

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(0);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(1);

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_STOCKING_RATIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_STOCKING_RATIO = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/company-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyProductRepository companyProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyProductMockMvc;

    private CompanyProduct companyProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyProduct createEntity(EntityManager em) {
        CompanyProduct companyProduct = new CompanyProduct()
            .quantity(DEFAULT_QUANTITY)
            .sku(DEFAULT_SKU)
            .stockingRatio(DEFAULT_STOCKING_RATIO);
        return companyProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyProduct createUpdatedEntity(EntityManager em) {
        CompanyProduct companyProduct = new CompanyProduct()
            .quantity(UPDATED_QUANTITY)
            .sku(UPDATED_SKU)
            .stockingRatio(UPDATED_STOCKING_RATIO);
        return companyProduct;
    }

    @BeforeEach
    public void initTest() {
        companyProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyProduct() throws Exception {
        int databaseSizeBeforeCreate = companyProductRepository.findAll().size();
        // Create the CompanyProduct
        restCompanyProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isCreated());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyProduct testCompanyProduct = companyProductList.get(companyProductList.size() - 1);
        assertThat(testCompanyProduct.getQuantity()).isEqualByComparingTo(DEFAULT_QUANTITY);
        assertThat(testCompanyProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testCompanyProduct.getStockingRatio()).isEqualByComparingTo(DEFAULT_STOCKING_RATIO);
    }

    @Test
    @Transactional
    void createCompanyProductWithExistingId() throws Exception {
        // Create the CompanyProduct with an existing ID
        companyProduct.setId(1L);

        int databaseSizeBeforeCreate = companyProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyProductRepository.findAll().size();
        // set the field null
        companyProduct.setQuantity(null);

        // Create the CompanyProduct, which fails.

        restCompanyProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isBadRequest());

        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockingRatioIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyProductRepository.findAll().size();
        // set the field null
        companyProduct.setStockingRatio(null);

        // Create the CompanyProduct, which fails.

        restCompanyProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isBadRequest());

        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanyProducts() throws Exception {
        // Initialize the database
        companyProductRepository.saveAndFlush(companyProduct);

        // Get all the companyProductList
        restCompanyProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(sameNumber(DEFAULT_QUANTITY))))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].stockingRatio").value(hasItem(sameNumber(DEFAULT_STOCKING_RATIO))));
    }

    @Test
    @Transactional
    void getCompanyProduct() throws Exception {
        // Initialize the database
        companyProductRepository.saveAndFlush(companyProduct);

        // Get the companyProduct
        restCompanyProductMockMvc
            .perform(get(ENTITY_API_URL_ID, companyProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyProduct.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(sameNumber(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.stockingRatio").value(sameNumber(DEFAULT_STOCKING_RATIO)));
    }

    @Test
    @Transactional
    void getNonExistingCompanyProduct() throws Exception {
        // Get the companyProduct
        restCompanyProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompanyProduct() throws Exception {
        // Initialize the database
        companyProductRepository.saveAndFlush(companyProduct);

        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();

        // Update the companyProduct
        CompanyProduct updatedCompanyProduct = companyProductRepository.findById(companyProduct.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyProduct are not directly saved in db
        em.detach(updatedCompanyProduct);
        updatedCompanyProduct.quantity(UPDATED_QUANTITY).sku(UPDATED_SKU).stockingRatio(UPDATED_STOCKING_RATIO);

        restCompanyProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyProduct))
            )
            .andExpect(status().isOk());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
        CompanyProduct testCompanyProduct = companyProductList.get(companyProductList.size() - 1);
        assertThat(testCompanyProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCompanyProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testCompanyProduct.getStockingRatio()).isEqualTo(UPDATED_STOCKING_RATIO);
    }

    @Test
    @Transactional
    void putNonExistingCompanyProduct() throws Exception {
        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();
        companyProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyProduct() throws Exception {
        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();
        companyProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyProduct() throws Exception {
        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();
        companyProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyProductWithPatch() throws Exception {
        // Initialize the database
        companyProductRepository.saveAndFlush(companyProduct);

        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();

        // Update the companyProduct using partial update
        CompanyProduct partialUpdatedCompanyProduct = new CompanyProduct();
        partialUpdatedCompanyProduct.setId(companyProduct.getId());

        partialUpdatedCompanyProduct.quantity(UPDATED_QUANTITY);

        restCompanyProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyProduct))
            )
            .andExpect(status().isOk());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
        CompanyProduct testCompanyProduct = companyProductList.get(companyProductList.size() - 1);
        assertThat(testCompanyProduct.getQuantity()).isEqualByComparingTo(UPDATED_QUANTITY);
        assertThat(testCompanyProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testCompanyProduct.getStockingRatio()).isEqualByComparingTo(DEFAULT_STOCKING_RATIO);
    }

    @Test
    @Transactional
    void fullUpdateCompanyProductWithPatch() throws Exception {
        // Initialize the database
        companyProductRepository.saveAndFlush(companyProduct);

        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();

        // Update the companyProduct using partial update
        CompanyProduct partialUpdatedCompanyProduct = new CompanyProduct();
        partialUpdatedCompanyProduct.setId(companyProduct.getId());

        partialUpdatedCompanyProduct.quantity(UPDATED_QUANTITY).sku(UPDATED_SKU).stockingRatio(UPDATED_STOCKING_RATIO);

        restCompanyProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyProduct))
            )
            .andExpect(status().isOk());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
        CompanyProduct testCompanyProduct = companyProductList.get(companyProductList.size() - 1);
        assertThat(testCompanyProduct.getQuantity()).isEqualByComparingTo(UPDATED_QUANTITY);
        assertThat(testCompanyProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testCompanyProduct.getStockingRatio()).isEqualByComparingTo(UPDATED_STOCKING_RATIO);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyProduct() throws Exception {
        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();
        companyProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyProduct() throws Exception {
        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();
        companyProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyProduct() throws Exception {
        int databaseSizeBeforeUpdate = companyProductRepository.findAll().size();
        companyProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companyProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyProduct in the database
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyProduct() throws Exception {
        // Initialize the database
        companyProductRepository.saveAndFlush(companyProduct);

        int databaseSizeBeforeDelete = companyProductRepository.findAll().size();

        // Delete the companyProduct
        restCompanyProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyProduct> companyProductList = companyProductRepository.findAll();
        assertThat(companyProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
