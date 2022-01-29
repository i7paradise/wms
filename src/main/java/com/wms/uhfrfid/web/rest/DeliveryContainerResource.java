package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.DeliveryContainerRepository;
import com.wms.uhfrfid.service.DeliveryContainerService;
import com.wms.uhfrfid.service.dto.DeliveryContainerDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.DeliveryContainer}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryContainerResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryContainerResource.class);

    private static final String ENTITY_NAME = "deliveryContainer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryContainerService deliveryContainerService;

    private final DeliveryContainerRepository deliveryContainerRepository;

    public DeliveryContainerResource(
        DeliveryContainerService deliveryContainerService,
        DeliveryContainerRepository deliveryContainerRepository
    ) {
        this.deliveryContainerService = deliveryContainerService;
        this.deliveryContainerRepository = deliveryContainerRepository;
    }

    /**
     * {@code POST  /delivery-containers} : Create a new deliveryContainer.
     *
     * @param deliveryContainerDTO the deliveryContainerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryContainerDTO, or with status {@code 400 (Bad Request)} if the deliveryContainer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-containers")
    public ResponseEntity<DeliveryContainerDTO> createDeliveryContainer(@RequestBody DeliveryContainerDTO deliveryContainerDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryContainer : {}", deliveryContainerDTO);
        if (deliveryContainerDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliveryContainer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryContainerDTO result = deliveryContainerService.save(deliveryContainerDTO);
        return ResponseEntity
            .created(new URI("/api/delivery-containers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-containers/:id} : Updates an existing deliveryContainer.
     *
     * @param id the id of the deliveryContainerDTO to save.
     * @param deliveryContainerDTO the deliveryContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryContainerDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryContainerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-containers/{id}")
    public ResponseEntity<DeliveryContainerDTO> updateDeliveryContainer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliveryContainerDTO deliveryContainerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryContainer : {}, {}", id, deliveryContainerDTO);
        if (deliveryContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryContainerDTO result = deliveryContainerService.save(deliveryContainerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryContainerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-containers/:id} : Partial updates given fields of an existing deliveryContainer, field will ignore if it is null
     *
     * @param id the id of the deliveryContainerDTO to save.
     * @param deliveryContainerDTO the deliveryContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryContainerDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryContainerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryContainerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-containers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryContainerDTO> partialUpdateDeliveryContainer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliveryContainerDTO deliveryContainerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryContainer partially : {}, {}", id, deliveryContainerDTO);
        if (deliveryContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryContainerDTO> result = deliveryContainerService.partialUpdate(deliveryContainerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryContainerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-containers} : get all the deliveryContainers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryContainers in body.
     */
    @GetMapping("/delivery-containers")
    public ResponseEntity<List<DeliveryContainerDTO>> getAllDeliveryContainers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DeliveryContainers");
        Page<DeliveryContainerDTO> page = deliveryContainerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delivery-containers/:id} : get the "id" deliveryContainer.
     *
     * @param id the id of the deliveryContainerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryContainerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-containers/{id}")
    public ResponseEntity<DeliveryContainerDTO> getDeliveryContainer(@PathVariable Long id) {
        log.debug("REST request to get DeliveryContainer : {}", id);
        Optional<DeliveryContainerDTO> deliveryContainerDTO = deliveryContainerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryContainerDTO);
    }

    /**
     * {@code DELETE  /delivery-containers/:id} : delete the "id" deliveryContainer.
     *
     * @param id the id of the deliveryContainerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-containers/{id}")
    public ResponseEntity<Void> deleteDeliveryContainer(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryContainer : {}", id);
        deliveryContainerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
