package com.wms.uhfrfid.web.rest;

import com.wms.uhfrfid.security.SecurityUtils;
import com.wms.uhfrfid.service.OrderContainerImplService;
import com.wms.uhfrfid.service.dto.CreateWithTagsDTO;
import com.wms.uhfrfid.service.dto.OrderContainerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderContainerImplResource controller
 */
@RestController
@RequestMapping(OrderContainerImplResource.PATH)
public class OrderContainerImplResource {

    public static final String PATH = "/api/v1/order-containers";
    private final Logger log = LoggerFactory.getLogger(OrderContainerImplResource.class);

    private final OrderContainerImplService orderContainerService;

    public OrderContainerImplResource(OrderContainerImplService orderContainerService) {
        this.orderContainerService = orderContainerService;
    }

    /**
     * GET findOrderContainers
     */
    @GetMapping("/from-order-item/{id}")
    public ResponseEntity<List<OrderContainerDTO>> findOrderContainers(@PathVariable Long id) {
        log.debug("REST get order containers for order-item {}", id);
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        List<OrderContainerDTO> list = orderContainerService.findOrderContainers(id);
        return ResponseEntity.ok(list);
    }

    /**
     * POST createOrderContainersWithTags
     */
    @PostMapping("/create-with-tags")
    public ResponseEntity<List<OrderContainerDTO>> createOrderContainersWithTags(@RequestBody CreateWithTagsDTO createWithTags) {
        log.debug("REST create order containers for order-item {} with tags : {}", createWithTags.getOrderItemId(), createWithTags.getTagsList());
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        List<OrderContainerDTO> list = orderContainerService.createOrderContainers(createWithTags, userLogin);
        return ResponseEntity.ok(list);
    }

    /**
     * DELETE deleteContainer
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderContainer(@PathVariable Long id) {
        log.debug("REST request to delete OrderContainer : {}", id);
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("TODO 401"));
        orderContainerService.delete(id, userLogin);
        return ResponseEntity.noContent().build();
    }

}
