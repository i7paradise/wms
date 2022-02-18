package com.wms.uhfrfid.web.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.uhfrfid.service.WarehouseService;
import com.wms.uhfrfid.service.XSPSWarehouseService;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTOV2;
import com.wms.uhfrfid.service.dto.WarehouseDTOV2;

import tech.jhipster.web.util.ResponseUtil;

/**
 * XSPSWarehouseResource controller
 */
@RestController
@RequestMapping(ReaderResource.PATH)
public class XSPSWarehouseResource {

	public static final String PATH = "/api/v1/warehouse";

    private final Logger log = LoggerFactory.getLogger(XSPSWarehouseResource.class);
    private final XSPSWarehouseService xspsWarehouseService;

    public XSPSWarehouseResource(XSPSWarehouseService xspsWarehouseService) {
		super();
		this.xspsWarehouseService = xspsWarehouseService;
	}

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTOV2> getReader(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<WarehouseDTOV2> readerDTO = xspsWarehouseService.findOneDetailed(id);
        return ResponseUtil.wrapOrNotFound(readerDTO);

    }

}
