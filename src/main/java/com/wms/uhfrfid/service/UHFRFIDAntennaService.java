package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import com.wms.uhfrfid.repository.UHFRFIDAntennaRepository;
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

    public UHFRFIDAntennaService(UHFRFIDAntennaRepository uHFRFIDAntennaRepository) {
        this.uHFRFIDAntennaRepository = uHFRFIDAntennaRepository;
    }

    /**
     * Save a uHFRFIDAntenna.
     *
     * @param uHFRFIDAntenna the entity to save.
     * @return the persisted entity.
     */
    public UHFRFIDAntenna save(UHFRFIDAntenna uHFRFIDAntenna) {
        log.debug("Request to save UHFRFIDAntenna : {}", uHFRFIDAntenna);
        return uHFRFIDAntennaRepository.save(uHFRFIDAntenna);
    }

    /**
     * Partially update a uHFRFIDAntenna.
     *
     * @param uHFRFIDAntenna the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UHFRFIDAntenna> partialUpdate(UHFRFIDAntenna uHFRFIDAntenna) {
        log.debug("Request to partially update UHFRFIDAntenna : {}", uHFRFIDAntenna);

        return uHFRFIDAntennaRepository
            .findById(uHFRFIDAntenna.getId())
            .map(existingUHFRFIDAntenna -> {
                if (uHFRFIDAntenna.getName() != null) {
                    existingUHFRFIDAntenna.setName(uHFRFIDAntenna.getName());
                }
                if (uHFRFIDAntenna.getOutputPower() != null) {
                    existingUHFRFIDAntenna.setOutputPower(uHFRFIDAntenna.getOutputPower());
                }
                if (uHFRFIDAntenna.getStatus() != null) {
                    existingUHFRFIDAntenna.setStatus(uHFRFIDAntenna.getStatus());
                }

                return existingUHFRFIDAntenna;
            })
            .map(uHFRFIDAntennaRepository::save);
    }

    /**
     * Get all the uHFRFIDAntennas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UHFRFIDAntenna> findAll(Pageable pageable) {
        log.debug("Request to get all UHFRFIDAntennas");
        return uHFRFIDAntennaRepository.findAll(pageable);
    }

    /**
     * Get one uHFRFIDAntenna by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UHFRFIDAntenna> findOne(Long id) {
        log.debug("Request to get UHFRFIDAntenna : {}", id);
        return uHFRFIDAntennaRepository.findById(id);
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
