package com.wms.uhfrfid.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeliveryOrderDTOV2 extends DeliveryOrderDTO {

    private List<DeliveryOrderItemDTO> deliveryOrderItems;

    public void setDeliveryOrderItems(List<DeliveryOrderItemDTO> deliveryOrderItems) {
        this.deliveryOrderItems = deliveryOrderItems;
    }

    public List<DeliveryOrderItemDTO> getDeliveryOrderItems() {
        return deliveryOrderItems;
    }
}
