package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Position;
import com.wms.uhfrfid.repository.PositionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Position}.
 */
@Service
@Transactional
public class PositionService {

    private final Logger log = LoggerFactory.getLogger(PositionService.class);

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    /**
     * Save a position.
     *
     * @param position the entity to save.
     * @return the persisted entity.
     */
    public Position save(Position position) {
        log.debug("Request to save Position : {}", position);
        return positionRepository.save(position);
    }

    /**
     * Partially update a position.
     *
     * @param position the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Position> partialUpdate(Position position) {
        log.debug("Request to partially update Position : {}", position);

        return positionRepository
            .findById(position.getId())
            .map(existingPosition -> {
                if (position.getName() != null) {
                    existingPosition.setName(position.getName());
                }
                if (position.getNote() != null) {
                    existingPosition.setNote(position.getNote());
                }

                return existingPosition;
            })
            .map(positionRepository::save);
    }

    /**
     * Get all the positions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Position> findAll(Pageable pageable) {
        log.debug("Request to get all Positions");
        return positionRepository.findAll(pageable);
    }

    /**
     * Get one position by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Position> findOne(Long id) {
        log.debug("Request to get Position : {}", id);
        return positionRepository.findById(id);
    }

    /**
     * Delete the position by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Position : {}", id);
        positionRepository.deleteById(id);
    }
}
