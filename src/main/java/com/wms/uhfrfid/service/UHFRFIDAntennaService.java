package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import com.wms.uhfrfid.repository.UHFRFIDAntennaRepository;
import com.wms.uhfrfid.service.dto.UHFRFIDAntennaDTO;
import com.wms.uhfrfid.service.mapper.UHFRFIDAntennaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UHFRFIDAntenna}.
 */
@Service
@Transactional
public class UHFRFIDAntennaService {

    private final Logger log = LoggerFactory.getLogger(UHFRFIDAntennaService.class);

    private final UHFRFIDAntennaRepository uHFRFIDAntennaRepository;

    private final UHFRFIDAntennaMapper uHFRFIDAntennaMapper;

    public UHFRFIDAntennaService(UHFRFIDAntennaRepository uHFRFIDAntennaRepository, UHFRFIDAntennaMapper uHFRFIDAntennaMapper) {
        this.uHFRFIDAntennaRepository = uHFRFIDAntennaRepository;
        this.uHFRFIDAntennaMapper = uHFRFIDAntennaMapper;
    }

    /**
     * Save a uHFRFIDAntenna.
     *
     * @param uHFRFIDAntennaDTO the entity to save.
     * @return the persisted entity.
     */
    public UHFRFIDAntennaDTO save(UHFRFIDAntennaDTO uHFRFIDAntennaDTO) {
        log.debug("Request to save UHFRFIDAntenna : {}", uHFRFIDAntennaDTO);
        UHFRFIDAntenna uHFRFIDAntenna = uHFRFIDAntennaMapper.toEntity(uHFRFIDAntennaDTO);
        uHFRFIDAntenna = uHFRFIDAntennaRepository.save(uHFRFIDAntenna);
        return uHFRFIDAntennaMapper.toDto(uHFRFIDAntenna);
    }

    /**
     * Partially update a uHFRFIDAntenna.
     *
     * @param uHFRFIDAntennaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UHFRFIDAntennaDTO> partialUpdate(UHFRFIDAntennaDTO uHFRFIDAntennaDTO) {
        log.debug("Request to partially update UHFRFIDAntenna : {}", uHFRFIDAntennaDTO);

        return uHFRFIDAntennaRepository
            .findById(uHFRFIDAntennaDTO.getId())
            .map(existingUHFRFIDAntenna -> {
                uHFRFIDAntennaMapper.partialUpdate(existingUHFRFIDAntenna, uHFRFIDAntennaDTO);

                return existingUHFRFIDAntenna;
            })
            .map(uHFRFIDAntennaRepository::save)
            .map(uHFRFIDAntennaMapper::toDto);
    }

    /**
     * Get all the uHFRFIDAntennas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UHFRFIDAntennaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UHFRFIDAntennas");
        return uHFRFIDAntennaRepository.findAll(pageable).map(uHFRFIDAntennaMapper::toDto);
    }

    /**
     * Get one uHFRFIDAntenna by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UHFRFIDAntennaDTO> findOne(Long id) {
        log.debug("Request to get UHFRFIDAntenna : {}", id);
        return uHFRFIDAntennaRepository.findById(id).map(uHFRFIDAntennaMapper::toDto);
    }

    /**
     * Delete the uHFRFIDAntenna by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UHFRFIDAntenna : {}", id);
        uHFRFIDAntennaRepository.deleteById(id);
    }
}
