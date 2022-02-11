package com.wms.uhfrfid.service.dto;

import com.wms.uhfrfid.domain.enumeration.OrderItemStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.wms.uhfrfid.domain.OrderItem} entity.
 */
public class OrderItemDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal quantity;

    @NotNull
    private OrderItemStatus status;

    @Min(value = 0)
    private Integer containersCount;

    @Min(value = 0)
    private Integer productsPerContainerCount;

    private OrderDTO order;

    private CompanyProductDTO companyProduct;

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

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public Integer getContainersCount() {
        return containersCount;
    }

    public void setContainersCount(Integer containersCount) {
        this.containersCount = containersCount;
    }

    public Integer getProductsPerContainerCount() {
        return productsPerContainerCount;
    }

    public void setProductsPerContainerCount(Integer productsPerContainerCount) {
        this.productsPerContainerCount = productsPerContainerCount;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public CompanyProductDTO getCompanyProduct() {
        return companyProduct;
    }

    public void setCompanyProduct(CompanyProductDTO companyProduct) {
        this.companyProduct = companyProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", status='" + getStatus() + "'" +
            ", containersCount=" + getContainersCount() +
            ", productsPerContainerCount=" + getProductsPerContainerCount() +
            ", order=" + getOrder() +
            ", companyProduct=" + getCompanyProduct() +
            "}";
    }
}
