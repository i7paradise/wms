package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.CompanyContainer;
import com.wms.uhfrfid.repository.CompanyContainerRepository;
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
 * Integration tests for the {@link CompanyContainerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyContainerResourceIT {

    private static final String DEFAULT_RFID_TAG = "AAAAAAAAAA";
    private static final String UPDATED_RFID_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/company-containers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyContainerRepository companyContainerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyContainerMockMvc;

    private CompanyContainer companyContainer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyContainer createEntity(EntityManager em) {
        CompanyContainer companyContainer = new CompanyContainer().rfidTag(DEFAULT_RFID_TAG).color(DEFAULT_COLOR);
        return companyContainer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyContainer createUpdatedEntity(EntityManager em) {
        CompanyContainer companyContainer = new CompanyContainer().rfidTag(UPDATED_RFID_TAG).color(UPDATED_COLOR);
        return companyContainer;
    }

    @BeforeEach
    public void initTest() {
        companyContainer = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyContainer() throws Exception {
        int databaseSizeBeforeCreate = companyContainerRepository.findAll().size();
        // Create the CompanyContainer
        restCompanyContainerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isCreated());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyContainer testCompanyContainer = companyContainerList.get(companyContainerList.size() - 1);
        assertThat(testCompanyContainer.getRfidTag()).isEqualTo(DEFAULT_RFID_TAG);
        assertThat(testCompanyContainer.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void createCompanyContainerWithExistingId() throws Exception {
        // Create the CompanyContainer with an existing ID
        companyContainer.setId(1L);

        int databaseSizeBeforeCreate = companyContainerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyContainerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyContainers() throws Exception {
        // Initialize the database
        companyContainerRepository.saveAndFlush(companyContainer);

        // Get all the companyContainerList
        restCompanyContainerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyContainer.getId().intValue())))
            .andExpect(jsonPath("$.[*].rfidTag").value(hasItem(DEFAULT_RFID_TAG)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)));
    }

    @Test
    @Transactional
    void getCompanyContainer() throws Exception {
        // Initialize the database
        companyContainerRepository.saveAndFlush(companyContainer);

        // Get the companyContainer
        restCompanyContainerMockMvc
            .perform(get(ENTITY_API_URL_ID, companyContainer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyContainer.getId().intValue()))
            .andExpect(jsonPath("$.rfidTag").value(DEFAULT_RFID_TAG))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR));
    }

    @Test
    @Transactional
    void getNonExistingCompanyContainer() throws Exception {
        // Get the companyContainer
        restCompanyContainerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompanyContainer() throws Exception {
        // Initialize the database
        companyContainerRepository.saveAndFlush(companyContainer);

        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();

        // Update the companyContainer
        CompanyContainer updatedCompanyContainer = companyContainerRepository.findById(companyContainer.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyContainer are not directly saved in db
        em.detach(updatedCompanyContainer);
        updatedCompanyContainer.rfidTag(UPDATED_RFID_TAG).color(UPDATED_COLOR);

        restCompanyContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyContainer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyContainer))
            )
            .andExpect(status().isOk());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
        CompanyContainer testCompanyContainer = companyContainerList.get(companyContainerList.size() - 1);
        assertThat(testCompanyContainer.getRfidTag()).isEqualTo(UPDATED_RFID_TAG);
        assertThat(testCompanyContainer.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void putNonExistingCompanyContainer() throws Exception {
        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();
        companyContainer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyContainer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyContainer() throws Exception {
        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();
        companyContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyContainerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyContainer() throws Exception {
        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();
        companyContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyContainerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyContainerWithPatch() throws Exception {
        // Initialize the database
        companyContainerRepository.saveAndFlush(companyContainer);

        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();

        // Update the companyContainer using partial update
        CompanyContainer partialUpdatedCompanyContainer = new CompanyContainer();
        partialUpdatedCompanyContainer.setId(companyContainer.getId());

        restCompanyContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyContainer))
            )
            .andExpect(status().isOk());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
        CompanyContainer testCompanyContainer = companyContainerList.get(companyContainerList.size() - 1);
        assertThat(testCompanyContainer.getRfidTag()).isEqualTo(DEFAULT_RFID_TAG);
        assertThat(testCompanyContainer.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void fullUpdateCompanyContainerWithPatch() throws Exception {
        // Initialize the database
        companyContainerRepository.saveAndFlush(companyContainer);

        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();

        // Update the companyContainer using partial update
        CompanyContainer partialUpdatedCompanyContainer = new CompanyContainer();
        partialUpdatedCompanyContainer.setId(companyContainer.getId());

        partialUpdatedCompanyContainer.rfidTag(UPDATED_RFID_TAG).color(UPDATED_COLOR);

        restCompanyContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyContainer))
            )
            .andExpect(status().isOk());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
        CompanyContainer testCompanyContainer = companyContainerList.get(companyContainerList.size() - 1);
        assertThat(testCompanyContainer.getRfidTag()).isEqualTo(UPDATED_RFID_TAG);
        assertThat(testCompanyContainer.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyContainer() throws Exception {
        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();
        companyContainer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyContainer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyContainer() throws Exception {
        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();
        companyContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyContainerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyContainer() throws Exception {
        int databaseSizeBeforeUpdate = companyContainerRepository.findAll().size();
        companyContainer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyContainerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyContainer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyContainer in the database
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyContainer() throws Exception {
        // Initialize the database
        companyContainerRepository.saveAndFlush(companyContainer);

        int databaseSizeBeforeDelete = companyContainerRepository.findAll().size();

        // Delete the companyContainer
        restCompanyContainerMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyContainer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyContainer> companyContainerList = companyContainerRepository.findAll();
        assertThat(companyContainerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
