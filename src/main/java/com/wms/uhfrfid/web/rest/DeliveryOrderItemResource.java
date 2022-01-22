package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.DeliveryOrderItem;
import com.wms.uhfrfid.repository.DeliveryOrderItemRepository;
import com.wms.uhfrfid.service.DeliveryOrderItemService;
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
     * @param deliveryOrderItem the deliveryOrderItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryOrderItem, or with status {@code 400 (Bad Request)} if the deliveryOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-order-items")
    public ResponseEntity<DeliveryOrderItem> createDeliveryOrderItem(@Valid @RequestBody DeliveryOrderItem deliveryOrderItem)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryOrderItem : {}", deliveryOrderItem);
        if (deliveryOrderItem.getId() != null) {
            throw new BadRequestAlertException("A new deliveryOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryOrderItem result = deliveryOrderItemService.save(deliveryOrderItem);
        return ResponseEntity
            .created(new URI("/api/delivery-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-order-items/:id} : Updates an existing deliveryOrderItem.
     *
     * @param id the id of the deliveryOrderItem to save.
     * @param deliveryOrderItem the deliveryOrderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryOrderItem,
     * or with status {@code 400 (Bad Request)} if the deliveryOrderItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryOrderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-order-items/{id}")
    public ResponseEntity<DeliveryOrderItem> updateDeliveryOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryOrderItem deliveryOrderItem
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryOrderItem : {}, {}", id, deliveryOrderItem);
        if (deliveryOrderItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryOrderItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryOrderItem result = deliveryOrderItemService.save(deliveryOrderItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryOrderItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-order-items/:id} : Partial updates given fields of an existing deliveryOrderItem, field will ignore if it is null
     *
     * @param id the id of the deliveryOrderItem to save.
     * @param deliveryOrderItem the deliveryOrderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryOrderItem,
     * or with status {@code 400 (Bad Request)} if the deliveryOrderItem is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryOrderItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryOrderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-order-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryOrderItem> partialUpdateDeliveryOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryOrderItem deliveryOrderItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryOrderItem partially : {}, {}", id, deliveryOrderItem);
        if (deliveryOrderItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryOrderItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryOrderItem> result = deliveryOrderItemService.partialUpdate(deliveryOrderItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryOrderItem.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-order-items} : get all the deliveryOrderItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryOrderItems in body.
     */
    @GetMapping("/delivery-order-items")
    public ResponseEntity<List<DeliveryOrderItem>> getAllDeliveryOrderItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DeliveryOrderItems");
        Page<DeliveryOrderItem> page = deliveryOrderItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delivery-order-items/:id} : get the "id" deliveryOrderItem.
     *
     * @param id the id of the deliveryOrderItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryOrderItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-order-items/{id}")
    public ResponseEntity<DeliveryOrderItem> getDeliveryOrderItem(@PathVariable Long id) {
        log.debug("REST request to get DeliveryOrderItem : {}", id);
        Optional<DeliveryOrderItem> deliveryOrderItem = deliveryOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryOrderItem);
    }

    /**
     * {@code DELETE  /delivery-order-items/:id} : delete the "id" deliveryOrderItem.
     *
     * @param id the id of the deliveryOrderItem to delete.
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
