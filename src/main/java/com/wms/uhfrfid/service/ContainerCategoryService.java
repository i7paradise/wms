package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.ContainerCategory;
import com.wms.uhfrfid.repository.ContainerCategoryRepository;
import com.wms.uhfrfid.service.dto.ContainerCategoryDTO;
import com.wms.uhfrfid.service.mapper.ContainerCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContainerCategory}.
 */
@Service
@Transactional
public class ContainerCategoryService {

    private final Logger log = LoggerFactory.getLogger(ContainerCategoryService.class);

    private final ContainerCategoryRepository containerCategoryRepository;

    private final ContainerCategoryMapper containerCategoryMapper;

    public ContainerCategoryService(
        ContainerCategoryRepository containerCategoryRepository,
        ContainerCategoryMapper containerCategoryMapper
    ) {
        this.containerCategoryRepository = containerCategoryRepository;
        this.containerCategoryMapper = containerCategoryMapper;
    }

    /**
     * Save a containerCategory.
     *
     * @param containerCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ContainerCategoryDTO save(ContainerCategoryDTO containerCategoryDTO) {
        log.debug("Request to save ContainerCategory : {}", containerCategoryDTO);
        ContainerCategory containerCategory = containerCategoryMapper.toEntity(containerCategoryDTO);
        containerCategory = containerCategoryRepository.save(containerCategory);
        return containerCategoryMapper.toDto(containerCategory);
    }

    /**
     * Partially update a containerCategory.
     *
     * @param containerCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContainerCategoryDTO> partialUpdate(ContainerCategoryDTO containerCategoryDTO) {
        log.debug("Request to partially update ContainerCategory : {}", containerCategoryDTO);

        return containerCategoryRepository
            .findById(containerCategoryDTO.getId())
            .map(existingContainerCategory -> {
                containerCategoryMapper.partialUpdate(existingContainerCategory, containerCategoryDTO);

                return existingContainerCategory;
            })
            .map(containerCategoryRepository::save)
            .map(containerCategoryMapper::toDto);
    }

    /**
     * Get all the containerCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContainerCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContainerCategories");
        return containerCategoryRepository.findAll(pageable).map(containerCategoryMapper::toDto);
    }

    /**
     * Get one containerCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContainerCategoryDTO> findOne(Long id) {
        log.debug("Request to get ContainerCategory : {}", id);
        return containerCategoryRepository.findById(id).map(containerCategoryMapper::toDto);
    }

    /**
     * Delete the containerCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContainerCategory : {}", id);
        containerCategoryRepository.deleteById(id);
    }
}
