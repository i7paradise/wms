package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.DeliveryItemProduct;
import com.wms.uhfrfid.repository.DeliveryItemProductRepository;
import com.wms.uhfrfid.service.DeliveryItemProductService;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.DeliveryItemProduct}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryItemProductResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryItemProductResource.class);

    private static final String ENTITY_NAME = "deliveryItemProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryItemProductService deliveryItemProductService;

    private final DeliveryItemProductRepository deliveryItemProductRepository;

    public DeliveryItemProductResource(
        DeliveryItemProductService deliveryItemProductService,
        DeliveryItemProductRepository deliveryItemProductRepository
    ) {
        this.deliveryItemProductService = deliveryItemProductService;
        this.deliveryItemProductRepository = deliveryItemProductRepository;
    }

    /**
     * {@code POST  /delivery-item-products} : Create a new deliveryItemProduct.
     *
     * @param deliveryItemProduct the deliveryItemProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryItemProduct, or with status {@code 400 (Bad Request)} if the deliveryItemProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-item-products")
    public ResponseEntity<DeliveryItemProduct> createDeliveryItemProduct(@Valid @RequestBody DeliveryItemProduct deliveryItemProduct)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryItemProduct : {}", deliveryItemProduct);
        if (deliveryItemProduct.getId() != null) {
            throw new BadRequestAlertException("A new deliveryItemProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryItemProduct result = deliveryItemProductService.save(deliveryItemProduct);
        return ResponseEntity
            .created(new URI("/api/delivery-item-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-item-products/:id} : Updates an existing deliveryItemProduct.
     *
     * @param id the id of the deliveryItemProduct to save.
     * @param deliveryItemProduct the deliveryItemProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryItemProduct,
     * or with status {@code 400 (Bad Request)} if the deliveryItemProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryItemProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-item-products/{id}")
    public ResponseEntity<DeliveryItemProduct> updateDeliveryItemProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryItemProduct deliveryItemProduct
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryItemProduct : {}, {}", id, deliveryItemProduct);
        if (deliveryItemProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryItemProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryItemProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryItemProduct result = deliveryItemProductService.save(deliveryItemProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryItemProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-item-products/:id} : Partial updates given fields of an existing deliveryItemProduct, field will ignore if it is null
     *
     * @param id the id of the deliveryItemProduct to save.
     * @param deliveryItemProduct the deliveryItemProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryItemProduct,
     * or with status {@code 400 (Bad Request)} if the deliveryItemProduct is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryItemProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryItemProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-item-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryItemProduct> partialUpdateDeliveryItemProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryItemProduct deliveryItemProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryItemProduct partially : {}, {}", id, deliveryItemProduct);
        if (deliveryItemProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryItemProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryItemProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryItemProduct> result = deliveryItemProductService.partialUpdate(deliveryItemProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryItemProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-item-products} : get all the deliveryItemProducts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryItemProducts in body.
     */
    @GetMapping("/delivery-item-products")
    public ResponseEntity<List<DeliveryItemProduct>> getAllDeliveryItemProducts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DeliveryItemProducts");
        Page<DeliveryItemProduct> page = deliveryItemProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delivery-item-products/:id} : get the "id" deliveryItemProduct.
     *
     * @param id the id of the deliveryItemProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryItemProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-item-products/{id}")
    public ResponseEntity<DeliveryItemProduct> getDeliveryItemProduct(@PathVariable Long id) {
        log.debug("REST request to get DeliveryItemProduct : {}", id);
        Optional<DeliveryItemProduct> deliveryItemProduct = deliveryItemProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryItemProduct);
    }

    /**
     * {@code DELETE  /delivery-item-products/:id} : delete the "id" deliveryItemProduct.
     *
     * @param id the id of the deliveryItemProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-item-products/{id}")
    public ResponseEntity<Void> deleteDeliveryItemProduct(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryItemProduct : {}", id);
        deliveryItemProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
