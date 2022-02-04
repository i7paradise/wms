package com.wms.uhfrfid.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderDTOV2 extends OrderDTO {

    private List<OrderItemDTO> orderItems;

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }
}
