package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.OrderContainerRepository;
import com.wms.uhfrfid.service.OrderContainerService;
import com.wms.uhfrfid.service.dto.OrderContainerDTO;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.OrderContainer}.
 */
@RestController
@RequestMapping("/api")
public class OrderContainerResource {

    private final Logger log = LoggerFactory.getLogger(OrderContainerResource.class);

    private static final String ENTITY_NAME = "orderContainer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderContainerService orderContainerService;

    private final OrderContainerRepository orderContainerRepository;

    public OrderContainerResource(OrderContainerService orderContainerService, OrderContainerRepository orderContainerRepository) {
        this.orderContainerService = orderContainerService;
        this.orderContainerRepository = orderContainerRepository;
    }

    /**
     * {@code POST  /order-containers} : Create a new orderContainer.
     *
     * @param orderContainerDTO the orderContainerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderContainerDTO, or with status {@code 400 (Bad Request)} if the orderContainer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-containers")
    public ResponseEntity<OrderContainerDTO> createOrderContainer(@RequestBody OrderContainerDTO orderContainerDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrderContainer : {}", orderContainerDTO);
        if (orderContainerDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderContainer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderContainerDTO result = orderContainerService.save(orderContainerDTO);
        return ResponseEntity
            .created(new URI("/api/order-containers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-containers/:id} : Updates an existing orderContainer.
     *
     * @param id the id of the orderContainerDTO to save.
     * @param orderContainerDTO the orderContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderContainerDTO,
     * or with status {@code 400 (Bad Request)} if the orderContainerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-containers/{id}")
    public ResponseEntity<OrderContainerDTO> updateOrderContainer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderContainerDTO orderContainerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderContainer : {}, {}", id, orderContainerDTO);
        if (orderContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderContainerDTO result = orderContainerService.save(orderContainerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderContainerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-containers/:id} : Partial updates given fields of an existing orderContainer, field will ignore if it is null
     *
     * @param id the id of the orderContainerDTO to save.
     * @param orderContainerDTO the orderContainerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderContainerDTO,
     * or with status {@code 400 (Bad Request)} if the orderContainerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderContainerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderContainerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-containers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderContainerDTO> partialUpdateOrderContainer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderContainerDTO orderContainerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderContainer partially : {}, {}", id, orderContainerDTO);
        if (orderContainerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderContainerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderContainerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderContainerDTO> result = orderContainerService.partialUpdate(orderContainerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderContainerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-containers} : get all the orderContainers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderContainers in body.
     */
    @GetMapping("/order-containers")
    public ResponseEntity<List<OrderContainerDTO>> getAllOrderContainers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OrderContainers");
        Page<OrderContainerDTO> page = orderContainerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-containers/:id} : get the "id" orderContainer.
     *
     * @param id the id of the orderContainerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderContainerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-containers/{id}")
    public ResponseEntity<OrderContainerDTO> getOrderContainer(@PathVariable Long id) {
        log.debug("REST request to get OrderContainer : {}", id);
        Optional<OrderContainerDTO> orderContainerDTO = orderContainerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderContainerDTO);
    }

    /**
     * {@code DELETE  /order-containers/:id} : delete the "id" orderContainer.
     *
     * @param id the id of the orderContainerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-containers/{id}")
    public ResponseEntity<Void> deleteOrderContainer(@PathVariable Long id) {
        log.debug("REST request to delete OrderContainer : {}", id);
        orderContainerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
