package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.DoorAntenna;
import com.wms.uhfrfid.repository.DoorAntennaRepository;
import com.wms.uhfrfid.service.DoorAntennaService;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.DoorAntenna}.
 */
@RestController
@RequestMapping("/api")
public class DoorAntennaResource {

    private final Logger log = LoggerFactory.getLogger(DoorAntennaResource.class);

    private static final String ENTITY_NAME = "doorAntenna";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoorAntennaService doorAntennaService;

    private final DoorAntennaRepository doorAntennaRepository;

    public DoorAntennaResource(DoorAntennaService doorAntennaService, DoorAntennaRepository doorAntennaRepository) {
        this.doorAntennaService = doorAntennaService;
        this.doorAntennaRepository = doorAntennaRepository;
    }

    /**
     * {@code POST  /door-antennas} : Create a new doorAntenna.
     *
     * @param doorAntenna the doorAntenna to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doorAntenna, or with status {@code 400 (Bad Request)} if the doorAntenna has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/door-antennas")
    public ResponseEntity<DoorAntenna> createDoorAntenna(@Valid @RequestBody DoorAntenna doorAntenna) throws URISyntaxException {
        log.debug("REST request to save DoorAntenna : {}", doorAntenna);
        if (doorAntenna.getId() != null) {
            throw new BadRequestAlertException("A new doorAntenna cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoorAntenna result = doorAntennaService.save(doorAntenna);
        return ResponseEntity
            .created(new URI("/api/door-antennas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /door-antennas/:id} : Updates an existing doorAntenna.
     *
     * @param id the id of the doorAntenna to save.
     * @param doorAntenna the doorAntenna to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doorAntenna,
     * or with status {@code 400 (Bad Request)} if the doorAntenna is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doorAntenna couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/door-antennas/{id}")
    public ResponseEntity<DoorAntenna> updateDoorAntenna(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DoorAntenna doorAntenna
    ) throws URISyntaxException {
        log.debug("REST request to update DoorAntenna : {}, {}", id, doorAntenna);
        if (doorAntenna.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doorAntenna.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doorAntennaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DoorAntenna result = doorAntennaService.save(doorAntenna);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doorAntenna.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /door-antennas/:id} : Partial updates given fields of an existing doorAntenna, field will ignore if it is null
     *
     * @param id the id of the doorAntenna to save.
     * @param doorAntenna the doorAntenna to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doorAntenna,
     * or with status {@code 400 (Bad Request)} if the doorAntenna is not valid,
     * or with status {@code 404 (Not Found)} if the doorAntenna is not found,
     * or with status {@code 500 (Internal Server Error)} if the doorAntenna couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/door-antennas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DoorAntenna> partialUpdateDoorAntenna(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DoorAntenna doorAntenna
    ) throws URISyntaxException {
        log.debug("REST request to partial update DoorAntenna partially : {}, {}", id, doorAntenna);
        if (doorAntenna.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doorAntenna.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doorAntennaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DoorAntenna> result = doorAntennaService.partialUpdate(doorAntenna);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doorAntenna.getId().toString())
        );
    }

    /**
     * {@code GET  /door-antennas} : get all the doorAntennas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doorAntennas in body.
     */
    @GetMapping("/door-antennas")
    public ResponseEntity<List<DoorAntenna>> getAllDoorAntennas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DoorAntennas");
        Page<DoorAntenna> page = doorAntennaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /door-antennas/:id} : get the "id" doorAntenna.
     *
     * @param id the id of the doorAntenna to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doorAntenna, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/door-antennas/{id}")
    public ResponseEntity<DoorAntenna> getDoorAntenna(@PathVariable Long id) {
        log.debug("REST request to get DoorAntenna : {}", id);
        Optional<DoorAntenna> doorAntenna = doorAntennaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doorAntenna);
    }

    /**
     * {@code DELETE  /door-antennas/:id} : delete the "id" doorAntenna.
     *
     * @param id the id of the doorAntenna to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/door-antennas/{id}")
    public ResponseEntity<Void> deleteDoorAntenna(@PathVariable Long id) {
        log.debug("REST request to delete DoorAntenna : {}", id);
        doorAntennaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
