package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.ContainerCategoryRepository;
import com.wms.uhfrfid.service.ContainerCategoryService;
import com.wms.uhfrfid.service.dto.ContainerCategoryDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.ContainerCategory}.
 */
@RestController
@RequestMapping("/api")
public class ContainerCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ContainerCategoryResource.class);

    private static final String ENTITY_NAME = "containerCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContainerCategoryService containerCategoryService;

    private final ContainerCategoryRepository containerCategoryRepository;

    public ContainerCategoryResource(
        ContainerCategoryService containerCategoryService,
        ContainerCategoryRepository containerCategoryRepository
    ) {
        this.containerCategoryService = containerCategoryService;
        this.containerCategoryRepository = containerCategoryRepository;
    }

    /**
     * {@code POST  /container-categories} : Create a new containerCategory.
     *
     * @param containerCategoryDTO the containerCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new containerCategoryDTO, or with status {@code 400 (Bad Request)} if the containerCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/container-categories")
    public ResponseEntity<ContainerCategoryDTO> createContainerCategory(@Valid @RequestBody ContainerCategoryDTO containerCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContainerCategory : {}", containerCategoryDTO);
        if (containerCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new containerCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContainerCategoryDTO result = containerCategoryService.save(containerCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/container-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /container-categories/:id} : Updates an existing containerCategory.
     *
     * @param id the id of the containerCategoryDTO to save.
     * @param containerCategoryDTO the containerCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated containerCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the containerCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the containerCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/container-categories/{id}")
    public ResponseEntity<ContainerCategoryDTO> updateContainerCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContainerCategoryDTO containerCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContainerCategory : {}, {}", id, containerCategoryDTO);
        if (containerCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, containerCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!containerCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContainerCategoryDTO result = containerCategoryService.save(containerCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, containerCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /container-categories/:id} : Partial updates given fields of an existing containerCategory, field will ignore if it is null
     *
     * @param id the id of the containerCategoryDTO to save.
     * @param containerCategoryDTO the containerCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated containerCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the containerCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the containerCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the containerCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/container-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContainerCategoryDTO> partialUpdateContainerCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContainerCategoryDTO containerCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContainerCategory partially : {}, {}", id, containerCategoryDTO);
        if (containerCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, containerCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!containerCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContainerCategoryDTO> result = containerCategoryService.partialUpdate(containerCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, containerCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /container-categories} : get all the containerCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of containerCategories in body.
     */
    @GetMapping("/container-categories")
    public ResponseEntity<List<ContainerCategoryDTO>> getAllContainerCategories(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ContainerCategories");
        Page<ContainerCategoryDTO> page = containerCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /container-categories/:id} : get the "id" containerCategory.
     *
     * @param id the id of the containerCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the containerCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/container-categories/{id}")
    public ResponseEntity<ContainerCategoryDTO> getContainerCategory(@PathVariable Long id) {
        log.debug("REST request to get ContainerCategory : {}", id);
        Optional<ContainerCategoryDTO> containerCategoryDTO = containerCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(containerCategoryDTO);
    }

    /**
     * {@code DELETE  /container-categories/:id} : delete the "id" containerCategory.
     *
     * @param id the id of the containerCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/container-categories/{id}")
    public ResponseEntity<Void> deleteContainerCategory(@PathVariable Long id) {
        log.debug("REST request to delete ContainerCategory : {}", id);
        containerCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
