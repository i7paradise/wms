package com.wms.uhfrfid.service.dto;

import com.wms.uhfrfid.domain.enumeration.AreaType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.Area} entity.
 */
public class AreaDTO implements Serializable {

    private Long id;

    @NotNull
    private AreaType type;

    private WarehouseDTO warehouse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AreaType getType() {
        return type;
    }

    public void setType(AreaType type) {
        this.type = type;
    }

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaDTO)) {
            return false;
        }

        AreaDTO areaDTO = (AreaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, areaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", warehouse=" + getWarehouse() +
            "}";
    }
}
