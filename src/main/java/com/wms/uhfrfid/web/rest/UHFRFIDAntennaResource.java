package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.UHFRFIDAntennaRepository;
import com.wms.uhfrfid.service.UHFRFIDAntennaService;
import com.wms.uhfrfid.service.dto.UHFRFIDAntennaDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.UHFRFIDAntenna}.
 */
@RestController
@RequestMapping("/api")
public class UHFRFIDAntennaResource {

    private final Logger log = LoggerFactory.getLogger(UHFRFIDAntennaResource.class);

    private static final String ENTITY_NAME = "uHFRFIDAntenna";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UHFRFIDAntennaService uHFRFIDAntennaService;

    private final UHFRFIDAntennaRepository uHFRFIDAntennaRepository;

    public UHFRFIDAntennaResource(UHFRFIDAntennaService uHFRFIDAntennaService, UHFRFIDAntennaRepository uHFRFIDAntennaRepository) {
        this.uHFRFIDAntennaService = uHFRFIDAntennaService;
        this.uHFRFIDAntennaRepository = uHFRFIDAntennaRepository;
    }

    /**
     * {@code POST  /uhfrfid-antennas} : Create a new uHFRFIDAntenna.
     *
     * @param uHFRFIDAntennaDTO the uHFRFIDAntennaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uHFRFIDAntennaDTO, or with status {@code 400 (Bad Request)} if the uHFRFIDAntenna has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uhfrfid-antennas")
    public ResponseEntity<UHFRFIDAntennaDTO> createUHFRFIDAntenna(@Valid @RequestBody UHFRFIDAntennaDTO uHFRFIDAntennaDTO)
        throws URISyntaxException {
        log.debug("REST request to save UHFRFIDAntenna : {}", uHFRFIDAntennaDTO);
        if (uHFRFIDAntennaDTO.getId() != null) {
            throw new BadRequestAlertException("A new uHFRFIDAntenna cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UHFRFIDAntennaDTO result = uHFRFIDAntennaService.save(uHFRFIDAntennaDTO);
        return ResponseEntity
            .created(new URI("/api/uhfrfid-antennas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uhfrfid-antennas/:id} : Updates an existing uHFRFIDAntenna.
     *
     * @param id the id of the uHFRFIDAntennaDTO to save.
     * @param uHFRFIDAntennaDTO the uHFRFIDAntennaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uHFRFIDAntennaDTO,
     * or with status {@code 400 (Bad Request)} if the uHFRFIDAntennaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uHFRFIDAntennaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uhfrfid-antennas/{id}")
    public ResponseEntity<UHFRFIDAntennaDTO> updateUHFRFIDAntenna(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UHFRFIDAntennaDTO uHFRFIDAntennaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UHFRFIDAntenna : {}, {}", id, uHFRFIDAntennaDTO);
        if (uHFRFIDAntennaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uHFRFIDAntennaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uHFRFIDAntennaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UHFRFIDAntennaDTO result = uHFRFIDAntennaService.save(uHFRFIDAntennaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uHFRFIDAntennaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /uhfrfid-antennas/:id} : Partial updates given fields of an existing uHFRFIDAntenna, field will ignore if it is null
     *
     * @param id the id of the uHFRFIDAntennaDTO to save.
     * @param uHFRFIDAntennaDTO the uHFRFIDAntennaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uHFRFIDAntennaDTO,
     * or with status {@code 400 (Bad Request)} if the uHFRFIDAntennaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uHFRFIDAntennaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uHFRFIDAntennaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/uhfrfid-antennas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UHFRFIDAntennaDTO> partialUpdateUHFRFIDAntenna(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UHFRFIDAntennaDTO uHFRFIDAntennaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UHFRFIDAntenna partially : {}, {}", id, uHFRFIDAntennaDTO);
        if (uHFRFIDAntennaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uHFRFIDAntennaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uHFRFIDAntennaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UHFRFIDAntennaDTO> result = uHFRFIDAntennaService.partialUpdate(uHFRFIDAntennaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uHFRFIDAntennaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /uhfrfid-antennas} : get all the uHFRFIDAntennas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uHFRFIDAntennas in body.
     */
    @GetMapping("/uhfrfid-antennas")
    public ResponseEntity<List<UHFRFIDAntennaDTO>> getAllUHFRFIDAntennas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UHFRFIDAntennas");
        Page<UHFRFIDAntennaDTO> page = uHFRFIDAntennaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /uhfrfid-antennas/:id} : get the "id" uHFRFIDAntenna.
     *
     * @param id the id of the uHFRFIDAntennaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uHFRFIDAntennaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uhfrfid-antennas/{id}")
    public ResponseEntity<UHFRFIDAntennaDTO> getUHFRFIDAntenna(@PathVariable Long id) {
        log.debug("REST request to get UHFRFIDAntenna : {}", id);
        Optional<UHFRFIDAntennaDTO> uHFRFIDAntennaDTO = uHFRFIDAntennaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uHFRFIDAntennaDTO);
    }

    /**
     * {@code DELETE  /uhfrfid-antennas/:id} : delete the "id" uHFRFIDAntenna.
     *
     * @param id the id of the uHFRFIDAntennaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uhfrfid-antennas/{id}")
    public ResponseEntity<Void> deleteUHFRFIDAntenna(@PathVariable Long id) {
        log.debug("REST request to delete UHFRFIDAntenna : {}", id);
        uHFRFIDAntennaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
