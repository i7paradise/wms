package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.UHFRFIDReader;
import com.wms.uhfrfid.repository.UHFRFIDReaderRepository;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTO;
import com.wms.uhfrfid.service.mapper.UHFRFIDReaderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UHFRFIDReader}.
 */
@Service
@Transactional
public class UHFRFIDReaderService {

    private final Logger log = LoggerFactory.getLogger(UHFRFIDReaderService.class);

    private final UHFRFIDReaderRepository uHFRFIDReaderRepository;

    private final UHFRFIDReaderMapper uHFRFIDReaderMapper;

    public UHFRFIDReaderService(UHFRFIDReaderRepository uHFRFIDReaderRepository, UHFRFIDReaderMapper uHFRFIDReaderMapper) {
        this.uHFRFIDReaderRepository = uHFRFIDReaderRepository;
        this.uHFRFIDReaderMapper = uHFRFIDReaderMapper;
    }

    /**
     * Save a uHFRFIDReader.
     *
     * @param uHFRFIDReaderDTO the entity to save.
     * @return the persisted entity.
     */
    public UHFRFIDReaderDTO save(UHFRFIDReaderDTO uHFRFIDReaderDTO) {
        log.debug("Request to save UHFRFIDReader : {}", uHFRFIDReaderDTO);
        UHFRFIDReader uHFRFIDReader = uHFRFIDReaderMapper.toEntity(uHFRFIDReaderDTO);
        uHFRFIDReader = uHFRFIDReaderRepository.save(uHFRFIDReader);
        return uHFRFIDReaderMapper.toDto(uHFRFIDReader);
    }

    /**
     * Partially update a uHFRFIDReader.
     *
     * @param uHFRFIDReaderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UHFRFIDReaderDTO> partialUpdate(UHFRFIDReaderDTO uHFRFIDReaderDTO) {
        log.debug("Request to partially update UHFRFIDReader : {}", uHFRFIDReaderDTO);

        return uHFRFIDReaderRepository
            .findById(uHFRFIDReaderDTO.getId())
            .map(existingUHFRFIDReader -> {
                uHFRFIDReaderMapper.partialUpdate(existingUHFRFIDReader, uHFRFIDReaderDTO);

                return existingUHFRFIDReader;
            })
            .map(uHFRFIDReaderRepository::save)
            .map(uHFRFIDReaderMapper::toDto);
    }

    /**
     * Get all the uHFRFIDReaders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UHFRFIDReaderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UHFRFIDReaders");
        return uHFRFIDReaderRepository.findAll(pageable).map(uHFRFIDReaderMapper::toDto);
    }

    /**
     * Get one uHFRFIDReader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UHFRFIDReaderDTO> findOne(Long id) {
        log.debug("Request to get UHFRFIDReader : {}", id);
        return uHFRFIDReaderRepository.findById(id).map(uHFRFIDReaderMapper::toDto);
    }

    /**
     * Delete the uHFRFIDReader by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UHFRFIDReader : {}", id);
        uHFRFIDReaderRepository.deleteById(id);
    }
}
