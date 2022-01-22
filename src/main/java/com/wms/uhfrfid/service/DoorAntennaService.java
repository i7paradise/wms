package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DoorAntenna;
import com.wms.uhfrfid.repository.DoorAntennaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DoorAntenna}.
 */
@Service
@Transactional
public class DoorAntennaService {

    private final Logger log = LoggerFactory.getLogger(DoorAntennaService.class);

    private final DoorAntennaRepository doorAntennaRepository;

    public DoorAntennaService(DoorAntennaRepository doorAntennaRepository) {
        this.doorAntennaRepository = doorAntennaRepository;
    }

    /**
     * Save a doorAntenna.
     *
     * @param doorAntenna the entity to save.
     * @return the persisted entity.
     */
    public DoorAntenna save(DoorAntenna doorAntenna) {
        log.debug("Request to save DoorAntenna : {}", doorAntenna);
        return doorAntennaRepository.save(doorAntenna);
    }

    /**
     * Partially update a doorAntenna.
     *
     * @param doorAntenna the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DoorAntenna> partialUpdate(DoorAntenna doorAntenna) {
        log.debug("Request to partially update DoorAntenna : {}", doorAntenna);

        return doorAntennaRepository
            .findById(doorAntenna.getId())
            .map(existingDoorAntenna -> {
                if (doorAntenna.getType() != null) {
                    existingDoorAntenna.setType(doorAntenna.getType());
                }

                return existingDoorAntenna;
            })
            .map(doorAntennaRepository::save);
    }

    /**
     * Get all the doorAntennas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoorAntenna> findAll(Pageable pageable) {
        log.debug("Request to get all DoorAntennas");
        return doorAntennaRepository.findAll(pageable);
    }

    /**
     * Get one doorAntenna by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoorAntenna> findOne(Long id) {
        log.debug("Request to get DoorAntenna : {}", id);
        return doorAntennaRepository.findById(id);
    }

    /**
     * Delete the doorAntenna by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DoorAntenna : {}", id);
        doorAntennaRepository.deleteById(id);
    }
}
