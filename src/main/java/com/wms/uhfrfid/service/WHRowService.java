package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.WHRow;
import com.wms.uhfrfid.repository.WHRowRepository;
import com.wms.uhfrfid.service.dto.WHRowDTO;
import com.wms.uhfrfid.service.mapper.WHRowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WHRow}.
 */
@Service
@Transactional
public class WHRowService {

    private final Logger log = LoggerFactory.getLogger(WHRowService.class);

    private final WHRowRepository wHRowRepository;

    private final WHRowMapper wHRowMapper;

    public WHRowService(WHRowRepository wHRowRepository, WHRowMapper wHRowMapper) {
        this.wHRowRepository = wHRowRepository;
        this.wHRowMapper = wHRowMapper;
    }

    /**
     * Save a wHRow.
     *
     * @param wHRowDTO the entity to save.
     * @return the persisted entity.
     */
    public WHRowDTO save(WHRowDTO wHRowDTO) {
        log.debug("Request to save WHRow : {}", wHRowDTO);
        WHRow wHRow = wHRowMapper.toEntity(wHRowDTO);
        wHRow = wHRowRepository.save(wHRow);
        return wHRowMapper.toDto(wHRow);
    }

    /**
     * Partially update a wHRow.
     *
     * @param wHRowDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WHRowDTO> partialUpdate(WHRowDTO wHRowDTO) {
        log.debug("Request to partially update WHRow : {}", wHRowDTO);

        return wHRowRepository
            .findById(wHRowDTO.getId())
            .map(existingWHRow -> {
                wHRowMapper.partialUpdate(existingWHRow, wHRowDTO);

                return existingWHRow;
            })
            .map(wHRowRepository::save)
            .map(wHRowMapper::toDto);
    }

    /**
     * Get all the wHRows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WHRowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WHRows");
        return wHRowRepository.findAll(pageable).map(wHRowMapper::toDto);
    }

    /**
     * Get one wHRow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WHRowDTO> findOne(Long id) {
        log.debug("Request to get WHRow : {}", id);
        return wHRowRepository.findById(id).map(wHRowMapper::toDto);
    }

    /**
     * Delete the wHRow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WHRow : {}", id);
        wHRowRepository.deleteById(id);
    }
}
