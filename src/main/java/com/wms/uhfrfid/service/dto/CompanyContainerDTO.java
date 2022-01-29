package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.CompanyContainer} entity.
 */
public class CompanyContainerDTO implements Serializable {

    private Long id;

    private String rfidTag;

    private String color;

    private CompanyDTO company;

    private ContainerDTO container;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfidTag() {
        return rfidTag;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public ContainerDTO getContainer() {
        return container;
    }

    public void setContainer(ContainerDTO container) {
        this.container = container;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyContainerDTO)) {
            return false;
        }

        CompanyContainerDTO companyContainerDTO = (CompanyContainerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyContainerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyContainerDTO{" +
            "id=" + getId() +
            ", rfidTag='" + getRfidTag() + "'" +
            ", color='" + getColor() + "'" +
            ", company=" + getCompany() +
            ", container=" + getContainer() +
            "}";
    }
}
