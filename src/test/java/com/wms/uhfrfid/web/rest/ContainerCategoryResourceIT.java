package com.wms.uhfrfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wms.uhfrfid.IntegrationTest;
import com.wms.uhfrfid.domain.ContainerCategory;
import com.wms.uhfrfid.repository.ContainerCategoryRepository;
import com.wms.uhfrfid.service.dto.ContainerCategoryDTO;
import com.wms.uhfrfid.service.mapper.ContainerCategoryMapper;
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
 * Integration tests for the {@link ContainerCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContainerCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/container-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContainerCategoryRepository containerCategoryRepository;

    @Autowired
    private ContainerCategoryMapper containerCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContainerCategoryMockMvc;

    private ContainerCategory containerCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContainerCategory createEntity(EntityManager em) {
        ContainerCategory containerCategory = new ContainerCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return containerCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContainerCategory createUpdatedEntity(EntityManager em) {
        ContainerCategory containerCategory = new ContainerCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return containerCategory;
    }

    @BeforeEach
    public void initTest() {
        containerCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createContainerCategory() throws Exception {
        int databaseSizeBeforeCreate = containerCategoryRepository.findAll().size();
        // Create the ContainerCategory
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);
        restContainerCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ContainerCategory testContainerCategory = containerCategoryList.get(containerCategoryList.size() - 1);
        assertThat(testContainerCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContainerCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createContainerCategoryWithExistingId() throws Exception {
        // Create the ContainerCategory with an existing ID
        containerCategory.setId(1L);
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        int databaseSizeBeforeCreate = containerCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContainerCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = containerCategoryRepository.findAll().size();
        // set the field null
        containerCategory.setName(null);

        // Create the ContainerCategory, which fails.
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        restContainerCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContainerCategories() throws Exception {
        // Initialize the database
        containerCategoryRepository.saveAndFlush(containerCategory);

        // Get all the containerCategoryList
        restContainerCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(containerCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getContainerCategory() throws Exception {
        // Initialize the database
        containerCategoryRepository.saveAndFlush(containerCategory);

        // Get the containerCategory
        restContainerCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, containerCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(containerCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingContainerCategory() throws Exception {
        // Get the containerCategory
        restContainerCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContainerCategory() throws Exception {
        // Initialize the database
        containerCategoryRepository.saveAndFlush(containerCategory);

        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();

        // Update the containerCategory
        ContainerCategory updatedContainerCategory = containerCategoryRepository.findById(containerCategory.getId()).get();
        // Disconnect from session so that the updates on updatedContainerCategory are not directly saved in db
        em.detach(updatedContainerCategory);
        updatedContainerCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(updatedContainerCategory);

        restContainerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, containerCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
        ContainerCategory testContainerCategory = containerCategoryList.get(containerCategoryList.size() - 1);
        assertThat(testContainerCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContainerCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingContainerCategory() throws Exception {
        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();
        containerCategory.setId(count.incrementAndGet());

        // Create the ContainerCategory
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContainerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, containerCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContainerCategory() throws Exception {
        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();
        containerCategory.setId(count.incrementAndGet());

        // Create the ContainerCategory
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContainerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContainerCategory() throws Exception {
        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();
        containerCategory.setId(count.incrementAndGet());

        // Create the ContainerCategory
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContainerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContainerCategoryWithPatch() throws Exception {
        // Initialize the database
        containerCategoryRepository.saveAndFlush(containerCategory);

        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();

        // Update the containerCategory using partial update
        ContainerCategory partialUpdatedContainerCategory = new ContainerCategory();
        partialUpdatedContainerCategory.setId(containerCategory.getId());

        partialUpdatedContainerCategory.name(UPDATED_NAME);

        restContainerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContainerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContainerCategory))
            )
            .andExpect(status().isOk());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
        ContainerCategory testContainerCategory = containerCategoryList.get(containerCategoryList.size() - 1);
        assertThat(testContainerCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContainerCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateContainerCategoryWithPatch() throws Exception {
        // Initialize the database
        containerCategoryRepository.saveAndFlush(containerCategory);

        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();

        // Update the containerCategory using partial update
        ContainerCategory partialUpdatedContainerCategory = new ContainerCategory();
        partialUpdatedContainerCategory.setId(containerCategory.getId());

        partialUpdatedContainerCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContainerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContainerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContainerCategory))
            )
            .andExpect(status().isOk());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
        ContainerCategory testContainerCategory = containerCategoryList.get(containerCategoryList.size() - 1);
        assertThat(testContainerCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContainerCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingContainerCategory() throws Exception {
        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();
        containerCategory.setId(count.incrementAndGet());

        // Create the ContainerCategory
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContainerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, containerCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContainerCategory() throws Exception {
        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();
        containerCategory.setId(count.incrementAndGet());

        // Create the ContainerCategory
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContainerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContainerCategory() throws Exception {
        int databaseSizeBeforeUpdate = containerCategoryRepository.findAll().size();
        containerCategory.setId(count.incrementAndGet());

        // Create the ContainerCategory
        ContainerCategoryDTO containerCategoryDTO = containerCategoryMapper.toDto(containerCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContainerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(containerCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContainerCategory in the database
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContainerCategory() throws Exception {
        // Initialize the database
        containerCategoryRepository.saveAndFlush(containerCategory);

        int databaseSizeBeforeDelete = containerCategoryRepository.findAll().size();

        // Delete the containerCategory
        restContainerCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, containerCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContainerCategory> containerCategoryList = containerCategoryRepository.findAll();
        assertThat(containerCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
