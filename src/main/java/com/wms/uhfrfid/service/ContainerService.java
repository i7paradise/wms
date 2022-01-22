package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Container;
import com.wms.uhfrfid.repository.ContainerRepository;
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

    public ContainerService(ContainerRepository containerRepository) {
        this.containerRepository = containerRepository;
    }

    /**
     * Save a container.
     *
     * @param container the entity to save.
     * @return the persisted entity.
     */
    public Container save(Container container) {
        log.debug("Request to save Container : {}", container);
        return containerRepository.save(container);
    }

    /**
     * Partially update a container.
     *
     * @param container the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Container> partialUpdate(Container container) {
        log.debug("Request to partially update Container : {}", container);

        return containerRepository
            .findById(container.getId())
            .map(existingContainer -> {
                if (container.getName() != null) {
                    existingContainer.setName(container.getName());
                }
                if (container.getDescription() != null) {
                    existingContainer.setDescription(container.getDescription());
                }

                return existingContainer;
            })
            .map(containerRepository::save);
    }

    /**
     * Get all the containers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Container> findAll(Pageable pageable) {
        log.debug("Request to get all Containers");
        return containerRepository.findAll(pageable);
    }

    /**
     * Get one container by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Container> findOne(Long id) {
        log.debug("Request to get Container : {}", id);
        return containerRepository.findById(id);
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
