package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Area;
import com.wms.uhfrfid.repository.AreaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Area}.
 */
@Service
@Transactional
public class AreaService {

    private final Logger log = LoggerFactory.getLogger(AreaService.class);

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    /**
     * Save a area.
     *
     * @param area the entity to save.
     * @return the persisted entity.
     */
    public Area save(Area area) {
        log.debug("Request to save Area : {}", area);
        return areaRepository.save(area);
    }

    /**
     * Partially update a area.
     *
     * @param area the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Area> partialUpdate(Area area) {
        log.debug("Request to partially update Area : {}", area);

        return areaRepository
            .findById(area.getId())
            .map(existingArea -> {
                if (area.getType() != null) {
                    existingArea.setType(area.getType());
                }

                return existingArea;
            })
            .map(areaRepository::save);
    }

    /**
     * Get all the areas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Area> findAll(Pageable pageable) {
        log.debug("Request to get all Areas");
        return areaRepository.findAll(pageable);
    }

    /**
     * Get one area by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Area> findOne(Long id) {
        log.debug("Request to get Area : {}", id);
        return areaRepository.findById(id);
    }

    /**
     * Delete the area by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Area : {}", id);
        areaRepository.deleteById(id);
    }
}
