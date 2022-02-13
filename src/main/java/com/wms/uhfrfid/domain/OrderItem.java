package com.wms.uhfrfid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wms.uhfrfid.domain.enumeration.OrderItemStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderItemStatus status;

    @Min(value = 0)
    @Column(name = "containers_count")
    private Integer containersCount;

    @Min(value = 0)
    @Column(name = "products_per_container_count")
    private Integer productsPerContainerCount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "orderItems" }, allowSetters = true)
    private Order order;

    @ManyToOne
    @JsonIgnoreProperties(value = { "containerCategory", "company", "product" }, allowSetters = true)
    private CompanyProduct companyProduct;

    @OneToMany(mappedBy = "orderItem")
    @JsonIgnoreProperties(value = { "companyContainer", "orderItem", "orderItemProducts" }, allowSetters = true)
    private Set<OrderContainer> orderContainers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(BigDecimal quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public OrderItemStatus getStatus() {
        return this.status;
    }

    public OrderItem status(OrderItemStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public Integer getContainersCount() {
        return this.containersCount;
    }

    public OrderItem containersCount(Integer containersCount) {
        this.setContainersCount(containersCount);
        return this;
    }

    public void setContainersCount(Integer containersCount) {
        this.containersCount = containersCount;
    }

    public Integer getProductsPerContainerCount() {
        return this.productsPerContainerCount;
    }

    public OrderItem productsPerContainerCount(Integer productsPerContainerCount) {
        this.setProductsPerContainerCount(productsPerContainerCount);
        return this;
    }

    public void setProductsPerContainerCount(Integer productsPerContainerCount) {
        this.productsPerContainerCount = productsPerContainerCount;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem order(Order order) {
        this.setOrder(order);
        return this;
    }

    public CompanyProduct getCompanyProduct() {
        return this.companyProduct;
    }

    public void setCompanyProduct(CompanyProduct companyProduct) {
        this.companyProduct = companyProduct;
    }

    public OrderItem companyProduct(CompanyProduct companyProduct) {
        this.setCompanyProduct(companyProduct);
        return this;
    }

    public Set<OrderContainer> getOrderContainers() {
        return this.orderContainers;
    }

    public void setOrderContainers(Set<OrderContainer> orderContainers) {
        if (this.orderContainers != null) {
            this.orderContainers.forEach(i -> i.setOrderItem(null));
        }
        if (orderContainers != null) {
            orderContainers.forEach(i -> i.setOrderItem(this));
        }
        this.orderContainers = orderContainers;
    }

    public OrderItem orderContainers(Set<OrderContainer> orderContainers) {
        this.setOrderContainers(orderContainers);
        return this;
    }

    public OrderItem addOrderContainer(OrderContainer orderContainer) {
        this.orderContainers.add(orderContainer);
        orderContainer.setOrderItem(this);
        return this;
    }

    public OrderItem removeOrderContainer(OrderContainer orderContainer) {
        this.orderContainers.remove(orderContainer);
        orderContainer.setOrderItem(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", status='" + getStatus() + "'" +
            ", containersCount=" + getContainersCount() +
            ", productsPerContainerCount=" + getProductsPerContainerCount() +
            "}";
    }
}
