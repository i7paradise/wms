package com.wms.uhfrfid.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.CompanyProduct} entity.
 */
public class CompanyProductDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal quantity;

    private String sku;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal containerStockingRatio;

    private ContainerCategoryDTO containerCategory;

    private CompanyDTO company;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getContainerStockingRatio() {
        return containerStockingRatio;
    }

    public void setContainerStockingRatio(BigDecimal containerStockingRatio) {
        this.containerStockingRatio = containerStockingRatio;
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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyProductDTO)) {
            return false;
        }

        CompanyProductDTO companyProductDTO = (CompanyProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyProductDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyProductDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", sku='" + getSku() + "'" +
            ", containerStockingRatio=" + getContainerStockingRatio() +
            ", containerCategory=" + getContainerCategory() +
            ", company=" + getCompany() +
            ", product=" + getProduct() +
            "}";
    }
}
