package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.security.SecurityUtils;
import com.wms.uhfrfid.service.ReceptionService;
import com.wms.uhfrfid.service.WarehouseService;
import com.wms.uhfrfid.service.dto.OrderDTO;
import com.wms.uhfrfid.service.dto.OrderDTOV2;
import com.wms.uhfrfid.service.dto.WarehouseDTO;
import com.wms.uhfrfid.service.dto.WarehouseDTOV2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;

/**
 * ReceptionResource controller
 */
@RestController
@RequestMapping(XSPSWarehouseResource.PATH)
public class XSPSWarehouseResource {

    public static final String PATH = "/api/v1/receptions";

    private final Logger log = LoggerFactory.getLogger(XSPSWarehouseResource.class);
    private final WarehouseService warehouseService;

    public XSPSWarehouseResource(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * GET fetchOpenReceptions
     */
    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getReceptions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Orders");
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        Page<WarehouseDTO> page = warehouseService.findAll(userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTOV2> getReception(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        Optional<WarehouseDTOV2> warehouseDTO = warehouseService.findOne(id, userLogin);
        return ResponseUtil.wrapOrNotFound(warehouseDTO);
    }
}
