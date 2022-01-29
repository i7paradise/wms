package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Door;
import com.wms.uhfrfid.repository.DoorRepository;
import com.wms.uhfrfid.service.dto.DoorDTO;
import com.wms.uhfrfid.service.mapper.DoorMapper;
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

    private final DoorMapper doorMapper;

    public DoorService(DoorRepository doorRepository, DoorMapper doorMapper) {
        this.doorRepository = doorRepository;
        this.doorMapper = doorMapper;
    }

    /**
     * Save a door.
     *
     * @param doorDTO the entity to save.
     * @return the persisted entity.
     */
    public DoorDTO save(DoorDTO doorDTO) {
        log.debug("Request to save Door : {}", doorDTO);
        Door door = doorMapper.toEntity(doorDTO);
        door = doorRepository.save(door);
        return doorMapper.toDto(door);
    }

    /**
     * Partially update a door.
     *
     * @param doorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DoorDTO> partialUpdate(DoorDTO doorDTO) {
        log.debug("Request to partially update Door : {}", doorDTO);

        return doorRepository
            .findById(doorDTO.getId())
            .map(existingDoor -> {
                doorMapper.partialUpdate(existingDoor, doorDTO);

                return existingDoor;
            })
            .map(doorRepository::save)
            .map(doorMapper::toDto);
    }

    /**
     * Get all the doors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Doors");
        return doorRepository.findAll(pageable).map(doorMapper::toDto);
    }

    /**
     * Get one door by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoorDTO> findOne(Long id) {
        log.debug("Request to get Door : {}", id);
        return doorRepository.findById(id).map(doorMapper::toDto);
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
