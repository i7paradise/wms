package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Door;
import com.wms.uhfrfid.repository.DoorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Door}.
 */
@Service
@Transactional
public class DoorService {

    private final Logger log = LoggerFactory.getLogger(DoorService.class);

    private final DoorRepository doorRepository;

    public DoorService(DoorRepository doorRepository) {
        this.doorRepository = doorRepository;
    }

    /**
     * Save a door.
     *
     * @param door the entity to save.
     * @return the persisted entity.
     */
    public Door save(Door door) {
        log.debug("Request to save Door : {}", door);
        return doorRepository.save(door);
    }

    /**
     * Partially update a door.
     *
     * @param door the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Door> partialUpdate(Door door) {
        log.debug("Request to partially update Door : {}", door);

        return doorRepository
            .findById(door.getId())
            .map(existingDoor -> {
                if (door.getName() != null) {
                    existingDoor.setName(door.getName());
                }

                return existingDoor;
            })
            .map(doorRepository::save);
    }

    /**
     * Get all the doors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Door> findAll(Pageable pageable) {
        log.debug("Request to get all Doors");
        return doorRepository.findAll(pageable);
    }

    /**
     * Get one door by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Door> findOne(Long id) {
        log.debug("Request to get Door : {}", id);
        return doorRepository.findById(id);
    }

    /**
     * Delete the door by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Door : {}", id);
        doorRepository.deleteById(id);
    }
}
