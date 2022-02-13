package com.wms.uhfrfid.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ReaderResource controller
 */
@RestController
@RequestMapping("/api/reader")
public class ReaderResource {

    private final Logger log = LoggerFactory.getLogger(ReaderResource.class);

    /**
     * GET getReader
     */
    @GetMapping("/get-reader")
    public String getReader() {
        return "getReader";
    }
}
