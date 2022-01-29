package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.CompanyUser} entity.
 */
public class CompanyUserDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyUserDTO)) {
            return false;
        }

        CompanyUserDTO companyUserDTO = (CompanyUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyUserDTO{" +
            "id=" + getId() +
            "}";
    }
}
