package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.CompanyContainerRepository;
import com.wms.uhfrfid.service.CompanyContainerService;
import com.wms.uhfrfid.service.dto.CompanyContainerDTO;
import com.wms.uhfrfid.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.CompanyContainer}.
 */
@RestController
@RequestMapping("/api")
public class CompanyContainerResource {

    private final Logger log = LoggerFactory.getLogger(CompanyContainerResource.class);

    private static final String ENTITY_NAME = "companyContainer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyContainerService companyContainerService;

    private final CompanyContainerRepository companyContainerRepository;

    public CompanyContainerResource(
        CompanyContainerService companyContainerService,
        CompanyContainerRepository companyContainerRepository
    ) {
        this.companyContainerService = companyContainerService;
        this.companyContainerRepository = companyContainerRepository;
    }

    /**
     * {@code POST  /company-containers} : Create a new companyContainer.
     *
     * @param companyContainerDTO the companyContainerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyContainerDTO, or with status {@code 400 (Bad Request)} if the companyContainer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-containers")
    public ResponseEntity<CompanyContainerDTO> createCompanyContainer(@RequestBody CompanyContainerDTO companyContainerDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompanyContainer : {}", companyContainerDTO);
        if (companyContainerDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyContainer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyContainerDTO result = companyContainerService.save(companyContainerDTO);
        return ResponseEntity
            .created(new URI("/api/company-containers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-containers/:id} : Updates an existing companyContainer.
     *
     * @param id the id of the companyContainerDTO to save.
     * @param companyContainerDTO the companyContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyContainerDTO,
     * or with status {@code 400 (Bad Request)} if the companyContainerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-containers/{id}")
    public ResponseEntity<CompanyContainerDTO> updateCompanyContainer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyContainerDTO companyContainerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyContainer : {}, {}", id, companyContainerDTO);
        if (companyContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyContainerDTO result = companyContainerService.save(companyContainerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyContainerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-containers/:id} : Partial updates given fields of an existing companyContainer, field will ignore if it is null
     *
     * @param id the id of the companyContainerDTO to save.
     * @param companyContainerDTO the companyContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyContainerDTO,
     * or with status {@code 400 (Bad Request)} if the companyContainerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyContainerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-containers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyContainerDTO> partialUpdateCompanyContainer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompanyContainerDTO companyContainerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyContainer partially : {}, {}", id, companyContainerDTO);
        if (companyContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyContainerDTO> result = companyContainerService.partialUpdate(companyContainerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyContainerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /company-containers} : get all the companyContainers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyContainers in body.
     */
    @GetMapping("/company-containers")
    public ResponseEntity<List<CompanyContainerDTO>> getAllCompanyContainers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CompanyContainers");
        Page<CompanyContainerDTO> page = companyContainerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-containers/:id} : get the "id" companyContainer.
     *
     * @param id the id of the companyContainerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyContainerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-containers/{id}")
    public ResponseEntity<CompanyContainerDTO> getCompanyContainer(@PathVariable Long id) {
        log.debug("REST request to get CompanyContainer : {}", id);
        Optional<CompanyContainerDTO> companyContainerDTO = companyContainerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyContainerDTO);
    }

    /**
     * {@code DELETE  /company-containers/:id} : delete the "id" companyContainer.
     *
     * @param id the id of the companyContainerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-containers/{id}")
    public ResponseEntity<Void> deleteCompanyContainer(@PathVariable Long id) {
        log.debug("REST request to delete CompanyContainer : {}", id);
        companyContainerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
