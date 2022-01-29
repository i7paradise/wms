package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.WHRow;
import com.wms.uhfrfid.repository.WHRowRepository;
import com.wms.uhfrfid.service.dto.WHRowDTO;
import com.wms.uhfrfid.service.mapper.WHRowMapper;
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
 * Integration tests for the {@link WHRowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WHRowResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wh-rows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WHRowRepository wHRowRepository;

    @Autowired
    private WHRowMapper wHRowMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWHRowMockMvc;

    private WHRow wHRow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WHRow createEntity(EntityManager em) {
        WHRow wHRow = new WHRow().name(DEFAULT_NAME).note(DEFAULT_NOTE);
        return wHRow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WHRow createUpdatedEntity(EntityManager em) {
        WHRow wHRow = new WHRow().name(UPDATED_NAME).note(UPDATED_NOTE);
        return wHRow;
    }

    @BeforeEach
    public void initTest() {
        wHRow = createEntity(em);
    }

    @Test
    @Transactional
    void createWHRow() throws Exception {
        int databaseSizeBeforeCreate = wHRowRepository.findAll().size();
        // Create the WHRow
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);
        restWHRowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHRowDTO)))
            .andExpect(status().isCreated());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeCreate + 1);
        WHRow testWHRow = wHRowList.get(wHRowList.size() - 1);
        assertThat(testWHRow.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWHRow.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createWHRowWithExistingId() throws Exception {
        // Create the WHRow with an existing ID
        wHRow.setId(1L);
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        int databaseSizeBeforeCreate = wHRowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWHRowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHRowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wHRowRepository.findAll().size();
        // set the field null
        wHRow.setName(null);

        // Create the WHRow, which fails.
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        restWHRowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHRowDTO)))
            .andExpect(status().isBadRequest());

        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWHRows() throws Exception {
        // Initialize the database
        wHRowRepository.saveAndFlush(wHRow);

        // Get all the wHRowList
        restWHRowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wHRow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getWHRow() throws Exception {
        // Initialize the database
        wHRowRepository.saveAndFlush(wHRow);

        // Get the wHRow
        restWHRowMockMvc
            .perform(get(ENTITY_API_URL_ID, wHRow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wHRow.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingWHRow() throws Exception {
        // Get the wHRow
        restWHRowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWHRow() throws Exception {
        // Initialize the database
        wHRowRepository.saveAndFlush(wHRow);

        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();

        // Update the wHRow
        WHRow updatedWHRow = wHRowRepository.findById(wHRow.getId()).get();
        // Disconnect from session so that the updates on updatedWHRow are not directly saved in db
        em.detach(updatedWHRow);
        updatedWHRow.name(UPDATED_NAME).note(UPDATED_NOTE);
        WHRowDTO wHRowDTO = wHRowMapper.toDto(updatedWHRow);

        restWHRowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wHRowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHRowDTO))
            )
            .andExpect(status().isOk());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
        WHRow testWHRow = wHRowList.get(wHRowList.size() - 1);
        assertThat(testWHRow.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWHRow.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingWHRow() throws Exception {
        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();
        wHRow.setId(count.incrementAndGet());

        // Create the WHRow
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWHRowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wHRowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHRowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWHRow() throws Exception {
        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();
        wHRow.setId(count.incrementAndGet());

        // Create the WHRow
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHRowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHRowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWHRow() throws Exception {
        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();
        wHRow.setId(count.incrementAndGet());

        // Create the WHRow
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHRowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHRowDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWHRowWithPatch() throws Exception {
        // Initialize the database
        wHRowRepository.saveAndFlush(wHRow);

        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();

        // Update the wHRow using partial update
        WHRow partialUpdatedWHRow = new WHRow();
        partialUpdatedWHRow.setId(wHRow.getId());

        partialUpdatedWHRow.name(UPDATED_NAME).note(UPDATED_NOTE);

        restWHRowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWHRow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWHRow))
            )
            .andExpect(status().isOk());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
        WHRow testWHRow = wHRowList.get(wHRowList.size() - 1);
        assertThat(testWHRow.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWHRow.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateWHRowWithPatch() throws Exception {
        // Initialize the database
        wHRowRepository.saveAndFlush(wHRow);

        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();

        // Update the wHRow using partial update
        WHRow partialUpdatedWHRow = new WHRow();
        partialUpdatedWHRow.setId(wHRow.getId());

        partialUpdatedWHRow.name(UPDATED_NAME).note(UPDATED_NOTE);

        restWHRowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWHRow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWHRow))
            )
            .andExpect(status().isOk());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
        WHRow testWHRow = wHRowList.get(wHRowList.size() - 1);
        assertThat(testWHRow.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWHRow.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingWHRow() throws Exception {
        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();
        wHRow.setId(count.incrementAndGet());

        // Create the WHRow
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWHRowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wHRowDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wHRowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWHRow() throws Exception {
        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();
        wHRow.setId(count.incrementAndGet());

        // Create the WHRow
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHRowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wHRowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWHRow() throws Exception {
        int databaseSizeBeforeUpdate = wHRowRepository.findAll().size();
        wHRow.setId(count.incrementAndGet());

        // Create the WHRow
        WHRowDTO wHRowDTO = wHRowMapper.toDto(wHRow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHRowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wHRowDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WHRow in the database
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWHRow() throws Exception {
        // Initialize the database
        wHRowRepository.saveAndFlush(wHRow);

        int databaseSizeBeforeDelete = wHRowRepository.findAll().size();

        // Delete the wHRow
        restWHRowMockMvc
            .perform(delete(ENTITY_API_URL_ID, wHRow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WHRow> wHRowList = wHRowRepository.findAll();
        assertThat(wHRowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
