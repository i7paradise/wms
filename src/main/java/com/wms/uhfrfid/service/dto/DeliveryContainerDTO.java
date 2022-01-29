package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.DeliveryContainer} entity.
 */
public class DeliveryContainerDTO implements Serializable {

    private Long id;

    private String supplierRFIDTag;

    private DeliveryOrderItemDTO deliveryOrderItem;

    private CompanyContainerDTO companyContainer;

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

    public DeliveryOrderItemDTO getDeliveryOrderItem() {
        return deliveryOrderItem;
    }

    public void setDeliveryOrderItem(DeliveryOrderItemDTO deliveryOrderItem) {
        this.deliveryOrderItem = deliveryOrderItem;
    }

    public CompanyContainerDTO getCompanyContainer() {
        return companyContainer;
    }

    public void setCompanyContainer(CompanyContainerDTO companyContainer) {
        this.companyContainer = companyContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryContainerDTO)) {
            return false;
        }

        DeliveryContainerDTO deliveryContainerDTO = (DeliveryContainerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryContainerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryContainerDTO{" +
            "id=" + getId() +
            ", supplierRFIDTag='" + getSupplierRFIDTag() + "'" +
            ", deliveryOrderItem=" + getDeliveryOrderItem() +
            ", companyContainer=" + getCompanyContainer() +
            "}";
    }
}
