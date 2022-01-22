package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Bay;
import com.wms.uhfrfid.repository.BayRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bay}.
 */
@Service
@Transactional
public class BayService {

    private final Logger log = LoggerFactory.getLogger(BayService.class);

    private final BayRepository bayRepository;

    public BayService(BayRepository bayRepository) {
        this.bayRepository = bayRepository;
    }

    /**
     * Save a bay.
     *
     * @param bay the entity to save.
     * @return the persisted entity.
     */
    public Bay save(Bay bay) {
        log.debug("Request to save Bay : {}", bay);
        return bayRepository.save(bay);
    }

    /**
     * Partially update a bay.
     *
     * @param bay the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Bay> partialUpdate(Bay bay) {
        log.debug("Request to partially update Bay : {}", bay);

        return bayRepository
            .findById(bay.getId())
            .map(existingBay -> {
                if (bay.getName() != null) {
                    existingBay.setName(bay.getName());
                }
                if (bay.getNote() != null) {
                    existingBay.setNote(bay.getNote());
                }

                return existingBay;
            })
            .map(bayRepository::save);
    }

    /**
     * Get all the bays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Bay> findAll(Pageable pageable) {
        log.debug("Request to get all Bays");
        return bayRepository.findAll(pageable);
    }

    /**
     * Get one bay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bay> findOne(Long id) {
        log.debug("Request to get Bay : {}", id);
        return bayRepository.findById(id);
    }

    /**
     * Delete the bay by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bay : {}", id);
        bayRepository.deleteById(id);
    }
}
