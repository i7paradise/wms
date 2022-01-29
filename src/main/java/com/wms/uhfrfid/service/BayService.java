package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Bay;
import com.wms.uhfrfid.repository.BayRepository;
import com.wms.uhfrfid.service.dto.BayDTO;
import com.wms.uhfrfid.service.mapper.BayMapper;
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

    private final BayMapper bayMapper;

    public BayService(BayRepository bayRepository, BayMapper bayMapper) {
        this.bayRepository = bayRepository;
        this.bayMapper = bayMapper;
    }

    /**
     * Save a bay.
     *
     * @param bayDTO the entity to save.
     * @return the persisted entity.
     */
    public BayDTO save(BayDTO bayDTO) {
        log.debug("Request to save Bay : {}", bayDTO);
        Bay bay = bayMapper.toEntity(bayDTO);
        bay = bayRepository.save(bay);
        return bayMapper.toDto(bay);
    }

    /**
     * Partially update a bay.
     *
     * @param bayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BayDTO> partialUpdate(BayDTO bayDTO) {
        log.debug("Request to partially update Bay : {}", bayDTO);

        return bayRepository
            .findById(bayDTO.getId())
            .map(existingBay -> {
                bayMapper.partialUpdate(existingBay, bayDTO);

                return existingBay;
            })
            .map(bayRepository::save)
            .map(bayMapper::toDto);
    }

    /**
     * Get all the bays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bays");
        return bayRepository.findAll(pageable).map(bayMapper::toDto);
    }

    /**
     * Get one bay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BayDTO> findOne(Long id) {
        log.debug("Request to get Bay : {}", id);
        return bayRepository.findById(id).map(bayMapper::toDto);
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
