package com.wms.uhfrfid.service.dto;

import com.wms.uhfrfid.domain.enumeration.DeliveryOrderItemStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.DeliveryOrderItem} entity.
 */
public class DeliveryOrderItemDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitQuantity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal containerQuantity;

    @NotNull
    private DeliveryOrderItemStatus status;

    private CompanyProductDTO compganyProduct;

    private DeliveryOrderDTO deliveryOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(BigDecimal unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public BigDecimal getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(BigDecimal containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public DeliveryOrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryOrderItemStatus status) {
        this.status = status;
    }

    public CompanyProductDTO getCompganyProduct() {
        return compganyProduct;
    }

    public void setCompganyProduct(CompanyProductDTO compganyProduct) {
        this.compganyProduct = compganyProduct;
    }

    public DeliveryOrderDTO getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrderDTO deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryOrderItemDTO)) {
            return false;
        }

        DeliveryOrderItemDTO deliveryOrderItemDTO = (DeliveryOrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryOrderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryOrderItemDTO{" +
            "id=" + getId() +
            ", unitQuantity=" + getUnitQuantity() +
            ", containerQuantity=" + getContainerQuantity() +
            ", status='" + getStatus() + "'" +
            ", compganyProduct=" + getCompganyProduct() +
            ", deliveryOrder=" + getDeliveryOrder() +
            "}";
    }
}
