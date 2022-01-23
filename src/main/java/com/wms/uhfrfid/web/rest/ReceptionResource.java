package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.domain.DeliveryOrder;
import com.wms.uhfrfid.security.SecurityUtils;
import com.wms.uhfrfid.service.ReceptionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * ReceptionResource controller
 */
@RestController
@RequestMapping(ReceptionResource.PATH)
public class ReceptionResource {

    public static final String PATH = "/api/v1/reception";

    private final Logger log = LoggerFactory.getLogger(ReceptionResource.class);
    private final ReceptionService receptionService;

    public ReceptionResource(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }

    /**
     * GET fetchOpenReceptions
     */
    @GetMapping("/all-open")
    public ResponseEntity<List<DeliveryOrder>> fetchOpenReceptions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DeliveryOrders");
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        Page<DeliveryOrder> page = receptionService.findAllOpen(userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
