package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.WHLevel;
import com.wms.uhfrfid.repository.WHLevelRepository;
import com.wms.uhfrfid.service.dto.WHLevelDTO;
import com.wms.uhfrfid.service.mapper.WHLevelMapper;
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
 * Integration tests for the {@link WHLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WHLevelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wh-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WHLevelRepository wHLevelRepository;

    @Autowired
    private WHLevelMapper wHLevelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWHLevelMockMvc;

    private WHLevel wHLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WHLevel createEntity(EntityManager em) {
        WHLevel wHLevel = new WHLevel().name(DEFAULT_NAME).note(DEFAULT_NOTE);
        return wHLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WHLevel createUpdatedEntity(EntityManager em) {
        WHLevel wHLevel = new WHLevel().name(UPDATED_NAME).note(UPDATED_NOTE);
        return wHLevel;
    }

    @BeforeEach
    public void initTest() {
        wHLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createWHLevel() throws Exception {
        int databaseSizeBeforeCreate = wHLevelRepository.findAll().size();
        // Create the WHLevel
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);
        restWHLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHLevelDTO)))
            .andExpect(status().isCreated());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeCreate + 1);
        WHLevel testWHLevel = wHLevelList.get(wHLevelList.size() - 1);
        assertThat(testWHLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWHLevel.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createWHLevelWithExistingId() throws Exception {
        // Create the WHLevel with an existing ID
        wHLevel.setId(1L);
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        int databaseSizeBeforeCreate = wHLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWHLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHLevelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wHLevelRepository.findAll().size();
        // set the field null
        wHLevel.setName(null);

        // Create the WHLevel, which fails.
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        restWHLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHLevelDTO)))
            .andExpect(status().isBadRequest());

        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWHLevels() throws Exception {
        // Initialize the database
        wHLevelRepository.saveAndFlush(wHLevel);

        // Get all the wHLevelList
        restWHLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wHLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getWHLevel() throws Exception {
        // Initialize the database
        wHLevelRepository.saveAndFlush(wHLevel);

        // Get the wHLevel
        restWHLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, wHLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wHLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingWHLevel() throws Exception {
        // Get the wHLevel
        restWHLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWHLevel() throws Exception {
        // Initialize the database
        wHLevelRepository.saveAndFlush(wHLevel);

        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();

        // Update the wHLevel
        WHLevel updatedWHLevel = wHLevelRepository.findById(wHLevel.getId()).get();
        // Disconnect from session so that the updates on updatedWHLevel are not directly saved in db
        em.detach(updatedWHLevel);
        updatedWHLevel.name(UPDATED_NAME).note(UPDATED_NOTE);
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(updatedWHLevel);

        restWHLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wHLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHLevelDTO))
            )
            .andExpect(status().isOk());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
        WHLevel testWHLevel = wHLevelList.get(wHLevelList.size() - 1);
        assertThat(testWHLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWHLevel.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingWHLevel() throws Exception {
        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();
        wHLevel.setId(count.incrementAndGet());

        // Create the WHLevel
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWHLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wHLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWHLevel() throws Exception {
        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();
        wHLevel.setId(count.incrementAndGet());

        // Create the WHLevel
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wHLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWHLevel() throws Exception {
        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();
        wHLevel.setId(count.incrementAndGet());

        // Create the WHLevel
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHLevelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wHLevelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWHLevelWithPatch() throws Exception {
        // Initialize the database
        wHLevelRepository.saveAndFlush(wHLevel);

        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();

        // Update the wHLevel using partial update
        WHLevel partialUpdatedWHLevel = new WHLevel();
        partialUpdatedWHLevel.setId(wHLevel.getId());

        restWHLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWHLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWHLevel))
            )
            .andExpect(status().isOk());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
        WHLevel testWHLevel = wHLevelList.get(wHLevelList.size() - 1);
        assertThat(testWHLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWHLevel.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateWHLevelWithPatch() throws Exception {
        // Initialize the database
        wHLevelRepository.saveAndFlush(wHLevel);

        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();

        // Update the wHLevel using partial update
        WHLevel partialUpdatedWHLevel = new WHLevel();
        partialUpdatedWHLevel.setId(wHLevel.getId());

        partialUpdatedWHLevel.name(UPDATED_NAME).note(UPDATED_NOTE);

        restWHLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWHLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWHLevel))
            )
            .andExpect(status().isOk());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
        WHLevel testWHLevel = wHLevelList.get(wHLevelList.size() - 1);
        assertThat(testWHLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWHLevel.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingWHLevel() throws Exception {
        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();
        wHLevel.setId(count.incrementAndGet());

        // Create the WHLevel
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWHLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wHLevelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wHLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWHLevel() throws Exception {
        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();
        wHLevel.setId(count.incrementAndGet());

        // Create the WHLevel
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wHLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWHLevel() throws Exception {
        int databaseSizeBeforeUpdate = wHLevelRepository.findAll().size();
        wHLevel.setId(count.incrementAndGet());

        // Create the WHLevel
        WHLevelDTO wHLevelDTO = wHLevelMapper.toDto(wHLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWHLevelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wHLevelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WHLevel in the database
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWHLevel() throws Exception {
        // Initialize the database
        wHLevelRepository.saveAndFlush(wHLevel);

        int databaseSizeBeforeDelete = wHLevelRepository.findAll().size();

        // Delete the wHLevel
        restWHLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, wHLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WHLevel> wHLevelList = wHLevelRepository.findAll();
        assertThat(wHLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
