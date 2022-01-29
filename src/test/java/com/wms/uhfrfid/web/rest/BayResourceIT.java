package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.Bay;
import com.wms.uhfrfid.repository.BayRepository;
import com.wms.uhfrfid.service.dto.BayDTO;
import com.wms.uhfrfid.service.mapper.BayMapper;
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
 * Integration tests for the {@link BayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BayRepository bayRepository;

    @Autowired
    private BayMapper bayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBayMockMvc;

    private Bay bay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bay createEntity(EntityManager em) {
        Bay bay = new Bay().name(DEFAULT_NAME).note(DEFAULT_NOTE);
        return bay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bay createUpdatedEntity(EntityManager em) {
        Bay bay = new Bay().name(UPDATED_NAME).note(UPDATED_NOTE);
        return bay;
    }

    @BeforeEach
    public void initTest() {
        bay = createEntity(em);
    }

    @Test
    @Transactional
    void createBay() throws Exception {
        int databaseSizeBeforeCreate = bayRepository.findAll().size();
        // Create the Bay
        BayDTO bayDTO = bayMapper.toDto(bay);
        restBayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bayDTO)))
            .andExpect(status().isCreated());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeCreate + 1);
        Bay testBay = bayList.get(bayList.size() - 1);
        assertThat(testBay.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBay.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createBayWithExistingId() throws Exception {
        // Create the Bay with an existing ID
        bay.setId(1L);
        BayDTO bayDTO = bayMapper.toDto(bay);

        int databaseSizeBeforeCreate = bayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bayRepository.findAll().size();
        // set the field null
        bay.setName(null);

        // Create the Bay, which fails.
        BayDTO bayDTO = bayMapper.toDto(bay);

        restBayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bayDTO)))
            .andExpect(status().isBadRequest());

        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBays() throws Exception {
        // Initialize the database
        bayRepository.saveAndFlush(bay);

        // Get all the bayList
        restBayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getBay() throws Exception {
        // Initialize the database
        bayRepository.saveAndFlush(bay);

        // Get the bay
        restBayMockMvc
            .perform(get(ENTITY_API_URL_ID, bay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bay.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingBay() throws Exception {
        // Get the bay
        restBayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBay() throws Exception {
        // Initialize the database
        bayRepository.saveAndFlush(bay);

        int databaseSizeBeforeUpdate = bayRepository.findAll().size();

        // Update the bay
        Bay updatedBay = bayRepository.findById(bay.getId()).get();
        // Disconnect from session so that the updates on updatedBay are not directly saved in db
        em.detach(updatedBay);
        updatedBay.name(UPDATED_NAME).note(UPDATED_NOTE);
        BayDTO bayDTO = bayMapper.toDto(updatedBay);

        restBayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bayDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
        Bay testBay = bayList.get(bayList.size() - 1);
        assertThat(testBay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBay.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingBay() throws Exception {
        int databaseSizeBeforeUpdate = bayRepository.findAll().size();
        bay.setId(count.incrementAndGet());

        // Create the Bay
        BayDTO bayDTO = bayMapper.toDto(bay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBay() throws Exception {
        int databaseSizeBeforeUpdate = bayRepository.findAll().size();
        bay.setId(count.incrementAndGet());

        // Create the Bay
        BayDTO bayDTO = bayMapper.toDto(bay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBay() throws Exception {
        int databaseSizeBeforeUpdate = bayRepository.findAll().size();
        bay.setId(count.incrementAndGet());

        // Create the Bay
        BayDTO bayDTO = bayMapper.toDto(bay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBayWithPatch() throws Exception {
        // Initialize the database
        bayRepository.saveAndFlush(bay);

        int databaseSizeBeforeUpdate = bayRepository.findAll().size();

        // Update the bay using partial update
        Bay partialUpdatedBay = new Bay();
        partialUpdatedBay.setId(bay.getId());

        partialUpdatedBay.name(UPDATED_NAME).note(UPDATED_NOTE);

        restBayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBay))
            )
            .andExpect(status().isOk());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
        Bay testBay = bayList.get(bayList.size() - 1);
        assertThat(testBay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBay.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateBayWithPatch() throws Exception {
        // Initialize the database
        bayRepository.saveAndFlush(bay);

        int databaseSizeBeforeUpdate = bayRepository.findAll().size();

        // Update the bay using partial update
        Bay partialUpdatedBay = new Bay();
        partialUpdatedBay.setId(bay.getId());

        partialUpdatedBay.name(UPDATED_NAME).note(UPDATED_NOTE);

        restBayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBay))
            )
            .andExpect(status().isOk());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
        Bay testBay = bayList.get(bayList.size() - 1);
        assertThat(testBay.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBay.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingBay() throws Exception {
        int databaseSizeBeforeUpdate = bayRepository.findAll().size();
        bay.setId(count.incrementAndGet());

        // Create the Bay
        BayDTO bayDTO = bayMapper.toDto(bay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBay() throws Exception {
        int databaseSizeBeforeUpdate = bayRepository.findAll().size();
        bay.setId(count.incrementAndGet());

        // Create the Bay
        BayDTO bayDTO = bayMapper.toDto(bay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBay() throws Exception {
        int databaseSizeBeforeUpdate = bayRepository.findAll().size();
        bay.setId(count.incrementAndGet());

        // Create the Bay
        BayDTO bayDTO = bayMapper.toDto(bay);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBayMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bay in the database
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBay() throws Exception {
        // Initialize the database
        bayRepository.saveAndFlush(bay);

        int databaseSizeBeforeDelete = bayRepository.findAll().size();

        // Delete the bay
        restBayMockMvc.perform(delete(ENTITY_API_URL_ID, bay.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bay> bayList = bayRepository.findAll();
        assertThat(bayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
