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

    @JsonIgnoreProperties(value = { "containerCategory", "company", "product" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CompanyProduct compganyProduct;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "orderItems" }, allowSetters = true)
    private Order order;

    @OneToMany(mappedBy = "orderItem")
    @JsonIgnoreProperties(value = { "orderItem", "orderItemProducts" }, allowSetters = true)
    private Set<ContainerCategory> containerCategories = new HashSet<>();

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

    public CompanyProduct getCompganyProduct() {
        return this.compganyProduct;
    }

    public void setCompganyProduct(CompanyProduct companyProduct) {
        this.compganyProduct = companyProduct;
    }

    public OrderItem compganyProduct(CompanyProduct companyProduct) {
        this.setCompganyProduct(companyProduct);
        return this;
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

    public Set<ContainerCategory> getContainerCategories() {
        return this.containerCategories;
    }

    public void setContainerCategories(Set<ContainerCategory> containerCategories) {
        if (this.containerCategories != null) {
            this.containerCategories.forEach(i -> i.setOrderItem(null));
        }
        if (containerCategories != null) {
            containerCategories.forEach(i -> i.setOrderItem(this));
        }
        this.containerCategories = containerCategories;
    }

    public OrderItem containerCategories(Set<ContainerCategory> containerCategories) {
        this.setContainerCategories(containerCategories);
        return this;
    }

    public OrderItem addContainerCategory(ContainerCategory containerCategory) {
        this.containerCategories.add(containerCategory);
        containerCategory.setOrderItem(this);
        return this;
    }

    public OrderItem removeContainerCategory(ContainerCategory containerCategory) {
        this.containerCategories.remove(containerCategory);
        containerCategory.setOrderItem(null);
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
            "}";
    }
}
