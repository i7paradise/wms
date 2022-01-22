package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.WHLevel;
import com.wms.uhfrfid.repository.WHLevelRepository;
import com.wms.uhfrfid.service.WHLevelService;
import com.wms.uhfrfid.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wms.uhfrfid.domain.WHLevel}.
 */
@RestController
@RequestMapping("/api")
public class WHLevelResource {

    private final Logger log = LoggerFactory.getLogger(WHLevelResource.class);

    private static final String ENTITY_NAME = "wHLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WHLevelService wHLevelService;

    private final WHLevelRepository wHLevelRepository;

    public WHLevelResource(WHLevelService wHLevelService, WHLevelRepository wHLevelRepository) {
        this.wHLevelService = wHLevelService;
        this.wHLevelRepository = wHLevelRepository;
    }

    /**
     * {@code POST  /wh-levels} : Create a new wHLevel.
     *
     * @param wHLevel the wHLevel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wHLevel, or with status {@code 400 (Bad Request)} if the wHLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wh-levels")
    public ResponseEntity<WHLevel> createWHLevel(@Valid @RequestBody WHLevel wHLevel) throws URISyntaxException {
        log.debug("REST request to save WHLevel : {}", wHLevel);
        if (wHLevel.getId() != null) {
            throw new BadRequestAlertException("A new wHLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WHLevel result = wHLevelService.save(wHLevel);
        return ResponseEntity
            .created(new URI("/api/wh-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wh-levels/:id} : Updates an existing wHLevel.
     *
     * @param id the id of the wHLevel to save.
     * @param wHLevel the wHLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wHLevel,
     * or with status {@code 400 (Bad Request)} if the wHLevel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wHLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wh-levels/{id}")
    public ResponseEntity<WHLevel> updateWHLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WHLevel wHLevel
    ) throws URISyntaxException {
        log.debug("REST request to update WHLevel : {}, {}", id, wHLevel);
        if (wHLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wHLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wHLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WHLevel result = wHLevelService.save(wHLevel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wHLevel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wh-levels/:id} : Partial updates given fields of an existing wHLevel, field will ignore if it is null
     *
     * @param id the id of the wHLevel to save.
     * @param wHLevel the wHLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wHLevel,
     * or with status {@code 400 (Bad Request)} if the wHLevel is not valid,
     * or with status {@code 404 (Not Found)} if the wHLevel is not found,
     * or with status {@code 500 (Internal Server Error)} if the wHLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wh-levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WHLevel> partialUpdateWHLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WHLevel wHLevel
    ) throws URISyntaxException {
        log.debug("REST request to partial update WHLevel partially : {}, {}", id, wHLevel);
        if (wHLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wHLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wHLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WHLevel> result = wHLevelService.partialUpdate(wHLevel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wHLevel.getId().toString())
        );
    }

    /**
     * {@code GET  /wh-levels} : get all the wHLevels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wHLevels in body.
     */
    @GetMapping("/wh-levels")
    public ResponseEntity<List<WHLevel>> getAllWHLevels(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WHLevels");
        Page<WHLevel> page = wHLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wh-levels/:id} : get the "id" wHLevel.
     *
     * @param id the id of the wHLevel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wHLevel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wh-levels/{id}")
    public ResponseEntity<WHLevel> getWHLevel(@PathVariable Long id) {
        log.debug("REST request to get WHLevel : {}", id);
        Optional<WHLevel> wHLevel = wHLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wHLevel);
    }

    /**
     * {@code DELETE  /wh-levels/:id} : delete the "id" wHLevel.
     *
     * @param id the id of the wHLevel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wh-levels/{id}")
    public ResponseEntity<Void> deleteWHLevel(@PathVariable Long id) {
        log.debug("REST request to delete WHLevel : {}", id);
        wHLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
