package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.service.ReaderService;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTOV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.util.Optional;

/**
 * ReaderResource controller
 */
@RestController
@RequestMapping(ReaderResource.PATH)
public class ReaderResource {

    public static final String PATH = "/api/v1/reader";

    private final Logger log = LoggerFactory.getLogger(ReaderResource.class);
    private final ReaderService readerService;

    public ReaderResource(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UHFRFIDReaderDTOV2> getReader(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<UHFRFIDReaderDTOV2> readerDTO = readerService.findOneDetailed(id);
        return ResponseUtil.wrapOrNotFound(readerDTO);

    }
}
