package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.CompanyProduct;
import com.wms.uhfrfid.repository.CompanyProductRepository;
import com.wms.uhfrfid.service.CompanyProductService;
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
 * REST controller for managing {@link com.wms.uhfrfid.domain.CompanyProduct}.
 */
@RestController
@RequestMapping("/api")
public class CompanyProductResource {

    private final Logger log = LoggerFactory.getLogger(CompanyProductResource.class);

    private static final String ENTITY_NAME = "companyProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyProductService companyProductService;

    private final CompanyProductRepository companyProductRepository;

    public CompanyProductResource(CompanyProductService companyProductService, CompanyProductRepository companyProductRepository) {
        this.companyProductService = companyProductService;
        this.companyProductRepository = companyProductRepository;
    }

    /**
     * {@code POST  /company-products} : Create a new companyProduct.
     *
     * @param companyProduct the companyProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyProduct, or with status {@code 400 (Bad Request)} if the companyProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-products")
    public ResponseEntity<CompanyProduct> createCompanyProduct(@Valid @RequestBody CompanyProduct companyProduct)
        throws URISyntaxException {
        log.debug("REST request to save CompanyProduct : {}", companyProduct);
        if (companyProduct.getId() != null) {
            throw new BadRequestAlertException("A new companyProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyProduct result = companyProductService.save(companyProduct);
        return ResponseEntity
            .created(new URI("/api/company-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-products/:id} : Updates an existing companyProduct.
     *
     * @param id the id of the companyProduct to save.
     * @param companyProduct the companyProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyProduct,
     * or with status {@code 400 (Bad Request)} if the companyProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-products/{id}")
    public ResponseEntity<CompanyProduct> updateCompanyProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompanyProduct companyProduct
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyProduct : {}, {}", id, companyProduct);
        if (companyProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyProduct result = companyProductService.save(companyProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-products/:id} : Partial updates given fields of an existing companyProduct, field will ignore if it is null
     *
     * @param id the id of the companyProduct to save.
     * @param companyProduct the companyProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyProduct,
     * or with status {@code 400 (Bad Request)} if the companyProduct is not valid,
     * or with status {@code 404 (Not Found)} if the companyProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyProduct> partialUpdateCompanyProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompanyProduct companyProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyProduct partially : {}, {}", id, companyProduct);
        if (companyProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyProduct> result = companyProductService.partialUpdate(companyProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /company-products} : get all the companyProducts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyProducts in body.
     */
    @GetMapping("/company-products")
    public ResponseEntity<List<CompanyProduct>> getAllCompanyProducts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CompanyProducts");
        Page<CompanyProduct> page = companyProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-products/:id} : get the "id" companyProduct.
     *
     * @param id the id of the companyProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-products/{id}")
    public ResponseEntity<CompanyProduct> getCompanyProduct(@PathVariable Long id) {
        log.debug("REST request to get CompanyProduct : {}", id);
        Optional<CompanyProduct> companyProduct = companyProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyProduct);
    }

    /**
     * {@code DELETE  /company-products/:id} : delete the "id" companyProduct.
     *
     * @param id the id of the companyProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-products/{id}")
    public ResponseEntity<Void> deleteCompanyProduct(@PathVariable Long id) {
        log.debug("REST request to delete CompanyProduct : {}", id);
        companyProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
