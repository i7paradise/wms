package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.repository.CompanyProductRepository;
import com.wms.uhfrfid.service.CompanyProductService;
import com.wms.uhfrfid.service.dto.CompanyProductDTO;
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
     * @param companyProductDTO the companyProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyProductDTO, or with status {@code 400 (Bad Request)} if the companyProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-products")
    public ResponseEntity<CompanyProductDTO> createCompanyProduct(@Valid @RequestBody CompanyProductDTO companyProductDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompanyProduct : {}", companyProductDTO);
        if (companyProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyProductDTO result = companyProductService.save(companyProductDTO);
        return ResponseEntity
            .created(new URI("/api/company-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-products/:id} : Updates an existing companyProduct.
     *
     * @param id the id of the companyProductDTO to save.
     * @param companyProductDTO the companyProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyProductDTO,
     * or with status {@code 400 (Bad Request)} if the companyProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-products/{id}")
    public ResponseEntity<CompanyProductDTO> updateCompanyProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompanyProductDTO companyProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyProduct : {}, {}", id, companyProductDTO);
        if (companyProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompanyProductDTO result = companyProductService.save(companyProductDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /company-products/:id} : Partial updates given fields of an existing companyProduct, field will ignore if it is null
     *
     * @param id the id of the companyProductDTO to save.
     * @param companyProductDTO the companyProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyProductDTO,
     * or with status {@code 400 (Bad Request)} if the companyProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/company-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyProductDTO> partialUpdateCompanyProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompanyProductDTO companyProductDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyProduct partially : {}, {}", id, companyProductDTO);
        if (companyProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyProductDTO> result = companyProductService.partialUpdate(companyProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /company-products} : get all the companyProducts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyProducts in body.
     */
    @GetMapping("/company-products")
    public ResponseEntity<List<CompanyProductDTO>> getAllCompanyProducts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CompanyProducts");
        Page<CompanyProductDTO> page = companyProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /company-products/:id} : get the "id" companyProduct.
     *
     * @param id the id of the companyProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-products/{id}")
    public ResponseEntity<CompanyProductDTO> getCompanyProduct(@PathVariable Long id) {
        log.debug("REST request to get CompanyProduct : {}", id);
        Optional<CompanyProductDTO> companyProductDTO = companyProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyProductDTO);
    }

    /**
     * {@code DELETE  /company-products/:id} : delete the "id" companyProduct.
     *
     * @param id the id of the companyProductDTO to delete.
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
