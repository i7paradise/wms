package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.UHFRFIDReader;
import com.wms.uhfrfid.repository.UHFRFIDReaderRepository;
import com.wms.uhfrfid.service.UHFRFIDReaderService;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.UHFRFIDReader}.
 */
@RestController
@RequestMapping("/api")
public class UHFRFIDReaderResource {

    private final Logger log = LoggerFactory.getLogger(UHFRFIDReaderResource.class);

    private static final String ENTITY_NAME = "uHFRFIDReader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UHFRFIDReaderService uHFRFIDReaderService;

    private final UHFRFIDReaderRepository uHFRFIDReaderRepository;

    public UHFRFIDReaderResource(UHFRFIDReaderService uHFRFIDReaderService, UHFRFIDReaderRepository uHFRFIDReaderRepository) {
        this.uHFRFIDReaderService = uHFRFIDReaderService;
        this.uHFRFIDReaderRepository = uHFRFIDReaderRepository;
    }

    /**
     * {@code POST  /uhfrfid-readers} : Create a new uHFRFIDReader.
     *
     * @param uHFRFIDReader the uHFRFIDReader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uHFRFIDReader, or with status {@code 400 (Bad Request)} if the uHFRFIDReader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uhfrfid-readers")
    public ResponseEntity<UHFRFIDReader> createUHFRFIDReader(@Valid @RequestBody UHFRFIDReader uHFRFIDReader) throws URISyntaxException {
        log.debug("REST request to save UHFRFIDReader : {}", uHFRFIDReader);
        if (uHFRFIDReader.getId() != null) {
            throw new BadRequestAlertException("A new uHFRFIDReader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UHFRFIDReader result = uHFRFIDReaderService.save(uHFRFIDReader);
        return ResponseEntity
            .created(new URI("/api/uhfrfid-readers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uhfrfid-readers/:id} : Updates an existing uHFRFIDReader.
     *
     * @param id the id of the uHFRFIDReader to save.
     * @param uHFRFIDReader the uHFRFIDReader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uHFRFIDReader,
     * or with status {@code 400 (Bad Request)} if the uHFRFIDReader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uHFRFIDReader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uhfrfid-readers/{id}")
    public ResponseEntity<UHFRFIDReader> updateUHFRFIDReader(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UHFRFIDReader uHFRFIDReader
    ) throws URISyntaxException {
        log.debug("REST request to update UHFRFIDReader : {}, {}", id, uHFRFIDReader);
        if (uHFRFIDReader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uHFRFIDReader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uHFRFIDReaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UHFRFIDReader result = uHFRFIDReaderService.save(uHFRFIDReader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uHFRFIDReader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /uhfrfid-readers/:id} : Partial updates given fields of an existing uHFRFIDReader, field will ignore if it is null
     *
     * @param id the id of the uHFRFIDReader to save.
     * @param uHFRFIDReader the uHFRFIDReader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uHFRFIDReader,
     * or with status {@code 400 (Bad Request)} if the uHFRFIDReader is not valid,
     * or with status {@code 404 (Not Found)} if the uHFRFIDReader is not found,
     * or with status {@code 500 (Internal Server Error)} if the uHFRFIDReader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/uhfrfid-readers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UHFRFIDReader> partialUpdateUHFRFIDReader(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UHFRFIDReader uHFRFIDReader
    ) throws URISyntaxException {
        log.debug("REST request to partial update UHFRFIDReader partially : {}, {}", id, uHFRFIDReader);
        if (uHFRFIDReader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uHFRFIDReader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uHFRFIDReaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UHFRFIDReader> result = uHFRFIDReaderService.partialUpdate(uHFRFIDReader);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uHFRFIDReader.getId().toString())
        );
    }

    /**
     * {@code GET  /uhfrfid-readers} : get all the uHFRFIDReaders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uHFRFIDReaders in body.
     */
    @GetMapping("/uhfrfid-readers")
    public ResponseEntity<List<UHFRFIDReader>> getAllUHFRFIDReaders(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UHFRFIDReaders");
        Page<UHFRFIDReader> page = uHFRFIDReaderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /uhfrfid-readers/:id} : get the "id" uHFRFIDReader.
     *
     * @param id the id of the uHFRFIDReader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uHFRFIDReader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uhfrfid-readers/{id}")
    public ResponseEntity<UHFRFIDReader> getUHFRFIDReader(@PathVariable Long id) {
        log.debug("REST request to get UHFRFIDReader : {}", id);
        Optional<UHFRFIDReader> uHFRFIDReader = uHFRFIDReaderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uHFRFIDReader);
    }

    /**
     * {@code DELETE  /uhfrfid-readers/:id} : delete the "id" uHFRFIDReader.
     *
     * @param id the id of the uHFRFIDReader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uhfrfid-readers/{id}")
    public ResponseEntity<Void> deleteUHFRFIDReader(@PathVariable Long id) {
        log.debug("REST request to delete UHFRFIDReader : {}", id);
        uHFRFIDReaderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
