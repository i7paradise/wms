package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Container;
import com.wms.uhfrfid.repository.ContainerRepository;
import com.wms.uhfrfid.service.dto.ContainerDTO;
import com.wms.uhfrfid.service.mapper.ContainerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Container}.
 */
@Service
@Transactional
public class ContainerService {

    private final Logger log = LoggerFactory.getLogger(ContainerService.class);

    private final ContainerRepository containerRepository;

    private final ContainerMapper containerMapper;

    public ContainerService(ContainerRepository containerRepository, ContainerMapper containerMapper) {
        this.containerRepository = containerRepository;
        this.containerMapper = containerMapper;
    }

    /**
     * Save a container.
     *
     * @param containerDTO the entity to save.
     * @return the persisted entity.
     */
    public ContainerDTO save(ContainerDTO containerDTO) {
        log.debug("Request to save Container : {}", containerDTO);
        Container container = containerMapper.toEntity(containerDTO);
        container = containerRepository.save(container);
        return containerMapper.toDto(container);
    }

    /**
     * Partially update a container.
     *
     * @param containerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContainerDTO> partialUpdate(ContainerDTO containerDTO) {
        log.debug("Request to partially update Container : {}", containerDTO);

        return containerRepository
            .findById(containerDTO.getId())
            .map(existingContainer -> {
                containerMapper.partialUpdate(existingContainer, containerDTO);

                return existingContainer;
            })
            .map(containerRepository::save)
            .map(containerMapper::toDto);
    }

    /**
     * Get all the containers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContainerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Containers");
        return containerRepository.findAll(pageable).map(containerMapper::toDto);
    }

    /**
     * Get one container by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContainerDTO> findOne(Long id) {
        log.debug("Request to get Container : {}", id);
        return containerRepository.findById(id).map(containerMapper::toDto);
    }

    /**
     * Delete the container by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Container : {}", id);
        containerRepository.deleteById(id);
    }
}
