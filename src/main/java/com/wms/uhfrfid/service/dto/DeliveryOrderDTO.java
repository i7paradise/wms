package com.wms.uhfrfid.service.dto;

import com.wms.uhfrfid.domain.enumeration.DeliveryOrderStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.DeliveryOrder} entity.
 */
public class DeliveryOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private String doNumber;

    @NotNull
    private Instant placedDate;

    @NotNull
    private DeliveryOrderStatus status;

    @NotNull
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public Instant getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public DeliveryOrderStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryOrderStatus status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryOrderDTO)) {
            return false;
        }

        DeliveryOrderDTO deliveryOrderDTO = (DeliveryOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryOrderDTO{" +
            "id=" + getId() +
            ", doNumber='" + getDoNumber() + "'" +
            ", placedDate='" + getPlacedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
