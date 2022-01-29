package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.DeliveryOrderItemRepository;
import com.wms.uhfrfid.service.DeliveryOrderItemService;
import com.wms.uhfrfid.service.dto.DeliveryOrderItemDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.DeliveryOrderItem}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryOrderItemResource.class);

    private static final String ENTITY_NAME = "deliveryOrderItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryOrderItemService deliveryOrderItemService;

    private final DeliveryOrderItemRepository deliveryOrderItemRepository;

    public DeliveryOrderItemResource(
        DeliveryOrderItemService deliveryOrderItemService,
        DeliveryOrderItemRepository deliveryOrderItemRepository
    ) {
        this.deliveryOrderItemService = deliveryOrderItemService;
        this.deliveryOrderItemRepository = deliveryOrderItemRepository;
    }

    /**
     * {@code POST  /delivery-order-items} : Create a new deliveryOrderItem.
     *
     * @param deliveryOrderItemDTO the deliveryOrderItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryOrderItemDTO, or with status {@code 400 (Bad Request)} if the deliveryOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-order-items")
    public ResponseEntity<DeliveryOrderItemDTO> createDeliveryOrderItem(@Valid @RequestBody DeliveryOrderItemDTO deliveryOrderItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryOrderItem : {}", deliveryOrderItemDTO);
        if (deliveryOrderItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliveryOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryOrderItemDTO result = deliveryOrderItemService.save(deliveryOrderItemDTO);
        return ResponseEntity
            .created(new URI("/api/delivery-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-order-items/:id} : Updates an existing deliveryOrderItem.
     *
     * @param id the id of the deliveryOrderItemDTO to save.
     * @param deliveryOrderItemDTO the deliveryOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryOrderItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-order-items/{id}")
    public ResponseEntity<DeliveryOrderItemDTO> updateDeliveryOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryOrderItemDTO deliveryOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryOrderItem : {}, {}", id, deliveryOrderItemDTO);
        if (deliveryOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryOrderItemDTO result = deliveryOrderItemService.save(deliveryOrderItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryOrderItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-order-items/:id} : Partial updates given fields of an existing deliveryOrderItem, field will ignore if it is null
     *
     * @param id the id of the deliveryOrderItemDTO to save.
     * @param deliveryOrderItemDTO the deliveryOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryOrderItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryOrderItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-order-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryOrderItemDTO> partialUpdateDeliveryOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryOrderItemDTO deliveryOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryOrderItem partially : {}, {}", id, deliveryOrderItemDTO);
        if (deliveryOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryOrderItemDTO> result = deliveryOrderItemService.partialUpdate(deliveryOrderItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryOrderItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-order-items} : get all the deliveryOrderItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryOrderItems in body.
     */
    @GetMapping("/delivery-order-items")
    public ResponseEntity<List<DeliveryOrderItemDTO>> getAllDeliveryOrderItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DeliveryOrderItems");
        Page<DeliveryOrderItemDTO> page = deliveryOrderItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delivery-order-items/:id} : get the "id" deliveryOrderItem.
     *
     * @param id the id of the deliveryOrderItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryOrderItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-order-items/{id}")
    public ResponseEntity<DeliveryOrderItemDTO> getDeliveryOrderItem(@PathVariable Long id) {
        log.debug("REST request to get DeliveryOrderItem : {}", id);
        Optional<DeliveryOrderItemDTO> deliveryOrderItemDTO = deliveryOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryOrderItemDTO);
    }

    /**
     * {@code DELETE  /delivery-order-items/:id} : delete the "id" deliveryOrderItem.
     *
     * @param id the id of the deliveryOrderItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-order-items/{id}")
    public ResponseEntity<Void> deleteDeliveryOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryOrderItem : {}", id);
        deliveryOrderItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
