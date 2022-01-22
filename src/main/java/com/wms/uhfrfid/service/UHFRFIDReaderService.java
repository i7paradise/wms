package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.UHFRFIDReader;
import com.wms.uhfrfid.repository.UHFRFIDReaderRepository;
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

    public UHFRFIDReaderService(UHFRFIDReaderRepository uHFRFIDReaderRepository) {
        this.uHFRFIDReaderRepository = uHFRFIDReaderRepository;
    }

    /**
     * Save a uHFRFIDReader.
     *
     * @param uHFRFIDReader the entity to save.
     * @return the persisted entity.
     */
    public UHFRFIDReader save(UHFRFIDReader uHFRFIDReader) {
        log.debug("Request to save UHFRFIDReader : {}", uHFRFIDReader);
        return uHFRFIDReaderRepository.save(uHFRFIDReader);
    }

    /**
     * Partially update a uHFRFIDReader.
     *
     * @param uHFRFIDReader the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UHFRFIDReader> partialUpdate(UHFRFIDReader uHFRFIDReader) {
        log.debug("Request to partially update UHFRFIDReader : {}", uHFRFIDReader);

        return uHFRFIDReaderRepository
            .findById(uHFRFIDReader.getId())
            .map(existingUHFRFIDReader -> {
                if (uHFRFIDReader.getName() != null) {
                    existingUHFRFIDReader.setName(uHFRFIDReader.getName());
                }
                if (uHFRFIDReader.getIp() != null) {
                    existingUHFRFIDReader.setIp(uHFRFIDReader.getIp());
                }
                if (uHFRFIDReader.getPort() != null) {
                    existingUHFRFIDReader.setPort(uHFRFIDReader.getPort());
                }
                if (uHFRFIDReader.getStatus() != null) {
                    existingUHFRFIDReader.setStatus(uHFRFIDReader.getStatus());
                }

                return existingUHFRFIDReader;
            })
            .map(uHFRFIDReaderRepository::save);
    }

    /**
     * Get all the uHFRFIDReaders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UHFRFIDReader> findAll(Pageable pageable) {
        log.debug("Request to get all UHFRFIDReaders");
        return uHFRFIDReaderRepository.findAll(pageable);
    }

    /**
     * Get one uHFRFIDReader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UHFRFIDReader> findOne(Long id) {
        log.debug("Request to get UHFRFIDReader : {}", id);
        return uHFRFIDReaderRepository.findById(id);
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
