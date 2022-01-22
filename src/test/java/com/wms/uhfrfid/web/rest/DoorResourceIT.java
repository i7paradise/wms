package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.Door;
import com.wms.uhfrfid.repository.DoorRepository;
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
 * Integration tests for the {@link DoorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DoorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DoorRepository doorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoorMockMvc;

    private Door door;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Door createEntity(EntityManager em) {
        Door door = new Door().name(DEFAULT_NAME);
        return door;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Door createUpdatedEntity(EntityManager em) {
        Door door = new Door().name(UPDATED_NAME);
        return door;
    }

    @BeforeEach
    public void initTest() {
        door = createEntity(em);
    }

    @Test
    @Transactional
    void createDoor() throws Exception {
        int databaseSizeBeforeCreate = doorRepository.findAll().size();
        // Create the Door
        restDoorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(door)))
            .andExpect(status().isCreated());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeCreate + 1);
        Door testDoor = doorList.get(doorList.size() - 1);
        assertThat(testDoor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDoorWithExistingId() throws Exception {
        // Create the Door with an existing ID
        door.setId(1L);

        int databaseSizeBeforeCreate = doorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(door)))
            .andExpect(status().isBadRequest());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doorRepository.findAll().size();
        // set the field null
        door.setName(null);

        // Create the Door, which fails.

        restDoorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(door)))
            .andExpect(status().isBadRequest());

        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoors() throws Exception {
        // Initialize the database
        doorRepository.saveAndFlush(door);

        // Get all the doorList
        restDoorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(door.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDoor() throws Exception {
        // Initialize the database
        doorRepository.saveAndFlush(door);

        // Get the door
        restDoorMockMvc
            .perform(get(ENTITY_API_URL_ID, door.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(door.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDoor() throws Exception {
        // Get the door
        restDoorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDoor() throws Exception {
        // Initialize the database
        doorRepository.saveAndFlush(door);

        int databaseSizeBeforeUpdate = doorRepository.findAll().size();

        // Update the door
        Door updatedDoor = doorRepository.findById(door.getId()).get();
        // Disconnect from session so that the updates on updatedDoor are not directly saved in db
        em.detach(updatedDoor);
        updatedDoor.name(UPDATED_NAME);

        restDoorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDoor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDoor))
            )
            .andExpect(status().isOk());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
        Door testDoor = doorList.get(doorList.size() - 1);
        assertThat(testDoor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDoor() throws Exception {
        int databaseSizeBeforeUpdate = doorRepository.findAll().size();
        door.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, door.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(door))
            )
            .andExpect(status().isBadRequest());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoor() throws Exception {
        int databaseSizeBeforeUpdate = doorRepository.findAll().size();
        door.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(door))
            )
            .andExpect(status().isBadRequest());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoor() throws Exception {
        int databaseSizeBeforeUpdate = doorRepository.findAll().size();
        door.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(door)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoorWithPatch() throws Exception {
        // Initialize the database
        doorRepository.saveAndFlush(door);

        int databaseSizeBeforeUpdate = doorRepository.findAll().size();

        // Update the door using partial update
        Door partialUpdatedDoor = new Door();
        partialUpdatedDoor.setId(door.getId());

        partialUpdatedDoor.name(UPDATED_NAME);

        restDoorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoor))
            )
            .andExpect(status().isOk());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
        Door testDoor = doorList.get(doorList.size() - 1);
        assertThat(testDoor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDoorWithPatch() throws Exception {
        // Initialize the database
        doorRepository.saveAndFlush(door);

        int databaseSizeBeforeUpdate = doorRepository.findAll().size();

        // Update the door using partial update
        Door partialUpdatedDoor = new Door();
        partialUpdatedDoor.setId(door.getId());

        partialUpdatedDoor.name(UPDATED_NAME);

        restDoorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoor))
            )
            .andExpect(status().isOk());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
        Door testDoor = doorList.get(doorList.size() - 1);
        assertThat(testDoor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDoor() throws Exception {
        int databaseSizeBeforeUpdate = doorRepository.findAll().size();
        door.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, door.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(door))
            )
            .andExpect(status().isBadRequest());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoor() throws Exception {
        int databaseSizeBeforeUpdate = doorRepository.findAll().size();
        door.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(door))
            )
            .andExpect(status().isBadRequest());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoor() throws Exception {
        int databaseSizeBeforeUpdate = doorRepository.findAll().size();
        door.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(door)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Door in the database
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoor() throws Exception {
        // Initialize the database
        doorRepository.saveAndFlush(door);

        int databaseSizeBeforeDelete = doorRepository.findAll().size();

        // Delete the door
        restDoorMockMvc
            .perform(delete(ENTITY_API_URL_ID, door.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Door> doorList = doorRepository.findAll();
        assertThat(doorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
