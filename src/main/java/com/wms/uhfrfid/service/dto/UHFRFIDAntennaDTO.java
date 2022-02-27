package com.wms.uhfrfid.service.dto;

import com.wms.uhfrfid.domain.enumeration.UHFRFIDAntennaStatus;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.UHFRFIDAntenna} entity.
 */
public class UHFRFIDAntennaDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer outputPower;

    @NotNull
    private UHFRFIDAntennaStatus status;

    private UHFRFIDReaderDTO uhfRFIDReader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOutputPower() {
        return outputPower;
    }

    public void setOutputPower(Integer outputPower) {
        this.outputPower = outputPower;
    }

    public UHFRFIDAntennaStatus getStatus() {
        return status;
    }

    public void setStatus(UHFRFIDAntennaStatus status) {
        this.status = status;
    }

    public UHFRFIDReaderDTO getUhfRFIDReader() {
        return uhfRFIDReader;
    }

    public void setUhfRFIDReader(UHFRFIDReaderDTO uhfRFIDReader) {
        this.uhfRFIDReader = uhfRFIDReader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UHFRFIDAntennaDTO)) {
            return false;
        }

        UHFRFIDAntennaDTO uHFRFIDAntennaDTO = (UHFRFIDAntennaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uHFRFIDAntennaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UHFRFIDAntennaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", outputPower=" + getOutputPower() +
            ", status='" + getStatus() + "'" +
            ", uhfRFIDReader=" + getUhfRFIDReader() +
            "}";
    }
}
