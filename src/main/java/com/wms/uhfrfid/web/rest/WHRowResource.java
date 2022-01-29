package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.WHRowRepository;
import com.wms.uhfrfid.service.WHRowService;
import com.wms.uhfrfid.service.dto.WHRowDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.WHRow}.
 */
@RestController
@RequestMapping("/api")
public class WHRowResource {

    private final Logger log = LoggerFactory.getLogger(WHRowResource.class);

    private static final String ENTITY_NAME = "wHRow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WHRowService wHRowService;

    private final WHRowRepository wHRowRepository;

    public WHRowResource(WHRowService wHRowService, WHRowRepository wHRowRepository) {
        this.wHRowService = wHRowService;
        this.wHRowRepository = wHRowRepository;
    }

    /**
     * {@code POST  /wh-rows} : Create a new wHRow.
     *
     * @param wHRowDTO the wHRowDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wHRowDTO, or with status {@code 400 (Bad Request)} if the wHRow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wh-rows")
    public ResponseEntity<WHRowDTO> createWHRow(@Valid @RequestBody WHRowDTO wHRowDTO) throws URISyntaxException {
        log.debug("REST request to save WHRow : {}", wHRowDTO);
        if (wHRowDTO.getId() != null) {
            throw new BadRequestAlertException("A new wHRow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WHRowDTO result = wHRowService.save(wHRowDTO);
        return ResponseEntity
            .created(new URI("/api/wh-rows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wh-rows/:id} : Updates an existing wHRow.
     *
     * @param id the id of the wHRowDTO to save.
     * @param wHRowDTO the wHRowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wHRowDTO,
     * or with status {@code 400 (Bad Request)} if the wHRowDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wHRowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wh-rows/{id}")
    public ResponseEntity<WHRowDTO> updateWHRow(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WHRowDTO wHRowDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WHRow : {}, {}", id, wHRowDTO);
        if (wHRowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wHRowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wHRowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WHRowDTO result = wHRowService.save(wHRowDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wHRowDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wh-rows/:id} : Partial updates given fields of an existing wHRow, field will ignore if it is null
     *
     * @param id the id of the wHRowDTO to save.
     * @param wHRowDTO the wHRowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wHRowDTO,
     * or with status {@code 400 (Bad Request)} if the wHRowDTO is not valid,
     * or with status {@code 404 (Not Found)} if the wHRowDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the wHRowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wh-rows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WHRowDTO> partialUpdateWHRow(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WHRowDTO wHRowDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WHRow partially : {}, {}", id, wHRowDTO);
        if (wHRowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wHRowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wHRowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WHRowDTO> result = wHRowService.partialUpdate(wHRowDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wHRowDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wh-rows} : get all the wHRows.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wHRows in body.
     */
    @GetMapping("/wh-rows")
    public ResponseEntity<List<WHRowDTO>> getAllWHRows(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WHRows");
        Page<WHRowDTO> page = wHRowService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wh-rows/:id} : get the "id" wHRow.
     *
     * @param id the id of the wHRowDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wHRowDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wh-rows/{id}")
    public ResponseEntity<WHRowDTO> getWHRow(@PathVariable Long id) {
        log.debug("REST request to get WHRow : {}", id);
        Optional<WHRowDTO> wHRowDTO = wHRowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wHRowDTO);
    }

    /**
     * {@code DELETE  /wh-rows/:id} : delete the "id" wHRow.
     *
     * @param id the id of the wHRowDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wh-rows/{id}")
    public ResponseEntity<Void> deleteWHRow(@PathVariable Long id) {
        log.debug("REST request to delete WHRow : {}", id);
        wHRowService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
