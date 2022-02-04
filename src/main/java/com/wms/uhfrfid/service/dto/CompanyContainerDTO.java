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

    private ContainerCategoryDTO containerCategory;

    private CompanyDTO company;

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

    public ContainerCategoryDTO getContainerCategory() {
        return containerCategory;
    }

    public void setContainerCategory(ContainerCategoryDTO containerCategory) {
        this.containerCategory = containerCategory;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
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
            ", containerCategory=" + getContainerCategory() +
            ", company=" + getCompany() +
            "}";
    }
}
