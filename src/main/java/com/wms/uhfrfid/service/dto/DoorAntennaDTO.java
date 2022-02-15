package com.wms.uhfrfid.service.dto;

import com.wms.uhfrfid.domain.enumeration.DoorAntennaType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.DoorAntenna} entity.
 */
public class DoorAntennaDTO implements Serializable {

    private Long id;

    @NotNull
    private DoorAntennaType type;

    private DoorDTO door;

    private UHFRFIDAntennaDTO rfidAntenna;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoorAntennaType getType() {
        return type;
    }

    public void setType(DoorAntennaType type) {
        this.type = type;
    }

    public DoorDTO getDoor() {
        return door;
    }

    public void setDoor(DoorDTO door) {
        this.door = door;
    }

    public UHFRFIDAntennaDTO getRfidAntenna() {
        return rfidAntenna;
    }

    public void setRfidAntenna(UHFRFIDAntennaDTO rfidAntenna) {
        this.rfidAntenna = rfidAntenna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoorAntennaDTO)) {
            return false;
        }

        DoorAntennaDTO doorAntennaDTO = (DoorAntennaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, doorAntennaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoorAntennaDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", door=" + getDoor() +
            ", rfidAntenna=" + getRfidAntenna() +
            "}";
    }
}
