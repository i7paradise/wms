package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import com.wms.uhfrfid.domain.enumeration.UHFRFIDAntennaStatus;
import com.wms.uhfrfid.repository.UHFRFIDAntennaRepository;
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
 * Integration tests for the {@link UHFRFIDAntennaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UHFRFIDAntennaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_OUTPUT_POWER = 1;
    private static final Integer UPDATED_OUTPUT_POWER = 2;

    private static final UHFRFIDAntennaStatus DEFAULT_STATUS = UHFRFIDAntennaStatus.AVAILABLE;
    private static final UHFRFIDAntennaStatus UPDATED_STATUS = UHFRFIDAntennaStatus.IN_USE;

    private static final String ENTITY_API_URL = "/api/uhfrfid-antennas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UHFRFIDAntennaRepository uHFRFIDAntennaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUHFRFIDAntennaMockMvc;

    private UHFRFIDAntenna uHFRFIDAntenna;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UHFRFIDAntenna createEntity(EntityManager em) {
        UHFRFIDAntenna uHFRFIDAntenna = new UHFRFIDAntenna().name(DEFAULT_NAME).outputPower(DEFAULT_OUTPUT_POWER).status(DEFAULT_STATUS);
        return uHFRFIDAntenna;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UHFRFIDAntenna createUpdatedEntity(EntityManager em) {
        UHFRFIDAntenna uHFRFIDAntenna = new UHFRFIDAntenna().name(UPDATED_NAME).outputPower(UPDATED_OUTPUT_POWER).status(UPDATED_STATUS);
        return uHFRFIDAntenna;
    }

    @BeforeEach
    public void initTest() {
        uHFRFIDAntenna = createEntity(em);
    }

    @Test
    @Transactional
    void createUHFRFIDAntenna() throws Exception {
        int databaseSizeBeforeCreate = uHFRFIDAntennaRepository.findAll().size();
        // Create the UHFRFIDAntenna
        restUHFRFIDAntennaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isCreated());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeCreate + 1);
        UHFRFIDAntenna testUHFRFIDAntenna = uHFRFIDAntennaList.get(uHFRFIDAntennaList.size() - 1);
        assertThat(testUHFRFIDAntenna.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUHFRFIDAntenna.getOutputPower()).isEqualTo(DEFAULT_OUTPUT_POWER);
        assertThat(testUHFRFIDAntenna.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createUHFRFIDAntennaWithExistingId() throws Exception {
        // Create the UHFRFIDAntenna with an existing ID
        uHFRFIDAntenna.setId(1L);

        int databaseSizeBeforeCreate = uHFRFIDAntennaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUHFRFIDAntennaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = uHFRFIDAntennaRepository.findAll().size();
        // set the field null
        uHFRFIDAntenna.setName(null);

        // Create the UHFRFIDAntenna, which fails.

        restUHFRFIDAntennaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutputPowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = uHFRFIDAntennaRepository.findAll().size();
        // set the field null
        uHFRFIDAntenna.setOutputPower(null);

        // Create the UHFRFIDAntenna, which fails.

        restUHFRFIDAntennaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = uHFRFIDAntennaRepository.findAll().size();
        // set the field null
        uHFRFIDAntenna.setStatus(null);

        // Create the UHFRFIDAntenna, which fails.

        restUHFRFIDAntennaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUHFRFIDAntennas() throws Exception {
        // Initialize the database
        uHFRFIDAntennaRepository.saveAndFlush(uHFRFIDAntenna);

        // Get all the uHFRFIDAntennaList
        restUHFRFIDAntennaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uHFRFIDAntenna.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].outputPower").value(hasItem(DEFAULT_OUTPUT_POWER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getUHFRFIDAntenna() throws Exception {
        // Initialize the database
        uHFRFIDAntennaRepository.saveAndFlush(uHFRFIDAntenna);

        // Get the uHFRFIDAntenna
        restUHFRFIDAntennaMockMvc
            .perform(get(ENTITY_API_URL_ID, uHFRFIDAntenna.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uHFRFIDAntenna.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.outputPower").value(DEFAULT_OUTPUT_POWER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUHFRFIDAntenna() throws Exception {
        // Get the uHFRFIDAntenna
        restUHFRFIDAntennaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUHFRFIDAntenna() throws Exception {
        // Initialize the database
        uHFRFIDAntennaRepository.saveAndFlush(uHFRFIDAntenna);

        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();

        // Update the uHFRFIDAntenna
        UHFRFIDAntenna updatedUHFRFIDAntenna = uHFRFIDAntennaRepository.findById(uHFRFIDAntenna.getId()).get();
        // Disconnect from session so that the updates on updatedUHFRFIDAntenna are not directly saved in db
        em.detach(updatedUHFRFIDAntenna);
        updatedUHFRFIDAntenna.name(UPDATED_NAME).outputPower(UPDATED_OUTPUT_POWER).status(UPDATED_STATUS);

        restUHFRFIDAntennaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUHFRFIDAntenna.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUHFRFIDAntenna))
            )
            .andExpect(status().isOk());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
        UHFRFIDAntenna testUHFRFIDAntenna = uHFRFIDAntennaList.get(uHFRFIDAntennaList.size() - 1);
        assertThat(testUHFRFIDAntenna.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUHFRFIDAntenna.getOutputPower()).isEqualTo(UPDATED_OUTPUT_POWER);
        assertThat(testUHFRFIDAntenna.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUHFRFIDAntenna() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();
        uHFRFIDAntenna.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUHFRFIDAntennaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uHFRFIDAntenna.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUHFRFIDAntenna() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();
        uHFRFIDAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDAntennaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUHFRFIDAntenna() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();
        uHFRFIDAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDAntennaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUHFRFIDAntennaWithPatch() throws Exception {
        // Initialize the database
        uHFRFIDAntennaRepository.saveAndFlush(uHFRFIDAntenna);

        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();

        // Update the uHFRFIDAntenna using partial update
        UHFRFIDAntenna partialUpdatedUHFRFIDAntenna = new UHFRFIDAntenna();
        partialUpdatedUHFRFIDAntenna.setId(uHFRFIDAntenna.getId());

        partialUpdatedUHFRFIDAntenna.name(UPDATED_NAME);

        restUHFRFIDAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUHFRFIDAntenna.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUHFRFIDAntenna))
            )
            .andExpect(status().isOk());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
        UHFRFIDAntenna testUHFRFIDAntenna = uHFRFIDAntennaList.get(uHFRFIDAntennaList.size() - 1);
        assertThat(testUHFRFIDAntenna.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUHFRFIDAntenna.getOutputPower()).isEqualTo(DEFAULT_OUTPUT_POWER);
        assertThat(testUHFRFIDAntenna.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUHFRFIDAntennaWithPatch() throws Exception {
        // Initialize the database
        uHFRFIDAntennaRepository.saveAndFlush(uHFRFIDAntenna);

        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();

        // Update the uHFRFIDAntenna using partial update
        UHFRFIDAntenna partialUpdatedUHFRFIDAntenna = new UHFRFIDAntenna();
        partialUpdatedUHFRFIDAntenna.setId(uHFRFIDAntenna.getId());

        partialUpdatedUHFRFIDAntenna.name(UPDATED_NAME).outputPower(UPDATED_OUTPUT_POWER).status(UPDATED_STATUS);

        restUHFRFIDAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUHFRFIDAntenna.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUHFRFIDAntenna))
            )
            .andExpect(status().isOk());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
        UHFRFIDAntenna testUHFRFIDAntenna = uHFRFIDAntennaList.get(uHFRFIDAntennaList.size() - 1);
        assertThat(testUHFRFIDAntenna.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUHFRFIDAntenna.getOutputPower()).isEqualTo(UPDATED_OUTPUT_POWER);
        assertThat(testUHFRFIDAntenna.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUHFRFIDAntenna() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();
        uHFRFIDAntenna.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUHFRFIDAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uHFRFIDAntenna.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUHFRFIDAntenna() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();
        uHFRFIDAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUHFRFIDAntenna() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDAntennaRepository.findAll().size();
        uHFRFIDAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(uHFRFIDAntenna))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UHFRFIDAntenna in the database
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUHFRFIDAntenna() throws Exception {
        // Initialize the database
        uHFRFIDAntennaRepository.saveAndFlush(uHFRFIDAntenna);

        int databaseSizeBeforeDelete = uHFRFIDAntennaRepository.findAll().size();

        // Delete the uHFRFIDAntenna
        restUHFRFIDAntennaMockMvc
            .perform(delete(ENTITY_API_URL_ID, uHFRFIDAntenna.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UHFRFIDAntenna> uHFRFIDAntennaList = uHFRFIDAntennaRepository.findAll();
        assertThat(uHFRFIDAntennaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
