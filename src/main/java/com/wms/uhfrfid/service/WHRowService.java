package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.WHRow;
import com.wms.uhfrfid.repository.WHRowRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WHRow}.
 */
@Service
@Transactional
public class WHRowService {

    private final Logger log = LoggerFactory.getLogger(WHRowService.class);

    private final WHRowRepository wHRowRepository;

    public WHRowService(WHRowRepository wHRowRepository) {
        this.wHRowRepository = wHRowRepository;
    }

    /**
     * Save a wHRow.
     *
     * @param wHRow the entity to save.
     * @return the persisted entity.
     */
    public WHRow save(WHRow wHRow) {
        log.debug("Request to save WHRow : {}", wHRow);
        return wHRowRepository.save(wHRow);
    }

    /**
     * Partially update a wHRow.
     *
     * @param wHRow the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WHRow> partialUpdate(WHRow wHRow) {
        log.debug("Request to partially update WHRow : {}", wHRow);

        return wHRowRepository
            .findById(wHRow.getId())
            .map(existingWHRow -> {
                if (wHRow.getName() != null) {
                    existingWHRow.setName(wHRow.getName());
                }
                if (wHRow.getNote() != null) {
                    existingWHRow.setNote(wHRow.getNote());
                }

                return existingWHRow;
            })
            .map(wHRowRepository::save);
    }

    /**
     * Get all the wHRows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WHRow> findAll(Pageable pageable) {
        log.debug("Request to get all WHRows");
        return wHRowRepository.findAll(pageable);
    }

    /**
     * Get one wHRow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WHRow> findOne(Long id) {
        log.debug("Request to get WHRow : {}", id);
        return wHRowRepository.findById(id);
    }

    /**
     * Delete the wHRow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WHRow : {}", id);
        wHRowRepository.deleteById(id);
    }
}
