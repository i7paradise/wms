package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.DeliveryOrderRepository;
import com.wms.uhfrfid.service.DeliveryOrderService;
import com.wms.uhfrfid.service.dto.DeliveryOrderDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.DeliveryOrder}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryOrderResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryOrderResource.class);

    private static final String ENTITY_NAME = "deliveryOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryOrderService deliveryOrderService;

    private final DeliveryOrderRepository deliveryOrderRepository;

    public DeliveryOrderResource(DeliveryOrderService deliveryOrderService, DeliveryOrderRepository deliveryOrderRepository) {
        this.deliveryOrderService = deliveryOrderService;
        this.deliveryOrderRepository = deliveryOrderRepository;
    }

    /**
     * {@code POST  /delivery-orders} : Create a new deliveryOrder.
     *
     * @param deliveryOrderDTO the deliveryOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryOrderDTO, or with status {@code 400 (Bad Request)} if the deliveryOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-orders")
    public ResponseEntity<DeliveryOrderDTO> createDeliveryOrder(@Valid @RequestBody DeliveryOrderDTO deliveryOrderDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryOrder : {}", deliveryOrderDTO);
        if (deliveryOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliveryOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryOrderDTO result = deliveryOrderService.save(deliveryOrderDTO);
        return ResponseEntity
            .created(new URI("/api/delivery-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-orders/:id} : Updates an existing deliveryOrder.
     *
     * @param id the id of the deliveryOrderDTO to save.
     * @param deliveryOrderDTO the deliveryOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryOrderDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-orders/{id}")
    public ResponseEntity<DeliveryOrderDTO> updateDeliveryOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryOrderDTO deliveryOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryOrder : {}, {}", id, deliveryOrderDTO);
        if (deliveryOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryOrderDTO result = deliveryOrderService.save(deliveryOrderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-orders/:id} : Partial updates given fields of an existing deliveryOrder, field will ignore if it is null
     *
     * @param id the id of the deliveryOrderDTO to save.
     * @param deliveryOrderDTO the deliveryOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryOrderDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryOrderDTO> partialUpdateDeliveryOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryOrderDTO deliveryOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryOrder partially : {}, {}", id, deliveryOrderDTO);
        if (deliveryOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryOrderDTO> result = deliveryOrderService.partialUpdate(deliveryOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-orders} : get all the deliveryOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryOrders in body.
     */
    @GetMapping("/delivery-orders")
    public ResponseEntity<List<DeliveryOrderDTO>> getAllDeliveryOrders(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DeliveryOrders");
        Page<DeliveryOrderDTO> page = deliveryOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delivery-orders/:id} : get the "id" deliveryOrder.
     *
     * @param id the id of the deliveryOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-orders/{id}")
    public ResponseEntity<DeliveryOrderDTO> getDeliveryOrder(@PathVariable Long id) {
        log.debug("REST request to get DeliveryOrder : {}", id);
        Optional<DeliveryOrderDTO> deliveryOrderDTO = deliveryOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryOrderDTO);
    }

    /**
     * {@code DELETE  /delivery-orders/:id} : delete the "id" deliveryOrder.
     *
     * @param id the id of the deliveryOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-orders/{id}")
    public ResponseEntity<Void> deleteDeliveryOrder(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryOrder : {}", id);
        deliveryOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}