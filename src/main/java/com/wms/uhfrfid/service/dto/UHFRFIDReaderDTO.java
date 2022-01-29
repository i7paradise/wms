package com.wms.uhfrfid.service.dto;

import com.wms.uhfrfid.domain.enumeration.UHFRFIDReaderStatus;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.UHFRFIDReader} entity.
 */
public class UHFRFIDReaderDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String ip;

    @NotNull
    private Integer port;

    @NotNull
    private UHFRFIDReaderStatus status;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public UHFRFIDReaderStatus getStatus() {
        return status;
    }

    public void setStatus(UHFRFIDReaderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UHFRFIDReaderDTO)) {
            return false;
        }

        UHFRFIDReaderDTO uHFRFIDReaderDTO = (UHFRFIDReaderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uHFRFIDReaderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UHFRFIDReaderDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ip='" + getIp() + "'" +
            ", port=" + getPort() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
