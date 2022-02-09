package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.OrderItemProduct} entity.
 */
public class OrderItemProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String rfidTAG;

    private OrderContainerDTO orderItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfidTAG() {
        return rfidTAG;
    }

    public void setRfidTAG(String rfidTAG) {
        this.rfidTAG = rfidTAG;
    }

    public OrderContainerDTO getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderContainerDTO orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemProductDTO)) {
            return false;
        }

        OrderItemProductDTO orderItemProductDTO = (OrderItemProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemProductDTO{" +
            "id=" + getId() +
            ", rfidTAG='" + getRfidTAG() + "'" +
            ", orderItem=" + getOrderItem() +
            "}";
    }
}
