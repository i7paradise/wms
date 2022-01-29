package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.WHLevel;
import com.wms.uhfrfid.repository.WHLevelRepository;
import com.wms.uhfrfid.service.dto.WHLevelDTO;
import com.wms.uhfrfid.service.mapper.WHLevelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WHLevel}.
 */
@Service
@Transactional
public class WHLevelService {

    private final Logger log = LoggerFactory.getLogger(WHLevelService.class);

    private final WHLevelRepository wHLevelRepository;

    private final WHLevelMapper wHLevelMapper;

    public WHLevelService(WHLevelRepository wHLevelRepository, WHLevelMapper wHLevelMapper) {
        this.wHLevelRepository = wHLevelRepository;
        this.wHLevelMapper = wHLevelMapper;
    }

    /**
     * Save a wHLevel.
     *
     * @param wHLevelDTO the entity to save.
     * @return the persisted entity.
     */
    public WHLevelDTO save(WHLevelDTO wHLevelDTO) {
        log.debug("Request to save WHLevel : {}", wHLevelDTO);
        WHLevel wHLevel = wHLevelMapper.toEntity(wHLevelDTO);
        wHLevel = wHLevelRepository.save(wHLevel);
        return wHLevelMapper.toDto(wHLevel);
    }

    /**
     * Partially update a wHLevel.
     *
     * @param wHLevelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WHLevelDTO> partialUpdate(WHLevelDTO wHLevelDTO) {
        log.debug("Request to partially update WHLevel : {}", wHLevelDTO);

        return wHLevelRepository
            .findById(wHLevelDTO.getId())
            .map(existingWHLevel -> {
                wHLevelMapper.partialUpdate(existingWHLevel, wHLevelDTO);

                return existingWHLevel;
            })
            .map(wHLevelRepository::save)
            .map(wHLevelMapper::toDto);
    }

    /**
     * Get all the wHLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WHLevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WHLevels");
        return wHLevelRepository.findAll(pageable).map(wHLevelMapper::toDto);
    }

    /**
     * Get one wHLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WHLevelDTO> findOne(Long id) {
        log.debug("Request to get WHLevel : {}", id);
        return wHLevelRepository.findById(id).map(wHLevelMapper::toDto);
    }

    /**
     * Delete the wHLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WHLevel : {}", id);
        wHLevelRepository.deleteById(id);
    }
}
