package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.security.SecurityUtils;
import com.wms.uhfrfid.service.ReceptionItemService;
import com.wms.uhfrfid.service.ReceptionService;
import com.wms.uhfrfid.service.dto.OrderDTO;
import com.wms.uhfrfid.service.dto.OrderDTOV2;
import com.wms.uhfrfid.service.dto.OrderItemDTO;
import com.wms.uhfrfid.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ReceptionResource controller
 */
@RestController
@RequestMapping(ReceptionResource.PATH)
public class ReceptionResource {

    public static final String PATH = "/api/v1/receptions";

    private final Logger log = LoggerFactory.getLogger(ReceptionResource.class);
    private final ReceptionService receptionService;
    private final ReceptionItemService receptionItemService;

    public ReceptionResource(ReceptionService receptionService, ReceptionItemService receptionItemService) {
        this.receptionService = receptionService;
        this.receptionItemService = receptionItemService;
    }

    /**
     * GET fetchOpenReceptions
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getReceptions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Orders");
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        Page<OrderDTO> page = receptionService.findAll(userLogin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTOV2> getReception(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        Optional<OrderDTOV2> orderDTO = receptionService.findOne(id, userLogin);
        return ResponseUtil.wrapOrNotFound(orderDTO);
    }

    @GetMapping("/order-item/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItem(@PathVariable Long id) {
        log.debug("REST request to get OrderItem : {}", id);
        Optional<OrderItemDTO> orderItemDTO = receptionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemDTO);
    }

    @PutMapping("/order-item/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderItemDTO orderItemDTO
    ) throws URISyntaxException {
        final String ENTITY_NAME = "order-item";
        log.debug("REST request to update OrderItem : {}, {}", id, orderItemDTO);
        if (orderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        OrderItemDTO result = receptionItemService.save(orderItemDTO);
        return ResponseEntity.ok().body(result);
    }
}
