package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.WHLevel;
import com.wms.uhfrfid.repository.WHLevelRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WHLevel}.
 */
@Service
@Transactional
public class WHLevelService {

    private final Logger log = LoggerFactory.getLogger(WHLevelService.class);

    private final WHLevelRepository wHLevelRepository;

    public WHLevelService(WHLevelRepository wHLevelRepository) {
        this.wHLevelRepository = wHLevelRepository;
    }

    /**
     * Save a wHLevel.
     *
     * @param wHLevel the entity to save.
     * @return the persisted entity.
     */
    public WHLevel save(WHLevel wHLevel) {
        log.debug("Request to save WHLevel : {}", wHLevel);
        return wHLevelRepository.save(wHLevel);
    }

    /**
     * Partially update a wHLevel.
     *
     * @param wHLevel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WHLevel> partialUpdate(WHLevel wHLevel) {
        log.debug("Request to partially update WHLevel : {}", wHLevel);

        return wHLevelRepository
            .findById(wHLevel.getId())
            .map(existingWHLevel -> {
                if (wHLevel.getName() != null) {
                    existingWHLevel.setName(wHLevel.getName());
                }
                if (wHLevel.getNote() != null) {
                    existingWHLevel.setNote(wHLevel.getNote());
                }

                return existingWHLevel;
            })
            .map(wHLevelRepository::save);
    }

    /**
     * Get all the wHLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WHLevel> findAll(Pageable pageable) {
        log.debug("Request to get all WHLevels");
        return wHLevelRepository.findAll(pageable);
    }

    /**
     * Get one wHLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WHLevel> findOne(Long id) {
        log.debug("Request to get WHLevel : {}", id);
        return wHLevelRepository.findById(id);
    }

    /**
     * Delete the wHLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WHLevel : {}", id);
        wHLevelRepository.deleteById(id);
    }
}
