package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.OrderContainer} entity.
 */
public class OrderContainerDTO implements Serializable {

    private Long id;

    private String supplierRFIDTag;

    private CompanyContainerDTO companyContainer;

    private OrderItemDTO orderItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierRFIDTag() {
        return supplierRFIDTag;
    }

    public void setSupplierRFIDTag(String supplierRFIDTag) {
        this.supplierRFIDTag = supplierRFIDTag;
    }

    public CompanyContainerDTO getCompanyContainer() {
        return companyContainer;
    }

    public void setCompanyContainer(CompanyContainerDTO companyContainer) {
        this.companyContainer = companyContainer;
    }

    public OrderItemDTO getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemDTO orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderContainerDTO)) {
            return false;
        }

        OrderContainerDTO orderContainerDTO = (OrderContainerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderContainerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderContainerDTO{" +
            "id=" + getId() +
            ", supplierRFIDTag='" + getSupplierRFIDTag() + "'" +
            ", companyContainer=" + getCompanyContainer() +
            ", orderItem=" + getOrderItem() +
            "}";
    }
}
