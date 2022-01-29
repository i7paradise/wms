package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.DeliveryItemProduct} entity.
 */
public class DeliveryItemProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String rfidTAG;

    private DeliveryContainerDTO deliveryContainer;

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

    public DeliveryContainerDTO getDeliveryContainer() {
        return deliveryContainer;
    }

    public void setDeliveryContainer(DeliveryContainerDTO deliveryContainer) {
        this.deliveryContainer = deliveryContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryItemProductDTO)) {
            return false;
        }

        DeliveryItemProductDTO deliveryItemProductDTO = (DeliveryItemProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryItemProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryItemProductDTO{" +
            "id=" + getId() +
            ", rfidTAG='" + getRfidTAG() + "'" +
            ", deliveryContainer=" + getDeliveryContainer() +
            "}";
    }
}
