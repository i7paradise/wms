package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.DoorRepository;
import com.wms.uhfrfid.service.DoorService;
import com.wms.uhfrfid.service.dto.DoorDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.Door}.
 */
@RestController
@RequestMapping("/api")
public class DoorResource {

    private final Logger log = LoggerFactory.getLogger(DoorResource.class);

    private static final String ENTITY_NAME = "door";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoorService doorService;

    private final DoorRepository doorRepository;

    public DoorResource(DoorService doorService, DoorRepository doorRepository) {
        this.doorService = doorService;
        this.doorRepository = doorRepository;
    }

    /**
     * {@code POST  /doors} : Create a new door.
     *
     * @param doorDTO the doorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doorDTO, or with status {@code 400 (Bad Request)} if the door has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doors")
    public ResponseEntity<DoorDTO> createDoor(@Valid @RequestBody DoorDTO doorDTO) throws URISyntaxException {
        log.debug("REST request to save Door : {}", doorDTO);
        if (doorDTO.getId() != null) {
            throw new BadRequestAlertException("A new door cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoorDTO result = doorService.save(doorDTO);
        return ResponseEntity
            .created(new URI("/api/doors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doors/:id} : Updates an existing door.
     *
     * @param id the id of the doorDTO to save.
     * @param doorDTO the doorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doorDTO,
     * or with status {@code 400 (Bad Request)} if the doorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doors/{id}")
    public ResponseEntity<DoorDTO> updateDoor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DoorDTO doorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Door : {}, {}", id, doorDTO);
        if (doorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DoorDTO result = doorService.save(doorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doors/:id} : Partial updates given fields of an existing door, field will ignore if it is null
     *
     * @param id the id of the doorDTO to save.
     * @param doorDTO the doorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doorDTO,
     * or with status {@code 400 (Bad Request)} if the doorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the doorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the doorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DoorDTO> partialUpdateDoor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DoorDTO doorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Door partially : {}, {}", id, doorDTO);
        if (doorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DoorDTO> result = doorService.partialUpdate(doorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /doors} : get all the doors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doors in body.
     */
    @GetMapping("/doors")
    public ResponseEntity<List<DoorDTO>> getAllDoors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Doors");
        Page<DoorDTO> page = doorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doors/:id} : get the "id" door.
     *
     * @param id the id of the doorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doors/{id}")
    public ResponseEntity<DoorDTO> getDoor(@PathVariable Long id) {
        log.debug("REST request to get Door : {}", id);
        Optional<DoorDTO> doorDTO = doorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doorDTO);
    }

    /**
     * {@code DELETE  /doors/:id} : delete the "id" door.
     *
     * @param id the id of the doorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doors/{id}")
    public ResponseEntity<Void> deleteDoor(@PathVariable Long id) {
        log.debug("REST request to delete Door : {}", id);
        doorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
