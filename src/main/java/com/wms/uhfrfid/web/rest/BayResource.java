package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.Bay;
import com.wms.uhfrfid.repository.BayRepository;
import com.wms.uhfrfid.service.BayService;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.Bay}.
 */
@RestController
@RequestMapping("/api")
public class BayResource {

    private final Logger log = LoggerFactory.getLogger(BayResource.class);

    private static final String ENTITY_NAME = "bay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BayService bayService;

    private final BayRepository bayRepository;

    public BayResource(BayService bayService, BayRepository bayRepository) {
        this.bayService = bayService;
        this.bayRepository = bayRepository;
    }

    /**
     * {@code POST  /bays} : Create a new bay.
     *
     * @param bay the bay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bay, or with status {@code 400 (Bad Request)} if the bay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bays")
    public ResponseEntity<Bay> createBay(@Valid @RequestBody Bay bay) throws URISyntaxException {
        log.debug("REST request to save Bay : {}", bay);
        if (bay.getId() != null) {
            throw new BadRequestAlertException("A new bay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bay result = bayService.save(bay);
        return ResponseEntity
            .created(new URI("/api/bays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bays/:id} : Updates an existing bay.
     *
     * @param id the id of the bay to save.
     * @param bay the bay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bay,
     * or with status {@code 400 (Bad Request)} if the bay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bays/{id}")
    public ResponseEntity<Bay> updateBay(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Bay bay)
        throws URISyntaxException {
        log.debug("REST request to update Bay : {}, {}", id, bay);
        if (bay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bay result = bayService.save(bay);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bay.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bays/:id} : Partial updates given fields of an existing bay, field will ignore if it is null
     *
     * @param id the id of the bay to save.
     * @param bay the bay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bay,
     * or with status {@code 400 (Bad Request)} if the bay is not valid,
     * or with status {@code 404 (Not Found)} if the bay is not found,
     * or with status {@code 500 (Internal Server Error)} if the bay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bay> partialUpdateBay(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Bay bay)
        throws URISyntaxException {
        log.debug("REST request to partial update Bay partially : {}, {}", id, bay);
        if (bay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bay> result = bayService.partialUpdate(bay);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bay.getId().toString())
        );
    }

    /**
     * {@code GET  /bays} : get all the bays.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bays in body.
     */
    @GetMapping("/bays")
    public ResponseEntity<List<Bay>> getAllBays(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Bays");
        Page<Bay> page = bayService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bays/:id} : get the "id" bay.
     *
     * @param id the id of the bay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bays/{id}")
    public ResponseEntity<Bay> getBay(@PathVariable Long id) {
        log.debug("REST request to get Bay : {}", id);
        Optional<Bay> bay = bayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bay);
    }

    /**
     * {@code DELETE  /bays/:id} : delete the "id" bay.
     *
     * @param id the id of the bay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bays/{id}")
    public ResponseEntity<Void> deleteBay(@PathVariable Long id) {
        log.debug("REST request to delete Bay : {}", id);
        bayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
