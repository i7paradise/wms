package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.DoorAntenna;
import com.wms.uhfrfid.domain.enumeration.DoorAntennaType;
import com.wms.uhfrfid.repository.DoorAntennaRepository;
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
 * Integration tests for the {@link DoorAntennaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DoorAntennaResourceIT {

    private static final DoorAntennaType DEFAULT_TYPE = DoorAntennaType.INNER;
    private static final DoorAntennaType UPDATED_TYPE = DoorAntennaType.OUTER;

    private static final String ENTITY_API_URL = "/api/door-antennas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DoorAntennaRepository doorAntennaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoorAntennaMockMvc;

    private DoorAntenna doorAntenna;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoorAntenna createEntity(EntityManager em) {
        DoorAntenna doorAntenna = new DoorAntenna().type(DEFAULT_TYPE);
        return doorAntenna;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoorAntenna createUpdatedEntity(EntityManager em) {
        DoorAntenna doorAntenna = new DoorAntenna().type(UPDATED_TYPE);
        return doorAntenna;
    }

    @BeforeEach
    public void initTest() {
        doorAntenna = createEntity(em);
    }

    @Test
    @Transactional
    void createDoorAntenna() throws Exception {
        int databaseSizeBeforeCreate = doorAntennaRepository.findAll().size();
        // Create the DoorAntenna
        restDoorAntennaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doorAntenna)))
            .andExpect(status().isCreated());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeCreate + 1);
        DoorAntenna testDoorAntenna = doorAntennaList.get(doorAntennaList.size() - 1);
        assertThat(testDoorAntenna.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createDoorAntennaWithExistingId() throws Exception {
        // Create the DoorAntenna with an existing ID
        doorAntenna.setId(1L);

        int databaseSizeBeforeCreate = doorAntennaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoorAntennaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doorAntenna)))
            .andExpect(status().isBadRequest());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = doorAntennaRepository.findAll().size();
        // set the field null
        doorAntenna.setType(null);

        // Create the DoorAntenna, which fails.

        restDoorAntennaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doorAntenna)))
            .andExpect(status().isBadRequest());

        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoorAntennas() throws Exception {
        // Initialize the database
        doorAntennaRepository.saveAndFlush(doorAntenna);

        // Get all the doorAntennaList
        restDoorAntennaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doorAntenna.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getDoorAntenna() throws Exception {
        // Initialize the database
        doorAntennaRepository.saveAndFlush(doorAntenna);

        // Get the doorAntenna
        restDoorAntennaMockMvc
            .perform(get(ENTITY_API_URL_ID, doorAntenna.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doorAntenna.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDoorAntenna() throws Exception {
        // Get the doorAntenna
        restDoorAntennaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDoorAntenna() throws Exception {
        // Initialize the database
        doorAntennaRepository.saveAndFlush(doorAntenna);

        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();

        // Update the doorAntenna
        DoorAntenna updatedDoorAntenna = doorAntennaRepository.findById(doorAntenna.getId()).get();
        // Disconnect from session so that the updates on updatedDoorAntenna are not directly saved in db
        em.detach(updatedDoorAntenna);
        updatedDoorAntenna.type(UPDATED_TYPE);

        restDoorAntennaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDoorAntenna.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDoorAntenna))
            )
            .andExpect(status().isOk());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
        DoorAntenna testDoorAntenna = doorAntennaList.get(doorAntennaList.size() - 1);
        assertThat(testDoorAntenna.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDoorAntenna() throws Exception {
        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();
        doorAntenna.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoorAntennaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doorAntenna.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doorAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoorAntenna() throws Exception {
        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();
        doorAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorAntennaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doorAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoorAntenna() throws Exception {
        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();
        doorAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorAntennaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doorAntenna)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoorAntennaWithPatch() throws Exception {
        // Initialize the database
        doorAntennaRepository.saveAndFlush(doorAntenna);

        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();

        // Update the doorAntenna using partial update
        DoorAntenna partialUpdatedDoorAntenna = new DoorAntenna();
        partialUpdatedDoorAntenna.setId(doorAntenna.getId());

        partialUpdatedDoorAntenna.type(UPDATED_TYPE);

        restDoorAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoorAntenna.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoorAntenna))
            )
            .andExpect(status().isOk());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
        DoorAntenna testDoorAntenna = doorAntennaList.get(doorAntennaList.size() - 1);
        assertThat(testDoorAntenna.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDoorAntennaWithPatch() throws Exception {
        // Initialize the database
        doorAntennaRepository.saveAndFlush(doorAntenna);

        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();

        // Update the doorAntenna using partial update
        DoorAntenna partialUpdatedDoorAntenna = new DoorAntenna();
        partialUpdatedDoorAntenna.setId(doorAntenna.getId());

        partialUpdatedDoorAntenna.type(UPDATED_TYPE);

        restDoorAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoorAntenna.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoorAntenna))
            )
            .andExpect(status().isOk());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
        DoorAntenna testDoorAntenna = doorAntennaList.get(doorAntennaList.size() - 1);
        assertThat(testDoorAntenna.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDoorAntenna() throws Exception {
        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();
        doorAntenna.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoorAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doorAntenna.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doorAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoorAntenna() throws Exception {
        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();
        doorAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doorAntenna))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoorAntenna() throws Exception {
        int databaseSizeBeforeUpdate = doorAntennaRepository.findAll().size();
        doorAntenna.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorAntennaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(doorAntenna))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DoorAntenna in the database
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoorAntenna() throws Exception {
        // Initialize the database
        doorAntennaRepository.saveAndFlush(doorAntenna);

        int databaseSizeBeforeDelete = doorAntennaRepository.findAll().size();

        // Delete the doorAntenna
        restDoorAntennaMockMvc
            .perform(delete(ENTITY_API_URL_ID, doorAntenna.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DoorAntenna> doorAntennaList = doorAntennaRepository.findAll();
        assertThat(doorAntennaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
