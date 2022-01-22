package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.UHFRFIDReader;
import com.wms.uhfrfid.domain.enumeration.UHFRFIDReaderStatus;
import com.wms.uhfrfid.repository.UHFRFIDReaderRepository;
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
 * Integration tests for the {@link UHFRFIDReaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UHFRFIDReaderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;

    private static final UHFRFIDReaderStatus DEFAULT_STATUS = UHFRFIDReaderStatus.DISCONNECTED;
    private static final UHFRFIDReaderStatus UPDATED_STATUS = UHFRFIDReaderStatus.CONNECTED;

    private static final String ENTITY_API_URL = "/api/uhfrfid-readers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UHFRFIDReaderRepository uHFRFIDReaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUHFRFIDReaderMockMvc;

    private UHFRFIDReader uHFRFIDReader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UHFRFIDReader createEntity(EntityManager em) {
        UHFRFIDReader uHFRFIDReader = new UHFRFIDReader().name(DEFAULT_NAME).ip(DEFAULT_IP).port(DEFAULT_PORT).status(DEFAULT_STATUS);
        return uHFRFIDReader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UHFRFIDReader createUpdatedEntity(EntityManager em) {
        UHFRFIDReader uHFRFIDReader = new UHFRFIDReader().name(UPDATED_NAME).ip(UPDATED_IP).port(UPDATED_PORT).status(UPDATED_STATUS);
        return uHFRFIDReader;
    }

    @BeforeEach
    public void initTest() {
        uHFRFIDReader = createEntity(em);
    }

    @Test
    @Transactional
    void createUHFRFIDReader() throws Exception {
        int databaseSizeBeforeCreate = uHFRFIDReaderRepository.findAll().size();
        // Create the UHFRFIDReader
        restUHFRFIDReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader)))
            .andExpect(status().isCreated());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeCreate + 1);
        UHFRFIDReader testUHFRFIDReader = uHFRFIDReaderList.get(uHFRFIDReaderList.size() - 1);
        assertThat(testUHFRFIDReader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUHFRFIDReader.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testUHFRFIDReader.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testUHFRFIDReader.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createUHFRFIDReaderWithExistingId() throws Exception {
        // Create the UHFRFIDReader with an existing ID
        uHFRFIDReader.setId(1L);

        int databaseSizeBeforeCreate = uHFRFIDReaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUHFRFIDReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader)))
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = uHFRFIDReaderRepository.findAll().size();
        // set the field null
        uHFRFIDReader.setName(null);

        // Create the UHFRFIDReader, which fails.

        restUHFRFIDReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader)))
            .andExpect(status().isBadRequest());

        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = uHFRFIDReaderRepository.findAll().size();
        // set the field null
        uHFRFIDReader.setIp(null);

        // Create the UHFRFIDReader, which fails.

        restUHFRFIDReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader)))
            .andExpect(status().isBadRequest());

        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = uHFRFIDReaderRepository.findAll().size();
        // set the field null
        uHFRFIDReader.setPort(null);

        // Create the UHFRFIDReader, which fails.

        restUHFRFIDReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader)))
            .andExpect(status().isBadRequest());

        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = uHFRFIDReaderRepository.findAll().size();
        // set the field null
        uHFRFIDReader.setStatus(null);

        // Create the UHFRFIDReader, which fails.

        restUHFRFIDReaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader)))
            .andExpect(status().isBadRequest());

        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUHFRFIDReaders() throws Exception {
        // Initialize the database
        uHFRFIDReaderRepository.saveAndFlush(uHFRFIDReader);

        // Get all the uHFRFIDReaderList
        restUHFRFIDReaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uHFRFIDReader.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getUHFRFIDReader() throws Exception {
        // Initialize the database
        uHFRFIDReaderRepository.saveAndFlush(uHFRFIDReader);

        // Get the uHFRFIDReader
        restUHFRFIDReaderMockMvc
            .perform(get(ENTITY_API_URL_ID, uHFRFIDReader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uHFRFIDReader.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUHFRFIDReader() throws Exception {
        // Get the uHFRFIDReader
        restUHFRFIDReaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUHFRFIDReader() throws Exception {
        // Initialize the database
        uHFRFIDReaderRepository.saveAndFlush(uHFRFIDReader);

        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();

        // Update the uHFRFIDReader
        UHFRFIDReader updatedUHFRFIDReader = uHFRFIDReaderRepository.findById(uHFRFIDReader.getId()).get();
        // Disconnect from session so that the updates on updatedUHFRFIDReader are not directly saved in db
        em.detach(updatedUHFRFIDReader);
        updatedUHFRFIDReader.name(UPDATED_NAME).ip(UPDATED_IP).port(UPDATED_PORT).status(UPDATED_STATUS);

        restUHFRFIDReaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUHFRFIDReader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUHFRFIDReader))
            )
            .andExpect(status().isOk());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
        UHFRFIDReader testUHFRFIDReader = uHFRFIDReaderList.get(uHFRFIDReaderList.size() - 1);
        assertThat(testUHFRFIDReader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUHFRFIDReader.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testUHFRFIDReader.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testUHFRFIDReader.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUHFRFIDReader() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();
        uHFRFIDReader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUHFRFIDReaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uHFRFIDReader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUHFRFIDReader() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();
        uHFRFIDReader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDReaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUHFRFIDReader() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();
        uHFRFIDReader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDReaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUHFRFIDReaderWithPatch() throws Exception {
        // Initialize the database
        uHFRFIDReaderRepository.saveAndFlush(uHFRFIDReader);

        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();

        // Update the uHFRFIDReader using partial update
        UHFRFIDReader partialUpdatedUHFRFIDReader = new UHFRFIDReader();
        partialUpdatedUHFRFIDReader.setId(uHFRFIDReader.getId());

        partialUpdatedUHFRFIDReader.name(UPDATED_NAME);

        restUHFRFIDReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUHFRFIDReader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUHFRFIDReader))
            )
            .andExpect(status().isOk());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
        UHFRFIDReader testUHFRFIDReader = uHFRFIDReaderList.get(uHFRFIDReaderList.size() - 1);
        assertThat(testUHFRFIDReader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUHFRFIDReader.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testUHFRFIDReader.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testUHFRFIDReader.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUHFRFIDReaderWithPatch() throws Exception {
        // Initialize the database
        uHFRFIDReaderRepository.saveAndFlush(uHFRFIDReader);

        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();

        // Update the uHFRFIDReader using partial update
        UHFRFIDReader partialUpdatedUHFRFIDReader = new UHFRFIDReader();
        partialUpdatedUHFRFIDReader.setId(uHFRFIDReader.getId());

        partialUpdatedUHFRFIDReader.name(UPDATED_NAME).ip(UPDATED_IP).port(UPDATED_PORT).status(UPDATED_STATUS);

        restUHFRFIDReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUHFRFIDReader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUHFRFIDReader))
            )
            .andExpect(status().isOk());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
        UHFRFIDReader testUHFRFIDReader = uHFRFIDReaderList.get(uHFRFIDReaderList.size() - 1);
        assertThat(testUHFRFIDReader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUHFRFIDReader.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testUHFRFIDReader.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testUHFRFIDReader.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUHFRFIDReader() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();
        uHFRFIDReader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUHFRFIDReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uHFRFIDReader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUHFRFIDReader() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();
        uHFRFIDReader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDReaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader))
            )
            .andExpect(status().isBadRequest());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUHFRFIDReader() throws Exception {
        int databaseSizeBeforeUpdate = uHFRFIDReaderRepository.findAll().size();
        uHFRFIDReader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUHFRFIDReaderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(uHFRFIDReader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UHFRFIDReader in the database
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUHFRFIDReader() throws Exception {
        // Initialize the database
        uHFRFIDReaderRepository.saveAndFlush(uHFRFIDReader);

        int databaseSizeBeforeDelete = uHFRFIDReaderRepository.findAll().size();

        // Delete the uHFRFIDReader
        restUHFRFIDReaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, uHFRFIDReader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UHFRFIDReader> uHFRFIDReaderList = uHFRFIDReaderRepository.findAll();
        assertThat(uHFRFIDReaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
