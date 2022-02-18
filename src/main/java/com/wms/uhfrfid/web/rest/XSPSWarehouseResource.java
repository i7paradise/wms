package com.wms.uhfrfid.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * XSPSWarehouseResource controller
 */
@RestController
@RequestMapping("/api/xsps-warehouse")
public class XSPSWarehouseResource {

    private final Logger log = LoggerFactory.getLogger(XSPSWarehouseResource.class);

    /**
     * GET getWarehouse
     */
    @GetMapping("/get-warehouse")
    public String getWarehouse() {
        return "getWarehouse";
    }
}
