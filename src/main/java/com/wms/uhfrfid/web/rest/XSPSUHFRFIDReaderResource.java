package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.security.SecurityUtils;
import com.wms.uhfrfid.service.ReceptionService;
import com.wms.uhfrfid.service.XSPSUHFRFIDReaderService;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTO;
import com.wms.uhfrfid.service.dto.UHFRFIDReaderDTOV2;

import java.util.List;
import java.util.Optional;
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

/**
 * ReceptionResource controller
 */
@RestController
@RequestMapping(XSPSUHFRFIDReaderResource.PATH)
public class XSPSUHFRFIDReaderResource {

    public static final String PATH = "/api/v1/receptions";

    private final Logger log = LoggerFactory.getLogger(XSPSUHFRFIDReaderResource.class);
    private final XSPSUHFRFIDReaderService xspsUHFRFIDReaderService;

    public XSPSUHFRFIDReaderResource(XSPSUHFRFIDReaderService xspsUHFRFIDReaderService) {
        this.xspsUHFRFIDReaderService = xspsUHFRFIDReaderService;
    }

    /**
     * GET fetchOpenReceptions
     */
    @GetMapping
    public ResponseEntity<List<UHFRFIDReaderDTO>> fetchReceptions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DeliveryOrders");
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        Page<UHFRFIDReaderDTO> page = xspsUHFRFIDReaderService.findAll(userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UHFRFIDReaderDTOV2> fetchReception(@PathVariable Long id) {
        log.debug("REST request to get DeliveryOrder : {}", id);
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        Optional<UHFRFIDReaderDTOV2> UHFRFIDReaderDTO = xspsUHFRFIDReaderService.findOne(id, userLogin);
        return ResponseUtil.wrapOrNotFound(UHFRFIDReaderDTO);
    }
}
