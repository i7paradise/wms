package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.DoorAntenna;
import com.wms.uhfrfid.repository.DoorAntennaRepository;
import com.wms.uhfrfid.service.dto.DoorAntennaDTO;
import com.wms.uhfrfid.service.mapper.DoorAntennaMapper;
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

    private final DoorAntennaMapper doorAntennaMapper;

    public DoorAntennaService(DoorAntennaRepository doorAntennaRepository, DoorAntennaMapper doorAntennaMapper) {
        this.doorAntennaRepository = doorAntennaRepository;
        this.doorAntennaMapper = doorAntennaMapper;
    }

    /**
     * Save a doorAntenna.
     *
     * @param doorAntennaDTO the entity to save.
     * @return the persisted entity.
     */
    public DoorAntennaDTO save(DoorAntennaDTO doorAntennaDTO) {
        log.debug("Request to save DoorAntenna : {}", doorAntennaDTO);
        DoorAntenna doorAntenna = doorAntennaMapper.toEntity(doorAntennaDTO);
        doorAntenna = doorAntennaRepository.save(doorAntenna);
        return doorAntennaMapper.toDto(doorAntenna);
    }

    /**
     * Partially update a doorAntenna.
     *
     * @param doorAntennaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DoorAntennaDTO> partialUpdate(DoorAntennaDTO doorAntennaDTO) {
        log.debug("Request to partially update DoorAntenna : {}", doorAntennaDTO);

        return doorAntennaRepository
            .findById(doorAntennaDTO.getId())
            .map(existingDoorAntenna -> {
                doorAntennaMapper.partialUpdate(existingDoorAntenna, doorAntennaDTO);

                return existingDoorAntenna;
            })
            .map(doorAntennaRepository::save)
            .map(doorAntennaMapper::toDto);
    }

    /**
     * Get all the doorAntennas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoorAntennaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DoorAntennas");
        return doorAntennaRepository.findAll(pageable).map(doorAntennaMapper::toDto);
    }

    /**
     * Get one doorAntenna by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoorAntennaDTO> findOne(Long id) {
        log.debug("Request to get DoorAntenna : {}", id);
        return doorAntennaRepository.findById(id).map(doorAntennaMapper::toDto);
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
